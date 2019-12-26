package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.CoreTransactionTypes;

public class HtlcRefund extends AbstractTransactionBuilder<HtlcRefund> {

    public HtlcRefund htlcRefundAsset(String lockTransactionId) {
        this.transaction.asset.htlcRefundAsset.lockTransactionId = lockTransactionId;
        return this;
    }

    @Override
    public int getType() {
        return CoreTransactionTypes.HTLC_REFUND.getValue();
    }

    @Override
    public HtlcRefund instance() {
        return this;
    }
}
