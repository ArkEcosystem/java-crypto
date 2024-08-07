package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.junit.jupiter.api.Test;

class ValidatorRegistrationBuilderTest {
    @Test
    void build() {
        Transaction actual =
                new ValidatorRegistrationBuilder()
                        .publicKeyAsset(
                                "a08058db53e2665c84a40f5152e76dd2b652125a6079130d4c315e728bcf4dd1dfb44ac26e82302331d61977d3141118")
                        .nonce(3)
                        .sign("this is a top secret passphrase")
                        .transaction;

        HashMap actualHashMap = actual.toHashMap();

        HashMap asset = (HashMap) actualHashMap.get("asset");

        assertEquals(
                asset.get("validatorPublicKey"),
                "a08058db53e2665c84a40f5152e76dd2b652125a6079130d4c315e728bcf4dd1dfb44ac26e82302331d61977d3141118");

        assertTrue(actual.verify());
    }

    @Test
    void testValidatorRegistrationTransactionWithInvalidBlsPublicKey() {
        Exception exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> {
                            new ValidatorRegistrationBuilder()
                                    .publicKeyAsset(
                                            "a5fea88b9aab3f0b122e5a7e1b07917e62a63ea59103d0a0715ecded3c41685af88f0a9606309b148b3b50f51a2edddg")
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
}
