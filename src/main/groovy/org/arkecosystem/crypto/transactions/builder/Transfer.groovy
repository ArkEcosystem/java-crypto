package org.arkecosystem.crypto.transactions.builder

import static org.arkecosystem.crypto.enums.Types.*

class Transfer extends AbstractTransaction {
    AbstractTransaction recipient(String recipientId)
    {
        this.transaction.recipientId = recipientId

        return this
    }

    AbstractTransaction amount(long amount)
    {
        this.transaction.amount = amount

        return this
    }

    AbstractTransaction vendorField(String vendorField)
    {
        this.transaction.vendorField = vendorField

        return this
    }

    int getType()
    {
        TRANSFER.getValue()
    }
}
