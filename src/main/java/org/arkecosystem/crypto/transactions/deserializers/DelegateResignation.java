package org.arkecosystem.crypto.transactions.deserializers;

import org.arkecosystem.crypto.transactions.Transaction;

import java.nio.ByteBuffer;

public class DelegateResignation extends AbstractDeserializer{
    public DelegateResignation(String serialized, ByteBuffer buffer, Transaction transaction) {
        super(serialized, buffer, transaction);
    }

    public void deserialize(int assetOffset) {
        this.transaction.parseSignatures(this.serialized, assetOffset);
    }
}
