package org.arkecosystem.crypto.transactions.deserializers

import org.arkecosystem.crypto.transactions.Transaction
import org.arkecosystem.crypto.helpers.Base58
import java.nio.ByteBuffer

class Transfer extends AbstractDeserializer {
    Transfer(String serialized, ByteBuffer buffer, Transaction transaction) {
        super(serialized, buffer, transaction)
    }

    void deserialize(int assetOffset) {
        this.buffer.position(assetOffset / 2 as int)

        this.transaction.amount     = this.buffer.getLong()
        this.transaction.expiration = this.buffer.getInt()

        def recipientId = new byte[21]
        this.buffer.get(recipientId)
        this.transaction.recipientId = Base58.encodeChecked(recipientId)

        this.transaction.parseSignatures(this.serialized, assetOffset + (8 + 4 + 21) * 2)
    }
}
