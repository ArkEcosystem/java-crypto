package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.arkecosystem.crypto.AbstractTest;
import org.arkecosystem.crypto.encoding.Hex;
import org.junit.jupiter.api.Test;

public class ValidatorRegistrationBuilderTest extends AbstractTest {

    private static final String VALIDATOR_PASSPHRASE =
            "gold favorite math anchor detect march purpose such sausage crucial reform novel"
                    + " connect misery update episode invite salute barely garbage exclude winner"
                    + " visa cruise";

    @Test
    public void it_should_sign_it_with_a_passphrase() throws Exception {
        Map<String, Object> fixture = loadFixture("validator-registration");
        Map<String, Object> data = (Map<String, Object>) fixture.get("data");

        ValidatorRegistrationBuilder builder =
                new ValidatorRegistrationBuilder()
                        .gasPrice(((Number) data.get("gasPrice")).longValue())
                        .nonce(Long.parseLong(data.get("nonce").toString()))
                        .network(((Number) data.get("network")).intValue())
                        .gasLimit(((Number) data.get("gasLimit")).longValue())
                        .validatorPassphrase(VALIDATOR_PASSPHRASE)
                        .recipientAddress((String) data.get("recipientAddress"))
                        .sign(this.passphrase);

        assertEquals(fixture.get("serialized"), Hex.encode(builder.transaction.serialize(false)));
        assertEquals(data.get("id"), builder.transaction.getId());
        assertTrue(builder.verify());
    }
}
