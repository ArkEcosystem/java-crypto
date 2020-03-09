package org.arkecosystem.crypto.transactions.deserializers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.internal.LinkedTreeMap;
import org.arkecosystem.crypto.enums.TransactionTypeGroup;
import org.arkecosystem.crypto.transactions.Deserializer;
import org.arkecosystem.crypto.transactions.FixtureLoader;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.junit.jupiter.api.Test;

class TransferTest {

    @Test
    void passphrase() {
        LinkedTreeMap<String, Object> fixture =
                FixtureLoader.load("transactions/v2-ecdsa/transfer-sign");

        LinkedTreeMap<String, Object> data = (LinkedTreeMap<String, Object>) fixture.get("data");

        Transaction actual = new Deserializer(fixture.get("serialized").toString()).deserialize();

        assertEquals(((Double) data.get("version")).intValue(), actual.version);
        assertEquals(((Double) data.get("network")).intValue(), actual.network);
        assertEquals(TransactionTypeGroup.CORE.getValue(), actual.typeGroup);
        assertEquals(((Double) data.get("type")).intValue(), actual.type);
        assertEquals((Long.valueOf((String) data.get("nonce"))), actual.nonce);
        assertEquals(data.get("senderPublicKey").toString(), actual.senderPublicKey);
        assertEquals((Long.valueOf((String) data.get("fee"))), actual.fee);
        assertEquals(data.get("signature").toString(), actual.signature);
        assertEquals(data.get("id").toString(), actual.id);

        assertEquals(data.get("recipientId").toString(), actual.recipientId);
        assertEquals((Long.valueOf((String) data.get("amount"))), actual.amount);
        assertEquals(((Double) data.get("expiration")).intValue(), actual.expiration);
    }

    @Test
    void passphraseVendorField() {
        LinkedTreeMap<String, Object> fixture =
                FixtureLoader.load("transactions/v2-ecdsa/transfer-with-vendor-field-sign");

        LinkedTreeMap<String, Object> data = (LinkedTreeMap<String, Object>) fixture.get("data");

        Transaction actual = new Deserializer(fixture.get("serialized").toString()).deserialize();

        assertEquals(((Double) data.get("version")).intValue(), actual.version);
        assertEquals(((Double) data.get("network")).intValue(), actual.network);
        assertEquals(TransactionTypeGroup.CORE.getValue(), actual.typeGroup);
        assertEquals(((Double) data.get("type")).intValue(), actual.type);
        assertEquals((Long.valueOf((String) data.get("nonce"))), actual.nonce);
        assertEquals(data.get("senderPublicKey").toString(), actual.senderPublicKey);
        assertEquals((Long.valueOf((String) data.get("fee"))), actual.fee);
        assertEquals(data.get("signature").toString(), actual.signature);
        assertEquals(data.get("id").toString(), actual.id);

        assertEquals(data.get("recipientId").toString(), actual.recipientId);
        assertEquals((Long.valueOf((String) data.get("amount"))), actual.amount);
        assertEquals(((Double) data.get("expiration")).intValue(), actual.expiration);
        assertEquals(data.get("vendorField").toString(), actual.vendorField);
    }

    @Test
    void secondPassphrase() {
        LinkedTreeMap<String, Object> fixture =
                FixtureLoader.load("transactions/v2-ecdsa/transfer-secondSign");

        LinkedTreeMap<String, Object> data = (LinkedTreeMap<String, Object>) fixture.get("data");

        Transaction actual = new Deserializer(fixture.get("serialized").toString()).deserialize();

        assertEquals(((Double) data.get("version")).intValue(), actual.version);
        assertEquals(((Double) data.get("network")).intValue(), actual.network);
        assertEquals(TransactionTypeGroup.CORE.getValue(), actual.typeGroup);
        assertEquals(((Double) data.get("type")).intValue(), actual.type);
        assertEquals((Long.valueOf((String) data.get("nonce"))), actual.nonce);
        assertEquals(data.get("senderPublicKey").toString(), actual.senderPublicKey);
        assertEquals((Long.valueOf((String) data.get("fee"))), actual.fee);
        assertEquals(data.get("signature").toString(), actual.signature);
        assertEquals(data.get("id").toString(), actual.id);

        assertEquals(data.get("recipientId").toString(), actual.recipientId);
        assertEquals((Long.valueOf((String) data.get("amount"))), actual.amount);
        assertEquals(((Double) data.get("expiration")).intValue(), actual.expiration);
        assertEquals(data.get("secondSignature").toString(), actual.secondSignature);
    }

    @Test
    void secondPassphraseVendorField() {
        LinkedTreeMap<String, Object> fixture =
                FixtureLoader.load("transactions/v2-ecdsa/transfer-with-vendor-field-secondSign");

        LinkedTreeMap<String, Object> data = (LinkedTreeMap<String, Object>) fixture.get("data");

        Transaction actual = new Deserializer(fixture.get("serialized").toString()).deserialize();

        assertEquals(((Double) data.get("version")).intValue(), actual.version);
        assertEquals(((Double) data.get("network")).intValue(), actual.network);
        assertEquals(TransactionTypeGroup.CORE.getValue(), actual.typeGroup);
        assertEquals(((Double) data.get("type")).intValue(), actual.type);
        assertEquals((Long.valueOf((String) data.get("nonce"))), actual.nonce);
        assertEquals(data.get("senderPublicKey").toString(), actual.senderPublicKey);
        assertEquals((Long.valueOf((String) data.get("fee"))), actual.fee);
        assertEquals(data.get("signature").toString(), actual.signature);
        assertEquals(data.get("id").toString(), actual.id);

        assertEquals(data.get("recipientId").toString(), actual.recipientId);
        assertEquals((Long.valueOf((String) data.get("amount"))), actual.amount);
        assertEquals(((Double) data.get("expiration")).intValue(), actual.expiration);
        assertEquals(data.get("vendorField").toString(), actual.vendorField);
        assertEquals(data.get("secondSignature").toString(), actual.secondSignature);
    }
}
