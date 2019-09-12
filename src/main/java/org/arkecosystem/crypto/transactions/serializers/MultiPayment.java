package org.arkecosystem.crypto.transactions.serializers;

import org.arkecosystem.crypto.transactions.Transaction;
import org.bitcoinj.core.Base58;

import java.nio.ByteBuffer;

public class MultiPayment extends AbstractSerializer {
    public MultiPayment(ByteBuffer buffer, Transaction transaction) {
        super(buffer, transaction);
    }

    public void serialize(){
        this.buffer.putInt(this.transaction.asset.multiPayment.payments.size());
        this.transaction.asset.multiPayment.payments.forEach((recipientId, amount) -> {
            this.buffer.putLong(amount);
            this.buffer.put(Base58.decodeChecked(recipientId));
        });
    }
}
