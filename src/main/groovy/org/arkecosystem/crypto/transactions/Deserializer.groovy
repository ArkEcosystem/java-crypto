package org.arkecosystem.crypto.transactions

import java.nio.ByteBuffer
import java.nio.ByteOrder
import org.arkecosystem.crypto.enums.Types
import org.arkecosystem.crypto.identities.Address
import org.arkecosystem.crypto.transactions.deserializers.DelegateRegistration
import org.arkecosystem.crypto.transactions.deserializers.MultiSignatureRegistration
import org.arkecosystem.crypto.transactions.deserializers.SecondSignatureRegistration
import org.arkecosystem.crypto.transactions.deserializers.Transfer
import org.arkecosystem.crypto.transactions.deserializers.Vote
import org.arkecosystem.crypto.encoding.*
import static javax.xml.bind.DatatypeConverter.parseHexBinary

class Deserializer {
    String serialized
    ByteBuffer buffer
    Transaction transaction

    Transaction deserialize(String serialized) {
        this.serialized = serialized

        this.buffer = ByteBuffer.wrap(parseHexBinary(serialized)).slice()
        this.buffer.order(ByteOrder.LITTLE_ENDIAN)
        this.buffer.get()

        this.transaction = new Transaction()

        int assetOffset = deserializeHeader()
        deserializeTypeSpecific(assetOffset)
        deserializeVersionOne()

        this.transaction
    }

    int deserializeHeader() {
        transaction.version = this.buffer.get()
        transaction.network = this.buffer.get()
        transaction.type = this.buffer.get()
        transaction.timestamp = this.buffer.getInt()

        def senderPublicKey = new byte[33]
        this.buffer.get(senderPublicKey)
        transaction.senderPublicKey = Hex.encode(senderPublicKey)

        transaction.fee = this.buffer.getLong()

        int vendorFieldLength = this.buffer.get()
        if (vendorFieldLength > 0) {
            def vendorFieldHex = new byte[vendorFieldLength]
            this.buffer.get(vendorFieldHex)
            transaction.vendorFieldHex = Hex.encode(vendorFieldHex)
        }

        return (41 + 8 + 1) * 2 + vendorFieldLength * 2
    }

    void deserializeTypeSpecific(int assetOffset) {
        switch (transaction.type) {
            case Types.TRANSFER.getValue():
                new Transfer(this.serialized, this.buffer, this.transaction).deserialize(assetOffset)
            break

            case Types.SECOND_SIGNATURE_REGISTRATION.getValue():
                new SecondSignatureRegistration(this.serialized, this.buffer, this.transaction).deserialize(assetOffset)
            break

            case Types.DELEGATE_REGISTRATION.getValue():
                new DelegateRegistration(this.serialized, this.buffer, this.transaction).deserialize(assetOffset)
            break

            case Types.VOTE.getValue():
                new Vote(this.serialized, this.buffer, this.transaction).deserialize(assetOffset)
            break

            case Types.MULTI_SIGNATURE_REGISTRATION.getValue():
                new MultiSignatureRegistration(this.serialized, this.buffer, this.transaction).deserialize(assetOffset)
            break

            // case Types.IPFS.getValue():
            //     new Ipfs(this.serialized, this.buffer, this.transaction).deserialize(assetOffset)
            // break

            // case Types.TIMELOCK_TRANSFER.getValue():
            //     new TimelockTransfer(this.serialized, this.buffer, this.transaction).deserialize(assetOffset)
            // break

            // case Types.MULTI_PAYMENT.getValue():
            //     new MultiPayment(this.serialized, this.buffer, this.transaction).deserialize(assetOffset)
            // break

            // case Types.DELEGATE_RESIGNATION.getValue():
            //     new DelegateResignation(this.serialized, this.buffer, this.transaction).deserialize(assetOffset)
            // break
        }
    }

    void deserializeVersionOne() {
        if (!transaction.amount) {
            transaction.amount = 0
        }

        if (transaction.secondSignature) {
            transaction.signSignature = transaction.secondSignature
        }

        if (Types.VOTE.getValue() == transaction.type) {
            transaction.recipientId = Address.fromPublicKey(transaction.senderPublicKey)
        }

        if (Types.MULTI_SIGNATURE_REGISTRATION.getValue() == transaction.type) {
            for (int i = 0; i < transaction.asset.multisignature.keysgroup.size(); i++) {
                transaction.asset.multisignature.keysgroup[i] = '+' + transaction.asset.multisignature.keysgroup[i]
            }
        }

        if (transaction.vendorFieldHex) {
            transaction.vendorField = new String(Hex.decode(transaction.vendorFieldHex))
        }

        if (!transaction.id) {
            transaction.id = transaction.getId()
        }

        // if (Types.SECOND_SIGNATURE_REGISTRATION.getValue() == transaction.type
        //     || Types.MULTI_SIGNATURE_REGISTRATION.getValue() == transaction.type) {
        //     transaction.recipientId = Address.fromPublicKey(transaction.senderPublicKey)
        // }
    }
}
