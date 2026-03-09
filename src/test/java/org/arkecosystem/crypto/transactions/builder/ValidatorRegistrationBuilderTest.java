package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.arkecosystem.crypto.AbstractTest;
import org.arkecosystem.crypto.encoding.Hex;
import org.junit.jupiter.api.Test;

public class ValidatorRegistrationBuilderTest extends AbstractTest {

    @Test
    public void it_should_sign_it_with_a_passphrase() throws Exception {
        Map<String, Object> fixture = loadFixture("validator-registration");

        Map<String, Object> data = (Map<String, Object>) fixture.get("data");

        ValidatorRegistrationBuilder builder =
                new ValidatorRegistrationBuilder()
                        .gasPrice(((Number) data.get("gasPrice")).intValue())
                        .nonce(Long.parseLong(data.get("nonce").toString()))
                        .network(((Number) data.get("network")).intValue())
                        .gasLimit(((Number) data.get("gasLimit")).intValue())
                        .validatorPublicKey(
                                "a08058db53e2665c84a40f5152e76dd2b652125a6079130d4c315e728bcf4dd1dfb44ac26e82302331d61977d3141118")
                        .recipientAddress((String) data.get("recipientAddress"))
                        .sign(this.passphrase);

        byte[] serializedBytes = builder.transaction.serialize(false);
        String serializedHex = Hex.encode(serializedBytes);

        assertEquals(fixture.get("serialized"), serializedHex);
        assertEquals(data.get("id"), builder.transaction.getId());
        assertTrue(builder.verify());
    }

    @Test
    public void it_should_throw_on_invalid_bls_public_key() {
        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    new ValidatorRegistrationBuilder().validatorPublicKey("invalid-bls-key");
                });
    }
}
