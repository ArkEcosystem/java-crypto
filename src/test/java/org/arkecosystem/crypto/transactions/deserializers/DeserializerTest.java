package org.arkecosystem.crypto.transactions.deserializers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.internal.LinkedTreeMap;
import java.nio.ByteBuffer;
import java.util.HashMap;
import org.arkecosystem.crypto.transactions.Deserializer;
import org.arkecosystem.crypto.transactions.FixtureLoader;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.junit.jupiter.api.Test;

class DeserializerTest {
    @Test
    void checkNewTransactionTypeShouldBeTrue() {
        LinkedTreeMap<String, Object> fixture =
                FixtureLoader.load("transactions/v2-ecdsa/transfer-sign");

        Deserializer deserializer = new Deserializer(fixture.get("serialized").toString());
        deserializer.setNewTransactionType(
                new Transaction() {
                    @Override
                    public byte[] serialize() {
                        return new byte[0];
                    }

                    @Override
                    public void deserialize(ByteBuffer buffer) {}

                    @Override
                    public int getTransactionType() {
                        return 1;
                    }

                    @Override
                    public int getTransactionTypeGroup() {
                        return 2;
                    }

                    @Override
                    public HashMap<String, Object> assetToHashMap() {
                        return null;
                    }
                });

        assertTrue(deserializer.hasTransactionType(2, 1));
    }

    @Test
    void checkNewTransactionTypeShouldBeFalse() {
        LinkedTreeMap<String, Object> fixture =
                FixtureLoader.load("transactions/v2-ecdsa/transfer-sign");

        Deserializer deserializer = new Deserializer(fixture.get("serialized").toString());

        assertFalse(deserializer.hasTransactionType(2, 1));
    }
}
