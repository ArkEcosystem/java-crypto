package org.arkecosystem.crypto.encoding

import org.bitcoinj.core.Sha256Hash
import org.bitcoinj.core.Base58 as BTCBase58
import org.arkecosystem.crypto.encoding.*

class Base58 {
    static String encodeChecked(byte[] value) {
        def checksum = Hex.encode(Sha256Hash.hashTwice(value)).substring(0, 8)
        def recipient = Hex.encode(value)

        BTCBase58.encode(Hex.decode(recipient + checksum))
    }

    static byte[] decodeChecked(String value) {
        BTCBase58.decodeChecked value
    }
}
