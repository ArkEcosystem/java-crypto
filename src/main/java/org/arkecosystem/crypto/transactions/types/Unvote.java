package org.arkecosystem.crypto.transactions.types;

import java.util.ArrayList;
import java.util.HashMap;
import org.arkecosystem.crypto.utils.AbiEncoder;

public class Unvote extends AbstractTransaction {
    @Override
    public String getPayload() {
        try {
            AbiEncoder abiEncoder = new AbiEncoder();
            return abiEncoder.encodeFunctionCall("vote", new ArrayList<>());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public HashMap<String, Object> assetToHashMap() {
        return null;
    }
}
