package org.arkecosystem.crypto.transactions.deserializers;

import com.google.gson.internal.LinkedTreeMap;
import org.arkecosystem.crypto.transactions.Deserializer;
import org.arkecosystem.crypto.transactions.FixtureLoader;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class DeserializerTest {
    @Test
    void checkNewTransactionType() {
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
        assertFalse(deserializer.hasTransactionType(2, 2));
        assertFalse(deserializer.hasTransactionType(3, 1));

    }

    @Test
    void checkNewTransactionToCoreGroup() {
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
                    return 11;
                }

                @Override
                public int getTransactionTypeGroup() {
                    return 1;
                }

                @Override
                public HashMap<String, Object> assetToHashMap() {
                    return null;
                }
            });
        assertTrue(deserializer.hasTransactionType(1, 11));
    }

    @Test
    void duplicateParticipantInMultiSignature() {
        String serialized = "ff0217010000000000010000000000000003fb92a2c3efaa177d8a51fc0cdf41905098d8b2cd900cbac94617492827e5f01580969800000000000000c2eb0b0000000000000000170995750207ecaf0ccf251c1265b92ad84f55366200c01c44bf33bea20a74d5acc12c5d6aafe82240f3571121382b77c871f4b33d6da2b62fdc6ca2cc6bc583abb2a69e7975be29e8d80a59c52bcff8d54514cf999e00c01c44bf33bea20a74d5acc12c5d6aafe82240f3571121382b77c871f4b33d6da2b62fdc6ca2cc6bc583abb2a69e7975be29e8d80a59c52bcff8d54514cf999e0292c299b739f0cb5e36133d85f51fff2fcc7745e8af4d0778908560c0874e3f2303e0436eabce1c09d88efab0004e61c5d6e47768aa8d2ab0cb9f14d523a38308";

        Exception thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            Deserializer deserializer = new Deserializer(serialized);
            deserializer.deserialize();
        });
        assertEquals("Duplicate participant in multi signature", thrown.getMessage());
    }
}
