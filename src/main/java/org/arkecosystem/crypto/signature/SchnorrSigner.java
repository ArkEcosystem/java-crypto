package org.arkecosystem.crypto.signature;

import org.arkecosystem.crypto.utils.Schnorr;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.secp256k1.api.P256K1KeyPair;
import org.bitcoinj.secp256k1.api.P256k1PrivKey;
import org.bitcoinj.secp256k1.api.Secp256k1;

public class SchnorrSigner implements Signer {
    @Override
    public byte[] sign(byte[] message, ECKey privateKey) {

        Secp256k1 secp = Secp256k1.get();

        byte[] privKeyBytes = Schnorr.hexStringToByteArray(privateKey.getPrivateKeyAsHex());

        P256k1PrivKey privKey = new P256k1PrivKeyImpl(privKeyBytes);

        P256K1KeyPair keyPair = secp.ecKeyPairCreate(privKey);

        return secp.schnorrSigSign32(message, keyPair);
    }
}
