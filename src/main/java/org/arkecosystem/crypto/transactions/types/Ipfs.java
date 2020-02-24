package org.arkecosystem.crypto.transactions.types;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.arkecosystem.crypto.encoding.Base58;
import org.arkecosystem.crypto.enums.CoreTransactionTypes;
import org.arkecosystem.crypto.enums.TransactionTypeGroup;

public class Ipfs extends Transaction {

    @Override
    public int getTransactionType() {
        return CoreTransactionTypes.IPFS.getValue();
    }

    @Override
    public int getTransactionTypeGroup() {
        return TransactionTypeGroup.CORE.getValue();
    }

    @Override
    public byte[] serialize() {
        byte[] ipfsBuffer = Base58.decode(this.asset.ipfs);
        ByteBuffer buffer = ByteBuffer.allocate(ipfsBuffer.length);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(ipfsBuffer);
        return buffer.array();
    }

    @Override
    public void deserialize(ByteBuffer buffer) {
        byte hashFunction = buffer.get();
        byte ipfsHashLength = buffer.get();
        byte[] hashBuffer = new byte[ipfsHashLength];
        buffer.get(hashBuffer);

        byte[] ipfsBuffer =
                ByteBuffer.allocate(ipfsHashLength + 2)
                        .put(hashFunction)
                        .put(ipfsHashLength)
                        .put(hashBuffer)
                        .array();

        this.asset.ipfs = Base58.encode(ipfsBuffer);
    }
}
