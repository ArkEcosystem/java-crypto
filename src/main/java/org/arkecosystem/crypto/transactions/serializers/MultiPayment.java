package org.arkecosystem.crypto.transactions.serializers;

import org.arkecosystem.crypto.transactions.Transaction;
import org.arkecosystem.crypto.transactions.TransactionAsset;
import org.bitcoinj.core.Base58;
import org.bouncycastle.jcajce.provider.symmetric.ARC4;

import java.nio.ByteBuffer;
import java.util.ListIterator;

public class MultiPayment extends AbstractSerializer {
    public MultiPayment(ByteBuffer buffer, Transaction transaction) {
        super(buffer, transaction);
    }

    public void serialize(){
        this.buffer.putInt(this.transaction.asset.multiPayment.payments.size());
        for (TransactionAsset.Payment current : this.transaction.asset.multiPayment.payments) {
            this.buffer.putLong(current.amount);
            this.buffer.put(Base58.decodeChecked(current.recipientId));
        }
    }
}
