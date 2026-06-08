package org.arkecosystem.crypto.identities;

import java.io.ByteArrayOutputStream;
import org.arkecosystem.crypto.encoding.Base58;
import org.arkecosystem.crypto.encoding.Hex;
import org.bitcoinj.core.ECKey;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;

public class LegacyAddress {
    public static String fromPassphrase(String passphrase, int pubKeyHash) {
        ECKey privateKey = PrivateKey.fromPassphrase(passphrase);
        return fromPrivateKey(privateKey, pubKeyHash);
    }

    public static String fromPublicKey(String publicKey, int pubKeyHash) {
        byte[] ripemd160 = ripemd160(Hex.decode(publicKey));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(pubKeyHash);
        outputStream.write(ripemd160, 0, ripemd160.length);

        return Base58.encodeChecked(outputStream.toByteArray());
    }

    public static String fromPrivateKey(ECKey privateKey, int pubKeyHash) {
        return fromPublicKey(Hex.encode(privateKey.getPubKey()), pubKeyHash);
    }

    public static boolean validate(String address, int pubKeyHash) {
        try {
            byte[] decoded = Base58.decodeChecked(address);

            if (decoded.length != 21) {
                return false;
            }

            return (decoded[0] & 0xFF) == pubKeyHash;
        } catch (Exception e) {
            return false;
        }
    }

    private static byte[] ripemd160(byte[] input) {
        RIPEMD160Digest digest = new RIPEMD160Digest();
        digest.update(input, 0, input.length);
        byte[] output = new byte[digest.getDigestSize()];
        digest.doFinal(output, 0);
        return output;
    }
}
