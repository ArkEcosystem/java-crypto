package org.arkecosystem.crypto.transactions.serializers;

import org.arkecosystem.crypto.transactions.Transaction;

import java.nio.ByteBuffer;

public class DelegateResignation extends AbstractSerializer{
    public DelegateResignation(ByteBuffer buffer, Transaction transaction) {
        super(buffer, transaction);
    }

    public void serialize() {}
}
