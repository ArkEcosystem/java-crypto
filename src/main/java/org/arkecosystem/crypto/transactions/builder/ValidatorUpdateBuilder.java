package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.transactions.types.AbstractTransaction;
import org.arkecosystem.crypto.transactions.types.ValidatorUpdate;
import org.arkecosystem.crypto.utils.ProofOfPossession;

public class ValidatorUpdateBuilder extends AbstractTransactionBuilder<ValidatorUpdateBuilder> {

    public ValidatorUpdateBuilder validatorPassphrase(String passphrase) {
        ProofOfPossession.Result pop = ProofOfPossession.fromMnemonic(passphrase);
        this.transaction.validatorPublicKey = bytesToHex(pop.pk);
        this.transaction.validatorProof = bytesToHex(pop.pop);
        this.transaction.refreshPayloadData();
        return this.instance();
    }

    @Override
    protected AbstractTransaction getTransactionInstance() {
        return new ValidatorUpdate();
    }

    @Override
    protected ValidatorUpdateBuilder instance() {
        return this;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
