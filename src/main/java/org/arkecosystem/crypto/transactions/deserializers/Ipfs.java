package org.arkecosystem.crypto.transactions.deserializers;

import java.nio.ByteBuffer;
import org.arkecosystem.crypto.encoding.Base58;
import org.arkecosystem.crypto.transactions.Transaction;

public class Ipfs extends AbstractDeserializer {
    public Ipfs(String serialized, ByteBuffer buffer, Transaction transaction) {
        super(serialized, buffer, transaction);
    }

    @Override
    public void deserialize(int assetOffset) {
        this.buffer.position(assetOffset / 2);

        byte hashFunction = this.buffer.get();
        byte ipfsHashLength = this.buffer.get();
        byte[] hashBuffer = new byte[ipfsHashLength];
        this.buffer.get(hashBuffer);

        byte[] ipfsBuffer =
                ByteBuffer.allocate(ipfsHashLength + 2)
                        .put(hashFunction)
                        .put(ipfsHashLength)
                        .put(hashBuffer)
                        .array();
        this.transaction.asset.ipfs = Base58.encode(ipfsBuffer);

        this.transaction.parseSignatures(this.serialized, assetOffset + (ipfsHashLength + 2) * 2);
    }
}
