package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.CoreTransactionTypes;
import org.arkecosystem.crypto.identities.PublicKey;

public class SecondSignatureRegistration extends AbstractTransaction<SecondSignatureRegistration> {

    public SecondSignatureRegistration signature(String signature) {
        this.transaction.asset.signature.publicKey = PublicKey.fromPassphrase(signature);

        return this;
    }

    @Override
    public CoreTransactionTypes getType() {
        return CoreTransactionTypes.SECOND_SIGNATURE_REGISTRATION;
    }

    @Override
    public SecondSignatureRegistration instance() {
        return this;
    }
}
