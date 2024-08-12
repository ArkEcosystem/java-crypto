package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.Fees;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.arkecosystem.crypto.transactions.types.UsernameResignation;

public class UsernameResignationBuilder
        extends AbstractTransactionBuilder<UsernameResignationBuilder> {

    public UsernameResignationBuilder() {
        super();
        this.transaction.fee = Fees.USERNAME_RESIGNATION.getValue();
    }

    @Override
    public Transaction getTransactionInstance() {
        return new UsernameResignation();
    }

    @Override
    public UsernameResignationBuilder instance() {
        return this;
    }
}
