package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.transactions.types.AbstractTransaction;
import org.arkecosystem.crypto.transactions.types.Transfer;

public class TransferBuilder extends AbstractTransactionBuilder<TransferBuilder> {
    public TransferBuilder value(long value) {
        this.transaction.value = value;

        this.transaction.refreshPayloadData();

        return this.instance();
    }

    @Override
    protected AbstractTransaction getTransactionInstance() {
        return new Transfer();
    }

    @Override
    protected TransferBuilder instance() {
        return this;
    }
}
