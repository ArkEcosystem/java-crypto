package org.arkecosystem.crypto.transactions.builder

import org.arkecosystem.crypto.configuration.Fee
import org.arkecosystem.crypto.transactions.Transaction
import org.arkecosystem.crypto.utils.Slot

abstract class AbstractTransaction {
    Transaction transaction

    AbstractTransaction initialize() {
        this.transaction = new Transaction(
            type: this.getType(),
            fee: Fee.get(this.getType()),
        )

        return this
    }

    AbstractTransaction sign(String passphrase) {
        this.transaction.timestamp = Slot.time()
        this.transaction.sign(passphrase)
        this.transaction.id = this.transaction.getId()

        return this
    }

    AbstractTransaction secondSign(String passphrase) {
        this.transaction.secondSign(passphrase)
        this.transaction.id = this.transaction.getId()

        return this
    }

    Transaction getTransaction() {
        this.transaction
    }

    abstract int getType()
}
