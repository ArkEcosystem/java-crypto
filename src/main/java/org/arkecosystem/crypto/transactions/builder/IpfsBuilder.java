package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.transactions.types.Ipfs;
import org.arkecosystem.crypto.transactions.types.Transaction;

public class IpfsBuilder extends AbstractTransactionBuilder<IpfsBuilder> {

    public IpfsBuilder ipfsAsset(String ipfsAsset) {
        this.transaction.asset.ipfs = ipfsAsset;
        return this;
    }

    @Override
    public Transaction getTransactionInstance() {
        return new Ipfs();
    }

    @Override
    public IpfsBuilder instance() {
        return this;
    }
}
