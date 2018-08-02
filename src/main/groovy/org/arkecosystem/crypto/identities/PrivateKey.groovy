package org.arkecosystem.crypto.identities

import org.bitcoinj.core.*
import org.arkecosystem.crypto.encoding.*

class PrivateKey {
    static ECKey fromPassphrase(String passphrase) {
        byte[] sha256 = Sha256Hash.hash(passphrase.getBytes())

        return ECKey.fromPrivate(sha256, true)
    }

    static ECKey fromHex(String privateKey) {
        return ECKey.fromPrivate(Hex.decode(privateKey), true)
    }
}
