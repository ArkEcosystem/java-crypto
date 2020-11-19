package org.arkecosystem.crypto.signature;

import org.arkecosystem.crypto.Schnorr;
import org.bitcoinj.core.ECKey;


public class SchnorrVerifier implements Verifier {
    @Override
    public boolean verify(byte[] hash, ECKey keys, byte[] signature) {
        return Schnorr.schnorrVerify(hash, keys.getPubKey(), signature);
    }
}
