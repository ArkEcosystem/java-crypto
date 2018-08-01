package org.arkecosystem.crypto.identities

import org.bitcoinj.core.*
import static com.google.common.io.BaseEncoding.base16

class PrivateKey {
    static ECKey fromPassphrase(String passphrase) {
        byte[] sha256 = Sha256Hash.hash(passphrase.getBytes())

        return ECKey.fromPrivate(sha256, true)
    }

    static ECKey fromHex(String privateKey) {
        return ECKey.fromPrivate(base16().lowerCase().decode(privateKey), true)
    }
}
