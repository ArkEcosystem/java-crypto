package org.arkecosystem.crypto.transactions.deserializers;

import com.google.gson.internal.LinkedTreeMap;
import org.arkecosystem.crypto.transactions.Deserializer;
import org.arkecosystem.crypto.transactions.FixtureLoader;
import org.arkecosystem.crypto.transactions.Transaction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SecondSignatureRegistrationTest {

    @Test
    void passphrase() {
        LinkedTreeMap<String, Object> fixture = FixtureLoader.load("transactions/second_signature_registration/second-passphrase");
        LinkedTreeMap<String, Object> data = (LinkedTreeMap<String, Object>) fixture.get("data");

        Transaction actual = new Deserializer().deserialize(fixture.get("serialized").toString());
        assertEquals(((Double) data.get("type")).intValue(), actual.type.getValue());
        assertEquals(((Double) data.get("amount")).longValue(), actual.amount);
        assertEquals(((Double) data.get("fee")).longValue(), actual.fee);
        assertEquals(((Double) data.get("timestamp")).intValue(), actual.timestamp);
        assertEquals(data.get("senderPublicKey").toString(), actual.senderPublicKey);
        assertEquals(data.get("signature").toString(), actual.signature);

        LinkedTreeMap<String, Object> asset = (LinkedTreeMap<String, Object>) ((LinkedTreeMap<String, Object>) data.get("asset")).get("signature");
        assertEquals((asset.get("publicKey")), actual.asset.signature.publicKey);

        assertEquals(data.get("id").toString(), actual.id);
    }

}
