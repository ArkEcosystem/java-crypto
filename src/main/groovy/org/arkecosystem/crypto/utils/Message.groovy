package org.arkecosystem.crypto.utils

import com.google.gson.Gson
import org.arkecosystem.crypto.identities.PrivateKey
import org.bitcoinj.core.ECKey
import org.bitcoinj.core.Sha256Hash

import org.arkecosystem.crypto.encoding.*

class Message {
    String publickey
    String signature
    String message

    Message(String publickey, String signature, String message) {
        this.publickey = publickey
        this.signature = signature
        this.message = message
    }

    static Message sign(String message, String passphrase) {
        def privateKey = PrivateKey.fromPassphrase(passphrase)
        def messageBytes = Sha256Hash.of(message.getBytes())

        return new Message(
            privateKey.getPublicKeyAsHex(),
            Hex.encode(privateKey.sign(messageBytes).encodeToDER()),
            message
        )
    }

    boolean verify() {
        ECKey keys = ECKey.fromPublicOnly Hex.decode(this.publickey)

        byte[] signature = Hex.decode this.signature
        byte[] messageBytes = Sha256Hash.hash(this.message.getBytes())

        ECKey.verify(messageBytes, signature, keys.getPubKey())
    }

    Map toMap() {
        this.properties.subMap(['publickey', 'signature', 'message'])
    }

    String toJson() {
        new Gson().toJson(toMap())
    }
}
