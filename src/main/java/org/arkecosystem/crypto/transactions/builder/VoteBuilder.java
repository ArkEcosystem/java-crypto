package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.transactions.types.AbstractTransaction;
import org.arkecosystem.crypto.transactions.types.Vote;

public class VoteBuilder extends AbstractTransactionBuilder<VoteBuilder> {

    public VoteBuilder vote(String vote) {
        this.transaction.vote = vote;

        this.transaction.refreshPayloadData();

        return this.instance();
    }

    @Override
    protected AbstractTransaction getTransactionInstance() {
        return new Vote();
    }

    @Override
    protected VoteBuilder instance() {
        return this;
    }
}
