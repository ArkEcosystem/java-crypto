package org.arkecosystem.crypto.transactions;

import static org.junit.jupiter.api.Assertions.*;

import org.arkecosystem.crypto.AbstractTest;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.transactions.types.*;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class DeserializerTest extends AbstractTest {

    @Test
    public void it_should_deserialize_a_transfer_signed_with_a_passphrase() throws Exception {
        Map<String, Object> fixture = loadFixture("transfer");

        AbstractTransaction transaction = assertTransaction(fixture);

        assertEquals("10000000000000000000", transaction.value);

        assertTrue(transaction instanceof Transfer);
    }

    @Test
    public void it_should_deserialize_a_vote_signed_with_a_passphrase() throws Exception {
        Map<String, Object> fixture = loadFixture("vote");

        AbstractTransaction transaction = assertTransaction(fixture);

        assertEquals("0x512F366D524157BcF734546eB29a6d687B762255", transaction.vote);

        assertEquals("749744e0d689c46e37ff2993a984599eac4989a9ef0028337b335c9d43abf936", transaction.id);

        assertTrue(transaction instanceof Vote);
    }

    @Test
    public void it_should_deserialize_a_unvote_signed_with_a_passphrase() throws Exception {
        Map<String, Object> fixture = loadFixture("unvote");

        AbstractTransaction transaction = assertTransaction(fixture);

        assertTrue(transaction instanceof Unvote);
    }

    @Test
    public void it_should_deserialize_a_validator_registration_signed_with_a_passphrase() throws Exception {
        Map<String, Object> fixture = loadFixture("validator-registration");

        AbstractTransaction transaction = assertTransaction(fixture);

        assertTrue(transaction instanceof ValidatorRegistration);
    }

    @Test
    public void it_should_deserialize_a_validator_resignation_signed_with_a_passphrase() throws Exception {
        Map<String, Object> fixture = loadFixture("validator-resignation");

        AbstractTransaction transaction = assertTransaction(fixture);

        assertTrue(transaction instanceof ValidatorResignation);
    }

    private AbstractTransaction assertTransaction(Map<String, Object> fixture) throws Exception {
        AbstractTransaction transaction = assertDeserialized(fixture, new String[]{
                "id",
                "nonce",
                "gasPrice",
                "gasLimit",
                "signature",
        });

        assertTrue(transaction.verify());

        return transaction;
    }

    private AbstractTransaction assertDeserialized(Map<String, Object> fixture, String[] keys) throws Exception {
        String serializedHex = (String) fixture.get("serialized");
        byte[] serializedBytes = Hex.decode(serializedHex);

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
                    assertEquals(((Number) expectedValue).intValue(), ((Number) actualValue).intValue(), "Field " + key + " does not match");
                } else {

                    assertEquals(expectedValue.toString(), actualValue.toString(), "Field " + key + " does not match");

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
