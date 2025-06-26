package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.transactions.types.AbstractTransaction;
import org.arkecosystem.crypto.transactions.types.UsernameResignation;

public class UsernameResignationBuilder
        extends AbstractTransactionBuilder<UsernameResignationBuilder> {

    @Override
    public AbstractTransaction getTransactionInstance() {
        return new UsernameResignation();
    }

    @Override
    public UsernameResignationBuilder instance() {
        return this;
    }
}
