package org.arkecosystem.crypto.transactions.types;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.enums.CoreTransactionTypes;
import org.arkecosystem.crypto.enums.TransactionTypeGroup;

public class ValidatorRegistration extends Transaction {
    @Override
    public int getTransactionType() {
        return CoreTransactionTypes.VALIDATOR_REGISTRATION.getValue();
    }

    @Override
    public int getTransactionTypeGroup() {
        return TransactionTypeGroup.CORE.getValue();
    }

    @Override
    public HashMap<String, Object> assetToHashMap() {
        HashMap<String, Object> asset = new HashMap<>();

        asset.put("validatorPublicKey", this.asset.validatorPublicKey);

        return asset;
    }

    @Override
    public byte[] serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(48);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(Hex.decode(this.asset.validatorPublicKey));

        return buffer.array();
    }

    @Override
    public void deserialize(ByteBuffer buffer) {
        byte[] validatorPublicKey = new byte[48];
        buffer.get(validatorPublicKey);
        this.asset.validatorPublicKey = Hex.encode(validatorPublicKey);
    }
}
