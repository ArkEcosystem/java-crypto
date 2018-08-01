package org.arkecosystem.crypto.identities

import org.bitcoinj.core.ECKey
import org.bitcoinj.core.Sha256Hash

class PrivateKey {
    static ECKey fromPassphrase(String passphrase) {
        byte[] sha256 = Sha256Hash.hash(passphrase.getBytes())

        return ECKey.fromPrivate(sha256, true)
    }
}
