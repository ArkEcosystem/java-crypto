package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.CoreTransactionTypes;

public class Ipfs extends AbstractTransactionBuilder<Ipfs> {

    public AbstractTransactionBuilder ipfsAsset(String ipfsId) {
        this.transaction.asset.ipfs = ipfsId;
        return this;
    }

    @Override
    public int getType() {
        return CoreTransactionTypes.IPFS.getValue();
    }

    @Override
    public Ipfs instance() {
        return this;
    }
}
