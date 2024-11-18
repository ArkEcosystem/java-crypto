package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.*;

import org.arkecosystem.crypto.encoding.Hex;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class VoteBuilderTest extends AbstractTransactionBuilderTest {

    @Test
    public void it_should_sign_it_with_a_passphrase() throws Exception {
        Map<String, Object> fixture = loadFixture( "vote");

        Map<String, Object> data = (Map<String, Object>) fixture.get("data");

        VoteBuilder builder = new VoteBuilder()
                .gasPrice(((Number) data.get("gasPrice")).intValue())
                .nonce(Long.parseLong(data.get("nonce").toString()))
                .network(((Number) data.get("network")).intValue())
                .vote("0x512F366D524157BcF734546eB29a6d687B762255")
                .gasLimit(((Number) data.get("gasLimit")).intValue())
                .recipientAddress((String) data.get("recipientAddress"))
                .sign(this.passphrase);

        byte[] serializedBytes = builder.transaction.serialize(false);
        String serializedHex = Hex.encode(serializedBytes);

        assertEquals(fixture.get("serialized"), serializedHex);
        assertEquals(data.get("id"), builder.transaction.getId());
        assertTrue(builder.verify());
    }
}
