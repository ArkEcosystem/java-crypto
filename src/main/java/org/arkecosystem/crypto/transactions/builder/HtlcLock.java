package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.HtlcLockExpirationType;
import org.arkecosystem.crypto.enums.TransactionType;
import org.arkecosystem.crypto.transactions.TransactionAsset;

public class HtlcLock extends  AbstractTransaction {

    public HtlcLock htlcLockAsset(String secretHash, HtlcLockExpirationType expirationType, long expirationValue){
        this.transaction.asset.htlcLockAsset.secretHash = secretHash;
        this.transaction.asset.htlcLockAsset.expiration = new TransactionAsset.Expiration(expirationType,expirationValue);
        return this;
    }

    public TransactionType getType() {
        return TransactionType.HTLC_LOCK;
    }
}
