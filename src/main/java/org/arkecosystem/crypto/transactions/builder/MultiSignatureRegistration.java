package org.arkecosystem.crypto.transactions.builder;

import java.util.List;
import org.arkecosystem.crypto.transactions.types.Transaction;

public class MultiSignatureRegistration
        extends AbstractTransactionBuilder<MultiSignatureRegistration> {
    public MultiSignatureRegistration() {
        super();
        this.transaction.version = 1;
    }

    @Override
    public Transaction getTransactionInstance() {
        return null;
    }

    public MultiSignatureRegistration min(int min) {
        return this.min((byte) min);
    }

    public MultiSignatureRegistration min(byte min) {
        this.transaction.asset.multisignature.min = min;

        return this;
    }

    public MultiSignatureRegistration lifetime(int lifetime) {
        return this.lifetime((byte) lifetime);
    }

    public MultiSignatureRegistration lifetime(byte lifetime) {
        this.transaction.asset.multisignature.lifetime = lifetime;

        return this;
    }

    public MultiSignatureRegistration keysgroup(List<String> keysgroup) {
        this.transaction.asset.multisignature.keysgroup = keysgroup;

        this.transaction.fee = (keysgroup.size() + 1) * this.transaction.fee;

        return this;
    }

    @Override
    public MultiSignatureRegistration instance() {
        return this;
    }
}
