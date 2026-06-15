package org.arkecosystem.crypto.transactions.builder;

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
                        .gasPrice(((Number) data.get("gasPrice")).longValue())
                        .nonce(Long.parseLong(data.get("nonce").toString()))
                        .network(((Number) data.get("network")).intValue())
                        .gasLimit(((Number) data.get("gasLimit")).longValue())
                        .recipientAddress((String) data.get("recipientAddress"))
                        .value((String) data.get("value"))
                        .sign(this.passphrase);

        byte[] serializedBytes = builder.transaction.serialize(false);
        String serializedHex = Hex.encode(serializedBytes);

        assertEquals(fixture.get("serialized"), serializedHex);
        assertEquals(data.get("id"), builder.transaction.getId());
        assertTrue(builder.verify());
    }

    @Test
    public void it_should_attach_a_legacy_second_signature() {
        TransferBuilder builder =
                new TransferBuilder()
                        .gasPrice(5_000_000_000L)
                        .gasLimit(21000)
                        .nonce(1L)
                        .recipientAddress("0xb693449AdDa7EFc015D87944EAE8b7C37EB1690A")
                        .value("100000000")
                        .legacySecondSign(this.passphrase, "second secret passphrase");

        assertNotNull(builder.transaction.signature);
        assertNotNull(builder.transaction.legacySecondSignature);
        assertEquals(130, builder.transaction.legacySecondSignature.length());
        assertNotEquals(builder.transaction.signature, builder.transaction.legacySecondSignature);
        assertTrue(builder.verify());
    }

    @Test
    public void legacy_second_signature_is_not_set_by_a_regular_sign() {
        TransferBuilder builder =
                new TransferBuilder()
                        .gasPrice(5_000_000_000L)
                        .gasLimit(21000)
                        .nonce(1L)
                        .recipientAddress("0xb693449AdDa7EFc015D87944EAE8b7C37EB1690A")
                        .value("100000000")
                        .sign(this.passphrase);

        assertNotNull(builder.transaction.signature);
        assertNull(builder.transaction.legacySecondSignature);
    }
}
