package org.arkecosystem.crypto.utils;

import org.arkecosystem.crypto.encoding.Hex;
import org.bitcoinj.core.Sha256Hash;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TransactionHasher {

    /**
     * Generates the transaction hash.
     *
     * @param transaction   The transaction data.
     * @param skipSignature Whether to skip the signature fields.
     * @return The hash of the transaction.
     */
    public static byte[] toHash(Map<String, Object> transaction, boolean skipSignature) {
        // Process recipientAddress
        String hex = ((String) transaction.get("recipientAddress")).replaceFirst("^0x", "");
        if (hex.length() % 2 != 0) {
            hex = "0" + hex;
        }
        byte[] recipientAddress = Hex.decode(hex);

        // Build the fields array
        List<Object> fields = new ArrayList<>();
        fields.add(toBeArray(new BigInteger(transaction.get("network").toString())));
        fields.add(toBeArray(new BigInteger(transaction.get("nonce").toString())));
        fields.add(toBeArray(new BigInteger(transaction.get("gasPrice").toString()))); // maxPriorityFeePerGas
        fields.add(toBeArray(new BigInteger(transaction.get("gasPrice").toString()))); // maxFeePerGas
        fields.add(toBeArray(new BigInteger(transaction.get("gasLimit").toString())));
        fields.add(recipientAddress);
        fields.add(toBeArray(new BigInteger(transaction.get("value").toString())));
        String dataHex = transaction.get("data") != null ? ((String) transaction.get("data")).replaceFirst("^0x", "") : "";
        byte[] data = Hex.decode(dataHex);
        fields.add(data);
        fields.add(new ArrayList<>()); // Access list is unused

        if (!skipSignature) {
            byte[] signatureBuffer = Hex.decode((String) transaction.get("signature"));
            byte[] r = Arrays.copyOfRange(signatureBuffer, 0, 32);
            byte[] s = Arrays.copyOfRange(signatureBuffer, 32, 64);
            int v = signatureBuffer[64] & 0xFF;

            fields.add(toBeArray(BigInteger.valueOf(v)));
            fields.add(r);
            fields.add(s);
        }

        byte eip1559Prefix = 0x02; // Marker for Type 2 (EIP-1559) transaction

        byte[] encoded = encodeRlp(fields);

        byte[] hashInput = new byte[1 + encoded.length];
        hashInput[0] = eip1559Prefix;
        System.arraycopy(encoded, 0, hashInput, 1, encoded.length);

        // Use SHA256 for hashing
        return Sha256Hash.hash(hashInput);
    }

    /**
     * Converts a big integer to a big-endian byte array without leading zero bytes.
     *
     * @param value The big integer value.
     * @return The byte array representation.
     */
    private static byte[] toBeArray(BigInteger value) {
        if (value.equals(BigInteger.ZERO)) {
            return new byte[0]; // Empty array represents zero
        }

        byte[] temp = value.toByteArray();
        // Remove leading zero byte if present
        if (temp[0] == 0x00) {
            temp = Arrays.copyOfRange(temp, 1, temp.length);
        }
        return temp;
    }

    /**
     * Encodes the length for RLP encoding.
     *
     * @param len The length to encode.
     * @return The encoded length as a byte array.
     */
    private static byte[] encodeLength(int len) {
        if (len == 0) {
            return new byte[0];
        }

        List<Byte> lenBytes = new ArrayList<>();
        while (len > 0) {
            lenBytes.add(0, (byte) (len & 0xFF));
            len >>= 8;
        }

        byte[] result = new byte[lenBytes.size()];
        for (int i = 0; i < lenBytes.size(); i++) {
            result[i] = lenBytes.get(i);
        }
        return result;
    }

    /**
     * RLP encoding function.
     *
     * @param input The input to encode.
     * @return The RLP-encoded byte array.
     */
    private static byte[] encodeRlp(Object input) {
        try {
            if (input instanceof byte[]) {
                byte[] inputBytes = (byte[]) input;
                int len = inputBytes.length;
                if (len == 1 && (inputBytes[0] & 0xFF) <= 0x7F) {
                    return inputBytes;
                } else if (len <= 55) {
                    byte[] result = new byte[1 + len];
                    result[0] = (byte) (0x80 + len);
                    System.arraycopy(inputBytes, 0, result, 1, len);
                    return result;
                } else {
                    byte[] lenBytes = encodeLength(len);
                    byte[] result = new byte[1 + lenBytes.length + len];
                    result[0] = (byte) (0xB7 + lenBytes.length);
                    System.arraycopy(lenBytes, 0, result, 1, lenBytes.length);
                    System.arraycopy(inputBytes, 0, result, 1 + lenBytes.length, len);
                    return result;
                }
            } else if (input instanceof String) {
                byte[] inputBytes = ((String) input).getBytes("UTF-8");
                return encodeRlp(inputBytes);
            } else if (input instanceof List) {
                @SuppressWarnings("unchecked")
                List<Object> inputList = (List<Object>) input;
                byte[] output = new byte[0];
                for (Object item : inputList) {
                    byte[] encodedItem = encodeRlp(item);
                    output = concatenate(output, encodedItem);
                }
                int len = output.length;
                if (len <= 55) {
                    byte[] result = new byte[1 + len];
                    result[0] = (byte) (0xC0 + len);
                    System.arraycopy(output, 0, result, 1, len);
                    return result;
                } else {
                    byte[] lenBytes = encodeLength(len);
                    byte[] result = new byte[1 + lenBytes.length + len];
                    result[0] = (byte) (0xF7 + lenBytes.length);
                    System.arraycopy(lenBytes, 0, result, 1, lenBytes.length);
                    System.arraycopy(output, 0, result, 1 + lenBytes.length, len);
                    return result;
                }
            } else if (input instanceof BigInteger) {
                return encodeRlp(toBeArray((BigInteger) input));
            } else if (input == null) {
                return encodeRlp(new byte[0]);
            } else {
                // Handle numbers and other types as strings
                return encodeRlp(input.toString().getBytes("UTF-8"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error in RLP encoding", e);
        }
    }

    /**
     * Helper method to concatenate two byte arrays.
     *
     * @param a First byte array.
     * @param b Second byte array.
     * @return Concatenated byte array.
     */
    private static byte[] concatenate(byte[] a, byte[] b) {
        byte[] output = new byte[a.length + b.length];
        System.arraycopy(a, 0, output, 0, a.length);
        System.arraycopy(b, 0, output, a.length, b.length);
        return output;
    }
}
