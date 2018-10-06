package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.*;

import java.util.List;


public class MultiSignatureRegistration extends AbstractTransaction {

    public AbstractTransaction min(byte min) {
        this.transaction.asset.multisignature.min = min;

        return this;
    }

    public AbstractTransaction lifetime(byte lifetime) {
        this.transaction.asset.multisignature.lifetime = lifetime;

        return this;
    }

    public AbstractTransaction keysgroup(List<String> keysgroup) {
        this.transaction.asset.multisignature.keysgroup = keysgroup;

        this.transaction.fee = (keysgroup.size() + 1) * this.transaction.fee;

        return this;
    }

    public Types getType() {
        return Types.TRANSFER;
    }

}
