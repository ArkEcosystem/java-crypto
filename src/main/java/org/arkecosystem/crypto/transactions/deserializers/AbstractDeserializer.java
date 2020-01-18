package org.arkecosystem.crypto.transactions.deserializers;

import java.nio.ByteBuffer;
import org.arkecosystem.crypto.transactions.Transaction;

public abstract class AbstractDeserializer {

    protected String serialized;
    protected ByteBuffer buffer;
    protected Transaction transaction;

    public AbstractDeserializer(String serialized, ByteBuffer buffer, Transaction transaction) {
        this.serialized = serialized;
        this.buffer = buffer;
        this.transaction = transaction;
    }

    public abstract void deserialize(int assetOffset);
}
