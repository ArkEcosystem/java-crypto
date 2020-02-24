package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.Fees;
import org.arkecosystem.crypto.transactions.types.DelegateRegistration;
import org.arkecosystem.crypto.transactions.types.Transaction;

public class DelegateRegistrationBuilder
        extends AbstractTransactionBuilder<DelegateRegistrationBuilder> {
    public DelegateRegistrationBuilder() {
        super();
        this.transaction.fee = Fees.DELEGATE_REGISTRATION.getValue();
    }

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
