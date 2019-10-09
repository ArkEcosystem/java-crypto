package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.TransactionType;

public class Ipfs extends AbstractTransaction<Ipfs> {

    public AbstractTransaction ipfsAsset(String ipfsId) {
        this.transaction.asset.ipfs = ipfsId;
        return this;
    }

    @Override
    public TransactionType getType() {
        return TransactionType.IPFS;
    }

    @Override
    public Ipfs instance() {
        return this;
    }
}
