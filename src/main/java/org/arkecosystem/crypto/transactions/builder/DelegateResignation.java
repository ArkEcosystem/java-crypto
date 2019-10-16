package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.CoreTransactionTypes;

public class DelegateResignation extends AbstractTransaction<DelegateResignation> {

    @Override
    public CoreTransactionTypes getType() {
        return CoreTransactionTypes.DELEGATE_RESIGNATION;
    }

    @Override
    public DelegateResignation instance() {
        return this;
    }
}
