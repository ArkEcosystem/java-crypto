package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.Fees;
import org.arkecosystem.crypto.transactions.types.AbstractTransaction;
import org.arkecosystem.crypto.transactions.types.ValidatorRegistration;

public class ValidatorRegistrationBuilder
        extends AbstractTransactionBuilder<ValidatorRegistrationBuilder> {
    public ValidatorRegistrationBuilder() {
        super();
        this.transaction.fee = Fees.VALIDATOR_REGISTRATION.getValue();
    }

    public ValidatorRegistrationBuilder validatorPublicKey(String validatorPublicKey) {
        this.transaction.asset.validatorPublicKey = validatorPublicKey;

        return this;
    }

    @Override
    public AbstractTransaction getTransactionInstance() {
        return new ValidatorRegistration();
    }

    @Override
    public ValidatorRegistrationBuilder instance() {
        return this;
    }
}
