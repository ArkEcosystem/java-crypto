package org.arkecosystem.crypto.signature;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.SignatureDecodeException;

public class ECDSAVerifier implements Verifier {
    @Override
    public boolean verify(byte[] hash, ECKey key, byte[] signature) {
        try {
            return ECKey.verify(hash, signature, key.getPubKey());
        } catch (SignatureDecodeException e) {
            return false;
        }
    }
}
