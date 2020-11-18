package org.arkecosystem.crypto.signature;


import org.bitcoinj.core.ECKey;

public interface Signer {

    byte[] sign(byte[] message, ECKey key);

}
