package org.arkecosystem.crypto.transactions.builder

import static org.arkecosystem.crypto.enums.Types.*

class DelegateRegistration extends AbstractTransaction {
    AbstractTransaction username(String username)
    {
        this.transaction.asset.delegate = [username: username]

        return this
    }

    int getType()
    {
        DELEGATE_REGISTRATION.getValue()
    }
}
