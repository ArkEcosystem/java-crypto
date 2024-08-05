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
}
