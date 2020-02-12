package org.arkecosystem.crypto.transactions.deserializers;

import java.nio.ByteBuffer;
import org.arkecosystem.crypto.transactions.Transaction;

public class DelegateResignation extends AbstractDeserializer {
    public DelegateResignation(String serialized, ByteBuffer buffer, Transaction transaction) {
        super(serialized, buffer, transaction);
    }

    @Override
    public void deserialize(int assetOffset) {
        this.transaction.parseSignatures(this.serialized, assetOffset);
    }
}
