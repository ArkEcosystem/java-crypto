package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.TransactionType;
import org.arkecosystem.crypto.transactions.TransactionAsset;

public class MultiPayment extends AbstractTransaction {

    public MultiPayment addPayment(String  recipientId, long amount){
        this.transaction.asset.multiPayment.payments.add(new TransactionAsset.Payment(amount,recipientId));
        return this;
    }

    public TransactionType getType() {
        return TransactionType.MULTI_PAYMENT;
    }
}
