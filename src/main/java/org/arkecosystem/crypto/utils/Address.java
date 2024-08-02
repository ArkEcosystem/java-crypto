package org.arkecosystem.crypto.utils;

import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;

import java.nio.ByteBuffer;

import org.bouncycastle.util.encoders.Hex;

public class Address {

    /**
     * Validate the given address.
     *
     * @param address The address to validate
     * @return true if the address is valid, false otherwise
     */
    public static boolean validate(String address) {
        if (address == null || !address.matches("^0x[a-fA-F0-9]{40}$")) {
            return false;
        }
        return address.equals(Keys.toChecksumAddress(address));
    }

    /**
     * Convert to hex string without 0x prefix.
     *
     * @param address The address to convert
     * @return The hex string without 0x prefix
     */
    public static String toBufferHexString(String address) {
        if (address.startsWith("0x")) {
            return address.substring(2).toLowerCase();
        }
        return address.toLowerCase();
    }

    /**
     * Extract the address from a byte buffer.
     *
     * @param buffer The ByteBuffer containing the address
     * @return The extracted address as a checksum address
     */
    public static String fromByteBuffer(ByteBuffer buffer) {
        byte[] addressBytes = new byte[20];
        buffer.get(addressBytes);
        String hexAddress = "0x" + Hex.toHexString(addressBytes);
        return Keys.toChecksumAddress(hexAddress);
    }
}
