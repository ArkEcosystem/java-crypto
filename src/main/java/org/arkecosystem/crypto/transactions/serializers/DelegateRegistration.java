package org.arkecosystem.crypto.transactions.serializers;

import java.nio.ByteBuffer;
import org.arkecosystem.crypto.transactions.Transaction;

public class DelegateRegistration extends AbstractSerializer {
    public DelegateRegistration(ByteBuffer buffer, Transaction transaction) {
        super(buffer, transaction);
    }

    @Override
    public void serialize() {
        byte[] delegateBytes = this.transaction.asset.delegate.username.getBytes();

        this.buffer.put((byte) delegateBytes.length);
        this.buffer.put(delegateBytes);
    }
}
