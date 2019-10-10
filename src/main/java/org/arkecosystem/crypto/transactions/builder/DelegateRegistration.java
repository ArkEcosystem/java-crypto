package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.TransactionType;

public class DelegateRegistration extends AbstractTransaction {
    public DelegateRegistration username(String username) {
        this.transaction.asset.delegate.username = username;

        return this;
    }

    public TransactionType getType() {
        return TransactionType.DELEGATE_REGISTRATION;
    }

}
