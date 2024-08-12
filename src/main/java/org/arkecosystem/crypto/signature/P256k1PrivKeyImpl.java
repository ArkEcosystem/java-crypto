package org.arkecosystem.crypto.signature;

import java.math.BigInteger;
import java.util.Arrays;
import org.bitcoinj.secp256k1.api.P256k1PrivKey;

public class P256k1PrivKeyImpl implements P256k1PrivKey {
    private final byte[] encoded;
    private boolean destroyed;

    public P256k1PrivKeyImpl(byte[] encoded) {
        if (encoded.length != 32) {
            throw new IllegalArgumentException("Private key must be 32 bytes");
        }
        this.encoded = Arrays.copyOf(encoded, encoded.length);
        this.destroyed = false;
    }

    @Override
    public byte[] getEncoded() {
        if (destroyed) {
            throw new IllegalStateException("Private key has been destroyed");
        }
        return Arrays.copyOf(encoded, encoded.length);
    }

    @Override
    public void destroy() {
        Arrays.fill(encoded, (byte) 0);
        this.destroyed = true;
    }

    @Override
    public BigInteger getS() {
        return toInteger(getEncoded());
    }

    /**
     * Converts a byte array into a BigInteger. Assumes the byte array represents an unsigned
     * integer in big-endian order.
     *
     * @param bytes the byte array to convert.
     * @return the BigInteger representation.
     */
    private BigInteger toInteger(byte[] bytes) {
        // BigInteger interprets the byte array as a signed integer in big-endian order.
        // To handle this correctly for unsigned values (like private keys), we prepend a zero byte
        // to ensure the value is always positive.
        byte[] unsignedBytes = new byte[bytes.length + 1];
        unsignedBytes[0] = 0; // Leading zero for positive sign
        System.arraycopy(bytes, 0, unsignedBytes, 1, bytes.length);
        return new BigInteger(unsignedBytes);
    }
}
