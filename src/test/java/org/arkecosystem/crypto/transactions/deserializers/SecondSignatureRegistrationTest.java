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
        LinkedTreeMap<String, Object> fixtureV2 =
                FixtureLoader.load("transactions/v2-ecdsa/second-signature-registration");
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
