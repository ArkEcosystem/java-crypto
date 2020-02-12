package org.arkecosystem.crypto.transactions.serializers;

import java.nio.ByteBuffer;
import org.arkecosystem.crypto.transactions.Transaction;
import org.arkecosystem.crypto.transactions.TransactionAsset;
import org.bitcoinj.core.Base58;

public class MultiPayment extends AbstractSerializer {
    public MultiPayment(ByteBuffer buffer, Transaction transaction) {
        super(buffer, transaction);
    }

    @Override
    public void serialize() {
        this.buffer.putShort((short) this.transaction.asset.multiPayment.payments.size());
        for (TransactionAsset.Payment current : this.transaction.asset.multiPayment.payments) {
            this.buffer.putLong(current.amount);
            this.buffer.put(Base58.decodeChecked(current.recipientId));
        }
    }
}
