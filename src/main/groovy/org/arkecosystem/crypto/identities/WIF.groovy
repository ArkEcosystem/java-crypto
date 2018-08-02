package org.arkecosystem.crypto.identities

import org.arkecosystem.crypto.configuration.Network
import org.arkecosystem.crypto.encoding.Base58
import org.bitcoinj.core.Sha256Hash

class WIF {
    static String fromPassphrase(String passphrase) {
        byte[] secret = Sha256Hash.hash(passphrase.getBytes())

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        outputStream.write Network.get().wif()
        outputStream.write secret
        outputStream.write 0x01

        Base58.encodeChecked(outputStream.toByteArray())
    }
}
