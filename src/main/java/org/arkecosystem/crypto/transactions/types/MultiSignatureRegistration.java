package org.arkecosystem.crypto.transactions.types;

import java.nio.ByteBuffer;
import java.util.HashMap;
import org.arkecosystem.crypto.enums.CoreTransactionTypes;
import org.arkecosystem.crypto.enums.TransactionTypeGroup;

public class MultiSignatureRegistration extends Transaction {

    @Override
    public int getTransactionType() {
        return CoreTransactionTypes.MULTI_SIGNATURE_REGISTRATION.getValue();
    }

    @Override
    public int getTransactionTypeGroup() {
        return TransactionTypeGroup.CORE.getValue();
    }

    @Override
    public HashMap<String, Object> assetHashMap() {

        throw new UnsupportedOperationException(
                "MultiSignatureRegistration is not supported in java sdk");
    }

    @Override
    public byte[] serialize() {
        throw new UnsupportedOperationException(
                "MultiSignatureRegistration is not supported in java sdk");
    }

    @Override
    public void deserialize(ByteBuffer buffer) {
        throw new UnsupportedOperationException(
                "MultiSignatureRegistration is not supported in java sdk");
    }
}
