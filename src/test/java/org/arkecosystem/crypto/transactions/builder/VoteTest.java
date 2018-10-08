package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.transactions.Transaction;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class VoteTest {


    @Test
    void build() {
        Transaction actual = new Vote()
            .votes(Arrays.asList("'+034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed192'"))
            .sign("this is a top secret passphrase")
            .transaction;

        assertTrue(actual.verify());
    }

    @Test
    void buildSecondSignature() {
        Transaction actual = new Vote()
            .votes(Arrays.asList("'+034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed192'"))
            .sign("this is a top secret passphrase")
            .secondSign("this is a top secret second passphrase")
            .transaction;

        assertTrue(actual.verify());
        assertTrue(actual.secondVerify("03699e966b2525f9088a6941d8d94f7869964a000efe65783d78ac82e1199fe609"));
    }

}
