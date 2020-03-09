package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.Fees;
import org.arkecosystem.crypto.enums.HtlcLockExpirationType;
import org.arkecosystem.crypto.transactions.TransactionAsset;
import org.arkecosystem.crypto.transactions.types.HtlcLock;
import org.arkecosystem.crypto.transactions.types.Transaction;

public class HtlcLockBuilder extends AbstractTransactionBuilder<HtlcLockBuilder> {
    public HtlcLockBuilder() {
        super();
        this.transaction.fee = Fees.HTLC_LOCK.getValue();
    }

    public HtlcLockBuilder recipientId(String recipientId) {
        this.transaction.recipientId = recipientId;
        return this;
    }

    public HtlcLockBuilder secretHash(String secretHash) {
        this.transaction.asset.htlcLockAsset.secretHash = secretHash;
        return this;
    }

    public HtlcLockBuilder expirationType(
            HtlcLockExpirationType expirationType, int expirationValue) {
        this.transaction.asset.htlcLockAsset.expiration =
                new TransactionAsset.Expiration(expirationType, expirationValue);
        return this;
    }

    public HtlcLockBuilder vendorField(String vendorField) {
        this.transaction.vendorField = vendorField;
        return this;
    }

    @Override
    public Transaction getTransactionInstance() {
        return new HtlcLock();
    }

    @Override
    public HtlcLockBuilder instance() {
        return this;
    }
}
