package org.arkecosystem.crypto.signature;

import java.util.Arrays;
import org.bitcoinj.secp256k1.api.CompressedPubKeyData;

public class CompressedPubKeyDataImpl implements CompressedPubKeyData {

    private final byte[] bytes;

    public CompressedPubKeyDataImpl(byte[] bytes) {
        if (bytes.length != 33) {
            throw new IllegalArgumentException("Compressed public key must be 33 bytes");
        }
        this.bytes = Arrays.copyOf(bytes, bytes.length);
    }

    @Override
    public byte[] bytes() {
        return Arrays.copyOf(bytes, bytes.length);
    }
}
