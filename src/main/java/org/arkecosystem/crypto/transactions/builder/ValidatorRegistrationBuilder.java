package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.Fees;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.arkecosystem.crypto.transactions.types.ValidatorRegistration;

public class ValidatorRegistrationBuilder
        extends AbstractTransactionBuilder<ValidatorRegistrationBuilder> {

    public ValidatorRegistrationBuilder() {
        super();
        this.transaction.fee = Fees.VALIDATOR_REGISTRATION.getValue();
    }

    public ValidatorRegistrationBuilder publicKeyAsset(String publicKey) {
        validateBlsPublicKey(publicKey);

        this.transaction.asset.validatorPublicKey = publicKey;
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

    private void validateBlsPublicKey(String publicKeyHex) {
        if (publicKeyHex.length() != 96) {
            throw new IllegalArgumentException("Invalid BLS public key length");
        }

        if (!publicKeyHex.matches("^[0-9A-Fa-f]+$")) {
            throw new IllegalArgumentException("Invalid BLS public key");
        }
    }
}
