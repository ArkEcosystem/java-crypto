package org.arkecosystem.crypto.transactions.types;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.enums.CoreTransactionTypes;
import org.arkecosystem.crypto.enums.TransactionTypeGroup;
import org.arkecosystem.crypto.utils.Address;
import org.web3j.crypto.Keys;

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
        ByteBuffer buffer = ByteBuffer.allocate(32);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putLong(this.amount);
        buffer.putInt(this.expiration);
        
        // Convert recipientId to a hex string without the 0x prefix and then to bytes
        byte[] recipientBytes = Hex.decode(Address.toBufferHexString(this.recipientId));
        buffer.put(recipientBytes);

        return buffer.array();
    }

    @Override
    public void deserialize(ByteBuffer buffer) {
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        
        this.amount = buffer.getLong();
        this.expiration = buffer.getInt();

        byte[] recipientId = new byte[20];
        buffer.get(recipientId);
        this.recipientId = Keys.toChecksumAddress("0x" + Hex.encode(recipientId));
    }
}
