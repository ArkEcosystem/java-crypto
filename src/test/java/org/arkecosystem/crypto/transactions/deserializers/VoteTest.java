package org.arkecosystem.crypto.transactions.deserializers;

import com.google.gson.internal.LinkedTreeMap;
import org.arkecosystem.crypto.transactions.Deserializer;
import org.arkecosystem.crypto.transactions.FixtureLoader;
import org.arkecosystem.crypto.transactions.Transaction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VoteTest {

    @Test
    void passphrase() {
        LinkedTreeMap<String, Object> fixture = FixtureLoader.load("transactions/V1/vote/passphrase");
        LinkedTreeMap<String, Object> data = (LinkedTreeMap<String, Object>) fixture.get("data");

        Transaction actual = new Deserializer().deserialize(fixture.get("serialized").toString());
        assertEquals(((Double) data.get("type")).intValue(), actual.type.getValue());
        assertEquals(((Double) data.get("amount")).longValue(), actual.amount);
        assertEquals(((Double) data.get("fee")).longValue(), actual.fee);
        assertEquals(data.get("recipientId").toString(), actual.recipientId);
        assertEquals(((Double) data.get("timestamp")).intValue(), actual.timestamp);
        assertEquals(data.get("senderPublicKey").toString(), actual.senderPublicKey);
        assertEquals(data.get("signature").toString(), actual.signature);

        LinkedTreeMap<String, Object> asset = (LinkedTreeMap<String, Object>) data.get("asset");
        assertEquals((asset.get("votes")), actual.asset.votes);

        assertEquals(data.get("id").toString(), actual.id);
    }

    @Test
    void secondPassphrase() {
        LinkedTreeMap<String, Object> fixture = FixtureLoader.load("transactions/V1/vote/second-passphrase");
        LinkedTreeMap<String, Object> data = (LinkedTreeMap<String, Object>) fixture.get("data");

        Transaction actual = new Deserializer().deserialize(fixture.get("serialized").toString());
        assertEquals(((Double) data.get("type")).intValue(), actual.type.getValue());
        assertEquals(((Double) data.get("amount")).longValue(), actual.amount);
        assertEquals(((Double) data.get("fee")).longValue(), actual.fee);
        assertEquals(data.get("recipientId").toString(), actual.recipientId);
        assertEquals(((Double) data.get("timestamp")).intValue(), actual.timestamp);
        assertEquals(data.get("senderPublicKey").toString(), actual.senderPublicKey);
        assertEquals(data.get("signature").toString(), actual.signature);
        assertEquals(data.get("signSignature").toString(), actual.signSignature);

        LinkedTreeMap<String, Object> asset = (LinkedTreeMap<String, Object>) data.get("asset");
        assertEquals((asset.get("votes")), actual.asset.votes);

        assertEquals(data.get("id").toString(), actual.id);
    }

}
