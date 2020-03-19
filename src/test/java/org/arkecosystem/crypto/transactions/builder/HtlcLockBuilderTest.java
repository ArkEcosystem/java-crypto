package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import org.arkecosystem.crypto.enums.HtlcLockExpirationType;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.junit.jupiter.api.Test;

class HtlcLockBuilderTest {
    @Test
    void build() {
        Transaction actual =
                new HtlcLockBuilder()
                        .amount(1000000)
                        .recipientId("AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25")
                        .secretHash(
                                "0f128d401958b1b30ad0d10406f47f9489321017b4614e6cb993fc63913c5454")
                        .expirationType(HtlcLockExpirationType.EPOCH_TIMESTAMP, 1)
                        .vendorField("This is vendor field java")
                        .nonce(3)
                        .sign("this is a top secret passphrase")
                        .transaction;

        HashMap actualHashMap = actual.toHashMap();
        HashMap actualAsset = (HashMap) actualHashMap.get("asset");
        HashMap lock = (HashMap) actualAsset.get("lock");
        HashMap expiration = (HashMap) lock.get("expiration");

        assertEquals(
                lock.get("secretHash"),
                "0f128d401958b1b30ad0d10406f47f9489321017b4614e6cb993fc63913c5454");
        assertEquals(expiration.get("type"), HtlcLockExpirationType.EPOCH_TIMESTAMP.getValue());
        assertEquals(expiration.get("value"), 1);

        assertTrue(actual.verify());
    }

    @Test
    void buildSecondSignature() {
        Transaction actual =
                new HtlcLockBuilder()
                        .amount(1000000)
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
