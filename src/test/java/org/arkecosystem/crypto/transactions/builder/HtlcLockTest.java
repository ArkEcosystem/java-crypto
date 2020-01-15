package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.arkecosystem.crypto.enums.HtlcLockExpirationType;
import org.arkecosystem.crypto.transactions.Transaction;
import org.junit.jupiter.api.Test;

public class HtlcLockTest {
    @Test
    void build() {
        Transaction actual =
                new HtlcLock()
                        .amount(1)
                        .recipientId("AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25")
                        .secretHash(
                                "0f128d401958b1b30ad0d10406f47f9489321017b4614e6cb993fc63913c5454")
                        .expirationType(HtlcLockExpirationType.EPOCH_TIMESTAMP, 1)
                        .nonce(3)
                        .sign("this is a top secret passphrase")
                        .transaction;

        assertTrue(actual.verify());
    }

    @Test
    void buildSecondSignature() {
        Transaction actual =
                new HtlcLock()
                        .amount(1)
                        .recipientId("AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25")
                        .secretHash(
                                "0f128d401958b1b30ad0d10406f47f9489321017b4614e6cb993fc63913c5454")
                        .expirationType(HtlcLockExpirationType.EPOCH_TIMESTAMP, 1)
                        .nonce(3)
                        .sign("this is a top secret passphrase")
                        .secondSign("this is a top secret second passphrase")
                        .transaction;

        assertTrue(actual.verify());
        assertTrue(
                actual.secondVerify(
                        "03699e966b2525f9088a6941d8d94f7869964a000efe65783d78ac82e1199fe609"));
    }
}
