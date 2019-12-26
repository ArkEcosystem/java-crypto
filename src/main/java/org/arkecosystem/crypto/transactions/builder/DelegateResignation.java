package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.CoreTransactionTypes;

public class DelegateResignation extends AbstractBuilder<DelegateResignation> {

    @Override
    public int getType() {
        return CoreTransactionTypes.DELEGATE_RESIGNATION.getValue();
    }

    @Override
    public DelegateResignation instance() {
        return this;
    }
}
