package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.transactions.types.AbstractTransaction;
import org.arkecosystem.crypto.transactions.types.Transfer;

public class TransferBuilder extends AbstractTransactionBuilder<TransferBuilder> {
    public TransferBuilder amount(long amount) {
        this.transaction.amount = amount;
        return this;
    }

    @Override
    public AbstractTransaction getTransactionInstance() {
        return new Transfer();
    }

    @Override
    public TransferBuilder instance() {
        return this;
    }
}
