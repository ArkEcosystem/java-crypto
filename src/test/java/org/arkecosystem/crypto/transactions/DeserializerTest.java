package org.arkecosystem.crypto.transactions;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.arkecosystem.crypto.AbstractTest;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.transactions.types.*;
import org.junit.jupiter.api.Test;

public class DeserializerTest extends AbstractTest {

    @Test
    public void it_should_deserialize_a_transfer_signed_with_a_passphrase() throws Exception {
        Map<String, Object> fixture = loadFixture("transfer");

        AbstractTransaction transaction = assertTransaction(fixture);

        assertEquals("100000000", transaction.value);

        assertTrue(transaction instanceof Transfer);
    }

    @Test
    public void it_should_deserialize_a_vote_signed_with_a_passphrase() throws Exception {
        Map<String, Object> fixture = loadFixture("vote");

        AbstractTransaction transaction = assertTransaction(fixture);

        assertTrue(transaction instanceof Vote);
    }

    @Test
    public void it_should_deserialize_a_unvote_signed_with_a_passphrase() throws Exception {
        Map<String, Object> fixture = loadFixture("unvote");

        AbstractTransaction transaction = assertTransaction(fixture);

        assertTrue(transaction instanceof Unvote);
    }

    @Test
    public void it_should_deserialize_a_validator_registration_signed_with_a_passphrase()
            throws Exception {
        Map<String, Object> fixture = loadFixture("validator-registration");

        AbstractTransaction transaction = assertTransaction(fixture);

        assertTrue(transaction instanceof ValidatorRegistration);
    }

    @Test
    public void it_should_deserialize_a_validator_resignation_signed_with_a_passphrase()
            throws Exception {
        Map<String, Object> fixture = loadFixture("validator-resignation");

        AbstractTransaction transaction = assertTransaction(fixture);

        assertTrue(transaction instanceof ValidatorResignation);
    }

    private AbstractTransaction assertTransaction(Map<String, Object> fixture) throws Exception {
        AbstractTransaction transaction =
                assertDeserialized(
                        fixture,
                        new String[] {
                            "id", "nonce", "gasPrice", "gasLimit",
                        });

        assertTrue(transaction.verify());

        return transaction;
    }

    private AbstractTransaction assertDeserialized(Map<String, Object> fixture, String[] keys)
            throws Exception {
        String serializedHex = (String) fixture.get("serialized");

        Deserializer deserializer = new Deserializer(serializedHex);
        AbstractTransaction transaction = deserializer.deserialize();

        byte[] reserializedBytes = transaction.serialize(false);
        String reserializedHex = Hex.encode(reserializedBytes);

        assertEquals(serializedHex, reserializedHex, "Serialized transaction does not match");

        Map<String, Object> expectedData = (Map<String, Object>) fixture.get("data");

        for (String key : keys) {
            Object expectedValue = expectedData.get(key);
            Object actualValue = getFieldValue(transaction, key);

            if (expectedValue != null) {
                if (expectedValue instanceof Number) {
                    assertEquals(
                            ((Number) expectedValue).longValue(),
                            ((Number) actualValue).longValue(),
                            "Field " + key + " does not match");
                } else {
                    assertEquals(
                            expectedValue.toString(),
                            actualValue.toString(),
                            "Field " + key + " does not match");
                }
            } else {
                assertNull(actualValue, "Field " + key + " should be null");
            }
        }

        return transaction;
    }

    private Object getFieldValue(AbstractTransaction transaction, String field) {
        switch (field) {
            case "id":
                return transaction.id;
            case "nonce":
                return transaction.nonce;
            case "gasPrice":
                return transaction.gasPrice;
            case "gasLimit":
                return transaction.gasLimit;
            case "signature":
                return transaction.signature;
            default:
                return null;
        }
    }
}
