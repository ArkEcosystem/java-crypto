package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.Fees;
import org.arkecosystem.crypto.transactions.TransactionAsset;
import org.arkecosystem.crypto.transactions.types.MultiPayment;
import org.arkecosystem.crypto.transactions.types.Transaction;

public class MultiPaymentBuilder extends AbstractTransactionBuilder<MultiPaymentBuilder> {

    public MultiPaymentBuilder() {
        super();
        this.transaction.fee = Fees.MULTI_PAYMENT.getValue();
    }

    public MultiPaymentBuilder addPayment(String recipientId, long amount) {
        if (this.transaction.asset.multiPayment.payments.size() >= 2258) {
            throw new MaximumPaymentCountExceededError();
        }
        this.transaction.asset.multiPayment.payments.add(
                new TransactionAsset.Payment(amount, recipientId));
        return this;
    }

    public MultiPaymentBuilder vendorField(String vendorField) {
        this.transaction.vendorField = vendorField;

        return this;
    }

    @Override
    public Transaction getTransactionInstance() {
        return new MultiPayment();
    }

    @Override
    public MultiPaymentBuilder instance() {
        return this;
    }
}

class MaximumPaymentCountExceededError extends RuntimeException {
    MaximumPaymentCountExceededError() {
        super("Expected a maximum of 2258 payments");
    }
}
