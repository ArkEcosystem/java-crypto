package org.arkecosystem.crypto.utils;

import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

public class AbiDecoder extends AbiBase {

    public AbiDecoder() throws IOException {
        super();
    }

    public Map<String, Object> decodeFunctionData(String data) throws Exception {
        data = stripHexPrefix(data);

        String functionSelector = data.substring(0, 8);

        Map<String, Object> abiItem = findFunctionBySelector(functionSelector);
        if (abiItem == null) {
            throw new Exception("Function selector not found in ABI: " + functionSelector);
        }

        String encodedParams = data.substring(8);
        List<Object> decodedParams = decodeAbiParameters((List<Map<String, Object>>) abiItem.get("inputs"), encodedParams);

        Map<String, Object> result = new HashMap<>();
        result.put("functionName", abiItem.get("name"));
        result.put("args", decodedParams);

        return result;
    }

    private Map<String, Object> findFunctionBySelector(String selector) {
        for (Map<String, Object> item : this.abi) {
            if ("function".equals(item.get("type"))) {
                String functionSignature = getFunctionSignature(item);
                String functionSelector = stripHexPrefix(keccak256(functionSignature)).substring(0, 8);
                if (functionSelector.equals(selector)) {
                    return item;
                }
            }
        }
        return null;
    }

    private List<Object> decodeAbiParameters(List<Map<String, Object>> params, String data) throws Exception {
        if ((data == null || data.isEmpty()) && !params.isEmpty()) {
            throw new Exception("No data to decode");
        }

        byte[] bytes = Numeric.hexStringToByteArray(data);
        int cursor = 0;

        List<Object> values = new ArrayList<>();
        for (Map<String, Object> param : params) {
            Object[] result = decodeParameter(bytes, cursor, param);
            Object value = result[0];
            int consumed = (int) result[1];
            cursor += consumed;
            values.add(value);
        }

        return values;
    }

    private Object[] decodeParameter(byte[] bytes, int offset, Map<String, Object> param) throws Exception {
        String type = (String) param.get("type");
        String[] arrayComponents = getArrayComponents(type);
        if (arrayComponents != null) {
            String lengthStr = arrayComponents[0];
            String baseType = arrayComponents[1];
            param.put("type", baseType);

            Integer length = lengthStr != null && !lengthStr.isEmpty() ? Integer.parseInt(lengthStr) : null;

            return decodeArray(bytes, offset, param, length);
        }

        switch (type) {
            case "address":
                return decodeAddress(bytes, offset);
            case "bool":
                return decodeBool(bytes, offset);
            case "string":
                return decodeString(bytes, offset);
            case "bytes":
                return decodeDynamicBytes(bytes, offset);
            default:
                if (type.matches("^bytes(\\d+)$")) {
                    int size = Integer.parseInt(type.substring(5));
                    return decodeFixedBytes(bytes, offset, size);
                } else if (type.matches("^(u?int)(\\d+)$")) {
                    boolean signed = type.startsWith("int");
                    int bits = Integer.parseInt(type.replaceAll("\\D", ""));
                    return decodeNumber(bytes, offset, bits, signed);
                } else if ("tuple".equals(type)) {
                    return decodeTuple(bytes, offset, param);
                } else {
                    throw new Exception("Unsupported type: " + type);
                }
        }
    }

    private Object[] decodeAddress(byte[] bytes, int offset) {
        byte[] data = Arrays.copyOfRange(bytes, offset, offset + 32);
        byte[] addressBytes = Arrays.copyOfRange(data, 12, 32);
        String address = "0x" + Numeric.toHexStringNoPrefix(addressBytes);
        address = org.web3j.crypto.Keys.toChecksumAddress(address);

        return new Object[]{address, 32};
    }

    private Object[] decodeBool(byte[] bytes, int offset) {
        byte[] data = Arrays.copyOfRange(bytes, offset, offset + 32);
        boolean value = new BigInteger(data).compareTo(BigInteger.ZERO) != 0;

        return new Object[]{value, 32};
    }

    private Object[] decodeNumber(byte[] bytes, int offset, int bits, boolean signed) {
        byte[] data = Arrays.copyOfRange(bytes, offset, offset + 32);
        BigInteger value = new BigInteger(1, data);
        if (signed && value.testBit(bits - 1)) {
            value = value.subtract(BigInteger.ONE.shiftLeft(bits));
        }

        return new Object[]{value.toString(), 32};
    }

    private Object[] decodeString(byte[] bytes, int offset) {
        int dataOffset = readUInt(bytes, offset).intValue();
        int stringOffset = offset + dataOffset;
        int length = readUInt(bytes, stringOffset).intValue();
        byte[] stringData = Arrays.copyOfRange(bytes, stringOffset + 32, stringOffset + 32 + length);
        String value = new String(stringData);

        return new Object[]{value, 32};
    }

    private Object[] decodeDynamicBytes(byte[] bytes, int offset) {
        int dataOffset = readUInt(bytes, offset).intValue();
        int bytesOffset = offset + dataOffset;
        int length = readUInt(bytes, bytesOffset).intValue();
        byte[] bytesData = Arrays.copyOfRange(bytes, bytesOffset + 32, bytesOffset + 32 + length);
        String value = "0x" + Numeric.toHexStringNoPrefix(bytesData);

        return new Object[]{value, 32};
    }

    private Object[] decodeFixedBytes(byte[] bytes, int offset, int size) {
        byte[] data = Arrays.copyOfRange(bytes, offset, offset + 32);
        String value = "0x" + Numeric.toHexStringNoPrefix(Arrays.copyOfRange(data, 0, size));

        return new Object[]{value, 32};
    }

    private Object[] decodeArray(byte[] bytes, int offset, Map<String, Object> param, Integer length) throws Exception {
        String baseType = (String) param.get("type");
        Map<String, Object> elementType = new HashMap<>(param);
        elementType.put("type", baseType);

        int arrayLength;
        int cursor;
        if (length == null) {
            int dataOffset = readUInt(bytes, offset).intValue();
            int arrayOffset = offset + dataOffset;
            arrayLength = readUInt(bytes, arrayOffset).intValue();
            cursor = arrayOffset + 32;
        } else {
            arrayLength = length;
            cursor = offset;
        }

        List<Object> values = new ArrayList<>();
        for (int i = 0; i < arrayLength; i++) {
            Object[] result = decodeParameter(bytes, cursor, elementType);
            Object value = result[0];
            int consumed = (int) result[1];
            cursor += consumed;
            values.add(value);
        }

        return new Object[]{values, 32};
    }

    private Object[] decodeTuple(byte[] bytes, int offset, Map<String, Object> param) throws Exception {
        List<Map<String, Object>> components = (List<Map<String, Object>>) param.get("components");
        Map<String, Object> values = new LinkedHashMap<>();
        int cursor = offset;

        for (Map<String, Object> component : components) {
            Object[] result = decodeParameter(bytes, cursor, component);
            Object value = result[0];
            int consumed = (int) result[1];
            cursor += consumed;
            String name = (String) component.getOrDefault("name", "");
            values.put(name, value);
        }

        return new Object[]{values, 32};
    }

    private BigInteger readUInt(byte[] bytes, int offset) {
        byte[] data = Arrays.copyOfRange(bytes, offset, offset + 32);
        return new BigInteger(1, data);
    }
}
