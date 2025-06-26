package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.signature.bls.Bls;
import org.arkecosystem.crypto.signature.bls.BlsConstants;
import org.arkecosystem.crypto.transactions.types.AbstractTransaction;
import org.arkecosystem.crypto.transactions.types.ValidatorRegistration;

public class ValidatorRegistrationBuilder
        extends AbstractTransactionBuilder<ValidatorRegistrationBuilder> {
    public ValidatorRegistrationBuilder validatorPublicKey(String validatorPublicKey) {
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

    private void validateBlsPublicKey(String publicKeyHex) {
        if (publicKeyHex.length() != 96) {
            throw new IllegalArgumentException("Invalid BLS public key length");
        }

        Bls bls = new Bls(BlsConstants.BLS12_381);

        bls.validateBlsPublicKey(publicKeyHex);
    }
}
