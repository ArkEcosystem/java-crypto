package org.arkecosystem.crypto.transactions.serializers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.internal.LinkedTreeMap;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.transactions.Deserializer;
import org.arkecosystem.crypto.transactions.FixtureLoader;
import org.arkecosystem.crypto.transactions.Serializer;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.junit.jupiter.api.Test;

class VoteTest {
    @Test
    void passphraseVote() {
        LinkedTreeMap<String, Object> fixture =
                FixtureLoader.load("transactions/v2-ecdsa/vote-sign");
        Transaction transaction =
                new Deserializer(fixture.get("serialized").toString()).deserialize();

        String actual = Hex.encode(Serializer.serialize(transaction));

        assertEquals(fixture.get("serialized").toString(), actual);
    }

    @Test
    void passphraseUnvote() {
        LinkedTreeMap<String, Object> fixture =
            FixtureLoader.load("transactions/v2-ecdsa/unvote-sign");
        Transaction transaction =
            new Deserializer(fixture.get("serialized").toString()).deserialize();

        String actual = Hex.encode(Serializer.serialize(transaction));

        assertEquals(fixture.get("serialized").toString(), actual);
    }

    @Test
    void secondPassphraseVote(){
        LinkedTreeMap<String, Object> fixture =
            FixtureLoader.load("transactions/v2-ecdsa/vote-secondSign");
        Transaction transaction =
            new Deserializer(fixture.get("serialized").toString()).deserialize();

        String actual = Hex.encode(Serializer.serialize(transaction));

        assertEquals(fixture.get("serialized").toString(), actual);
    }

    @Test
    void secondPassphraseUnvote(){
        LinkedTreeMap<String, Object> fixture =
            FixtureLoader.load("transactions/v2-ecdsa/unvote-secondSign");
        Transaction transaction =
            new Deserializer(fixture.get("serialized").toString()).deserialize();

        String actual = Hex.encode(Serializer.serialize(transaction));

        assertEquals(fixture.get("serialized").toString(), actual);
    }

}
