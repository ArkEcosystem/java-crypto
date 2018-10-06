package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.*;

public class DelegateRegistration extends AbstractTransaction {
    public AbstractTransaction username(String username) {
        this.transaction.asset.delegate.username = username;

        return this;
    }

    public Types getType() {
        return Types.DELEGATE_REGISTRATION;
    }

}
