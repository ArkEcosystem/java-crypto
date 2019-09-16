package org.arkecosystem.crypto.transactions.deserializers;

import org.arkecosystem.crypto.transactions.Transaction;

import java.nio.ByteBuffer;

public class Ipfs extends AbstractDeserializer {
    public Ipfs(String serialized, ByteBuffer buffer, Transaction transaction) {
        super(serialized, buffer, transaction);
    }

    public void deserialize(int assetOffset) {
    }
}
