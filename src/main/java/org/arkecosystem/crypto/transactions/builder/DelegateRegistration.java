package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.CoreTransactionTypes;

public class DelegateRegistration extends AbstractTransaction<DelegateRegistration> {
    public DelegateRegistration username(String username) {
        this.transaction.asset.delegate.username = username;

        return this;
    }

    @Override
    public CoreTransactionTypes getType() {
        return CoreTransactionTypes.DELEGATE_REGISTRATION;
    }

    @Override
    public DelegateRegistration instance() {
        return this;
    }
}
