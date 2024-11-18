package org.arkecosystem.crypto.transactions.types;

import java.util.Map;

public class Transfer extends AbstractTransaction {
    public Transfer() {
        super();
    }

    public Transfer(Map<String, Object> data) {
        super(data);
    }

    @Override
    public String getPayload() {
        return "";
    }
}
