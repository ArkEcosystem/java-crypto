package org.arkecosystem.crypto.transactions.deserializers;

import org.arkecosystem.crypto.transactions.Transaction;

import java.nio.ByteBuffer;

public abstract class AbstractDeserializer {

    protected String serialized;
    protected ByteBuffer buffer;
    protected Transaction transaction;

    public AbstractDeserializer(String serialized, ByteBuffer buffer, Transaction transaction) {
        this.serialized = serialized;
        this.buffer = buffer;
        this.transaction = transaction;
    }

}
