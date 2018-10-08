package org.arkecosystem.crypto.transactions.serializers;

import com.google.gson.internal.LinkedTreeMap;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.transactions.Deserializer;
import org.arkecosystem.crypto.transactions.FixtureLoader;
import org.arkecosystem.crypto.transactions.Serializer;
import org.arkecosystem.crypto.transactions.Transaction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferTest {

    @Test
    void passphrase() {
        LinkedTreeMap<String, Object> fixture = FixtureLoader.load("transactions/transfer/passphrase");

        Transaction transaction = new Deserializer().deserialize(fixture.get("serialized").toString());
        String actual = Hex.encode(new Serializer().serialize(transaction));

        assertEquals(fixture.get("serialized").toString(), actual);
    }

    @Test
    void passphraseVendorField() {
        LinkedTreeMap<String, Object> fixture = FixtureLoader.load("transactions/transfer/passphrase-with-vendor-field");

        Transaction transaction = new Deserializer().deserialize(fixture.get("serialized").toString());
        String actual = Hex.encode(new Serializer().serialize(transaction));

        assertEquals(fixture.get("serialized").toString(), actual);
    }

    @Test
    void passphraseVendorFieldHex() {
        LinkedTreeMap<String, Object> fixture = FixtureLoader.load("transactions/transfer/passphrase-with-vendor-field-hex");

        Transaction transaction = new Deserializer().deserialize(fixture.get("serialized").toString());
        String actual = Hex.encode(new Serializer().serialize(transaction));

        assertEquals(fixture.get("serialized").toString(), actual);
    }

    @Test
    void secondPassphrase() {
        LinkedTreeMap<String, Object> fixture = FixtureLoader.load("transactions/transfer/second-passphrase");

        Transaction transaction = new Deserializer().deserialize(fixture.get("serialized").toString());
        String actual = Hex.encode(new Serializer().serialize(transaction));

        assertEquals(fixture.get("serialized").toString(), actual);
    }

    @Test
    void secondPassphraseVendorField() {
        LinkedTreeMap<String, Object> fixture = FixtureLoader.load("transactions/transfer/second-passphrase-with-vendor-field");

        Transaction transaction = new Deserializer().deserialize(fixture.get("serialized").toString());
        String actual = Hex.encode(new Serializer().serialize(transaction));

        assertEquals(fixture.get("serialized").toString(), actual);
    }

    @Test
    void secondPassphraseVendorFieldHex() {
        LinkedTreeMap<String, Object> fixture = FixtureLoader.load("transactions/transfer/second-passphrase-with-vendor-field-hex");

        Transaction transaction = new Deserializer().deserialize(fixture.get("serialized").toString());
        String actual = Hex.encode(new Serializer().serialize(transaction));

        assertEquals(fixture.get("serialized").toString(), actual);
    }

}
