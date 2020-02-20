package org.arkecosystem.crypto.transactions.builder;

import java.util.List;
import org.arkecosystem.crypto.identities.Address;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.arkecosystem.crypto.transactions.types.Vote;

public class VoteBuilder extends AbstractTransactionBuilder<VoteBuilder> {

    public VoteBuilder addVotes(List votes) {
        this.transaction.asset.votes = votes;
        return this;
    }

    public VoteBuilder addVote(String vote) {
        this.transaction.asset.votes.add(vote);
        return this;
    }

    public VoteBuilder sign(String passphrase) {
        this.transaction.recipientId = Address.fromPassphrase(passphrase);

        super.sign(passphrase);

        return this;
    }

    @Override
    public Transaction getTransactionInstance() {
        return new Vote();
    }

    @Override
    public VoteBuilder instance() {
        return this;
    }
}
