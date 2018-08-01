package org.arkecosystem.crypto.transactions

import org.arkecosystem.crypto.enums.Types
import org.arkecosystem.crypto.identities.*
import com.google.gson.Gson
import java.nio.ByteBuffer
import java.nio.ByteOrder
import org.bitcoinj.core.Base58
import org.bitcoinj.core.ECKey
import org.bitcoinj.core.Sha256Hash
import static com.google.common.io.BaseEncoding.base16

class Transaction extends Object {
    String id
    int version
    int network
    int timestamp
    String recipientId
    Long amount
    Long fee
    int type
    String vendorField
    String signature
    String signSignature
    String senderPublicKey
    Map<String, Object> asset = [:]

    String getId() {
        base16().lowerCase().encode Sha256Hash.hash(toBytes(false, false))
    }

    Transaction sign(String passphrase) {
        ECKey privateKey = PrivateKey.fromPassphrase(passphrase)

        byte[] bytes = toBytes()
        privateKey.sign(Sha256Hash.of(bytes))

        senderPublicKey = base16().lowerCase().encode(privateKey.getPubKey())
        signature = base16().lowerCase().encode(privateKey.sign(Sha256Hash.of(bytes)).encodeToDER())

        return this
    }

    Transaction secondSign(String passphrase) {
        ECKey privateKey = PrivateKey.fromPassphrase(passphrase)

        byte[] bytes = toBytes(false)

        signSignature = base16().lowerCase().encode(privateKey.sign(Sha256Hash.of(bytes)).encodeToDER())

        return this
    }

    boolean verify() {
        ECKey keys = ECKey.fromPublicOnly(base16().lowerCase().decode(senderPublicKey))

        byte[] signature = base16().lowerCase().decode(signature)
        byte[] bytes = toBytes()

        return ECKey.verify(Sha256Hash.hash(bytes), signature, keys.getPubKey())
    }

    boolean secondVerify(String secondPublicKey) {
        ECKey keys = ECKey.fromPublicOnly base16().lowerCase().decode(secondPublicKey)

        byte[] signature = base16().lowerCase().decode(this.signSignature)
        byte[] bytes = toBytes(false)

        return ECKey.verify(Sha256Hash.hash(bytes), signature, keys.getPubKey())
    }

    byte[] toBytes(boolean skipSignature = true, boolean skipSecondSignature = true) {
        ByteBuffer buffer = ByteBuffer.allocate(1000)
        buffer.order(ByteOrder.LITTLE_ENDIAN)

        buffer.put type.byteValue()
        buffer.putInt timestamp
        buffer.put base16().lowerCase().decode(senderPublicKey)

        if (recipientId) {
            buffer.put Base58.decodeChecked(recipientId)
        } else {
            buffer.put new byte[21]
        }

        if (vendorField) {
            byte[] vbytes = vendorField.bytes
            if (vbytes.size() < 65) {
                buffer.put vbytes
                buffer.put new byte[64 - vbytes.size()]
            }
        } else {
            buffer.put new byte[64]
        }

        buffer.putLong amount
        buffer.putLong fee

        if (type == Types.SECOND_SIGNATURE_REGISTRATION.getValue()) {
            buffer.put base16().lowerCase().decode(asset?.get("signature")?.get("publicKey"))
        }

        if (type == Types.DELEGATE_REGISTRATION.getValue()) {
            buffer.put asset.username.bytes
        }

        if (type == Types.VOTE.getValue()) {
            buffer.put asset.votes.join("").bytes
        }

        if (type == Types.MULTI_SIGNATURE_REGISTRATION.getValue()) {
          buffer.put BaseEncoding.base16().lowerCase().decode(asset.signature)
        }

        if (!skipSignature && signature) {
            buffer.put base16().lowerCase().decode(signature)
        }
        if (!skipSecondSignature && signSignature) {
            buffer.put base16().lowerCase().decode(signSignature)
        }

        def result = new byte[buffer.position()]
        buffer.rewind()
        buffer.get(result)
        return result
    }

    String toObject() {
        this.properties.subMap([
            'id', 'timestamp', 'recipientId', 'amount', 'fee', 'type', 'asset',
            'vendorField', 'signature', 'signSignature', 'senderPublicKey'
        ])
    }

    String toJson() {
        new Gson().toJson(toObject())
    }
}
