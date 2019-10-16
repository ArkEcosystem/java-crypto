package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.configuration.Fee;
import org.arkecosystem.crypto.configuration.Network;
import org.arkecosystem.crypto.enums.CoreTransactionTypes;
import org.arkecosystem.crypto.enums.TransactionTypeGroup;
import org.arkecosystem.crypto.transactions.Transaction;
import org.arkecosystem.crypto.utils.Slot;

public abstract class AbstractTransaction<TBuilder extends AbstractTransaction<TBuilder>> {
    public Transaction transaction;

    public AbstractTransaction() {
        this.transaction = new Transaction();
        this.transaction.type = this.getType().getValue();
        this.transaction.fee = Fee.getCoreFee(this.getType());
        this.transaction.timestamp = Slot.time();
        this.transaction.version = 2;
        this.transaction.network = Network.get().version();
        this.transaction.typeGroup = TransactionTypeGroup.CORE.getValue();
        this.transaction.nonce = 0;
    }

    public TBuilder version(int version) {
        this.transaction.version = version;
        return this.instance();
    };

    public TBuilder typeGroup(int typeGroup) {
        if (typeGroup > Short.MAX_VALUE) {
            throw new IllegalArgumentException(
                    "Type group should not be bigger then 2 bytes (bigger then 32767)");
        }
        this.transaction.typeGroup = typeGroup;
        return this.instance();
    }

    public TBuilder nonce(long nonce) {
        this.transaction.nonce = nonce;
        return this.instance();
    };

    public TBuilder secondSign(String passphrase) {
        this.transaction.secondSign(passphrase);
        this.transaction.id = this.transaction.computeId();

        return this.instance();
    }

    public TBuilder network(int network) {
        this.transaction.network = network;
        return this.instance();
    }

    public TBuilder fee(long fee) {
        this.transaction.fee = fee;
        return this.instance();
    }

    public TBuilder amount(long amount) {
        this.transaction.amount = amount;
        return this.instance();
    }

    public TBuilder sign(String passphrase) {
        if (this.transaction.type == CoreTransactionTypes.MULTI_SIGNATURE_REGISTRATION.getValue()
                && this.transaction.version == 2) {
            throw new UnsupportedOperationException(
                    "Version 2 MultiSignatureRegistration is not supported in java sdk");
        }
        this.transaction.sign(passphrase);
        this.transaction.id = this.transaction.computeId();

        return this.instance();
    }

    abstract CoreTransactionTypes getType();

    abstract TBuilder instance();
}
