package org.arkecosystem.crypto.signature;


import org.bitcoinj.core.ECKey;

public interface Verifier {

    boolean verify(byte[] hash, ECKey key, byte[] signature);

}
