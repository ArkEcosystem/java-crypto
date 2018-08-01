package org.arkecosystem.crypto.identities

import com.google.common.io.BaseEncoding
import org.bitcoinj.core.ECKey
import org.bitcoinj.core.VersionedChecksummedBytes
import org.spongycastle.crypto.digests.RIPEMD160Digest

class Address {
    static String fromPassphrase(String passphrase) {
        fromPrivateKey(PrivateKey.fromPassphrase(passphrase))
    }

    static String fromPublicKey(String publicKey) {
        byte[] publicKeyBytes = BaseEncoding.base16().lowerCase().decode(publicKey)

        RIPEMD160Digest digest = new RIPEMD160Digest()
        digest.update(publicKeyBytes, 0, publicKeyBytes.length)
        byte[] out = new byte[20]
        digest.doFinal(out, 0)

        new VersionedChecksummedBytes(0x1e, out).toBase58()
    }

    static String fromPrivateKey(ECKey privateKey) {
        fromPublicKey(privateKey.getPublicKeyAsHex())
    }

    static Boolean validate(String address) {
        //
    }
}
