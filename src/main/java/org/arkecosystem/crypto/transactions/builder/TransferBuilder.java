package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.Fees;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.arkecosystem.crypto.transactions.types.Transfer;

public class TransferBuilder extends AbstractTransactionBuilder<TransferBuilder> {

    public TransferBuilder(){
        super();
        this.transaction.fee = Fees.TRANSFER.getValue();
    }

    public TransferBuilder recipient(String recipientId) {
        this.transaction.recipientId = recipientId;
        return this;
    }

    public TransferBuilder amount(long amount) {
        this.transaction.amount = amount;
        return this;
    }

    public TransferBuilder expiration(int expiration) {
        this.transaction.expiration = expiration;
        return this;
    }

    public TransferBuilder vendorField(String vendorField) {
        this.transaction.vendorField = vendorField;
        return this;
    }

    @Override
    public Transaction getTransactionInstance() {
        return new Transfer();
    }

    @Override
    public TransferBuilder instance() {
        return this;
    }
}
