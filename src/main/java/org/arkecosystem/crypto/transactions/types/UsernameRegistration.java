package org.arkecosystem.crypto.transactions.types;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import org.arkecosystem.crypto.enums.CoreTransactionTypes;
import org.arkecosystem.crypto.enums.TransactionTypeGroup;

public class UsernameRegistration extends Transaction {
    @Override
    public int getTransactionType() {
        return CoreTransactionTypes.USERNAME_REGISTRATION.getValue();
    }

    @Override
    public int getTransactionTypeGroup() {
        return TransactionTypeGroup.CORE.getValue();
    }

    @Override
    public HashMap<String, Object> assetToHashMap() {
        HashMap<String, Object> asset = new HashMap<>();

        asset.put("username", this.asset.username);

        return asset;
    }

    @Override
    public byte[] serializeData() {
        byte[] username = this.asset.username.getBytes();

        ByteBuffer buffer = ByteBuffer.allocate(username.length + 1);

        buffer.order(ByteOrder.LITTLE_ENDIAN);

        buffer.put((byte) username.length);
        buffer.put(username);

        return buffer.array();
    }

    @Override
    public void deserializeData(ByteBuffer buffer) {
        int usernameLength = buffer.get() & 0xff;

        byte[] username = new byte[usernameLength];
        buffer.get(username);

        String utf8Username = new String(username);
        this.asset.username = utf8Username;
    }
}
