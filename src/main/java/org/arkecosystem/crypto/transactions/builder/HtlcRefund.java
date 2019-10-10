package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.TransactionType;

public class HtlcRefund extends AbstractTransaction {

    public HtlcRefund htlcRefundAsset(String lockTransactionId){
        this.transaction.asset.htlcRefundAsset.lockTransactionId = lockTransactionId;
        return this;
    }

    public TransactionType getType() {
        return TransactionType.HTLC_REFUND;
    }
}
