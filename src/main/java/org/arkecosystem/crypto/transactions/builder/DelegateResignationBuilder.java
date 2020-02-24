package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.transactions.types.DelegateResignation;
import org.arkecosystem.crypto.transactions.types.Transaction;

public class DelegateResignationBuilder
        extends AbstractTransactionBuilder<DelegateResignationBuilder> {

    @Override
    public Transaction getTransactionInstance() {
        return new DelegateResignation();
    }

    @Override
    public DelegateResignationBuilder instance() {
        return this;
    }
}
