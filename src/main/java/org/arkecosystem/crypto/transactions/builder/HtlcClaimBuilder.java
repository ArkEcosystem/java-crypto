package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.Fees;
import org.arkecosystem.crypto.transactions.types.HtlcClaim;
import org.arkecosystem.crypto.transactions.types.Transaction;

public class HtlcClaimBuilder extends AbstractTransactionBuilder<HtlcClaimBuilder> {

    public HtlcClaimBuilder() {
        super();
        this.transaction.fee = Fees.HTLC_CLAIM.getValue();
    }

    public HtlcClaimBuilder htlcClaimAsset(String lockTransactionId, String unlockSecret) {
        this.transaction.asset.htlcClaimAsset.lockTransactionId = lockTransactionId;
        this.transaction.asset.htlcClaimAsset.unlockSecret = unlockSecret;
        return this;
    }

    @Override
    public Transaction getTransactionInstance() {
        return new HtlcClaim();
    }

    @Override
    public HtlcClaimBuilder instance() {
        return this;
    }
}
