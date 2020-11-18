package org.arkecosystem.crypto.transactions.types;

import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.enums.CoreTransactionTypes;
import org.arkecosystem.crypto.enums.TransactionTypeGroup;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;

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
    public HashMap<String, Object> assetToHashMap() {
        HashMap<String, Object> asset = new HashMap<>();

        HashMap<String, Object> publicKey = new HashMap<>();
        publicKey.put("min", this.asset.multisignature.min);
        publicKey.put("publicKeys", this.asset.multisignature.publicKeys);

        asset.put("multiSignature", publicKey);
        return asset;
    }

    @Override
    public byte[] serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(2 + this.asset.multisignature.publicKeys.size() * 33);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        buffer.put(this.asset.multisignature.min);

        buffer.put((byte) this.asset.multisignature.publicKeys.size());
        for (String publicKey: this.asset.multisignature.publicKeys) {
            buffer.put(Hex.decode(publicKey));
        }

        return buffer.array();
    }

    @Override
    public void deserialize(ByteBuffer buffer) {
        this.asset.multisignature.min = buffer.get();

        int publicKeyLength = buffer.get();
        for (int i = 0; i < publicKeyLength; i++) {
            byte[] publicKeyBuffer = new byte[33];
            buffer.get(publicKeyBuffer);
            this.asset.multisignature.publicKeys.add(Hex.encode(publicKeyBuffer));
        }
    }
}
