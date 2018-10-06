package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.*;

public class Transfer extends AbstractTransaction {

    public AbstractTransaction recipient(String recipientId) {
        this.transaction.recipientId = recipientId;

        return this;
    }

    public AbstractTransaction amount(long amount) {
        this.transaction.amount = amount;

        return this;
    }

    public AbstractTransaction vendorField(String vendorField) {
        this.transaction.vendorField = vendorField;

        return this;
    }

    public Types getType() {
        return Types.TRANSFER;
    }

}
