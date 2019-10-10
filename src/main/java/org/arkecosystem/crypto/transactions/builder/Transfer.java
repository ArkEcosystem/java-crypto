package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.CoreTransactionTypes;

public class Transfer extends AbstractTransaction<Transfer> {

    public Transfer recipient(String recipientId) {
        this.transaction.recipientId = recipientId;

        return this;
    }

    public Transfer amount(long amount) {
        this.transaction.amount = amount;

        return this;
    }

    public Transfer vendorField(String vendorField) {
        this.transaction.vendorField = vendorField;

        return this;
    }

    @Override
    public CoreTransactionTypes getType() {
        return CoreTransactionTypes.TRANSFER;
    }

    @Override
    public Transfer instance() {
        return this;
    }
}
