package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.TransactionType;

public class Ipfs extends AbstractTransaction{

    public AbstractTransaction ipfsAsset(String ipfsId){
        this.transaction.asset.ipfs = ipfsId;
        return this;
    }

    public TransactionType getType() {
        return TransactionType.IPFS;
    }
}
