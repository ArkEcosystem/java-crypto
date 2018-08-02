package org.arkecosystem.crypto.transactions.builder

import org.arkecosystem.crypto.configuration.Fee
import org.arkecosystem.crypto.transactions.Transaction
import org.arkecosystem.crypto.utils.Slot

abstract class AbstractTransaction {
    Transaction transaction

    AbstractTransaction() {
        this.transaction = new Transaction(
            type: this.getType(),
            fee: Fee.get(this.getType()),
            timestamp: Slot.time()
        )
    }

    AbstractTransaction sign(String passphrase) {
        this.transaction.sign(passphrase)
        this.transaction.id = this.transaction.computeId()

        return this
    }

    AbstractTransaction secondSign(String passphrase) {
        this.transaction.secondSign(passphrase)
        this.transaction.id = this.transaction.computeId()

        return this
    }

    Transaction getTransaction() {
        this.transaction
    }

    abstract int getType()
}
