package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.junit.jupiter.api.Test;

class SecondSignatureRegistrationBuilderTest {

    @Test
    void passphrase() {
        Transaction actual =
                new SecondSignatureRegistrationBuilder()
                        .signature("this is a top secret second passphrase'")
                        .version(2)
                        .nonce(3)
                        .sign("this is a top secret passphrase")
                        .transaction;

        assertTrue(actual.verify());

        HashMap actualHashMap = actual.toHashMap();
        HashMap actualAsset = (HashMap) actualHashMap.get("asset");
        HashMap actualSignature = (HashMap) actualAsset.get("signature");
        assertEquals(
                actualSignature.get("publicKey"),
                "03d8941c0ceb0bc6334a66679e660f7143f5313a0a6e034f3869009970207da847");
    }
}
