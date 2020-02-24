package org.arkecosystem.crypto.transactions.deserializers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.internal.LinkedTreeMap;
import java.util.ArrayList;
import org.arkecosystem.crypto.enums.TransactionTypeGroup;
import org.arkecosystem.crypto.transactions.Deserializer;
import org.arkecosystem.crypto.transactions.FixtureLoader;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.junit.jupiter.api.Test;

class MultiPaymentTest {
    @Test
    void passphrase() {
        LinkedTreeMap<String, Object> fixture =
                FixtureLoader.load("transactions/v2-ecdsa/multi-payment-sign");

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

        LinkedTreeMap<String, Object> asset = (LinkedTreeMap<String, Object>) data.get("asset");
        ArrayList payments = ((ArrayList) asset.get("payments"));
        for (int i = 0; i < payments.size(); i++) {
            String recipientId =
                    (String) ((LinkedTreeMap<String, Object>) payments.get(i)).get("recipientId");
            String amount =
                    ((String) ((LinkedTreeMap<String, Object>) payments.get(i)).get("amount"));
            assertEquals(recipientId, actual.asset.multiPayment.payments.get(i).recipientId);
            assertEquals(Long.valueOf(amount), actual.asset.multiPayment.payments.get(i).amount);
        }
    }

    @Test
    void passphraseVendorField() {
        LinkedTreeMap<String, Object> fixture =
                FixtureLoader.load("transactions/v2-ecdsa/multi-payment-with-vendor-field-sign");

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

        LinkedTreeMap<String, Object> asset = (LinkedTreeMap<String, Object>) data.get("asset");
        ArrayList payments = ((ArrayList) asset.get("payments"));
        for (int i = 0; i < payments.size(); i++) {
            String recipientId =
                    (String) ((LinkedTreeMap<String, Object>) payments.get(i)).get("recipientId");
            String amount =
                    ((String) ((LinkedTreeMap<String, Object>) payments.get(i)).get("amount"));
            assertEquals(recipientId, actual.asset.multiPayment.payments.get(i).recipientId);
            assertEquals(Long.valueOf(amount), actual.asset.multiPayment.payments.get(i).amount);
        }

        assertEquals(data.get("vendorField").toString(), actual.vendorField);
    }

    @Test
    void secondPassphrase() {
        LinkedTreeMap<String, Object> fixture =
                FixtureLoader.load("transactions/v2-ecdsa/multi-payment-secondSign");

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

        LinkedTreeMap<String, Object> asset = (LinkedTreeMap<String, Object>) data.get("asset");
        ArrayList payments = ((ArrayList) asset.get("payments"));
        for (int i = 0; i < payments.size(); i++) {
            String recipientId =
                    (String) ((LinkedTreeMap<String, Object>) payments.get(i)).get("recipientId");
            String amount =
                    ((String) ((LinkedTreeMap<String, Object>) payments.get(i)).get("amount"));
            assertEquals(recipientId, actual.asset.multiPayment.payments.get(i).recipientId);
            assertEquals(Long.valueOf(amount), actual.asset.multiPayment.payments.get(i).amount);
        }

        assertEquals(data.get("secondSignature").toString(), actual.secondSignature);
    }

    @Test
    void secondPassphraseVendorField() {
        LinkedTreeMap<String, Object> fixture =
                FixtureLoader.load(
                        "transactions/v2-ecdsa/multi-payment-with-vendor-field-secondSign");

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

        LinkedTreeMap<String, Object> asset = (LinkedTreeMap<String, Object>) data.get("asset");
        ArrayList payments = ((ArrayList) asset.get("payments"));
        for (int i = 0; i < payments.size(); i++) {
            String recipientId =
                    (String) ((LinkedTreeMap<String, Object>) payments.get(i)).get("recipientId");
            String amount =
                    ((String) ((LinkedTreeMap<String, Object>) payments.get(i)).get("amount"));
            assertEquals(recipientId, actual.asset.multiPayment.payments.get(i).recipientId);
            assertEquals(Long.valueOf(amount), actual.asset.multiPayment.payments.get(i).amount);
        }

        assertEquals(data.get("vendorField").toString(), actual.vendorField);
        assertEquals(data.get("secondSignature").toString(), actual.secondSignature);
    }
}
