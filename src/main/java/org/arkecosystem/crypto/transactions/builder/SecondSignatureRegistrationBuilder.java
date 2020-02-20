package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.identities.PublicKey;
import org.arkecosystem.crypto.transactions.types.SecondSignatureRegistration;
import org.arkecosystem.crypto.transactions.types.Transaction;

public class SecondSignatureRegistrationBuilder
        extends AbstractTransactionBuilder<SecondSignatureRegistrationBuilder> {

    public SecondSignatureRegistrationBuilder signature(String signature) {
        this.transaction.asset.signature.publicKey = PublicKey.fromPassphrase(signature);

        return this;
    }

    @Override
    public Transaction getTransactionInstance() {
        return new SecondSignatureRegistration();
    }

    @Override
    public SecondSignatureRegistrationBuilder instance() {
        return this;
    }
}
