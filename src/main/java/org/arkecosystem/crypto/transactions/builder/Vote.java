package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.identities.Address;
import org.arkecosystem.crypto.enums.*;

import java.util.List;

public class Vote extends AbstractTransaction {
    public Vote votes(List votes) {
        this.transaction.asset.votes = votes;

        return this;
    }

    public Vote sign(String passphrase) {
        this.transaction.recipientId = Address.fromPassphrase(passphrase);

        super.sign(passphrase);

        return this;
    }

    public Types getType() {
        return Types.VOTE;
    }

}
