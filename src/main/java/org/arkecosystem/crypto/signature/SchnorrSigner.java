package org.arkecosystem.crypto.signature;

import org.arkecosystem.crypto.Schnorr;
import org.bitcoinj.core.ECKey;


public class SchnorrSigner implements Signer {
    @Override
    public byte[] sign(byte[] message, ECKey privateKey) {
        return Schnorr.schnorrSign(message, privateKey.getPrivKey());
    }

    @Override
    public boolean verify(byte[] hash, ECKey keys, byte[] signature) {
        return Schnorr.schnorrVerify(hash, keys.getPubKey(), signature);
    }
}
