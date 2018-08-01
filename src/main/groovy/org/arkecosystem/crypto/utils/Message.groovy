package org.arkecosystem.crypto.utils

import org.arkecosystem.crypto.identities.*
import com.google.gson.Gson
import org.bitcoinj.core.*
import com.google.common.io.BaseEncoding

import static com.google.common.io.BaseEncoding.*

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
            base16().lowerCase().encode(privateKey.sign(messageBytes).encodeToDER()),
            message
        )
    }

    boolean verify()
    {
        ECKey keys = ECKey.fromPublicOnly(base16().lowerCase().decode(this.publickey))

        byte[] signature = base16().lowerCase().decode this.signature
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
