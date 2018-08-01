package org.arkecosystem.crypto.identities

import org.arkecosystem.crypto.configuration.Network
import org.bitcoinj.core.*

class WIF {
    static String fromPassphrase(String passphrase) {
        byte[] secret = Sha256Hash.hash(passphrase.getBytes())

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        outputStream.write secret
        outputStream.write 0x01

        new VersionedChecksummedBytes(Network.get().wif(), outputStream.toByteArray()).toBase58()
    }
}
