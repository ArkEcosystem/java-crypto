package org.arkecosystem.crypto.transactions.serializers;

import org.arkecosystem.crypto.transactions.Transaction;

import java.nio.ByteBuffer;

public abstract class AbstractSerializer {
    protected ByteBuffer buffer;
    protected Transaction transaction;

    public AbstractSerializer(ByteBuffer buffer, Transaction transaction) {
        this.buffer = buffer;
        this.transaction = transaction;
    }

}
