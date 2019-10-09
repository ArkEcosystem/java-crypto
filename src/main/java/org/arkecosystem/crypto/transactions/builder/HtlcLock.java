package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.HtlcLockExpirationType;
import org.arkecosystem.crypto.enums.TransactionType;
import org.arkecosystem.crypto.transactions.TransactionAsset;

public class HtlcLock extends AbstractTransaction {
    public HtlcLock recipientId(String recipientId) {
        this.transaction.recipientId = recipientId;
        return this;
    }

    public HtlcLock secretHash(String secretHash) {
        this.transaction.asset.htlcLockAsset.secretHash = secretHash;
        return this;
    }

    public HtlcLock amount(long amount) {
        this.transaction.amount = amount;
        return this;
    }

    public HtlcLock expirationType(HtlcLockExpirationType expirationType, int expirationValue) {
        this.transaction.asset.htlcLockAsset.expiration =
                new TransactionAsset.Expiration(expirationType, expirationValue);
        return this;
    }

    public HtlcLock vendorField(String vendorField) {
        this.transaction.vendorField = vendorField;
        return this;
    }

    public TransactionType getType() {
        return TransactionType.HTLC_LOCK;
    }
}
