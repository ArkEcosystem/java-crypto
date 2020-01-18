package org.arkecosystem.crypto.transactions.deserializers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.internal.LinkedTreeMap;
import org.arkecosystem.crypto.enums.TransactionTypeGroup;
import org.arkecosystem.crypto.transactions.Deserializer;
import org.arkecosystem.crypto.transactions.FixtureLoader;
import org.arkecosystem.crypto.transactions.Transaction;
import org.junit.jupiter.api.Test;

class SecondSignatureRegistrationTest {

    @Test
    void passphrase() {
        // V1 tests
        LinkedTreeMap<String, Object> fixtureV1 =
                FixtureLoader.load(
                        "transactions/V1/second_signature_registration/second-passphrase");
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

        LinkedTreeMap<String, Object> assetV1 =
                (LinkedTreeMap<String, Object>)
                        ((LinkedTreeMap<String, Object>) dataV1.get("asset")).get("signature");
        assertEquals((assetV1.get("publicKey")), actualV1.asset.signature.publicKey);

        assertEquals(dataV1.get("id").toString(), actualV1.id);

        // V2 tests
        LinkedTreeMap<String, Object> fixtureV2 =
                FixtureLoader.load("transactions/V2/second_signature_registration/passphrase");
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
                        ((LinkedTreeMap<String, Object>) dataV2.get("asset")).get("signature");
        assertEquals((assetV2.get("publicKey")), actualV2.asset.signature.publicKey);

        assertEquals(dataV2.get("id").toString(), actualV2.id);
    }
}
