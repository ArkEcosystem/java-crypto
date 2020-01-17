package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.arkecosystem.crypto.transactions.Transaction;
import org.junit.jupiter.api.Test;

public class SecondSignatureRegistrationTest {

    @Test
    void build() {
        Transaction actualV1 =
                new SecondSignatureRegistration()
                        .signature("this is a top secret second passphrase'")
                        .version(1)
                        .sign("this is a top secret passphrase")
                        .transaction;

        assertTrue(actualV1.verify());

        Transaction actualV2 =
                new SecondSignatureRegistration()
                        .signature("this is a top secret second passphrase'")
                        .version(2)
                        .nonce(3)
                        .sign("this is a top secret passphrase")
                        .transaction;

        assertTrue(actualV2.verify());
    }
}
