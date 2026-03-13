package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.configuration.Network;
import org.arkecosystem.crypto.transactions.types.AbstractTransaction;

public abstract class AbstractTransactionBuilder<
        TBuilder extends AbstractTransactionBuilder<TBuilder>> {
    public final AbstractTransaction transaction;

    public AbstractTransactionBuilder() {
        this.transaction = getTransactionInstance();

        initializeTransactionDefaults();
    }

    private void initializeTransactionDefaults() {
        this.transaction.value = "0";
        this.transaction.senderPublicKey = "";
        this.transaction.fee = '5';
        this.transaction.nonce = 1;
        this.transaction.network = Network.get().version();
        this.transaction.gasLimit = 1_000_000;
        // Set the default data for the transaction
        this.transaction.refreshPayloadData();
    }

    public TBuilder gasLimit(long gasLimit) {
        this.transaction.gasLimit = gasLimit;
        return this.instance();
    }

    public TBuilder recipientAddress(String recipientAddressId) {
        this.transaction.recipientAddress = recipientAddressId;
        return this.instance();
    }

    public TBuilder gasPrice(long gasPrice) {
        this.transaction.gasPrice = gasPrice;
        return this.instance();
    }

    public TBuilder nonce(long nonce) {
        this.transaction.nonce = nonce;
        return this.instance();
    }

    public TBuilder network(int network) {
        this.transaction.network = network;
        return this.instance();
    }

    public TBuilder sign(String passphrase) {
        this.transaction.sign(passphrase);
        this.transaction.computeId();
        return this.instance();
    }

    public boolean verify() {
        return this.transaction.verify();
    }

    public String toJson() {
        return this.transaction.toJson();
    }

    protected abstract AbstractTransaction getTransactionInstance();

    protected abstract TBuilder instance();
}
