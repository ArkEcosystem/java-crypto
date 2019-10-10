package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.TransactionType;
import org.arkecosystem.crypto.identities.PublicKey;

public class SecondSignatureRegistration extends AbstractTransaction {

    public SecondSignatureRegistration signature(String signature) {
        this.transaction.asset.signature.publicKey = PublicKey.fromPassphrase(signature);

        return this;
    }

    public TransactionType getType() {
        return TransactionType.SECOND_SIGNATURE_REGISTRATION;
    }
}
