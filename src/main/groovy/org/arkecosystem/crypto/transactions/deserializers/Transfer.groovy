package org.arkecosystem.crypto.transactions.deserializers

import org.arkecosystem.crypto.transactions.Transaction
import org.bitcoinj.core.VersionedChecksummedBytes
import java.nio.ByteBuffer

class Transfer extends AbstractDeserializer {
    Transfer(String serialized, ByteBuffer buffer, Transaction transaction) {
        super(serialized, buffer, transaction)
    }

    void deserialize(int assetOffset) {
        this.buffer.position(assetOffset / 2 as int)

        this.transaction.amount     = this.buffer.getLong()
        this.transaction.expiration = this.buffer.getInt()

        /* FIX: the address currently does not start with D as expected for devnet */
        def recipientId = new byte[21]
        this.buffer.get(recipientId)
        this.transaction.recipientId = new VersionedChecksummedBytes(this.transaction.network, recipientId).toBase58()

        this.transaction.parseSignatures(this.serialized, assetOffset + (8 + 4 + 21) * 2)
    }
}
