package org.arkecosystem.crypto.transactions.serializers;

import com.google.gson.internal.LinkedTreeMap;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.transactions.Deserializer;
import org.arkecosystem.crypto.transactions.FixtureLoader;
import org.arkecosystem.crypto.transactions.Serializer;
import org.arkecosystem.crypto.transactions.Transaction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DelegateRegistrationTest {

    @Test
    void passphrase() {
        LinkedTreeMap<String, Object> fixture = FixtureLoader.load("transactions/V1/delegate_registration/passphrase");

        Transaction transaction = new Deserializer().deserialize(fixture.get("serialized").toString());
        String actual = Hex.encode(new Serializer().serialize(transaction));

        assertEquals(fixture.get("serialized").toString(), actual);
    }

    @Test
    void secondPassphrase() {
        LinkedTreeMap<String, Object> fixture = FixtureLoader.load("transactions/V1/delegate_registration/second-passphrase");

        Transaction transaction = new Deserializer().deserialize(fixture.get("serialized").toString());
        String actual = Hex.encode(new Serializer().serialize(transaction));

        assertEquals(fixture.get("serialized").toString(), actual);
    }

}
