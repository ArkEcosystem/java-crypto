package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import java.util.Map;
import org.arkecosystem.crypto.AbstractTest;
import org.arkecosystem.crypto.encoding.Hex;
import org.junit.jupiter.api.Test;

public class TokenApproveBuilderTest extends AbstractTest {

    @Test
    public void it_should_sign_it_with_a_passphrase() throws Exception {
        Map<String, Object> fixture = loadFixture("token-approve");

        Map<String, Object> data = (Map<String, Object>) fixture.get("data");

        TokenApproveBuilder builder =
                new TokenApproveBuilder()
                        .contractAddress((String) data.get("recipientAddress"))
                        .spender((String) data.get("recipientAddress"), BigInteger.ZERO)
                        .gasPrice(((Number) data.get("gasPrice")).intValue())
                        .nonce(Long.parseLong(data.get("nonce").toString()))
                        .network(((Number) data.get("network")).intValue())
                        .gasLimit(((Number) data.get("gasLimit")).intValue())
                        .sign(this.passphrase);

        byte[] serializedBytes = builder.transaction.serialize(false);
        String serializedHex = Hex.encode(serializedBytes);

        assertEquals(fixture.get("serialized"), serializedHex);
        assertEquals(data.get("id"), builder.transaction.getId());
        assertTrue(builder.verify());
        assertTrue(builder.transaction.data.startsWith("095ea7b3"));
    }
}
