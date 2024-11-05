package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.transactions.types.AbstractTransaction;
import org.arkecosystem.crypto.transactions.types.ValidatorResignation;

public class ValidatorResignationBuilder
        extends AbstractTransactionBuilder<ValidatorResignationBuilder> {

    @Override
    public AbstractTransaction getTransactionInstance() {
        return new ValidatorResignation();
    }

    @Override
    public ValidatorResignationBuilder instance() {
        return this;
    }
}
