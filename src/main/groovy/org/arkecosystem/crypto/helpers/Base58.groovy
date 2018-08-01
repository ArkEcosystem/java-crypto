package org.arkecosystem.crypto.helpers

import org.bitcoinj.core.Sha256Hash
import org.bitcoinj.core.Base58 as BTCBase58
import static com.google.common.io.BaseEncoding.base16

class Base58 {
    static String encodeChecked(byte[] value) {
        def checksum = base16().lowerCase().encode(Sha256Hash.hashTwice(value)).substring(0, 8)
        def recipient = base16().lowerCase().encode(value)

        BTCBase58.encode(base16().lowerCase().decode(recipient + checksum))
    }
}
