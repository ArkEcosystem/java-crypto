package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.TransactionType;

public class DelegateResignation extends AbstractTransaction<DelegateResignation> {

    @Override
    public TransactionType getType() {
        return TransactionType.DELEGATE_RESIGNATION;
    }

    @Override
    public DelegateResignation instance() {
        return this;
    }
}
