package org.arkecosystem.crypto.transactions.deserializers;

import java.nio.ByteBuffer;
import org.arkecosystem.crypto.encoding.Base58;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.enums.HtlcLockExpirationType;
import org.arkecosystem.crypto.transactions.Transaction;
import org.arkecosystem.crypto.transactions.TransactionAsset;

public class HtlcLock extends AbstractDeserializer {
    public HtlcLock(String serialized, ByteBuffer buffer, Transaction transaction) {
        super(serialized, buffer, transaction);
    }

    public void deserialize(int assetOffset) {
        this.buffer.position(assetOffset / 2);

        this.transaction.amount = this.buffer.getLong();
        byte[] secretHash = new byte[32];
        this.buffer.get(secretHash);
        this.transaction.asset.htlcLockAsset.secretHash = Hex.encode(secretHash);
        this.transaction.asset.htlcLockAsset.expiration =
                new TransactionAsset.Expiration(
                        HtlcLockExpirationType.values()[this.buffer.get() - 1],
                        this.buffer.getInt());
        byte[] recipientId = new byte[21];
        this.buffer.get(recipientId);
        this.transaction.recipientId = Base58.encodeChecked(recipientId);

        this.transaction.parseSignatures(this.serialized, assetOffset + (8 + 32 + 1 + 4 + 21) * 2);
    }
}
