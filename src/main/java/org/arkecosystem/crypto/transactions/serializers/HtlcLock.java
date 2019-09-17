package org.arkecosystem.crypto.transactions.serializers;

import org.arkecosystem.crypto.encoding.Base58;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.transactions.Transaction;

import java.nio.ByteBuffer;

public class HtlcLock extends AbstractSerializer {
    public HtlcLock(ByteBuffer buffer, Transaction transaction) {
        super(buffer, transaction);
    }

    public void serialize() {
        byte[] byteBuffer = ByteBuffer
            .allocate(8 + 32 + 4 + 8 + 21)
            .putLong(this.transaction.amount)
            .put(Hex.decode(this.transaction.asset.htlcLockAsset.secretHash))
            .put((byte)this.transaction.asset.htlcLockAsset.expiration.type.getValue())
            .putLong(this.transaction.asset.htlcLockAsset.expiration.value)
            .put(Base58.decodeChecked(this.transaction.recipientId))
            .array();

            this.buffer.put(byteBuffer);
    }
}
