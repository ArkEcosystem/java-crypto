package org.arkecosystem.crypto.transactions.types;

public class EvmCall extends AbstractTransaction {
    @Override
    public String getPayload() {
        return this.data != null ? this.data : "";
    }
}
