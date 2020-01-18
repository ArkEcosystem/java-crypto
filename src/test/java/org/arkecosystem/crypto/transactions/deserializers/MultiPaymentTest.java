package org.arkecosystem.crypto.transactions.deserializers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.internal.LinkedTreeMap;
import java.util.ArrayList;
import org.arkecosystem.crypto.enums.TransactionTypeGroup;
import org.arkecosystem.crypto.transactions.Deserializer;
import org.arkecosystem.crypto.transactions.FixtureLoader;
import org.arkecosystem.crypto.transactions.Transaction;
import org.junit.jupiter.api.Test;

public class MultiPaymentTest {
    @Test
    void passphrase() {
        LinkedTreeMap<String, Object> fixture =
                FixtureLoader.load("transactions/V2/multi_payment/passphrase");
        LinkedTreeMap<String, Object> data = (LinkedTreeMap<String, Object>) fixture.get("data");

        Transaction actual = new Deserializer().deserialize(fixture.get("serialized").toString());
        assertEquals(((Double) data.get("type")).intValue(), actual.type);
        assertEquals(TransactionTypeGroup.CORE.getValue(), actual.typeGroup);
        assertEquals((Long.valueOf((String) data.get("fee"))), actual.fee);
        assertEquals((Long.valueOf((String) data.get("nonce"))), actual.nonce);
        assertEquals(data.get("senderPublicKey").toString(), actual.senderPublicKey);
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
    void secondPassphrase() {
        LinkedTreeMap<String, Object> fixture =
                FixtureLoader.load("transactions/V2/multi_payment/second-passphrase");
        LinkedTreeMap<String, Object> data = (LinkedTreeMap<String, Object>) fixture.get("data");

        Transaction actual = new Deserializer().deserialize(fixture.get("serialized").toString());
        assertEquals(((Double) data.get("type")).intValue(), actual.type);
        assertEquals(TransactionTypeGroup.CORE.getValue(), actual.typeGroup);
        assertEquals((Long.valueOf((String) data.get("fee"))), actual.fee);
        assertEquals((Long.valueOf((String) data.get("nonce"))), actual.nonce);
        assertEquals(data.get("senderPublicKey").toString(), actual.senderPublicKey);
        assertEquals(data.get("signature").toString(), actual.signature);
        assertEquals(data.get("secondSignature").toString(), actual.signSignature);
        assertEquals(data.get("secondSignature").toString(), actual.secondSignature);
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

        assertEquals(data.get("id").toString(), actual.id);
    }
}
