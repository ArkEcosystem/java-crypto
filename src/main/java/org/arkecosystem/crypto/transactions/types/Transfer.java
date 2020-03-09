package org.arkecosystem.crypto.transactions.types;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import org.arkecosystem.crypto.encoding.Base58;
import org.arkecosystem.crypto.enums.CoreTransactionTypes;
import org.arkecosystem.crypto.enums.TransactionTypeGroup;

public class Transfer extends Transaction {
    @Override
    public int getTransactionType() {
        return CoreTransactionTypes.TRANSFER.getValue();
    }

    @Override
    public int getTransactionTypeGroup() {
        return TransactionTypeGroup.CORE.getValue();
    }

    @Override
    public boolean hasVendorField() {
        return true;
    }

    @Override
    public HashMap<String, Object> assetToHashMap() {
        return null;
    }

    @Override
    public byte[] serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putLong(this.amount);
        buffer.putInt(this.expiration);
        buffer.put(Base58.decodeChecked(this.recipientId));
        return buffer.array();
    }

    @Override
    public void deserialize(ByteBuffer buffer) {
        this.amount = buffer.getLong();
        this.expiration = buffer.getInt();

        byte[] recipientId = new byte[21];
        buffer.get(recipientId);
        this.recipientId = Base58.encodeChecked(recipientId);
    }
}
