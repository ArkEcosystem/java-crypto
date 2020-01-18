package org.arkecosystem.crypto.transactions.deserializers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.internal.LinkedTreeMap;
import org.arkecosystem.crypto.enums.TransactionTypeGroup;
import org.arkecosystem.crypto.transactions.Deserializer;
import org.arkecosystem.crypto.transactions.FixtureLoader;
import org.arkecosystem.crypto.transactions.Transaction;
import org.junit.jupiter.api.Test;

class TransferTest {

    @Test
    void passphrase() {
        // V1 tests
        LinkedTreeMap<String, Object> fixtureV1 =
                FixtureLoader.load("transactions/V1/transfer/passphrase");
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
        assertEquals(dataV1.get("id").toString(), actualV1.id);

        // V2 tests
        LinkedTreeMap<String, Object> fixtureV2 =
                FixtureLoader.load("transactions/V2/transfer/passphrase");
        LinkedTreeMap<String, Object> dataV2 =
                (LinkedTreeMap<String, Object>) fixtureV2.get("data");

        Transaction actualV2 =
                new Deserializer().deserialize(fixtureV2.get("serialized").toString());
        assertEquals(((Double) dataV2.get("type")).intValue(), actualV2.type);
        assertEquals(TransactionTypeGroup.CORE.getValue(), actualV2.typeGroup);
        assertEquals((Long.valueOf((String) dataV2.get("fee"))), actualV2.fee);
        assertEquals(dataV2.get("recipientId").toString(), actualV2.recipientId);
        assertEquals((Long.valueOf((String) dataV2.get("nonce"))), actualV2.nonce);
        assertEquals(dataV2.get("senderPublicKey").toString(), actualV2.senderPublicKey);
        assertEquals(dataV2.get("signature").toString(), actualV2.signature);
        assertEquals(dataV2.get("id").toString(), actualV2.id);

        assertEquals((Long.valueOf((String) dataV2.get("amount"))), actualV2.amount);
    }

    @Test
    void passphraseVendorField() {
        // V1 tests
        LinkedTreeMap<String, Object> fixtureV1 =
                FixtureLoader.load("transactions/V1/transfer/passphrase-with-vendor-field");
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
        assertEquals(dataV1.get("vendorField").toString(), actualV1.vendorField);
        assertEquals(dataV1.get("signature").toString(), actualV1.signature);
        assertEquals(dataV1.get("id").toString(), actualV1.id);

        // V2 tests
        LinkedTreeMap<String, Object> fixtureV2 =
                FixtureLoader.load("transactions/V2/transfer/passphrase-with-vendor-field");
        LinkedTreeMap<String, Object> dataV2 =
                (LinkedTreeMap<String, Object>) fixtureV2.get("data");

        Transaction actualV2 =
                new Deserializer().deserialize(fixtureV2.get("serialized").toString());
        assertEquals(((Double) dataV2.get("type")).intValue(), actualV2.type);
        assertEquals(TransactionTypeGroup.CORE.getValue(), actualV2.typeGroup);
        assertEquals((Long.valueOf((String) dataV2.get("fee"))), actualV2.fee);
        assertEquals(dataV2.get("recipientId").toString(), actualV2.recipientId);
        assertEquals((Long.valueOf((String) dataV2.get("nonce"))), actualV2.nonce);
        assertEquals(dataV2.get("senderPublicKey").toString(), actualV2.senderPublicKey);
        assertEquals(dataV2.get("signature").toString(), actualV2.signature);
        assertEquals(dataV2.get("id").toString(), actualV2.id);

        assertEquals((Long.valueOf((String) dataV2.get("amount"))), actualV2.amount);
        assertEquals(dataV2.get("vendorField").toString(), actualV2.vendorField);
    }

    @Test
    void passphraseVendorFieldHex() {
        // V1 tests
        LinkedTreeMap<String, Object> fixtureV1 =
                FixtureLoader.load("transactions/V1/transfer/passphrase-with-vendor-field-hex");
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
        assertEquals(dataV1.get("vendorFieldHex").toString(), actualV1.vendorFieldHex);
        assertEquals(dataV1.get("signature").toString(), actualV1.signature);
        assertEquals(dataV1.get("id").toString(), actualV1.id);

        // V2 tests
        LinkedTreeMap<String, Object> fixtureV2 =
                FixtureLoader.load("transactions/V2/transfer/passphrase-with-vendor-field-hex");
        LinkedTreeMap<String, Object> dataV2 =
                (LinkedTreeMap<String, Object>) fixtureV2.get("data");

        Transaction actualV2 =
                new Deserializer().deserialize(fixtureV2.get("serialized").toString());
        assertEquals(((Double) dataV2.get("type")).intValue(), actualV2.type);
        assertEquals(TransactionTypeGroup.CORE.getValue(), actualV2.typeGroup);
        assertEquals((Long.valueOf((String) dataV2.get("fee"))), actualV2.fee);
        assertEquals(dataV2.get("recipientId").toString(), actualV2.recipientId);
        assertEquals((Long.valueOf((String) dataV2.get("nonce"))), actualV2.nonce);
        assertEquals(dataV2.get("senderPublicKey").toString(), actualV2.senderPublicKey);
        assertEquals(dataV2.get("signature").toString(), actualV2.signature);
        assertEquals(dataV2.get("id").toString(), actualV2.id);

        assertEquals((Long.valueOf((String) dataV2.get("amount"))), actualV2.amount);
        assertEquals(dataV2.get("vendorFieldHex").toString(), actualV2.vendorFieldHex);
    }

    @Test
    void secondPassphrase() {
        // V1 tests
        LinkedTreeMap<String, Object> fixtureV1 =
                FixtureLoader.load("transactions/V1/transfer/second-passphrase");
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
        assertEquals(dataV1.get("id").toString(), actualV1.id);

        // V2 tests
        LinkedTreeMap<String, Object> fixtureV2 =
                FixtureLoader.load("transactions/V2/transfer/second-passphrase");
        LinkedTreeMap<String, Object> dataV2 =
                (LinkedTreeMap<String, Object>) fixtureV2.get("data");

        Transaction actualV2 =
                new Deserializer().deserialize(fixtureV2.get("serialized").toString());
        assertEquals(((Double) dataV2.get("type")).intValue(), actualV2.type);
        assertEquals(TransactionTypeGroup.CORE.getValue(), actualV2.typeGroup);
        assertEquals((Long.valueOf((String) dataV2.get("fee"))), actualV2.fee);
        assertEquals(dataV2.get("recipientId").toString(), actualV2.recipientId);
        assertEquals((Long.valueOf((String) dataV2.get("nonce"))), actualV2.nonce);
        assertEquals(dataV2.get("senderPublicKey").toString(), actualV2.senderPublicKey);
        assertEquals(dataV2.get("signature").toString(), actualV2.signature);
        assertEquals(dataV2.get("id").toString(), actualV2.id);

        assertEquals((Long.valueOf((String) dataV2.get("amount"))), actualV2.amount);
        assertEquals(dataV2.get("secondSignature").toString(), actualV2.signSignature);
        assertEquals(dataV2.get("secondSignature").toString(), actualV2.secondSignature);
    }

    @Test
    void secondPassphraseVendorField() {
        // V1 tests
        LinkedTreeMap<String, Object> fixtureV1 =
                FixtureLoader.load("transactions/V1/transfer/second-passphrase-with-vendor-field");
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
        assertEquals(dataV1.get("vendorField").toString(), actualV1.vendorField);
        assertEquals(dataV1.get("signature").toString(), actualV1.signature);
        assertEquals(dataV1.get("signSignature").toString(), actualV1.signSignature);
        assertEquals(dataV1.get("id").toString(), actualV1.id);

        // V2 tests
        LinkedTreeMap<String, Object> fixtureV2 =
                FixtureLoader.load("transactions/V2/transfer/second-passphrase-with-vendor-field");
        LinkedTreeMap<String, Object> dataV2 =
                (LinkedTreeMap<String, Object>) fixtureV2.get("data");

        Transaction actualV2 =
                new Deserializer().deserialize(fixtureV2.get("serialized").toString());
        assertEquals(((Double) dataV2.get("type")).intValue(), actualV2.type);
        assertEquals(TransactionTypeGroup.CORE.getValue(), actualV2.typeGroup);
        assertEquals((Long.valueOf((String) dataV2.get("fee"))), actualV2.fee);
        assertEquals(dataV2.get("recipientId").toString(), actualV2.recipientId);
        assertEquals((Long.valueOf((String) dataV2.get("nonce"))), actualV2.nonce);
        assertEquals(dataV2.get("senderPublicKey").toString(), actualV2.senderPublicKey);
        assertEquals(dataV2.get("signature").toString(), actualV2.signature);
        assertEquals(dataV2.get("id").toString(), actualV2.id);

        assertEquals((Long.valueOf((String) dataV2.get("amount"))), actualV2.amount);
        assertEquals(dataV2.get("secondSignature").toString(), actualV2.signSignature);
        assertEquals(dataV2.get("secondSignature").toString(), actualV2.secondSignature);
        assertEquals(dataV2.get("vendorField").toString(), actualV2.vendorField);
    }

    @Test
    void secondPassphraseVendorFieldHex() {
        // V1 tests
        LinkedTreeMap<String, Object> fixtureV1 =
                FixtureLoader.load(
                        "transactions/V1/transfer/second-passphrase-with-vendor-field-hex");
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
        assertEquals(dataV1.get("vendorFieldHex").toString(), actualV1.vendorFieldHex);
        assertEquals(dataV1.get("signature").toString(), actualV1.signature);
        assertEquals(dataV1.get("signSignature").toString(), actualV1.signSignature);
        assertEquals(dataV1.get("id").toString(), actualV1.id);

        // V2 tests
        LinkedTreeMap<String, Object> fixtureV2 =
                FixtureLoader.load(
                        "transactions/V2/transfer/second-passphrase-with-vendor-field-hex");
        LinkedTreeMap<String, Object> dataV2 =
                (LinkedTreeMap<String, Object>) fixtureV2.get("data");

        Transaction actualV2 =
                new Deserializer().deserialize(fixtureV2.get("serialized").toString());
        assertEquals(((Double) dataV2.get("type")).intValue(), actualV2.type);
        assertEquals(TransactionTypeGroup.CORE.getValue(), actualV2.typeGroup);
        assertEquals((Long.valueOf((String) dataV2.get("fee"))), actualV2.fee);
        assertEquals(dataV2.get("recipientId").toString(), actualV2.recipientId);
        assertEquals((Long.valueOf((String) dataV2.get("nonce"))), actualV2.nonce);
        assertEquals(dataV2.get("senderPublicKey").toString(), actualV2.senderPublicKey);
        assertEquals(dataV2.get("signature").toString(), actualV2.signature);
        assertEquals(dataV2.get("id").toString(), actualV2.id);

        assertEquals((Long.valueOf((String) dataV2.get("amount"))), actualV2.amount);
        assertEquals(dataV2.get("secondSignature").toString(), actualV2.signSignature);
        assertEquals(dataV2.get("secondSignature").toString(), actualV2.secondSignature);
        assertEquals(dataV2.get("vendorFieldHex").toString(), actualV2.vendorFieldHex);
    }
}
