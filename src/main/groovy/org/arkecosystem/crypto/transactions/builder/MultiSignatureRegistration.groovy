package org.arkecosystem.crypto.transactions.builder

import static org.arkecosystem.crypto.enums.Types.*

class MultiSignatureRegistration extends AbstractTransaction {
    AbstractTransaction min(int min)
    {
        this.transaction.asset.multisignature = [
            min: null, lifetime: null, keysgroup: null
        ]

        this.transaction.asset.multisignature.min = min

        return this
    }

    AbstractTransaction lifetime(int lifetime)
    {
        this.transaction.asset.multisignature.lifetime = lifetime

        return this
    }

    AbstractTransaction keysgroup(List keysgroup)
    {
        this.transaction.asset.multisignature.keysgroup = keysgroup

        this.transaction.fee = (keysgroup.size() + 1) * this.transaction.fee

        return this
    }

    int getType()
    {
        TRANSFER.getValue()
    }
}
