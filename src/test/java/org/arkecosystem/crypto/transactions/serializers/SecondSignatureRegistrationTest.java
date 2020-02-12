package org.arkecosystem.crypto.transactions.serializers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.internal.LinkedTreeMap;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.transactions.Deserializer;
import org.arkecosystem.crypto.transactions.FixtureLoader;
import org.arkecosystem.crypto.transactions.Serializer;
import org.arkecosystem.crypto.transactions.Transaction;
import org.junit.jupiter.api.Test;

class SecondSignatureRegistrationTest {

    @Test
    void passphrase() {
        // V2 tests
        LinkedTreeMap<String, Object> fixtureV2 =
                FixtureLoader.load("transactions/v2-ecdsa/second-signature-registration");

        Transaction transactionV2 =
                new Deserializer().deserialize(fixtureV2.get("serialized").toString());
        String actualV2 = Hex.encode(new Serializer().serialize(transactionV2));

        assertEquals(fixtureV2.get("serialized").toString(), actualV2);
    }
}
