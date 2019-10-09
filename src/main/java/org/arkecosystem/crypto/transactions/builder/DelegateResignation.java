package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.TransactionType;

public class DelegateResignation extends AbstractTransaction{

    public TransactionType getType() {
        return TransactionType.DELEGATE_RESIGNATION;
    }
}
