package org.arkecosystem.crypto.transactions;

import static org.junit.jupiter.api.Assertions.*;

import org.arkecosystem.crypto.AbstractTest;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.transactions.builder.TransferBuilder;
import org.arkecosystem.crypto.transactions.types.*;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class SerializerTest extends AbstractTest {

    @Test
    public void it_should_serialize_a_transfer_transaction() throws Exception {
        Map<String, Object> fixture = loadFixture("transfer");

        Map<String, Object> data = (Map<String, Object>) fixture.get("data");

        Transfer transaction = new Transfer(data);
                
        byte[] serializedBytes = transaction.serialize(false);
        String serializedHex = Hex.encode(serializedBytes);

        assertEquals(fixture.get("serialized"), serializedHex);
    }

    @Test
    public void it_should_serialize_a_vote_transaction() throws Exception {
        Map<String, Object> fixture = loadFixture("vote");

        Map<String, Object> data = (Map<String, Object>) fixture.get("data");

        Vote transaction = new Vote(data);

        byte[] serializedBytes = transaction.serialize(false);
        String serializedHex = Hex.encode(serializedBytes);

        assertEquals(fixture.get("serialized"), serializedHex);
    }

    @Test
    public void it_should_serialize_a_unvote_transaction() throws Exception {
        Map<String, Object> fixture = loadFixture("unvote");

        Map<String, Object> data = (Map<String, Object>) fixture.get("data");

        Unvote transaction = new Unvote(data);

        byte[] serializedBytes = transaction.serialize(false);
        String serializedHex = Hex.encode(serializedBytes);

        assertEquals(fixture.get("serialized"), serializedHex);
    }

    @Test
    public void it_should_serialize_a_validator_registration_transaction() throws Exception {
        Map<String, Object> fixture = loadFixture("validator-registration");

        Map<String, Object> data = (Map<String, Object>) fixture.get("data");

        ValidatorRegistration transaction = new ValidatorRegistration(data);

        byte[] serializedBytes = transaction.serialize(false);
        String serializedHex = Hex.encode(serializedBytes);

        assertEquals(fixture.get("serialized"), serializedHex);
    }

    @Test
    public void it_should_serialize_a_validator_resignation_transaction() throws Exception {
        Map<String, Object> fixture = loadFixture("validator-resignation");

        Map<String, Object> data = (Map<String, Object>) fixture.get("data");

        ValidatorResignation transaction = new ValidatorResignation(data);

        byte[] serializedBytes = transaction.serialize(false);
        String serializedHex = Hex.encode(serializedBytes);

        assertEquals(fixture.get("serialized"), serializedHex);
    }
}
