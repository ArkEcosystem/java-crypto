package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.transactions.types.AbstractTransaction;
import org.arkecosystem.crypto.transactions.types.Vote;

public class VoteBuilder extends AbstractTransactionBuilder<VoteBuilder> {

    public VoteBuilder vote(String vote) {
        this.transaction.asset.vote = vote;

        return this;
    }

    @Override
    public AbstractTransaction getTransactionInstance() {
        return new Vote();
    }

    @Override
    public VoteBuilder instance() {
        return this;
    }
}
