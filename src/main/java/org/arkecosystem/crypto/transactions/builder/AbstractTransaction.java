package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.configuration.Fee;
import org.arkecosystem.crypto.configuration.Network;
import org.arkecosystem.crypto.enums.TransactionType;
import org.arkecosystem.crypto.enums.TransactionTypeGroup;
import org.arkecosystem.crypto.transactions.Transaction;
import org.arkecosystem.crypto.utils.Slot;

public abstract class AbstractTransaction {
    public Transaction transaction;

    public AbstractTransaction() {
        this.transaction = new Transaction();
        this.transaction.type = this.getType();
        this.transaction.fee = Fee.getCoreFee(this.getType());
        this.transaction.timestamp = Slot.time();
        this.transaction.version = 2;
        this.transaction.network = Network.get().version();
        this.transaction.typeGroup = TransactionTypeGroup.CORE;
        this.transaction.nonce = 0;
    }

    public AbstractTransaction sign(String passphrase) {
        if (this.transaction.type == TransactionType.MULTI_SIGNATURE_REGISTRATION && this.transaction.version == 2){
             throw new UnsupportedOperationException("Version 2 MultiSignatureRegistration is not supported in java sdk");
        }
        this.transaction.sign(passphrase);
        this.transaction.id = this.transaction.computeId();

        return this;
    }

    public AbstractTransaction secondSign(String passphrase) {
        this.transaction.secondSign(passphrase);
        this.transaction.id = this.transaction.computeId();

        return this;
    }

    abstract TransactionType getType();

    public AbstractTransaction version(int version){
        this.transaction.version = version;
        return this;
    };

    public AbstractTransaction nonce(long nonce){
        this.transaction.nonce = nonce;
        return this;
    };
}

