package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.transactions.types.AbstractTransaction;
import org.arkecosystem.crypto.transactions.types.Unvote;

public class UnvoteBuilder extends AbstractTransactionBuilder<UnvoteBuilder> {

    @Override
    protected AbstractTransaction getTransactionInstance() {
        return new Unvote();
    }

    @Override
    protected UnvoteBuilder instance() {
        return this;
    }
}
