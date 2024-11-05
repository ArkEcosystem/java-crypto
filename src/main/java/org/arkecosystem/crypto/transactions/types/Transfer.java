package org.arkecosystem.crypto.transactions.types;

import java.util.HashMap;

public class Transfer extends AbstractTransaction {
    @Override
    public String getPayload() {
        return "";
    }

    @Override
    public HashMap<String, Object> assetToHashMap() {
        return null;
    }
}
