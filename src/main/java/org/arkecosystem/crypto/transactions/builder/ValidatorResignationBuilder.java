package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.Fees;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.arkecosystem.crypto.transactions.types.ValidatorResignation;

public class ValidatorResignationBuilder
        extends AbstractTransactionBuilder<ValidatorResignationBuilder> {

    public ValidatorResignationBuilder() {
        super();
        this.transaction.fee = Fees.VALIDATOR_RESIGNATION.getValue();
    }

    @Override
    public Transaction getTransactionInstance() {
        return new ValidatorResignation();
    }

    @Override
    public ValidatorResignationBuilder instance() {
        return this;
    }
}
