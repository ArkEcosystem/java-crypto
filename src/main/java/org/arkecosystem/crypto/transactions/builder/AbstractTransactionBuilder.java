package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.configuration.Network;
import org.arkecosystem.crypto.enums.CoreTransactionTypes;
import org.arkecosystem.crypto.enums.Fees;
import org.arkecosystem.crypto.enums.TransactionTypeGroup;
import org.arkecosystem.crypto.transactions.TransactionAsset;
import org.arkecosystem.crypto.transactions.types.AbstractTransaction;

public abstract class AbstractTransactionBuilder<
        TBuilder extends AbstractTransactionBuilder<TBuilder>> {
    public final AbstractTransaction transaction;

    public AbstractTransactionBuilder() {
        this.transaction = getTransactionInstance();
        this.transaction.type = CoreTransactionTypes.EVM_CALL.getValue();
        this.transaction.typeGroup = TransactionTypeGroup.CORE.getValue();
        this.transaction.amount = 0;
        this.transaction.senderPublicKey = "";
        this.transaction.fee = Fees.EVM.getValue();
        this.transaction.version = 1;
        this.transaction.network = Network.get().version();
        this.transaction.nonce = 1;

        this.transaction.asset = new TransactionAsset();
    this.transaction.asset.evmCall.gasLimit = 1000000; // Default gas limit
    this.transaction.asset.evmCall.payload = ""; 
    }

    public TBuilder version(int version) {
        this.transaction.version = version;
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

    public TBuilder fee(long fee) {
        this.transaction.fee = fee;
        return this.instance();
    }

    public TBuilder amount(long amount) {
        this.transaction.amount = amount;
        return this.instance();
    }

    public TBuilder sign(String passphrase) {
        this.transaction.sign(passphrase);
        this.transaction.computeId();

        return this.instance();
    }

    public TBuilder secondSign(String passphrase) {
        this.transaction.secondSign(passphrase);
        this.transaction.computeId();

        return this.instance();
    }

    public TBuilder multiSign(String passphrase, int index) {
        this.transaction.multiSign(passphrase, index);
        this.transaction.computeId();

        return this.instance();
    }

    protected abstract AbstractTransaction getTransactionInstance();

    protected abstract TBuilder instance();
}
