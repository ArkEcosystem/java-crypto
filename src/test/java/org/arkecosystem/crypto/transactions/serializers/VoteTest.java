package org.arkecosystem.crypto.transactions.serializers;

import com.google.gson.internal.LinkedTreeMap;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.transactions.Deserializer;
import org.arkecosystem.crypto.transactions.FixtureLoader;
import org.arkecosystem.crypto.transactions.Serializer;
import org.arkecosystem.crypto.transactions.Transaction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VoteTest {

    @Test
    void passphrase() {
        LinkedTreeMap<String, Object> fixture = FixtureLoader.load("transactions/vote/passphrase");

        Transaction transaction = new Deserializer().deserialize(fixture.get("serialized").toString());
        String actual = Hex.encode(new Serializer().serialize(transaction));

        assertEquals(fixture.get("serialized").toString(), actual);
    }

    @Test
    void secondPassphrase() {
        LinkedTreeMap<String, Object> fixture = FixtureLoader.load("transactions/vote/second-passphrase");

        Transaction transaction = new Deserializer().deserialize(fixture.get("serialized").toString());
        String actual = Hex.encode(new Serializer().serialize(transaction));

        assertEquals(fixture.get("serialized").toString(), actual);
    }

}
