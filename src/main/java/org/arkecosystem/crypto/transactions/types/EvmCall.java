package org.arkecosystem.crypto.transactions.types;

import java.util.Map;

public class EvmCall extends AbstractTransaction {
    public EvmCall() {
        super();
    }

    public EvmCall(Map<String, Object> data) {
        super(data);
    }

    @Override
    public String getPayload() {
        return this.data != null ? this.data : "";
    }
}
