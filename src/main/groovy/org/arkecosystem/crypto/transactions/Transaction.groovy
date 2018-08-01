package org.arkecosystem.crypto.transactions

import com.google.gson.Gson
import java.nio.ByteBuffer
import java.nio.ByteOrder
import org.bitcoinj.core.Base58
import org.bitcoinj.core.ECKey
import org.bitcoinj.core.Sha256Hash
import static com.google.common.io.BaseEncoding.base16

class Transaction extends Object {
    String getId() {
        base16().lowerCase().encode Sha256Hash.hash(toBytes(false, false))
    }

    Transaction sign(String passphrase) {
        ECKey privateKey = PrivateKey.fromPassphrase(passphrase)

        byte[] txbytes = toBytes(t)
        privateKey.sign(Sha256Hash.of(bytes))

        senderPublicKey = base16().lowerCase().encode(privateKey.getPubKey())
        signature = base16().lowerCase().encode(privateKey.sign(Sha256Hash.of(bytes)).encodeToDER())

        return this
    }

    Transaction secondSign(String passphrase) {
        ECKey privateKey = PrivateKey.fromPassphrase(passphrase)

        byte[] txbytes = toBytes(false)

        signSignature = base16().lowerCase().encode(privateKey.sign(Sha256Hash.of(txbytes)).encodeToDER())

        return this
    }

    boolean verify() {
        ECKey keys = ECKey.fromPublicOnly(base16().lowerCase().decode(t.senderPublicKey))

        byte[] signature = base16().lowerCase().decode(t.signature)
        byte[] bytes = toBytes(t)

        return ECKey.verify(Sha256Hash.hash(bytes), signature, keys.getPubKey())
    }

    boolean secondVerify(String secondPublicKey) {
        ECKey keys = ECKey.fromPublicOnly base16().lowerCase().decode(secondPublicKeyHex)

        byte[] signature = base16().lowerCase().decode(t.signSignature)
        byte[] bytes = toBytes(false)

        return ECKey.verify(Sha256Hash.hash(bytes), signature, keys.getPubKey())
    }

    byte[] toBytes(boolean skipSignature = true, boolean skipSecondSignature = true) {
        ByteBuffer buffer = ByteBuffer.allocate(1000)
        buffer.order(ByteOrder.LITTLE_ENDIAN)

        buffer.put type.getByteValue()
        buffer.putInt timestamp
        buffer.put base16().lowerCase().decode(senderPublicKey)

        if (requesterPublicKey) {
            buffer.put base16().lowerCase().decode(requesterPublicKey)
        }

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

        if (type == TransactionType.SECONDSIGNATURE) {
            buffer.put base16().lowerCase().decode(asset?.get("signature")?.get("publicKey"))
        }

        if (type == TransactionType.DELEGATE) {
            buffer.put asset.username.bytes
        }

        if (type == TransactionType.VOTE) {
            buffer.put asset.votes.join("").bytes
        }

        if(type==4){
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
        new Gson().toJson(this)
    }
}
