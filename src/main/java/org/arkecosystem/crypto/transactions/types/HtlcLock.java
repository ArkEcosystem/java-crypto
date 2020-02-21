package org.arkecosystem.crypto.transactions.types;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.arkecosystem.crypto.encoding.Base58;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.enums.CoreTransactionTypes;
import org.arkecosystem.crypto.enums.HtlcLockExpirationType;
import org.arkecosystem.crypto.enums.TransactionTypeGroup;
import org.arkecosystem.crypto.transactions.TransactionAsset;

public class HtlcLock extends Transaction {

    @Override
    public int getTransactionType() {
        return CoreTransactionTypes.HTLC_LOCK.getValue();
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
    public byte[] serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(8 + 32 + 1 + 4 + 21);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putLong(this.amount);
        buffer.put(Hex.decode(this.asset.htlcLockAsset.secretHash));
        buffer.put((byte) this.asset.htlcLockAsset.expiration.type.getValue());
        buffer.putInt(this.asset.htlcLockAsset.expiration.value);
        buffer.put(Base58.decodeChecked(this.recipientId));
        return buffer.array();
    }

    @Override
    public void deserialize(ByteBuffer buffer) {
        this.amount = buffer.getLong();
        byte[] secretHash = new byte[32];
        buffer.get(secretHash);
        this.asset.htlcLockAsset.secretHash = Hex.encode(secretHash);
        this.asset.htlcLockAsset.expiration =
                new TransactionAsset.Expiration(
                        HtlcLockExpirationType.values()[buffer.get() - 1], buffer.getInt());
        byte[] recipientId = new byte[21];
        buffer.get(recipientId);
        this.recipientId = Base58.encodeChecked(recipientId);
    }
}
