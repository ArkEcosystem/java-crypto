package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.TransactionType;
import org.arkecosystem.crypto.transactions.TransactionAsset;

public class MultiPayment extends AbstractTransaction {

    public MultiPayment addPayment(String  recipientId, long amount){
        if (this.transaction.asset.multiPayment.payments.size() >= 2258){
            throw new MaximumPaymentCountExceededError();
        }
        this.transaction.asset.multiPayment.payments.add(new TransactionAsset.Payment(amount,recipientId));
        return this;
    }

    public TransactionType getType() {
        return TransactionType.MULTI_PAYMENT;
    }
}

class MaximumPaymentCountExceededError extends RuntimeException {
    MaximumPaymentCountExceededError() {
        super("Expected a maximum of 2258 payments");
    }
}
