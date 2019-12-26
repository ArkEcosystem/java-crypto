package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.CoreTransactionTypes;
import org.arkecosystem.crypto.identities.PublicKey;

public class SecondSignatureRegistration extends AbstractBuilder<SecondSignatureRegistration> {

    public SecondSignatureRegistration signature(String signature) {
        this.transaction.asset.signature.publicKey = PublicKey.fromPassphrase(signature);

        return this;
    }

    @Override
    public int getType() {
        return CoreTransactionTypes.SECOND_SIGNATURE_REGISTRATION.getValue();
    }

    @Override
    public SecondSignatureRegistration instance() {
        return this;
    }
}
