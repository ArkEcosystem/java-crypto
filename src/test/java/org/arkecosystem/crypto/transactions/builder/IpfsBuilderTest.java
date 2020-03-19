package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.junit.jupiter.api.Test;

class IpfsBuilderTest {

    @Test
    void passphrase() {
        Transaction actual =
                new IpfsBuilder()
                        .ipfsAsset("QmR45FmbVVrixReBwJkhEKde2qwHYaQzGxu4ZoDeswuF9w")
                        .version(2)
                        .nonce(3)
                        .sign("this is a top secret passphrase")
                        .transaction;

        HashMap actualHashMap = actual.toHashMap();
        HashMap actualAsset = (HashMap) actualHashMap.get("asset");
        assertEquals(actualAsset.get("ipfs"), "QmR45FmbVVrixReBwJkhEKde2qwHYaQzGxu4ZoDeswuF9w");

        assertTrue(actual.verify());
    }

    @Test
    void buildSecondSignature() {
        Transaction actual =
                new IpfsBuilder()
                        .ipfsAsset("QmR45FmbVVrixReBwJkhEKde2qwHYaQzGxu4ZoDeswuF9w")
                        .version(2)
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
