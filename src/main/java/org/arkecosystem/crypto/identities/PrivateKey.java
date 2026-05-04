package org.arkecosystem.crypto.identities;

import java.math.BigInteger;
import java.util.Arrays;
import org.arkecosystem.crypto.configuration.Network;
import org.arkecosystem.crypto.encoding.Base58;
import org.arkecosystem.crypto.encoding.Hex;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;

public class PrivateKey {
    public static ECKey fromPassphrase(String passphrase) {
        byte[] sha256 = Sha256Hash.hash(passphrase.getBytes());

        return ECKey.fromPrivate(sha256, true);
    }

    public static ECKey fromHex(String privateKey) {
        return ECKey.fromPrivate(Hex.decode(privateKey), true);
    }

    public static ECKey fromWif(String wif) {
        byte[] decoded = Base58.decodeChecked(wif);

        if (decoded.length < 33) {
            throw new IllegalArgumentException("Invalid WIF: payload too short.");
        }

        int expectedVersion = Network.get().wif() & 0xff;
        if ((decoded[0] & 0xff) != expectedVersion) {
            throw new IllegalArgumentException(
                    "Invalid WIF: version byte does not match the active network.");
        }

        byte[] privateKeyBytes = Arrays.copyOfRange(decoded, 1, 33);

        return ECKey.fromPrivate(privateKeyBytes, true);
    }

    public static byte[] sign(byte[] message, String passphrase) {
        return sign(message, fromPassphrase(passphrase));
    }

    public static byte[] sign(byte[] message, ECKey privateKey) {
        ECKey.ECDSASignature signature = privateKey.sign(Sha256Hash.wrap(message));

        int recId = -1;
        for (int i = 0; i < 4; i++) {
            ECKey k = ECKey.recoverFromSignature(i, signature, Sha256Hash.wrap(message), true);
            if (k != null && k.getPubKeyPoint().equals(privateKey.getPubKeyPoint())) {
                recId = i;
                break;
            }
        }
        if (recId == -1) {
            throw new RuntimeException("Could not find recId");
        }

        byte[] rBytes = bigIntegerToBytes(signature.r, 32);
        byte[] sBytes = bigIntegerToBytes(signature.s, 32);

        byte[] signatureWithRecId = new byte[65];
        System.arraycopy(rBytes, 0, signatureWithRecId, 0, 32);
        System.arraycopy(sBytes, 0, signatureWithRecId, 32, 32);
        signatureWithRecId[64] = (byte) recId;

        return signatureWithRecId;
    }

    private static byte[] bigIntegerToBytes(BigInteger b, int numBytes) {
        byte[] src = b.toByteArray();
        byte[] dest = new byte[numBytes];
        int srcPos = Math.max(0, src.length - numBytes);
        int destPos = Math.max(0, numBytes - src.length);
        int length = Math.min(src.length, numBytes);
        System.arraycopy(src, srcPos, dest, destPos, length);
        return dest;
    }
}
