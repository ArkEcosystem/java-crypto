package org.arkecosystem.crypto.transactions.deserializers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.internal.LinkedTreeMap;
import org.arkecosystem.crypto.enums.TransactionTypeGroup;
import org.arkecosystem.crypto.transactions.Deserializer;
import org.arkecosystem.crypto.transactions.FixtureLoader;
import org.arkecosystem.crypto.transactions.Transaction;
import org.junit.jupiter.api.Test;

class VoteTest {

    @Test
    void passphrase() {
        // V1 tests
        LinkedTreeMap<String, Object> fixtureV1 =
                FixtureLoader.load("transactions/V1/vote/passphrase");
        LinkedTreeMap<String, Object> dataV1 =
                (LinkedTreeMap<String, Object>) fixtureV1.get("data");

        Transaction actualV1 =
                new Deserializer().deserialize(fixtureV1.get("serialized").toString());
        assertEquals(((Double) dataV1.get("type")).intValue(), actualV1.type);
        assertEquals(((Double) dataV1.get("amount")).longValue(), actualV1.amount);
        assertEquals(((Double) dataV1.get("fee")).longValue(), actualV1.fee);
        assertEquals(dataV1.get("recipientId").toString(), actualV1.recipientId);
        assertEquals(((Double) dataV1.get("timestamp")).intValue(), actualV1.timestamp);
        assertEquals(dataV1.get("senderPublicKey").toString(), actualV1.senderPublicKey);
        assertEquals(dataV1.get("signature").toString(), actualV1.signature);

        LinkedTreeMap<String, Object> assetV1 = (LinkedTreeMap<String, Object>) dataV1.get("asset");
        assertEquals((assetV1.get("votes")), actualV1.asset.votes);

        assertEquals(dataV1.get("id").toString(), actualV1.id);

        // V2 tests
        LinkedTreeMap<String, Object> fixtureV2 =
                FixtureLoader.load("transactions/V2/vote/passphrase");
        LinkedTreeMap<String, Object> dataV2 =
                (LinkedTreeMap<String, Object>) fixtureV2.get("data");

        Transaction actualV2 =
                new Deserializer().deserialize(fixtureV2.get("serialized").toString());
        assertEquals(((Double) dataV2.get("type")).intValue(), actualV2.type);
        assertEquals(TransactionTypeGroup.CORE.getValue(), actualV2.typeGroup);
        assertEquals((Long.valueOf((String) dataV2.get("fee"))), actualV2.fee);
        assertEquals((Long.valueOf((String) dataV2.get("nonce"))), actualV2.nonce);
        assertEquals(dataV2.get("senderPublicKey").toString(), actualV2.senderPublicKey);
        assertEquals(dataV2.get("signature").toString(), actualV2.signature);
        assertEquals(dataV2.get("id").toString(), actualV2.id);

        LinkedTreeMap<String, Object> assetV2 = (LinkedTreeMap<String, Object>) dataV2.get("asset");
        assertEquals((assetV2.get("votes")), actualV2.asset.votes);

        assertEquals(dataV2.get("id").toString(), actualV2.id);
    }

    @Test
    void secondPassphrase() {
        // V1 tests
        LinkedTreeMap<String, Object> fixtureV1 =
                FixtureLoader.load("transactions/V1/vote/second-passphrase");
        LinkedTreeMap<String, Object> dataV1 =
                (LinkedTreeMap<String, Object>) fixtureV1.get("data");

        Transaction actualV1 =
                new Deserializer().deserialize(fixtureV1.get("serialized").toString());
        assertEquals(((Double) dataV1.get("type")).intValue(), actualV1.type);
        assertEquals(((Double) dataV1.get("amount")).longValue(), actualV1.amount);
        assertEquals(((Double) dataV1.get("fee")).longValue(), actualV1.fee);
        assertEquals(dataV1.get("recipientId").toString(), actualV1.recipientId);
        assertEquals(((Double) dataV1.get("timestamp")).intValue(), actualV1.timestamp);
        assertEquals(dataV1.get("senderPublicKey").toString(), actualV1.senderPublicKey);
        assertEquals(dataV1.get("signature").toString(), actualV1.signature);
        assertEquals(dataV1.get("signSignature").toString(), actualV1.signSignature);

        LinkedTreeMap<String, Object> assetV1 = (LinkedTreeMap<String, Object>) dataV1.get("asset");
        assertEquals((assetV1.get("votes")), actualV1.asset.votes);

        assertEquals(dataV1.get("id").toString(), actualV1.id);

        // V2 tests
        LinkedTreeMap<String, Object> fixtureV2 =
                FixtureLoader.load("transactions/V2/vote/second-passphrase");
        LinkedTreeMap<String, Object> dataV2 =
                (LinkedTreeMap<String, Object>) fixtureV2.get("data");

        Transaction actualV2 =
                new Deserializer().deserialize(fixtureV2.get("serialized").toString());
        assertEquals(((Double) dataV2.get("type")).intValue(), actualV2.type);
        assertEquals(TransactionTypeGroup.CORE.getValue(), actualV2.typeGroup);
        assertEquals((Long.valueOf((String) dataV2.get("fee"))), actualV2.fee);
        assertEquals((Long.valueOf((String) dataV2.get("nonce"))), actualV2.nonce);
        assertEquals(dataV2.get("senderPublicKey").toString(), actualV2.senderPublicKey);
        assertEquals(dataV2.get("signature").toString(), actualV2.signature);
        assertEquals(dataV2.get("id").toString(), actualV2.id);
        assertEquals((Long.valueOf((String) dataV2.get("amount"))), actualV2.amount);
        assertEquals(dataV2.get("secondSignature").toString(), actualV2.signSignature);
        assertEquals(dataV2.get("secondSignature").toString(), actualV2.secondSignature);

        LinkedTreeMap<String, Object> assetV2 = (LinkedTreeMap<String, Object>) dataV2.get("asset");
        assertEquals((assetV2.get("votes")), actualV2.asset.votes);

        assertEquals(dataV2.get("id").toString(), actualV2.id);
    }
}
