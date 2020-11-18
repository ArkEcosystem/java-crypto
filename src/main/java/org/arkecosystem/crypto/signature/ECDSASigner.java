package org.arkecosystem.crypto.signature;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.SignatureDecodeException;

public class ECDSASigner implements Signer {
    @Override
    public byte[] sign(byte[] message, ECKey privateKey) {
        return privateKey
            .sign(Sha256Hash.wrap(message))
            .encodeToDER();
    }

    @Override
    public boolean verify(byte[] hash, ECKey key, byte[] signature) {
        try {
            return ECKey.verify(hash, signature, key.getPubKey());
        } catch (SignatureDecodeException e) {
            return false;
        }
    }
}
