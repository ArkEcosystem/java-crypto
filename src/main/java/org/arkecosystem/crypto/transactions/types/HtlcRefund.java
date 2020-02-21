package org.arkecosystem.crypto.transactions.types;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.enums.CoreTransactionTypes;
import org.arkecosystem.crypto.enums.TransactionTypeGroup;

public class HtlcRefund extends Transaction {

    @Override
    public int getTransactionType() {
        return CoreTransactionTypes.HTLC_REFUND.getValue();
    }

    @Override
    public int getTransactionTypeGroup() {
        return TransactionTypeGroup.CORE.getValue();
    }

    @Override
    public byte[] serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(32);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(Hex.decode(this.asset.htlcRefundAsset.lockTransactionId));
        return buffer.array();
    }

    @Override
    public void deserialize(ByteBuffer buffer) {
        byte[] lockTransactionIdBuffer = new byte[32];
        buffer.get(lockTransactionIdBuffer);
        this.asset.htlcRefundAsset.lockTransactionId = Hex.encode(lockTransactionIdBuffer);
    }
}
