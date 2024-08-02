package org.arkecosystem.crypto.signature;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.secp256k1.api.P256K1KeyPair;
import org.bitcoinj.secp256k1.api.P256k1PrivKey;
import org.bitcoinj.secp256k1.api.Secp256k1;

public class SchnorrSigner implements Signer {
    @Override
    public byte[] sign(byte[] message, ECKey privateKey) {

        Secp256k1 secp = Secp256k1.get();

        // @TODO: check if this is the correct way to get the private key
        // String privateKeyHex = privateKey.getPrivateKeyAsHex();

        byte[] privKeyBytes = hexStringToByteArray(privateKey.getPrivateKeyAsHex());

        P256k1PrivKey privKey = new P256k1PrivKeyImpl(privKeyBytes);

        P256K1KeyPair keyPair = secp.ecKeyPairCreate(privKey);

        return secp.schnorrSigSign32(message, keyPair);
    }

    // Helper method to convert hex string to byte array
    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] =
                    (byte)
                            ((Character.digit(s.charAt(i), 16) << 4)
                                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
