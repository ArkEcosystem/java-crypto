package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.Fees;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.arkecosystem.crypto.transactions.types.UsernameRegistration;

public class UsernameRegistrationBuilder
        extends AbstractTransactionBuilder<UsernameRegistrationBuilder> {
    public UsernameRegistrationBuilder() {
        super();
        this.transaction.fee = Fees.VALIDATOR_REGISTRATION.getValue();
    }

    public UsernameRegistrationBuilder usernameAsset(String username) {
        this.transaction.asset.username = username;

        return this;
    }

    @Override
    public Transaction getTransactionInstance() {
        return new UsernameRegistration();
    }

    @Override
    public UsernameRegistrationBuilder instance() {
        return this;
    }
}
