package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.transactions.types.AbstractTransaction;
import org.arkecosystem.crypto.transactions.types.UsernameRegistration;

public class UsernameRegistrationBuilder
        extends AbstractTransactionBuilder<UsernameRegistrationBuilder> {
    public UsernameRegistrationBuilder username(String username) {
        this.transaction.username = username;

        return this;
    }

    @Override
    public AbstractTransaction getTransactionInstance() {
        return new UsernameRegistration();
    }

    @Override
    public UsernameRegistrationBuilder instance() {
        return this;
    }
}
