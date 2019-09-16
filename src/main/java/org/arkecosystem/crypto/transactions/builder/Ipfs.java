package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.TransactionType;

public class Ipfs extends AbstractTransaction{

    public TransactionType getType() {
        return TransactionType.IPFS;
    }
}
