package org.arkecosystem.crypto.transactions.deserializers;

import java.nio.ByteBuffer;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.transactions.Transaction;

public class HtlcClaim extends AbstractDeserializer {
    public HtlcClaim(String serialized, ByteBuffer buffer, Transaction transaction) {
        super(serialized, buffer, transaction);
    }

    public void deserialize(int assetOffset) {
        this.buffer.position(assetOffset / 2);

        byte[] lockTransactionIdBuf = new byte[32];
        this.buffer.get(lockTransactionIdBuf);

        byte[] unlockSecret = new byte[32];
        this.buffer.get(unlockSecret);

        this.transaction.asset.htlcClaimAsset.lockTransactionId = Hex.encode(lockTransactionIdBuf);
        this.transaction.asset.htlcClaimAsset.unlockSecret = new String(unlockSecret);

        this.transaction.parseSignatures(this.serialized, assetOffset + 64 + 64);
    }
}
