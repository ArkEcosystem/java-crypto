package org.arkecosystem.crypto.transactions.serializers;

import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.transactions.Transaction;

import java.nio.ByteBuffer;

public class HtlcRefund extends AbstractSerializer {
    public HtlcRefund(ByteBuffer buffer, Transaction transaction) {
        super(buffer, transaction);
    }

    public void serialize() {
        this.buffer.put(Hex.decode(this.transaction.asset.htlcRefundAsset.lockTransactionId));
    }

}
