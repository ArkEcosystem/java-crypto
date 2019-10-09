package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.TransactionType;

public class HtlcClaim extends AbstractTransaction {

    public HtlcClaim htlcClaimAsset(String lockTransactionId, String unlockSecret){
        this.transaction.asset.htlcClaimAsset.lockTransactionId = lockTransactionId;
        this.transaction.asset.htlcClaimAsset.unlockSecret = unlockSecret;
        return this;
    }

    public TransactionType getType() {
        return TransactionType.HTLC_CLAIM;
    }
}
