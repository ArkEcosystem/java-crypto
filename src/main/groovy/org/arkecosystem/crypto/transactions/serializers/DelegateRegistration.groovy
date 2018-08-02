package org.arkecosystem.crypto.transactions.serializers

import org.arkecosystem.crypto.transactions.Transaction
import org.arkecosystem.crypto.encoding.*
import java.nio.ByteBuffer

class DelegateRegistration extends AbstractSerializer {
    DelegateRegistration(ByteBuffer buffer, Transaction transaction) {
        super(buffer, transaction)
    }

    void serialize() {
        byte[] delegateBytes = transaction.asset.delegate.username.getBytes()

        this.buffer.put delegateBytes.length.byteValue()
        this.buffer.put delegateBytes
    }
}
