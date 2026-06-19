package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.transactions.types.AbstractTransaction;
import org.arkecosystem.crypto.transactions.types.ValidatorUpdate;
import org.arkecosystem.crypto.utils.ProofOfPossession;

public class ValidatorUpdateBuilder extends AbstractTransactionBuilder<ValidatorUpdateBuilder> {

    public ValidatorUpdateBuilder validatorPassphrase(String passphrase) {
        ProofOfPossession.Result pop = ProofOfPossession.fromMnemonic(passphrase);
        this.transaction.validatorPublicKey = Hex.encode(pop.pk);
        this.transaction.validatorProof = Hex.encode(pop.pop);
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

}
