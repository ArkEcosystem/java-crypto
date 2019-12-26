package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.CoreTransactionTypes;

public class HtlcClaim extends AbstractBuilder<HtlcClaim> {

    public HtlcClaim htlcClaimAsset(String lockTransactionId, String unlockSecret) {
        this.transaction.asset.htlcClaimAsset.lockTransactionId = lockTransactionId;
        this.transaction.asset.htlcClaimAsset.unlockSecret = unlockSecret;
        return this;
    }

    @Override
    public int getType() {
        return CoreTransactionTypes.HTLC_CLAIM.getValue();
    }

    @Override
    public HtlcClaim instance() {
        return this;
    }
}
