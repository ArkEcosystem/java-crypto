package org.arkecosystem.crypto.transactions.builder;

import java.util.HashMap;
import java.util.List;
import org.arkecosystem.crypto.transactions.types.Transaction;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.arkecosystem.crypto.AbstractTest;
import org.arkecosystem.crypto.encoding.Hex;
import org.junit.jupiter.api.Test;

public class TransferBuilderTest extends AbstractTest {

    @Test
    public void it_should_sign_it_with_a_passphrase() throws Exception {
        Map<String, Object> fixture = loadFixture("transfer");

        Map<String, Object> data = (Map<String, Object>) fixture.get("data");

        TransferBuilder builder =
                new TransferBuilder()
                        .gasPrice(((Number) data.get("gasPrice")).intValue())
                        .nonce(Long.parseLong(data.get("nonce").toString()))
                        .network(((Number) data.get("network")).intValue())
                        .gasLimit(((Number) data.get("gasLimit")).intValue())
                        .recipientAddress((String) data.get("recipientAddress"))
                        .value((String) data.get("value"))
                        .sign(this.passphrase);

        byte[] serializedBytes = builder.transaction.serialize(false);
        String serializedHex = Hex.encode(serializedBytes);

        // Compare the serialized transaction
        assertEquals(fixture.get("serialized"), serializedHex);
        assertEquals(data.get("id"), builder.transaction.getId());
        assertTrue(builder.verify());
    }

    @Test
    void buildMultiSignature() {
        Transaction actual =
                new TransferBuilder()
                        .recipient("0xb693449AdDa7EFc015D87944EAE8b7C37EB1690A")
                        .amount(133380000000L)
                        .expiration(100000)
                        .vendorField("This is a transaction from Java")
                        .nonce(3)
                        .multiSign("secret 1", 0)
                        .multiSign("secret 2", 1)
                        .multiSign("secret 3", 2)
                        .sign("secret 1")
                        .transaction;

        assertTrue(actual.verify());

        HashMap actualHashMap = actual.toHashMap();

        assertNotNull(actualHashMap.get("signatures"));

        List<String> actualSignatures = (List<String>) actualHashMap.get("signatures");

        assertEquals(3, actualSignatures.size());
    }
}
