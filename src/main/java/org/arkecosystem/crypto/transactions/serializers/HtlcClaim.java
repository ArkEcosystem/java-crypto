package org.arkecosystem.crypto.transactions.serializers;

import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.transactions.Transaction;

import java.nio.ByteBuffer;

public class HtlcClaim extends AbstractSerializer {
    public HtlcClaim(ByteBuffer buffer, Transaction transaction) {
        super(buffer, transaction);
    }

    public void serialize() {
        this.buffer.put(Hex.decode(this.transaction.asset.htlcClaimAsset.lockTransactionId));
        this.buffer.put(this.transaction.asset.htlcClaimAsset.unlockSecret.getBytes());
    }
}
