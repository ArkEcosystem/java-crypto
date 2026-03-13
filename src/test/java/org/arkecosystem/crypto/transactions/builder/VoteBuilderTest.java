package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.arkecosystem.crypto.AbstractTest;
import org.arkecosystem.crypto.encoding.Hex;
import org.junit.jupiter.api.Test;

public class VoteBuilderTest extends AbstractTest {

    @Test
    public void it_should_sign_it_with_a_passphrase() throws Exception {
        Map<String, Object> fixture = loadFixture("vote");

        Map<String, Object> data = (Map<String, Object>) fixture.get("data");

        VoteBuilder builder =
                new VoteBuilder()
                        .gasPrice(((Number) data.get("gasPrice")).longValue())
                        .nonce(Long.parseLong(data.get("nonce").toString()))
                        .network(((Number) data.get("network")).intValue())
                        .vote("0xc3Bbe9B1CEe1FF85AD72b87414b0e9b7f2366763")
                        .gasLimit(((Number) data.get("gasLimit")).longValue())
                        .recipientAddress((String) data.get("recipientAddress"))
                        .sign(this.passphrase);

        byte[] serializedBytes = builder.transaction.serialize(false);
        String serializedHex = Hex.encode(serializedBytes);

        assertEquals(fixture.get("serialized"), serializedHex);
        assertEquals(data.get("id"), builder.transaction.getId());
        assertTrue(builder.verify());
    }
}
