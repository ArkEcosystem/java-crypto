package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import org.arkecosystem.crypto.transactions.Transaction;
import org.junit.jupiter.api.Test;

public class VoteTest {

    @Test
    void build() {
        Transaction actualV1 =
                new Vote()
                        .votes(
                                Arrays.asList(
                                        "'+034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed192'"))
                        .version(1)
                        .sign("this is a top secret passphrase")
                        .transaction;

        assertTrue(actualV1.verify());

        Transaction actualV2 =
                new Vote()
                        .votes(
                                Arrays.asList(
                                        "+034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed192"))
                        .version(2)
                        .nonce(3)
                        .sign("this is a top secret passphrase")
                        .transaction;

        assertTrue(actualV2.verify());
    }

    @Test
    void buildSecondSignature() {
        Transaction actualV1 =
                new Vote()
                        .votes(
                                Arrays.asList(
                                        "'+034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed192'"))
                        .version(1)
                        .sign("this is a top secret passphrase")
                        .secondSign("this is a top secret second passphrase")
                        .transaction;

        assertTrue(actualV1.verify());
        assertTrue(
                actualV1.secondVerify(
                        "03699e966b2525f9088a6941d8d94f7869964a000efe65783d78ac82e1199fe609"));

        Transaction actualV2 =
                new Vote()
                        .votes(
                                Arrays.asList(
                                        "+034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed192"))
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
