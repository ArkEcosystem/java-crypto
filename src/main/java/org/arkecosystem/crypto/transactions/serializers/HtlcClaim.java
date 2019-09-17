package org.arkecosystem.crypto.transactions.serializers;

import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.transactions.Transaction;

import java.nio.ByteBuffer;

public class HtlcClaim extends AbstractSerializer {
    public HtlcClaim(ByteBuffer buffer, Transaction transaction) {
        super(buffer, transaction);
    }

    public void serialize() {
        byte[] buff = ByteBuffer.allocate(64)
            .put(Hex.decode(this.transaction.asset.htlcClaimAsset.lockTransactionId))
            .put(this.transaction.asset.htlcClaimAsset.unlockSecret.getBytes())
            .array();

        this.buffer.put(buff);
    }
}
