package org.arkecosystem.crypto.utils;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import org.web3j.utils.Numeric;

public class AbiEncoder extends AbiBase {

    public AbiEncoder() throws IOException {
        super();
    }

    public String encodeFunctionCall(String functionName) throws Exception {
        return encodeFunctionCall(functionName, Collections.emptyList());
    }

    public String encodeFunctionCall(String functionName, List<Object> args) throws Exception {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("abi", this.abi);
        parameters.put("functionName", functionName);
        parameters.put("args", args);

        return encodeFunctionData(parameters);
    }

    private String encodeFunctionData(Map<String, Object> parameters) throws Exception {
        List<Object> args = (List<Object>) parameters.getOrDefault("args", new ArrayList<>());

        Object[] result = prepareEncodeFunctionData(parameters);
        Map<String, Object> abiItem = (Map<String, Object>) result[0];
        String signature = (String) result[1];

        String data;
        List<Map<String, Object>> inputs = (List<Map<String, Object>>) abiItem.get("inputs");
        if (inputs != null && !inputs.isEmpty()) {
            data = encodeAbiParameters(inputs, args);
        } else {
            data = null;
        }

        return concatHex(Arrays.asList(signature, data != null ? data : "0x"));
    }

    private Object[] prepareEncodeFunctionData(Map<String, Object> params) throws Exception {
        List<Map<String, Object>> abi = (List<Map<String, Object>>) params.get("abi");
        String functionName = (String) params.get("functionName");

        if (functionName == null) {
            List<Map<String, Object>> functions = new ArrayList<>();
            for (Map<String, Object> item : abi) {
                if ("function".equals(item.get("type"))) {
                    functions.add(item);
                }
            }
            if (functions.size() == 1) {
                Map<String, Object> abiItem = functions.get(0);
                functionName = (String) abiItem.get("name");
            } else {
                throw new Exception("Function name is not provided and ABI has multiple functions");
            }
        }

        Map<String, Object> abiItem =
                getAbiItem(
                        abi,
                        functionName,
                        (List<Object>) params.getOrDefault("args", new ArrayList<>()));
        if (abiItem == null) {
            throw new Exception("Function not found in ABI: " + functionName);
        }

        String signature = toFunctionSelector(abiItem);

        return new Object[] {abiItem, signature};
    }

    private Map<String, Object> getAbiItem(
            List<Map<String, Object>> abi, String name, List<Object> args) throws Exception {
        List<Map<String, Object>> matchingItems = new ArrayList<>();
        for (Map<String, Object> item : abi) {
            if ("function".equals(item.get("type")) && name.equals(item.get("name"))) {
                matchingItems.add(item);
            }
        }

        if (matchingItems.isEmpty()) {
            throw new Exception("Function not found in ABI: " + name);
        }

        for (Map<String, Object> item : matchingItems) {
            List<Map<String, Object>> inputs = (List<Map<String, Object>>) item.get("inputs");
            if (inputs.size() == args.size()) {
                return item;
            }
        }

        throw new Exception("Function with matching arguments not found in ABI: " + name);
    }

    private String encodeAbiParameters(List<Map<String, Object>> params, List<Object> values)
            throws Exception {
        if (params.size() != values.size()) {
            throw new Exception("Length of parameters and values do not match");
        }

        List<Map<String, Object>> preparedParams = prepareParams(params, values);
        String data = encodeParams(preparedParams);

        return data != null && !data.isEmpty() ? data : "0x";
    }

    private List<Map<String, Object>> prepareParams(
            List<Map<String, Object>> params, List<Object> values) throws Exception {
        List<Map<String, Object>> preparedParams = new ArrayList<>();
        for (int i = 0; i < params.size(); i++) {
            Map<String, Object> preparedParam = prepareParam(params.get(i), values.get(i));
            preparedParams.add(preparedParam);
        }
        return preparedParams;
    }

    private Map<String, Object> prepareParam(Map<String, Object> param, Object value)
            throws Exception {
        String type = (String) param.get("type");
        String[] arrayComponents = getArrayComponents(type);
        if (arrayComponents != null) {
            String lengthStr = arrayComponents[0];
            String innerType = arrayComponents[1];
            Integer length =
                    lengthStr != null && !lengthStr.isEmpty() ? Integer.parseInt(lengthStr) : null;
            Map<String, Object> innerParam = new HashMap<>();
            innerParam.put("name", param.get("name"));
            innerParam.put("type", innerType);

            return encodeArray(value, length, innerParam);
        }

        switch (type) {
            case "tuple":
                return encodeTuple(value, param);
            case "address":
                return encodeAddress((String) value);
            case "bool":
                return encodeBool((Boolean) value);
            default:
                if (type.startsWith("uint") || type.startsWith("int")) {
                    boolean signed = type.startsWith("int");
                    return encodeNumber(value, signed);
                } else if (type.startsWith("bytes")) {
                    return encodeBytes((String) value, param);
                } else if ("string".equals(type)) {
                    return encodeString((String) value);
                } else {
                    throw new Exception("Invalid ABI type: " + type);
                }
        }
    }

    private Map<String, Object> encodeArray(Object value, Integer length, Map<String, Object> param)
            throws Exception {
        boolean dynamic = length == null;

        if (!(value instanceof List)) {
            throw new Exception("Invalid array value");
        }
        List<Object> valueList = (List<Object>) value;
        if (!dynamic && valueList.size() != length) {
            throw new Exception("Array length mismatch");
        }

        boolean dynamicChild = false;
        List<Map<String, Object>> preparedParams = new ArrayList<>();
        for (Object v : valueList) {
            Map<String, Object> preparedParam = prepareParam(param, v);
            if ((Boolean) preparedParam.get("dynamic")) {
                dynamicChild = true;
            }
            preparedParams.add(preparedParam);
        }

        if (dynamic || dynamicChild) {
            String data = encodeParams(preparedParams);
            if (dynamic) {
                String lengthHex = String.format("%064x", valueList.size());
                return Map.of("dynamic", true, "encoded", "0x" + lengthHex + data.substring(2));
            }
            if (dynamicChild) {
                return Map.of("dynamic", true, "encoded", data);
            }
        }
        StringBuilder encoded = new StringBuilder();
        for (Map<String, Object> p : preparedParams) {
            encoded.append(stripHexPrefix((String) p.get("encoded")));
        }

        return Map.of("dynamic", false, "encoded", "0x" + encoded.toString());
    }

    private String encodeParams(List<Map<String, Object>> preparedParams) {
        int staticSize = 0;
        for (Map<String, Object> param : preparedParams) {
            boolean dynamic = (Boolean) param.get("dynamic");
            if (dynamic) {
                staticSize += 32;
            } else {
                staticSize += (stripHexPrefix((String) param.get("encoded")).length()) / 2;
            }
        }

        List<String> staticParams = new ArrayList<>();
        List<String> dynamicParams = new ArrayList<>();
        int dynamicSize = 0;
        for (Map<String, Object> param : preparedParams) {
            boolean dynamic = (Boolean) param.get("dynamic");
            if (dynamic) {
                String offset = String.format("%064x", staticSize + dynamicSize);
                staticParams.add(offset);
                dynamicParams.add(stripHexPrefix((String) param.get("encoded")));
                dynamicSize += (stripHexPrefix((String) param.get("encoded")).length()) / 2;
            } else {
                staticParams.add(stripHexPrefix((String) param.get("encoded")));
            }
        }

        return "0x" + String.join("", staticParams) + String.join("", dynamicParams);
    }

    private Map<String, Object> encodeAddress(String value) throws Exception {
        if (!isValidAddress(value)) {
            throw new Exception("Invalid address: " + value);
        }
        value = stripHexPrefix(value).toLowerCase();

        // Pad the string to 64 characters with leading zeros
        String paddedValue = String.format("%64s", value).replace(' ', '0');

        return Map.of("dynamic", false, "encoded", "0x" + paddedValue);
    }

    private Map<String, Object> encodeBool(Boolean value) {
        String encoded = String.format("%064x", value ? 1 : 0);

        return Map.of("dynamic", false, "encoded", "0x" + encoded);
    }

    private Map<String, Object> encodeNumber(Object value, boolean signed) throws Exception {
        BigInteger bigValue;
        if (value instanceof BigInteger) {
            bigValue = (BigInteger) value;
        } else if (value instanceof Number) {
            bigValue = BigInteger.valueOf(((Number) value).longValue());
        } else if (value instanceof String) {
            bigValue = new BigInteger((String) value);
        } else {
            throw new Exception("Invalid number value");
        }

        if (signed) {
            if (bigValue.compareTo(BigInteger.ZERO) < 0) {
                bigValue = BigInteger.ONE.shiftLeft(256).add(bigValue);
            }
        } else {
            if (bigValue.compareTo(BigInteger.ZERO) < 0) {
                throw new Exception("Negative value provided for unsigned integer type");
            }
        }
        String hex = bigValue.toString(16);
        String encoded = String.format("%064s", hex);

        return Map.of("dynamic", false, "encoded", "0x" + encoded);
    }

    private Map<String, Object> encodeBytes(String value, Map<String, Object> param)
            throws Exception {
        int bytesSize = (stripHexPrefix(value).length()) / 2;
        String paramType = (String) param.get("type");
        String paramSizeStr = paramType.substring(5);
        if (paramSizeStr.isEmpty()) {
            String lengthHex = String.format("%064x", bytesSize);
            String valuePadded = stripHexPrefix(value);
            int padding = (32 - (bytesSize % 32)) % 32;
            if (padding > 0) {
                valuePadded = valuePadded + "0".repeat(padding * 2);
            }

            return Map.of("dynamic", true, "encoded", "0x" + lengthHex + valuePadded);
        }
        int paramSize = Integer.parseInt(paramSizeStr);
        if (bytesSize != paramSize) {
            throw new Exception(
                    "Bytes size mismatch: expected " + paramSize + ", got " + bytesSize);
        }
        String valuePadded = String.format("%-64s", stripHexPrefix(value)).replace(' ', '0');

        return Map.of("dynamic", false, "encoded", "0x" + valuePadded);
    }

    private Map<String, Object> encodeString(String value) {
        String hexValue = Numeric.toHexStringNoPrefix(value.getBytes());
        String lengthHex = String.format("%064x", value.length());
        String valuePadded = hexValue;
        int padding = (32 - (value.length() % 32)) % 32;
        if (padding > 0) {
            valuePadded = valuePadded + "00".repeat(padding);
        }

        return Map.of("dynamic", true, "encoded", "0x" + lengthHex + valuePadded);
    }

    private Map<String, Object> encodeTuple(Object value, Map<String, Object> param)
            throws Exception {
        boolean dynamic = false;
        List<Map<String, Object>> preparedParams = new ArrayList<>();

        List<Map<String, Object>> components = (List<Map<String, Object>>) param.get("components");
        Map<String, Object> valueMap;
        if (value instanceof Map) {
            valueMap = (Map<String, Object>) value;
        } else {
            throw new Exception("Tuple value must be a Map");
        }

        for (Map<String, Object> component : components) {
            String key = (String) component.get("name");
            if (!valueMap.containsKey(key)) {
                throw new Exception("Tuple value missing component: " + key);
            }
            Map<String, Object> preparedParam = prepareParam(component, valueMap.get(key));
            if ((Boolean) preparedParam.get("dynamic")) {
                dynamic = true;
            }
            preparedParams.add(preparedParam);
        }

        if (dynamic) {
            String encoded = encodeParams(preparedParams);
            return Map.of("dynamic", true, "encoded", encoded);
        }

        StringBuilder encoded = new StringBuilder("0x");
        for (Map<String, Object> p : preparedParams) {
            encoded.append(stripHexPrefix((String) p.get("encoded")));
        }

        return Map.of("dynamic", false, "encoded", encoded.toString());
    }
}
