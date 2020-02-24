package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.Fees;
import org.arkecosystem.crypto.transactions.types.DelegateResignation;
import org.arkecosystem.crypto.transactions.types.Transaction;

public class DelegateResignationBuilder
        extends AbstractTransactionBuilder<DelegateResignationBuilder> {

    public DelegateResignationBuilder() {
        super();
        this.transaction.fee = Fees.DELEGATE_RESIGNATION.getValue();
    }

    @Override
    public Transaction getTransactionInstance() {
        return new DelegateResignation();
    }

    @Override
    public DelegateResignationBuilder instance() {
        return this;
    }
}
