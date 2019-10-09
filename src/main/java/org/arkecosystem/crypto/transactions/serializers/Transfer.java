package org.arkecosystem.crypto.transactions.serializers;

import java.nio.ByteBuffer;
import org.arkecosystem.crypto.transactions.Transaction;
import org.bitcoinj.core.Base58;

public class Transfer extends AbstractSerializer {
    public Transfer(ByteBuffer buffer, Transaction transaction) {
        super(buffer, transaction);
    }

    @Override
    public void serialize() {
        this.buffer.putLong(this.transaction.amount);
        this.buffer.putInt(this.transaction.expiration);
        this.buffer.put(Base58.decodeChecked(this.transaction.recipientId));
    }
}
