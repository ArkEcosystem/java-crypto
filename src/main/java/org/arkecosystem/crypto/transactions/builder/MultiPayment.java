package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.TransactionType;

public class MultiPayment extends AbstractTransaction {

    public MultiPayment addPayment(String  recipientId, long amount){
        this.transaction.asset.multiPayment.payments.put(recipientId,amount);
        return this;
    }

    public TransactionType getType() {
        return TransactionType.MULTI_PAYMENT;
    }
}
