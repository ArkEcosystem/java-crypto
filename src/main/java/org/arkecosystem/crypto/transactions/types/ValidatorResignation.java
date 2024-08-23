package org.arkecosystem.crypto.transactions.types;

import java.nio.ByteBuffer;
import java.util.HashMap;
import org.arkecosystem.crypto.enums.CoreTransactionTypes;
import org.arkecosystem.crypto.enums.TransactionTypeGroup;

public class ValidatorResignation extends Transaction {
    @Override
    public int getTransactionType() {
        return CoreTransactionTypes.VALIDATOR_RESIGNATION.getValue();
    }

    @Override
    public int getTransactionTypeGroup() {
        return TransactionTypeGroup.CORE.getValue();
    }

    @Override
    public HashMap<String, Object> assetToHashMap() {
        return null;
    }

    @Override
    public byte[] serializeData() {
        return new byte[0];
    }

    @Override
    public void deserializeData(ByteBuffer buffer) {}
}
