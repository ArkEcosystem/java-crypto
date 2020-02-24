package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.transactions.types.DelegateRegistration;
import org.arkecosystem.crypto.transactions.types.Transaction;

public class DelegateRegistrationBuilder
        extends AbstractTransactionBuilder<DelegateRegistrationBuilder> {
    public DelegateRegistrationBuilder username(String username) {
        this.transaction.asset.delegate.username = username;

        return this;
    }

    @Override
    public Transaction getTransactionInstance() {
        return new DelegateRegistration();
    }

    @Override
    public DelegateRegistrationBuilder instance() {
        return this;
    }
}
