package org.arkecosystem.crypto.identities;

import java.math.BigInteger;
import java.util.Arrays;
import org.arkecosystem.crypto.encoding.Hex;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;

public class PublicKey {
    public static String fromPassphrase(String passphrase) {
        return PrivateKey.fromPassphrase(passphrase).getPublicKeyAsHex();
    }

    public static ECKey fromHex(String publicKey) {
        ECKey key = ECKey.fromPublicOnly(Hex.decode(publicKey));
        return ECKey.fromPublicOnly(key.getPubKeyPoint().getEncoded(true));
    }

    public static ECKey recover(byte[] message, byte[] signature) {
        if (signature == null || signature.length != 65) {
            throw new IllegalArgumentException(
                    "Signature must be a 65-byte compact signature (R || S || recId).");
        }

        byte recId = signature[64];
        BigInteger r = new BigInteger(1, Arrays.copyOfRange(signature, 0, 32));
        BigInteger s = new BigInteger(1, Arrays.copyOfRange(signature, 32, 64));

        ECKey.ECDSASignature ecdsa = new ECKey.ECDSASignature(r, s);
        ECKey recovered = ECKey.recoverFromSignature(recId, ecdsa, Sha256Hash.wrap(message), true);
        if (recovered == null) {
            throw new RuntimeException("Could not recover public key from signature.");
        }

        return recovered;
    }
}
