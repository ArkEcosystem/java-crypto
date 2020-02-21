package org.arkecosystem.crypto.transactions.serializers;

import com.google.gson.internal.LinkedTreeMap;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.transactions.Deserializer;
import org.arkecosystem.crypto.transactions.FixtureLoader;
import org.arkecosystem.crypto.transactions.Serializer;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MultiPaymentTest {
    @Test
    void passphrase() {
        LinkedTreeMap<String, Object> fixture =
            FixtureLoader.load("transactions/v2-ecdsa/multi-payment-sign");
        Transaction transaction =
            new Deserializer(fixture.get("serialized").toString()).deserialize();

        String actual = Hex.encode(Serializer.serialize(transaction));

        assertEquals(fixture.get("serialized").toString(), actual);
    }

    @Test
    void passphraseVendorField() {
        LinkedTreeMap<String, Object> fixture =
            FixtureLoader.load("transactions/v2-ecdsa/multi-payment-with-vendor-field-sign");
        Transaction transaction =
            new Deserializer(fixture.get("serialized").toString()).deserialize();

        String actual = Hex.encode(Serializer.serialize(transaction));

        assertEquals(fixture.get("serialized").toString(), actual);
    }

    @Test
    void secondPassphrase() {
        LinkedTreeMap<String, Object> fixture =
            FixtureLoader.load("transactions/v2-ecdsa/multi-payment-secondSign");
        Transaction transaction =
            new Deserializer(fixture.get("serialized").toString()).deserialize();

        String actual = Hex.encode(Serializer.serialize(transaction));

        assertEquals(fixture.get("serialized").toString(), actual);
    }

    @Test
    void secondPassphraseVendorField() {
        LinkedTreeMap<String, Object> fixture =
            FixtureLoader.load("transactions/v2-ecdsa/multi-payment-with-vendor-field-secondSign");
        Transaction transaction =
            new Deserializer(fixture.get("serialized").toString()).deserialize();

        String actual = Hex.encode(Serializer.serialize(transaction));

        assertEquals(fixture.get("serialized").toString(), actual);
    }
}
