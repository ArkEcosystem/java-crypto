package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import org.arkecosystem.crypto.transactions.types.Transaction;
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
    void testValidatorRegistrationTransactionWithInvalidBlsPublicKey() {
        Exception exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> {
                            new ValidatorRegistrationBuilder()
                                    .publicKeyAsset(
                                            "b5fea88b9aab3f0b122e5a7e1b07917e62a63ea59103d0a0715ecded3c41685af88f0a9606309b148b3b50f51a2edddd")
                                    .nonce(3)
                                    .sign("this is a top secret passphrase");
                        });

        String expectedMessage = "Invalid BLS public key";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testValidatorRegistrationTransactionWithInvalidBlsPublicKeyByLength() {
        Exception exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> {
                            new ValidatorRegistrationBuilder()
                                    .publicKeyAsset(
                                            "023efc1da7f315f3c533a4080e491f32cd4219731cef008976c3876539e1f192d3")
                                    .nonce(3)
                                    .sign("this is a top secret passphrase");
                        });

        String expectedMessage = "Invalid BLS public key";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void buildMultiSignature() {
        Transaction actual =
                new ValidatorRegistrationBuilder()
                        .publicKeyAsset(
                                "a08058db53e2665c84a40f5152e76dd2b652125a6079130d4c315e728bcf4dd1dfb44ac26e82302331d61977d3141118")
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
