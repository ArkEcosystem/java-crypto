package org.arkecosystem.crypto.transactions.serializers;


import org.arkecosystem.crypto.encoding.Base58;
import org.arkecosystem.crypto.transactions.Transaction;

import java.nio.ByteBuffer;

public class Ipfs extends AbstractSerializer{
    public Ipfs(ByteBuffer buffer, Transaction transaction) {
        super(buffer, transaction);
    }

    public void serialize() {
            this.buffer.put(Base58.decode(this.transaction.asset.ipfs));
    }

}
