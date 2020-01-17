package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.arkecosystem.crypto.transactions.Transaction;
import org.junit.jupiter.api.Test;

public class DelegateRegistrationTest {

    @Test
    void build() {
        Transaction actualV1 =
                new DelegateRegistration()
                        .username("java")
                        .version(1)
                        .sign("this is a top secret passphrase")
                        .transaction;

        assertTrue(actualV1.verify());

        Transaction actualV2 =
                new DelegateRegistration()
                        .username("java")
                        .version(2)
                        .nonce(3)
                        .sign("this is a top secret passphrase")
                        .transaction;

        assertTrue(actualV2.verify());
    }

    @Test
    void buildSecondSignature() {
        Transaction actualV1 =
                new DelegateRegistration()
                        .username("java")
                        .sign("this is a top secret passphrase")
                        .secondSign("this is a top secret second passphrase")
                        .transaction;

        assertTrue(actualV1.verify());
        assertTrue(
                actualV1.secondVerify(
                        "03699e966b2525f9088a6941d8d94f7869964a000efe65783d78ac82e1199fe609"));

        Transaction actualV2 =
                new DelegateRegistration()
                        .username("java")
                        .version(2)
                        .nonce(3)
                        .sign("this is a top secret passphrase")
                        .secondSign("this is a top secret second passphrase")
                        .transaction;

        assertTrue(actualV2.verify());
        assertTrue(
                actualV2.secondVerify(
                        "03699e966b2525f9088a6941d8d94f7869964a000efe65783d78ac82e1199fe609"));
    }
}
