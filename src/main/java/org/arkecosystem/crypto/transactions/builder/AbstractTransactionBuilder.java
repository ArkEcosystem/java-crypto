package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.configuration.Network;
import org.arkecosystem.crypto.enums.CoreTransactionTypes;
import org.arkecosystem.crypto.enums.Fees;
import org.arkecosystem.crypto.enums.TransactionTypeGroup;
import org.arkecosystem.crypto.transactions.TransactionAsset;
import org.arkecosystem.crypto.transactions.types.AbstractTransaction;

public abstract class AbstractTransactionBuilder<TBuilder extends AbstractTransactionBuilder<TBuilder>> {
    public final AbstractTransaction transaction;

    public AbstractTransactionBuilder() {
        this.transaction = getTransactionInstance();
        initializeTransactionDefaults();
    }

    private void initializeTransactionDefaults() {
        this.transaction.type = CoreTransactionTypes.EVM_CALL.getValue();
        this.transaction.typeGroup = TransactionTypeGroup.CORE.getValue();
        this.transaction.amount = 0;
        this.transaction.senderPublicKey = "";
        this.transaction.fee = Fees.EVM.getValue();
        this.transaction.version = 1;
        this.transaction.network = Network.get().version();
        this.transaction.nonce = 1;

        this.transaction.asset = new TransactionAsset();
        this.transaction.asset.evmCall.gasLimit = 1000000;
        this.transaction.asset.evmCall.payload = "";
    }

    public TBuilder gasLimit(int gasLimit) {
        this.transaction.asset.evmCall.gasLimit = gasLimit;
        return this.instance();
    }

    public TBuilder recipient(String recipientId) {
        this.transaction.recipientId = recipientId;
        return this.instance();
    }

    public TBuilder fee(long fee) {
        this.transaction.fee = fee;
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

    public TBuilder multiSign(String passphrase, int index) {
        this.transaction.multiSign(passphrase, index);
        this.transaction.computeId();
        return this.instance();
    }

    public TBuilder secondSign(String secondPassphrase) {
        this.transaction.secondSign(secondPassphrase);
        this.transaction.computeId();
        return this.instance();
    }

    public boolean verify() {
        return this.transaction.verify();
    }

    public boolean secondVerify(String secondPublicKey) {
        return this.transaction.secondVerify(secondPublicKey);
    }

    public String toJson() {
        return this.transaction.toJson();
    }

    protected abstract AbstractTransaction getTransactionInstance();

    protected abstract TBuilder instance();
}
