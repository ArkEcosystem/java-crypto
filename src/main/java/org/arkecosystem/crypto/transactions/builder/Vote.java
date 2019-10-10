package org.arkecosystem.crypto.transactions.builder;

import java.util.List;
import org.arkecosystem.crypto.enums.CoreTransactionTypes;
import org.arkecosystem.crypto.identities.Address;

public class Vote extends AbstractTransaction<Vote> {
    public Vote votes(List votes) {
        this.transaction.asset.votes = votes;

        return this;
    }

    public Vote sign(String passphrase) {
        this.transaction.recipientId = Address.fromPassphrase(passphrase);

        super.sign(passphrase);

        return this;
    }

    @Override
    public CoreTransactionTypes getType() {
        return CoreTransactionTypes.VOTE;
    }

    @Override
    public Vote instance() {
        return this;
    }
}
