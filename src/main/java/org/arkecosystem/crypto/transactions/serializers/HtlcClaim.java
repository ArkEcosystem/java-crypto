package org.arkecosystem.crypto.transactions.serializers;

import java.nio.ByteBuffer;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.transactions.Transaction;

public class HtlcClaim extends AbstractSerializer {
    public HtlcClaim(ByteBuffer buffer, Transaction transaction) {
        super(buffer, transaction);
    }

    @Override
    public void serialize() {
        this.buffer.put(Hex.decode(this.transaction.asset.htlcClaimAsset.lockTransactionId));
        this.buffer.put(this.transaction.asset.htlcClaimAsset.unlockSecret.getBytes());
    }
}
