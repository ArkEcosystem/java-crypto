package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.Fees;
import org.arkecosystem.crypto.transactions.types.ValidatorRegistration;
import org.arkecosystem.crypto.transactions.types.Transaction;

public class ValidatorRegistrationBuilder
        extends AbstractTransactionBuilder<ValidatorRegistrationBuilder> {
    public ValidatorRegistrationBuilder() {
        super();
        this.transaction.fee = Fees.VALIDATOR_REGISTRATION.getValue();
    }

    public ValidatorRegistrationBuilder username(String username) {
        this.transaction.asset.delegate.username = username;

        return this;
    }

    @Override
    public Transaction getTransactionInstance() {
        return new ValidatorRegistration();
    }

    @Override
    public ValidatorRegistrationBuilder instance() {
        return this;
    }
}
