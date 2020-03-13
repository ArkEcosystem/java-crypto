package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.arkecosystem.crypto.transactions.types.Transaction;
import org.junit.jupiter.api.Test;

class SecondSignatureRegistrationBuilderTest {

    @Test
    void passphrase() {
        Transaction actualV2 =
                new SecondSignatureRegistrationBuilder()
                        .signature("this is a top secret second passphrase'")
                        .version(2)
                        .nonce(3)
                        .sign("this is a top secret passphrase")
                        .transaction;

        assertTrue(actualV2.verify());
    }
}
