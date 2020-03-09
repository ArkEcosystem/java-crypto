package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.Fees;
import org.arkecosystem.crypto.transactions.types.HtlcRefund;
import org.arkecosystem.crypto.transactions.types.Transaction;

public class HtlcRefundBuilder extends AbstractTransactionBuilder<HtlcRefundBuilder> {

    public HtlcRefundBuilder() {
        super();
        this.transaction.fee = Fees.HTLC_REFUND.getValue();
    }

    public HtlcRefundBuilder htlcRefundAsset(String lockTransactionId) {
        this.transaction.asset.htlcRefundAsset.lockTransactionId = lockTransactionId;
        return this;
    }

    @Override
    public Transaction getTransactionInstance() {
        return new HtlcRefund();
    }

    @Override
    public HtlcRefundBuilder instance() {
        return this;
    }
}
