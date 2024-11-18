package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.arkecosystem.crypto.AbstractTest;
import org.junit.jupiter.api.Test;

public class EvmCallBuilderTest extends AbstractTest {

    @Test
    public void it_should_sign_it_with_a_passphrase() throws Exception {
        Map<String, Object> fixture = loadFixture("evm-sign");

        Map<String, Object> data = (Map<String, Object>) fixture.get("data");

        EvmCallBuilder builder =
                new EvmCallBuilder()
                        .gasPrice(((Number) data.get("gasPrice")).intValue())
                        .nonce(Long.parseLong(data.get("nonce").toString()))
                        .network(((Number) data.get("network")).intValue())
                        .payload((String) data.get("data"))
                        .gasLimit(((Number) data.get("gasLimit")).intValue())
                        .recipientAddress("0xE536720791A7DaDBeBdBCD8c8546fb0791a11901")
                        .sign(this.passphrase);

        assertTrue(builder.verify());
    }
}
