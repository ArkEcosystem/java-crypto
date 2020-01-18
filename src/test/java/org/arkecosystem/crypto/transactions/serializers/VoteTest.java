package org.arkecosystem.crypto.transactions.serializers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.internal.LinkedTreeMap;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.transactions.Deserializer;
import org.arkecosystem.crypto.transactions.FixtureLoader;
import org.arkecosystem.crypto.transactions.Serializer;
import org.arkecosystem.crypto.transactions.Transaction;
import org.junit.jupiter.api.Test;

class VoteTest {

    @Test
    void passphrase() {
        // V1 tests
        LinkedTreeMap<String, Object> fixtureV1 =
                FixtureLoader.load("transactions/V1/vote/passphrase");

        Transaction transactionV1 =
                new Deserializer().deserialize(fixtureV1.get("serialized").toString());
        String actualV1 = Hex.encode(new Serializer().serialize(transactionV1));

        assertEquals(fixtureV1.get("serialized").toString(), actualV1);

        // V2 tests
        LinkedTreeMap<String, Object> fixtureV2 =
                FixtureLoader.load("transactions/V2/vote/passphrase");

        Transaction transactionV2 =
                new Deserializer().deserialize(fixtureV2.get("serialized").toString());
        String actualV2 = Hex.encode(new Serializer().serialize(transactionV2));

        assertEquals(fixtureV2.get("serialized").toString(), actualV2);
    }

    @Test
    void secondPassphrase() {
        // V1 tests
        LinkedTreeMap<String, Object> fixtureV1 =
                FixtureLoader.load("transactions/V1/vote/second-passphrase");

        Transaction transactionV1 =
                new Deserializer().deserialize(fixtureV1.get("serialized").toString());
        String actualV1 = Hex.encode(new Serializer().serialize(transactionV1));

        assertEquals(fixtureV1.get("serialized").toString(), actualV1);

        // V2 tests
        LinkedTreeMap<String, Object> fixtureV2 =
                FixtureLoader.load("transactions/V2/vote/second-passphrase");

        Transaction transactionV2 =
                new Deserializer().deserialize(fixtureV2.get("serialized").toString());
        String actualV2 = Hex.encode(new Serializer().serialize(transactionV2));

        assertEquals(fixtureV2.get("serialized").toString(), actualV2);
    }
}
