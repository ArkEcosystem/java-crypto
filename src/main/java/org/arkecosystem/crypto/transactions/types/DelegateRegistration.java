package org.arkecosystem.crypto.transactions.types;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import org.arkecosystem.crypto.enums.CoreTransactionTypes;
import org.arkecosystem.crypto.enums.TransactionTypeGroup;

public class DelegateRegistration extends Transaction {
    @Override
    public int getTransactionType() {
        return CoreTransactionTypes.DELEGATE_REGISTRATION.getValue();
    }

    @Override
    public int getTransactionTypeGroup() {
        return TransactionTypeGroup.CORE.getValue();
    }

    @Override
    public HashMap<String, Object> assetToHashMap() {
        HashMap<String, Object> asset = new HashMap<>();

        HashMap<String, String> delegate = new HashMap<>();
        delegate.put("username", this.asset.delegate.username);
        asset.put("delegate", delegate);

        return asset;
    }

    @Override
    public byte[] serialize() {
        byte[] delegateBytes = this.asset.delegate.username.getBytes();

        ByteBuffer buffer = ByteBuffer.allocate(1 + delegateBytes.length);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte) delegateBytes.length);
        buffer.put(delegateBytes);
        return buffer.array();
    }

    @Override
    public void deserialize(ByteBuffer buffer) {
        int usernameLength = buffer.get();

        byte[] username = new byte[usernameLength];
        buffer.get(username);

        this.asset.delegate.username = new String(username);
    }
}
