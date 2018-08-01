package org.arkecosystem.crypto.transactions.builder

import static org.arkecosystem.crypto.enums.Types.*
import org.arkecosystem.crypto.identities.Address

class Vote extends AbstractTransaction {
    AbstractTransaction votes(List votes)
    {
        this.transaction.asset.votes = votes

        return this
    }

    AbstractTransaction sign(String passphrase) {
        this.transaction.recipientId = Address.fromPassphrase(passphrase)

        super.sign(passphrase)

        return this
    }

    int getType()
    {
        VOTE.getValue()
    }
}
