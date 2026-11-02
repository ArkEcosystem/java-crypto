package org.arkecosystem.crypto.transactions;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import org.arkecosystem.crypto.AbstractTest;
import org.arkecosystem.crypto.configuration.Network;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.identities.PrivateKey;
import org.arkecosystem.crypto.networks.Testnet;
import org.arkecosystem.crypto.transactions.types.AbstractTransaction;
import org.arkecosystem.crypto.transactions.types.Transfer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Test vector from the Mainsail LSS v1 spec, built for chain 10_000
public class LegacySecondSignatureTest extends AbstractTest {
    private Map<String, Object> fixture;
    private Map<String, Object> data;

    @BeforeEach
    public void setUpVector() throws Exception {
        this.fixture = loadFixture("transfer-legacy-second-signature-vector");
        this.data = (Map<String, Object>) fixture.get("data");

        int chainId = ((Number) fixture.get("chainId")).intValue();
        Network.set(
                new Testnet() {
                    @Override
                    public int chainId() {
                        return chainId;
                    }
                });
    }

    private Transfer unsignedTransfer() {
        Map<String, Object> unsignedData = new HashMap<>(this.data);
        unsignedData.remove("legacySecondSignature");
        unsignedData.remove("id");

        return new Transfer(unsignedData);
    }

    @Test
    public void it_should_sign_the_hash_of_the_signed_transaction_with_the_second_key() {
        Transfer transaction = unsignedTransfer();

        transaction.sign(PrivateKey.fromHex((String) fixture.get("privateKey")));

        assertEquals(fixture.get("signed"), Hex.encode(transaction.serialize()));
        assertEquals(data.get("id"), transaction.getId());

        transaction.legacySecondSign(PrivateKey.fromHex((String) fixture.get("secondPrivateKey")));

        assertEquals(data.get("legacySecondSignature"), transaction.legacySecondSignature);
        assertEquals(fixture.get("serialized"), Hex.encode(transaction.serialize()));
        assertEquals(data.get("id"), transaction.getId());
    }

    @Test
    public void it_should_use_a_0_or_1_recovery_id() {
        Transfer transaction = unsignedTransfer();

        transaction.sign(PrivateKey.fromHex((String) fixture.get("privateKey")));
        transaction.legacySecondSign(PrivateKey.fromHex((String) fixture.get("secondPrivateKey")));

        String recoveryId = transaction.legacySecondSignature.substring(128);
        assertTrue(recoveryId.equals("00") || recoveryId.equals("01"));
    }

    @Test
    public void it_should_require_the_primary_signature_first() {
        Transfer transaction = unsignedTransfer();

        IllegalStateException exception =
                assertThrows(
                        IllegalStateException.class,
                        () ->
                                transaction.legacySecondSign(
                                        PrivateKey.fromHex(
                                                (String) fixture.get("secondPrivateKey"))));

        assertEquals(
                "Transaction must be signed before adding a legacy second signature",
                exception.getMessage());
    }

    @Test
    public void it_should_deserialize_and_hash_only_the_first_9_fields() {
        AbstractTransaction transaction =
                Deserializer.newDeserializer((String) fixture.get("serialized")).deserialize();

        assertEquals(data.get("legacySecondSignature"), transaction.legacySecondSignature);
        assertEquals(data.get("id"), transaction.id);
        assertEquals(fixture.get("serialized"), Hex.encode(transaction.serialize()));
        assertTrue(transaction.verify());
    }
}
