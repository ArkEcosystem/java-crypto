package org.arkecosystem.crypto.utils;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class RlpEncoder {

    public static byte[] encode(Object input) {
        if (input instanceof byte[]) {
            return encodeBytes((byte[]) input);
        } else if (input instanceof List) {
            return encodeList((List<?>) input);
        } else if (input instanceof BigInteger) {
            return encodeBytes(toBeArray((BigInteger) input));
        } else if (input instanceof String) {
            String str = (String) input;
            if (str.startsWith("0x")) {
                return encodeBytes(hexToBytes(str.substring(2)));
            }
            return encodeBytes(str.getBytes());
        } else if (input instanceof Integer) {
            return encodeBytes(toBeArray(BigInteger.valueOf((Integer) input)));
        } else if (input instanceof Long) {
            return encodeBytes(toBeArray(BigInteger.valueOf((Long) input)));
        } else {
            return encodeBytes(new byte[0]);
        }
    }

    public static String encodeToHex(Object input) {
        byte[] encoded = encode(input);
        StringBuilder sb = new StringBuilder();
        for (byte b : encoded) {
            sb.append(String.format("%02x", b & 0xFF));
        }
        return sb.toString();
    }

    private static byte[] encodeBytes(byte[] input) {
        int len = input.length;
        if (len == 0) {
            return new byte[] {(byte) 0x80};
        }
        if (len == 1 && (input[0] & 0xFF) <= 0x7F) {
            return input;
        }
        if (len <= 55) {
            byte[] result = new byte[1 + len];
            result[0] = (byte) (0x80 + len);
            System.arraycopy(input, 0, result, 1, len);
            return result;
        }
        byte[] lenBytes = encodeLengthBytes(len);
        byte[] result = new byte[1 + lenBytes.length + len];
        result[0] = (byte) (0xB7 + lenBytes.length);
        System.arraycopy(lenBytes, 0, result, 1, lenBytes.length);
        System.arraycopy(input, 0, result, 1 + lenBytes.length, len);
        return result;
    }

    private static byte[] encodeList(List<?> input) {
        ByteArrayOutputStream payload = new ByteArrayOutputStream();
        for (Object item : input) {
            byte[] encodedItem = encode(item);
            payload.write(encodedItem, 0, encodedItem.length);
        }
        byte[] payloadBytes = payload.toByteArray();
        int len = payloadBytes.length;
        if (len <= 55) {
            byte[] result = new byte[1 + len];
            result[0] = (byte) (0xC0 + len);
            System.arraycopy(payloadBytes, 0, result, 1, len);
            return result;
        }
        byte[] lenBytes = encodeLengthBytes(len);
        byte[] result = new byte[1 + lenBytes.length + len];
        result[0] = (byte) (0xF7 + lenBytes.length);
        System.arraycopy(lenBytes, 0, result, 1, lenBytes.length);
        System.arraycopy(payloadBytes, 0, result, 1 + lenBytes.length, len);
        return result;
    }

    private static byte[] encodeLengthBytes(int len) {
        List<Byte> bytes = new ArrayList<>();
        while (len > 0) {
            bytes.add(0, (byte) (len & 0xFF));
            len >>= 8;
        }
        byte[] result = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            result[i] = bytes.get(i);
        }
        return result;
    }

    public static byte[] toBeArray(BigInteger value) {
        if (value.equals(BigInteger.ZERO)) {
            return new byte[0];
        }
        byte[] temp = value.toByteArray();
        if (temp[0] == 0x00) {
            byte[] trimmed = new byte[temp.length - 1];
            System.arraycopy(temp, 1, trimmed, 0, trimmed.length);
            return trimmed;
        }
        return temp;
    }

    private static byte[] hexToBytes(String hex) {
        if (hex == null || hex.isEmpty()) {
            return new byte[0];
        }
        if (hex.length() % 2 != 0) {
            hex = "0" + hex;
        }
        byte[] result = new byte[hex.length() / 2];
        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
        }
        return result;
    }
}
