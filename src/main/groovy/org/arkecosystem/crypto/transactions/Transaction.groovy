package org.arkecosystem.crypto.transactions

import com.google.gson.Gson
import org.arkecosystem.crypto.enums.Types
import org.arkecosystem.crypto.identities.PrivateKey
import org.bitcoinj.core.Base58
import org.bitcoinj.core.ECKey
import org.bitcoinj.core.Sha256Hash

import java.nio.ByteBuffer
import java.nio.ByteOrder

import static com.google.common.io.BaseEncoding.base16

class Transaction extends Object {
    int expiration
    int network
    int timestamp
    int type
    int version
    List<String> signatures
    Long amount = 0
    Long fee
    Map<String, Object> asset = [:]
    String id
    String recipientId
    String secondSignature
    String senderPublicKey
    String signature
    String signSignature
    String vendorField
    String vendorFieldHex

    String getId() {
        base16().lowerCase().encode Sha256Hash.hash(toBytes(false, false))
    }

    Transaction sign(String passphrase) {
        ECKey privateKey = PrivateKey.fromPassphrase(passphrase)

        this.senderPublicKey = privateKey.getPublicKeyAsHex()
        this.signature = base16().lowerCase().encode(privateKey.sign(Sha256Hash.of(toBytes())).encodeToDER())

        return this
    }

    Transaction secondSign(String passphrase) {
        ECKey privateKey = PrivateKey.fromPassphrase(passphrase)

        this.signSignature = base16().lowerCase().encode(privateKey.sign(Sha256Hash.of(toBytes(false))).encodeToDER())

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

    Transaction parseSignatures(String serialized, int startOffset)
    {
        this.signature = serialized.substring(startOffset)

        def multiSignatureOffset = 0

        if (this.signature.length() == 0) {
            this.signature = null
        } else {
            def signatureLength = Integer.parseInt(this.signature.substring(2, 4), 16) + 2
            this.signature       = serialized.substring(startOffset, startOffset + signatureLength * 2)
            multiSignatureOffset += signatureLength * 2
            this.secondSignature = serialized.substring(startOffset + signatureLength * 2)

            if(this.secondSignature.length() == 0) {
                this.secondSignature = null
            } else {
                if ('ff' == this.secondSignature.substring(0, 2)) {
                    this.secondSignature = null
                } else {
                    def secondSignatureLength = Integer.parseInt(this.secondSignature.substring(2, 4), 16) + 2
                    this.secondSignature = this.secondSignature.substring(0, secondSignatureLength * 2)
                    multiSignatureOffset += secondSignatureLength * 2
                }
            }

            def signatures = serialized.substring(startOffset + multiSignatureOffset)

            if (signatures.length() == 0) {
                return this
            }

            if ('ff' != signatures.substring(0, 2)) {
                return this
            }

            signatures      = signatures.substring(2)
            this.signatures = []

            def moreSignatures = true
            while (moreSignatures) {
                def mLength = Integer.parseInt(signatures.substring(2, 4), 16) + 2

                if (mLength > 0) {
                    this.signatures.add(signatures.substring(0, (mLength + 2) * 2))
                } else {
                    moreSignatures = false
                }

                signatures = signatures.substring((mLength + 2) * 2)
            }
        }

        this
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

        if (this.type == Types.SECOND_SIGNATURE_REGISTRATION.getValue()) {
            buffer.put base16().lowerCase().decode(asset.signature.publicKey)
        }

        if (this.type == Types.DELEGATE_REGISTRATION.getValue()) {
            buffer.put asset.delegate.username.bytes
        }

        if (this.type == Types.VOTE.getValue()) {
            buffer.put asset.votes.join("").bytes
        }

        if (this.type == Types.MULTI_SIGNATURE_REGISTRATION.getValue()) {
            buffer.put base16().lowerCase().decode(asset.signature)
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
