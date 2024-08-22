package org.arkecosystem.crypto.transactions.deserializers;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.internal.LinkedTreeMap;
import java.nio.ByteBuffer;
import java.util.HashMap;
import org.arkecosystem.crypto.transactions.Deserializer;
import org.arkecosystem.crypto.transactions.FixtureLoader;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DeserializerTest {
    @Test
    void checkNewTransactionType() {
        LinkedTreeMap<String, Object> fixture =
                FixtureLoader.load("transactions/transfer/transfer-sign");

        Deserializer deserializer = new Deserializer(fixture.get("serialized").toString());
        deserializer.setNewTransactionType(
                new Transaction() {
                    @Override
                    public byte[] serializeData() {
                        return new byte[0];
                    }

                    @Override
                    public void deserializeData(ByteBuffer buffer) {}

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
                FixtureLoader.load("transactions/transfer/transfer-sign");

        Deserializer deserializer = new Deserializer(fixture.get("serialized").toString());
        deserializer.setNewTransactionType(
                new Transaction() {
                    @Override
                    public byte[] serializeData() {
                        return new byte[0];
                    }

                    @Override
                    public void deserializeData(ByteBuffer buffer) {}

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
        String serialized =
                "ff011e0100000000000100000000000000023efc1da7f315f3c533a4080e491f32cd4219731cef008976c3876539e1f192d3809698000000000000010000000000000000000000b693449adda7efc015d87944eae8b7c37eb1690af25db2b781b79f671b7848284de63f3cb898f9938c0b6203a11cace4aaa4b962eabe9c4a67c207a3f8baea3b4f4cef90a0b67a2bfd84cd61dcc771597f52656d005afa0050c85a2ac9b34accb0f47c6a9cc9eee831ac713e62e846898d1b75d6a33034680bce95f638c6dfabf8056f8afa9ef73a3c35741234faf01d0a346e3e7d005afa0050c85a2ac9b34accb0f47c6a9cc9eee831ac713e62e846898d1b75d6a33034680bce95f638c6dfabf8056f8afa9ef73a3c35741234faf01d0a346e3e7d02d6af0f5a85a7967d677b2f1f86e00b8ca37facb714467e12810a692d5bcbdfcac4e4c2d4b79a70c7366405339bf84a854308d20cb48652816a9cf37fb3e86e00";

        Exception thrown =
                Assertions.assertThrows(
                        RuntimeException.class,
                        () -> {
                            Deserializer deserializer = new Deserializer(serialized);
                            deserializer.deserialize();
                        });
        assertEquals("Duplicate participant in multi signature", thrown.getMessage());
    }
}
