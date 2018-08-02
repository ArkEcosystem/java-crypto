package org.arkecosystem.crypto.transactions.deserializers

import org.arkecosystem.crypto.transactions.Transaction
import java.nio.ByteBuffer

abstract class AbstractDeserializer {
    String serialized
    ByteBuffer buffer
    Transaction transaction

    AbstractDeserializer(String serialized, ByteBuffer buffer, Transaction transaction) {
        this.serialized = serialized
        this.buffer = buffer
        this.transaction = transaction
    }
}
