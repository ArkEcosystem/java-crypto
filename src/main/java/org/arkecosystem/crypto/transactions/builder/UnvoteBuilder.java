package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.transactions.types.AbstractTransaction;
import org.arkecosystem.crypto.transactions.types.Unvote;

public class UnvoteBuilder extends AbstractTransactionBuilder<UnvoteBuilder> {

    @Override
    public AbstractTransaction getTransactionInstance() {
        return new Unvote();
    }

    @Override
    public UnvoteBuilder instance() {
        return this;
    }
}
