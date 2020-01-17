package org.arkecosystem.crypto.transactions.deserializers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.internal.LinkedTreeMap;
import org.arkecosystem.crypto.enums.TransactionTypeGroup;
import org.arkecosystem.crypto.transactions.Deserializer;
import org.arkecosystem.crypto.transactions.FixtureLoader;
import org.arkecosystem.crypto.transactions.Transaction;
import org.junit.jupiter.api.Test;

class DelegateRegistrationTest {

    @Test
    void passphrase() {
        // V1 tests
        LinkedTreeMap<String, Object> fixtureV1 =
                FixtureLoader.load("transactions/V1/delegate_registration/passphrase");
        LinkedTreeMap<String, Object> dataV1 =
                (LinkedTreeMap<String, Object>) fixtureV1.get("data");

        Transaction actualV1 =
                new Deserializer().deserialize(fixtureV1.get("serialized").toString());
        assertEquals(((Double) dataV1.get("type")).intValue(), actualV1.type);
        assertEquals(((Double) dataV1.get("amount")).longValue(), actualV1.amount);
        assertEquals(((Double) dataV1.get("fee")).longValue(), actualV1.fee);
        assertEquals(((Double) dataV1.get("timestamp")).intValue(), actualV1.timestamp);
        assertEquals(dataV1.get("senderPublicKey").toString(), actualV1.senderPublicKey);
        assertEquals(dataV1.get("signature").toString(), actualV1.signature);
        assertEquals(dataV1.get("id").toString(), actualV1.id);

        LinkedTreeMap<String, Object> assetV1 =
                (LinkedTreeMap<String, Object>)
                        ((LinkedTreeMap<String, Object>) dataV1.get("asset")).get("delegate");
        assertEquals((assetV1.get("username")), actualV1.asset.delegate.username);

        // V2 tests
        LinkedTreeMap<String, Object> fixtureV2 =
                FixtureLoader.load("transactions/V2/delegate_registration/passphrase");
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

        LinkedTreeMap<String, Object> assetV2 =
                (LinkedTreeMap<String, Object>)
                        ((LinkedTreeMap<String, Object>) dataV2.get("asset")).get("delegate");
        assertEquals((assetV2.get("username")), actualV2.asset.delegate.username);
    }

    @Test
    void secondPassphrase() {
        // V1 tests
        LinkedTreeMap<String, Object> fixtureV1 =
                FixtureLoader.load("transactions/V1/delegate_registration/second-passphrase");
        LinkedTreeMap<String, Object> dataV1 =
                (LinkedTreeMap<String, Object>) fixtureV1.get("data");

        Transaction actualV1 =
                new Deserializer().deserialize(fixtureV1.get("serialized").toString());
        assertEquals(((Double) dataV1.get("type")).intValue(), actualV1.type);
        assertEquals(((Double) dataV1.get("amount")).longValue(), actualV1.amount);
        assertEquals(((Double) dataV1.get("fee")).longValue(), actualV1.fee);
        assertEquals(((Double) dataV1.get("timestamp")).intValue(), actualV1.timestamp);
        assertEquals(dataV1.get("senderPublicKey").toString(), actualV1.senderPublicKey);
        assertEquals(dataV1.get("signature").toString(), actualV1.signature);
        assertEquals(dataV1.get("signSignature").toString(), actualV1.signSignature);
        assertEquals(dataV1.get("id").toString(), actualV1.id);

        LinkedTreeMap<String, Object> assetV1 =
                (LinkedTreeMap<String, Object>)
                        ((LinkedTreeMap<String, Object>) dataV1.get("asset")).get("delegate");
        assertEquals((assetV1.get("username")), actualV1.asset.delegate.username);

        // V2 tests
        LinkedTreeMap<String, Object> fixtureV2 =
                FixtureLoader.load("transactions/V2/delegate_registration/second-passphrase");
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
        assertEquals(dataV2.get("secondSignature").toString(), actualV2.signSignature);
        assertEquals(dataV2.get("secondSignature").toString(), actualV2.secondSignature);
        assertEquals(dataV2.get("id").toString(), actualV2.id);

        LinkedTreeMap<String, Object> assetV2 =
                (LinkedTreeMap<String, Object>)
                        ((LinkedTreeMap<String, Object>) dataV2.get("asset")).get("delegate");
        assertEquals((assetV2.get("username")), actualV2.asset.delegate.username);
    }
}
