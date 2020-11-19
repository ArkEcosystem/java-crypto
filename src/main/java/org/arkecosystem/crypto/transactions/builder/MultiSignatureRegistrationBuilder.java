package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.Fees;
import org.arkecosystem.crypto.transactions.types.MultiSignatureRegistration;
import org.arkecosystem.crypto.transactions.types.Transaction;

import java.util.List;

public class MultiSignatureRegistrationBuilder
        extends AbstractTransactionBuilder<MultiSignatureRegistrationBuilder> {
    public MultiSignatureRegistrationBuilder() {
        super();
        this.transaction.fee = Fees.MULTI_SIGNATURE_REGISTRATION.getValue();
    }

    @Override
    public Transaction getTransactionInstance() {
        return new MultiSignatureRegistration();
    }

    public MultiSignatureRegistrationBuilder min(int min) {
        return this.min((byte) min);
    }

    public MultiSignatureRegistrationBuilder min(byte min) {
        this.transaction.asset.multiSignature.min = min;

        return this;
    }

    public MultiSignatureRegistrationBuilder publicKeys(List<String> publicKeys) {
        this.transaction.asset.multiSignature.publicKeys = publicKeys;

        this.transaction.fee = (publicKeys.size() + 1) * this.transaction.fee;

        return this;
    }

    @Override
    public MultiSignatureRegistrationBuilder instance() {
        return this;
    }
}
