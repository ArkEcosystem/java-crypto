package org.arkecosystem.crypto.transactions.types;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.enums.CoreTransactionTypes;
import org.arkecosystem.crypto.enums.TransactionTypeGroup;
import org.arkecosystem.crypto.utils.AbiEncoder;

public class Vote extends AbstractTransaction {
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
