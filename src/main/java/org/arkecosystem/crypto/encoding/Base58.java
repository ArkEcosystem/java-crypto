package org.arkecosystem.crypto.encoding;

import org.bitcoinj.core.Sha256Hash;

public class Base58 {
    public static String encodeChecked(byte[] value) {
        String checksum = Hex.encode(Sha256Hash.hashTwice(value)).substring(0, 8);
        String recipient = Hex.encode(value);

        return org.bitcoinj.core.Base58.encode(Hex.decode(recipient + checksum));
    }

    public static byte[] decodeChecked(String value) {
        return org.bitcoinj.core.Base58.decodeChecked(value);
    }

    public static byte[] decode(String value) {
        return org.bitcoinj.core.Base58.decode(value);
    }

    public static String encode(byte[] value) {
        return org.bitcoinj.core.Base58.encode(value);
    }
}
