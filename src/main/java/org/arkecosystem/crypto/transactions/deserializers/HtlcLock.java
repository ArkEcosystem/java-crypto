package org.arkecosystem.crypto.transactions.deserializers;

import org.arkecosystem.crypto.encoding.Base58;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.enums.HtlcLockExpirationType;
import org.arkecosystem.crypto.transactions.Transaction;
import org.arkecosystem.crypto.transactions.TransactionAsset;

import java.nio.ByteBuffer;

public class HtlcLock extends AbstractDeserializer {
    public HtlcLock(String serialized, ByteBuffer buffer, Transaction transaction) {
        super(serialized, buffer, transaction);
    }

    public void deserialize(int assetOffset) {
        this.buffer.position(assetOffset / 2);

        this.transaction.amount = this.buffer.getLong();
        System.out.println("HtlcLock [deserialize]-amount: " + this.transaction.amount);
        byte[] secretHash = new byte[32];
        this.buffer.get(secretHash);
        this.transaction.asset.htlcLockAsset.secretHash = Hex.encode(secretHash);
        System.out.println("HtlcLock [deserialize]-secretHash: " + this.transaction.asset.htlcLockAsset.secretHash);
        int a = this.buffer.get() & 0xff;
        System.out.println("a:" + a);
        this.transaction.asset.htlcLockAsset.expiration = new TransactionAsset
            .Expiration(HtlcLockExpirationType.values()[a - 1], this.buffer.getLong());
        System.out.println("HtlcLock [deserialize]-expiration.type: "+this.transaction.asset.htlcLockAsset.expiration.type);
        System.out.println("HtlcLock [deserialize]-expiration.value: "+this.transaction.asset.htlcLockAsset.expiration.value);
        byte[] recipientId = new byte[21];
        this.buffer.get(recipientId);
        this.transaction.recipientId = Base58.encodeChecked(recipientId);
        System.out.println("HtlcLock [deserialize]-recipientId: "+this.transaction.recipientId);

        this.transaction.parseSignatures(this.serialized, assetOffset + ((8 + 32 + 1 + 8 + 21) * 2));
    }
}
