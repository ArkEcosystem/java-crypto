package org.arkecosystem.crypto.transactions.serializers


import java.nio.ByteBuffer
import org.bitcoinj.core.Base58

class Transfer extends AbstractSerializer {
    Transfer(ByteBuffer buffer, Transaction transaction) {
        super(buffer, transaction)
    }

    void serialize() {
        this.buffer.putLong this.transaction.amount

        if (this.transaction.expiration) {
            this.buffer.putInt this.transaction.expiration
        } else {
            this.buffer.putInt 0
        }

        this.buffer.put Base58.decodeChecked(this.transaction.recipientId)
    }
}
