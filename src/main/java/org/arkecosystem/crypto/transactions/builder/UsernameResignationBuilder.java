package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.ContractAddresses;
import org.arkecosystem.crypto.transactions.types.AbstractTransaction;
import org.arkecosystem.crypto.transactions.types.UsernameResignation;

public class UsernameResignationBuilder
        extends AbstractTransactionBuilder<UsernameResignationBuilder> {

    public UsernameResignationBuilder() {
        super();
        this.transaction.recipientAddress = ContractAddresses.USERNAMES.address();
    }

    @Override
    protected AbstractTransaction getTransactionInstance() {
        return new UsernameResignation();
    }

    @Override
    protected UsernameResignationBuilder instance() {
        return this;
    }
}
