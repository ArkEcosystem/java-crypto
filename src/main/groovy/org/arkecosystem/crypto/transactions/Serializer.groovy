package org.arkecosystem.crypto.transactions

import org.arkecosystem.crypto.configuration.Network
import org.arkecosystem.crypto.enums.Types
import org.arkecosystem.crypto.transactions.serializers.DelegateRegistration
import org.arkecosystem.crypto.transactions.serializers.MultiSignatureRegistration
import org.arkecosystem.crypto.transactions.serializers.SecondSignatureRegistration
import org.arkecosystem.crypto.transactions.serializers.Transfer
import org.arkecosystem.crypto.transactions.serializers.Vote

import java.nio.ByteBuffer
import java.nio.ByteOrder

import org.arkecosystem.crypto.encoding.*
import static javax.xml.bind.DatatypeConverter.parseHexBinary

class Serializer {
    ByteBuffer buffer
    Transaction transaction

    byte[] serialize(Transaction transaction) {
        this.transaction = transaction

        this.buffer = ByteBuffer.allocate(512)
        this.buffer.order(ByteOrder.LITTLE_ENDIAN)

        serializeHeader()
        serializeTypeSpecific()
        serializeSignatures()

        def result = new byte[this.buffer.position()]
        this.buffer.rewind()
        this.buffer.get(result)
        return result
    }

    void serializeHeader() {
        this.buffer.put 0xff.byteValue()

        if (this.transaction.version) {
            this.buffer.put this.transaction.version.byteValue()
        } else {
            this.buffer.put 0x01.byteValue()
        }

        if (this.transaction.network) {
            this.buffer.put this.transaction.network.byteValue()
        } else {
            this.buffer.put Network.get().version().byteValue()
        }

        this.buffer.put this.transaction.type.byteValue()
        this.buffer.putInt this.transaction.timestamp
        this.buffer.put Hex.decode(this.transaction.senderPublicKey)
        this.buffer.putLong this.transaction.fee

        if (this.transaction.vendorField) {
            int vendorFieldLength = this.transaction.vendorField.length()

            this.buffer.put vendorFieldLength.byteValue()
            this.buffer.put this.transaction.vendorField.bytes
        } else if (this.transaction.vendorFieldHex) {
            int vendorFieldHexLength = this.transaction.vendorFieldHex.length()

            this.buffer.put vendorFieldHexLength / 2 as byte
            this.buffer.put Hex.decode(this.transaction.vendorFieldHex)
        } else {
            this.buffer.put 0x00.byteValue()
        }
    }

    void serializeTypeSpecific() {
        switch (transaction.type) {
            case Types.TRANSFER.getValue():
                new Transfer(this.buffer, this.transaction).serialize()
                break

            case Types.SECOND_SIGNATURE_REGISTRATION.getValue():
                new SecondSignatureRegistration(this.buffer, this.transaction).serialize()
                break

            case Types.DELEGATE_REGISTRATION.getValue():
                new DelegateRegistration(this.buffer, this.transaction).serialize()
                break

            case Types.VOTE.getValue():
                new Vote(this.buffer, this.transaction).serialize()
                break

            case Types.MULTI_SIGNATURE_REGISTRATION.getValue():
                new MultiSignatureRegistration(this.buffer, this.transaction).serialize()
                break

        // case Types.IPFS.getValue():
        //     new Ipfs(this.buffer, this.transaction).serialize()
        // break

        // case Types.TIMELOCK_TRANSFER.getValue():
        //     new TimelockTransfer(this.buffer, this.transaction).serialize()
        // break

        // case Types.MULTI_PAYMENT.getValue():
        //     new MultiPayment(this.buffer, this.transaction).serialize()
        // break

        // case Types.DELEGATE_RESIGNATION.getValue():
        //     new DelegateResignation(this.buffer, this.transaction).serialize()
        // break
        }
    }

    void serializeSignatures() {
        if (this.transaction.signature) {
            buffer.put Hex.decode(this.transaction.signature)
        }

        if (this.transaction.secondSignature) {
            buffer.put Hex.decode(this.transaction.secondSignature)
        } else if (this.transaction.signSignature) {
            buffer.put Hex.decode(this.transaction.signSignature)
        }

        if (this.transaction.signatures) {
            this.buffer.put 0xff.byteValue()
            buffer.put Hex.decode(this.transaction.signatures.join(""))
        }
    }
}
