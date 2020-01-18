package org.arkecosystem.crypto.transactions.deserializers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.internal.LinkedTreeMap;
import org.arkecosystem.crypto.transactions.Deserializer;
import org.arkecosystem.crypto.transactions.FixtureLoader;
import org.arkecosystem.crypto.transactions.Transaction;
import org.junit.jupiter.api.Test;

class MultiSignatureRegistrationTest {

    @Test
    void passphrase() {
        LinkedTreeMap<String, Object> fixture =
                FixtureLoader.load("transactions/V1/multi_signature_registration/passphrase");
        LinkedTreeMap<String, Object> data = (LinkedTreeMap<String, Object>) fixture.get("data");

        Transaction actual = new Deserializer().deserialize(fixture.get("serialized").toString());
        assertEquals(((Double) data.get("type")).intValue(), actual.type);
        assertEquals(((Double) data.get("amount")).longValue(), actual.amount);
        assertEquals(((Double) data.get("fee")).longValue(), actual.fee);
        assertEquals(((Double) data.get("timestamp")).intValue(), actual.timestamp);
        assertEquals(data.get("senderPublicKey").toString(), actual.senderPublicKey);
        assertEquals(data.get("signature").toString(), actual.signature);
        assertEquals(data.get("signatures"), actual.signatures);

        LinkedTreeMap<String, Object> asset =
                (LinkedTreeMap<String, Object>)
                        ((LinkedTreeMap<String, Object>) data.get("asset")).get("multisignature");
        assertEquals(((Double) asset.get("min")).intValue(), actual.asset.multisignature.min);
        assertEquals(
                ((Double) asset.get("lifetime")).intValue(), actual.asset.multisignature.lifetime);
        assertEquals((asset.get("keysgroup")), actual.asset.multisignature.keysgroup);

        assertEquals(data.get("id").toString(), actual.id);
    }
}
