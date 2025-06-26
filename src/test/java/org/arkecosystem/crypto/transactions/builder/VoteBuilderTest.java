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

    @Test
    void buildMultiSignature() {
        Transaction actual =
                new VoteBuilder()
                        .addVote(
                                "034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed192")
                        .version(2)
                        .nonce(3)
                        .multiSign("secret 1", 0)
                        .multiSign("secret 2", 1)
                        .multiSign("secret 3", 2)
                        .sign("this is a top secret passphrase")
                        .transaction;

        assertTrue(actual.verify());

        HashMap actualHashMap = actual.toHashMap();

        assertNotNull(actualHashMap.get("signatures"));

        List<String> actualSignatures = (List<String>) actualHashMap.get("signatures");

        assertEquals(3, actualSignatures.size());
    }
}
