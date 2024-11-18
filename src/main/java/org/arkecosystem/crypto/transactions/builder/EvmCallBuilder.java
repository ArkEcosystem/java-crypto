package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.transactions.types.AbstractTransaction;
import org.arkecosystem.crypto.transactions.types.EvmCall;

public class EvmCallBuilder extends AbstractTransactionBuilder<EvmCallBuilder> {
    public EvmCallBuilder payload(String payload) {
        String cleanedPayload = payload.replaceFirst("^0x", "");

        this.transaction.data = cleanedPayload;

        return this.instance();
    }

    @Override
    protected AbstractTransaction getTransactionInstance() {
        return new EvmCall();
    }

    @Override
    protected EvmCallBuilder instance() {
        return this;
    }
}
