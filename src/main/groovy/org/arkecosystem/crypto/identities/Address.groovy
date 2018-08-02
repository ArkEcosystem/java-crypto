package org.arkecosystem.crypto.identities

import org.arkecosystem.crypto.encoding.Hex
import org.arkecosystem.crypto.encoding.Base58 as B58
import org.arkecosystem.crypto.configuration.Network
import org.bitcoinj.core.*
import org.spongycastle.crypto.digests.RIPEMD160Digest

class Address {
    static String fromPassphrase(String passphrase, Integer networkVersion = null) {
        fromPrivateKey(PrivateKey.fromPassphrase(passphrase), networkVersion)
    }

    static String fromPublicKey(String publicKey, Integer networkVersion = null) {
        byte[] publicKeyBytes = Hex.decode(publicKey)

        RIPEMD160Digest digest = new RIPEMD160Digest()
        digest.update(publicKeyBytes, 0, publicKeyBytes.length)
        byte[] out = new byte[20]
        digest.doFinal(out, 0)

        if(!networkVersion) {
            networkVersion = Network.get().version()
        }

        new VersionedChecksummedBytes(networkVersion, out).toBase58()
    }

    static String fromPrivateKey(ECKey privateKey, Integer networkVersion = null) {
        fromPublicKey(privateKey.getPublicKeyAsHex(), networkVersion)
    }

    static Boolean validate(String address, Integer networkVersion = null) {
        if(!networkVersion) {
            networkVersion = Network.get().version()
        }

        B58.decodeChecked(address)[0] == networkVersion
    }
}
