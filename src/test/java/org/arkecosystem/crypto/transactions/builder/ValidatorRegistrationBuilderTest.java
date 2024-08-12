package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.junit.jupiter.api.Test;

class ValidatorRegistrationBuilderTest {

    @Test
    void build() {
        List<String> publicKeys =
                List.of(
                        "97f1d3a73197d7942695638c4fa9ac0fc3688c4f9774b905a14e3a3f171bac586c55e83ff97a1aeffb3af00adb22c6bb"
                        // "b4865127896c3c5286296a7b26e7c8002586a3ecf5832bfb59e689336f1f4c75e10491b9dfaed8dfb2c2fbe22d11fa93"
                        // From python
                        // "a08058db53e2665c84a40f5152e76dd2b652125a6079130d4c315e728bcf4dd1dfb44ac26e82302331d61977d3141118"
                        );

        for (String publicKey : publicKeys) {
            Transaction actual =
                    new ValidatorRegistrationBuilder()
                            .publicKeyAsset(publicKey)
                            .nonce(3)
                            .sign("this is a top secret passphrase")
                            .transaction;

            HashMap actualHashMap = actual.toHashMap();
            HashMap asset = (HashMap) actualHashMap.get("asset");

            assertEquals(asset.get("validatorPublicKey"), publicKey);
            assertTrue(actual.verify());
        }
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
