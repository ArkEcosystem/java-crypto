package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.identities.BlsPublicKey;
import org.arkecosystem.crypto.transactions.types.AbstractTransaction;
import org.arkecosystem.crypto.transactions.types.ValidatorRegistration;

public class ValidatorRegistrationBuilder
        extends AbstractTransactionBuilder<ValidatorRegistrationBuilder> {
    public ValidatorRegistrationBuilder validatorPublicKey(String validatorPublicKey) {
        if (!BlsPublicKey.validate(validatorPublicKey)) {
            throw new IllegalArgumentException("Invalid BLS public key");
        }

        this.transaction.validatorPublicKey = validatorPublicKey;

        this.transaction.refreshPayloadData();

        return this.instance();
    }

    @Override
    protected AbstractTransaction getTransactionInstance() {
        return new ValidatorRegistration();
    }

    @Override
    protected ValidatorRegistrationBuilder instance() {
        return this;
    }
}
