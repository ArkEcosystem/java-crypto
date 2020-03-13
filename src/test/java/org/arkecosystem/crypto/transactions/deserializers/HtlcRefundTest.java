package org.arkecosystem.crypto.transactions.deserializers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.internal.LinkedTreeMap;
import org.arkecosystem.crypto.enums.TransactionTypeGroup;
import org.arkecosystem.crypto.transactions.Deserializer;
import org.arkecosystem.crypto.transactions.FixtureLoader;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.junit.jupiter.api.Test;

public class HtlcRefundTest {

    @Test
    void passphrase() {
        LinkedTreeMap<String, Object> fixture =
                FixtureLoader.load("transactions/v2-ecdsa/htlc-refund-sign");

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

        LinkedTreeMap<String, Object> asset =
                (LinkedTreeMap<String, Object>)
                        ((LinkedTreeMap<String, Object>) data.get("asset")).get("refund");
        assertEquals(
                (asset.get("lockTransactionId")), actual.asset.htlcRefundAsset.lockTransactionId);
    }

    @Test
    void secondPassphrase() {
        LinkedTreeMap<String, Object> fixture =
                FixtureLoader.load("transactions/v2-ecdsa/htlc-refund-secondSign");

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

        LinkedTreeMap<String, Object> asset =
                (LinkedTreeMap<String, Object>)
                        ((LinkedTreeMap<String, Object>) data.get("asset")).get("refund");
        assertEquals(
                (asset.get("lockTransactionId")), actual.asset.htlcRefundAsset.lockTransactionId);

        assertEquals(data.get("secondSignature").toString(), actual.secondSignature);
    }
}
