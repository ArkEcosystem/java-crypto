package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.*;

import org.arkecosystem.crypto.encoding.Hex;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class TransferBuilderTest extends AbstractTransactionBuilderTest {

    @Test
    public void it_should_sign_it_with_a_passphrase() throws Exception {
        Map<String, Object> fixture = loadFixture("transfer");

        Map<String, Object> data = (Map<String, Object>) fixture.get("data");

        TransferBuilder builder = new TransferBuilder()
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
}
