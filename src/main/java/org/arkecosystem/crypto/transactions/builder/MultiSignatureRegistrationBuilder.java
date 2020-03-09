package org.arkecosystem.crypto.transactions.builder;

import java.util.List;
import org.arkecosystem.crypto.enums.Fees;
import org.arkecosystem.crypto.transactions.types.MultiSignatureRegistration;
import org.arkecosystem.crypto.transactions.types.Transaction;

public class MultiSignatureRegistrationBuilder
        extends AbstractTransactionBuilder<MultiSignatureRegistrationBuilder> {
    public MultiSignatureRegistrationBuilder() {
        super();
        this.transaction.version = 1;
        this.transaction.fee = Fees.MULTI_SIGNATURE_REGISTRATION.getValue();
    }

    @Override
    public Transaction getTransactionInstance() {
        return new MultiSignatureRegistration();
    }

    @Deprecated
    public MultiSignatureRegistrationBuilder min(int min) {
        return this.min((byte) min);
    }

    @Deprecated
    public MultiSignatureRegistrationBuilder min(byte min) {
        this.transaction.asset.multisignature.min = min;

        return this;
    }

    @Deprecated
    public MultiSignatureRegistrationBuilder lifetime(int lifetime) {
        return this.lifetime((byte) lifetime);
    }

    @Deprecated
    public MultiSignatureRegistrationBuilder lifetime(byte lifetime) {
        this.transaction.asset.multisignature.lifetime = lifetime;
        return this;
    }

    @Deprecated
    public MultiSignatureRegistrationBuilder keysgroup(List<String> keysgroup) {
        this.transaction.asset.multisignature.keysgroup = keysgroup;

        this.transaction.fee = (keysgroup.size() + 1) * this.transaction.fee;

        return this;
    }

    @Override
    public MultiSignatureRegistrationBuilder instance() {
        return this;
    }
}
