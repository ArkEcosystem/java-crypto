package org.arkecosystem.crypto.utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.arkecosystem.crypto.configuration.Network;
import org.arkecosystem.crypto.encoding.Hex;
import org.web3j.crypto.Hash;

public class TransactionUtils {

    public static byte[] toBuffer(Map<String, Object> transaction, boolean skipSignature) {
        String toHex = parseHexFromStr((String) transaction.get("recipientAddress"));
        if (toHex.length() % 2 != 0) {
            toHex = "0" + toHex;
        }
        byte[] toBytes = hexToBytes(toHex.toLowerCase());

        List<Object> fields = new ArrayList<>();
        fields.add(RlpEncoder.toBeArray(new BigInteger(transaction.get("nonce").toString())));
        fields.add(RlpEncoder.toBeArray(new BigInteger(transaction.get("gasPrice").toString())));
        fields.add(RlpEncoder.toBeArray(new BigInteger(transaction.get("gasLimit").toString())));
        fields.add(toBytes);
        fields.add(RlpEncoder.toBeArray(new BigInteger(transaction.get("value").toString())));

        String dataHex =
                transaction.get("data") != null
                        ? parseHexFromStr((String) transaction.get("data"))
                        : "";
        byte[] data = dataHex.isEmpty() ? new byte[0] : hexToBytes(dataHex);
        fields.add(data);

        if (!skipSignature
                && transaction.containsKey("signature")
                && transaction.get("signature") != null) {
            byte[] signatureBytes = Hex.decode((String) transaction.get("signature"));
            byte[] r = Arrays.copyOfRange(signatureBytes, 0, 32);
            byte[] s = Arrays.copyOfRange(signatureBytes, 32, 64);
            int v = signatureBytes[64] & 0xFF;

            int chainId = Network.get().chainId();
            fields.add(RlpEncoder.toBeArray(BigInteger.valueOf(v + chainId * 2 + 35)));
            fields.add(r);
            fields.add(s);
        } else {
            int chainId = Network.get().chainId();
            fields.add(RlpEncoder.toBeArray(BigInteger.valueOf(chainId)));
            fields.add(RlpEncoder.toBeArray(BigInteger.ZERO));
            fields.add(RlpEncoder.toBeArray(BigInteger.ZERO));
        }

        return RlpEncoder.encode(fields);
    }

    public static byte[] toHash(Map<String, Object> transaction, boolean skipSignature) {
        byte[] buffer = toBuffer(transaction, skipSignature);
        return Hash.sha3(buffer);
    }

    public static String getId(Map<String, Object> transaction) {
        byte[] hash = toHash(transaction, false);
        return Hex.encode(hash);
    }

    private static String parseHexFromStr(String value) {
        if (value == null) return "";
        return value.replaceFirst("^0x", "");
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
