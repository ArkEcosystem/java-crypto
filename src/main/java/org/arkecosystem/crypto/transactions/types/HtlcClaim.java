package org.arkecosystem.crypto.transactions.types;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.enums.CoreTransactionTypes;
import org.arkecosystem.crypto.enums.TransactionTypeGroup;

public class HtlcClaim extends Transaction {

    @Override
    public int getTransactionType() {
        return CoreTransactionTypes.HTLC_CLAIM.getValue();
    }

    @Override
    public int getTransactionTypeGroup() {
        return TransactionTypeGroup.CORE.getValue();
    }

    @Override
    public HashMap<String, Object> assetToHashMap() {
        HashMap<String, Object> asset = new HashMap<>();

        HashMap<String, Object> claim = new HashMap<>();
        claim.put("lockTransactionId", this.asset.htlcClaimAsset.lockTransactionId);
        claim.put("unlockSecret", this.asset.htlcClaimAsset.unlockSecret);

        asset.put("claim", claim);
        return asset;
    }

    @Override
    public byte[] serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(32 + 32);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(Hex.decode(this.asset.htlcClaimAsset.lockTransactionId));
        buffer.put(Hex.decode(this.asset.htlcClaimAsset.unlockSecret));
        return buffer.array();
    }

    @Override
    public void deserialize(ByteBuffer buffer) {
        byte[] lockTransactionIdBuf = new byte[32];
        buffer.get(lockTransactionIdBuf);

        byte[] unlockSecret = new byte[32];
        buffer.get(unlockSecret);

        this.asset.htlcClaimAsset.lockTransactionId = Hex.encode(lockTransactionIdBuf);
        this.asset.htlcClaimAsset.unlockSecret = Hex.encode(unlockSecret);
    }
}
