package org.arkecosystem.crypto.signature;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;

public class ECDSASigner implements Signer {
    @Override
    public byte[] sign(byte[] message, ECKey privateKey) {
        return privateKey
            .sign(Sha256Hash.wrap(message))
            .encodeToDER();
    }
}
