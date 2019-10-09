package org.arkecosystem.crypto.transactions.serializers;

import java.nio.ByteBuffer;
import org.arkecosystem.crypto.transactions.Transaction;

public abstract class AbstractSerializer {
    protected ByteBuffer buffer;
    protected Transaction transaction;

    public AbstractSerializer(ByteBuffer buffer, Transaction transaction) {
        this.buffer = buffer;
        this.transaction = transaction;
    }
}
