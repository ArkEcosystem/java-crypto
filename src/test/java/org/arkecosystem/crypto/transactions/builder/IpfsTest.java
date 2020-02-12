package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.arkecosystem.crypto.transactions.Transaction;
import org.junit.jupiter.api.Test;

public class IpfsTest {

    @Test
    void build() {
        Transaction actualV2 =
                new Ipfs()
                        .ipfsAsset("QmR45FmbVVrixReBwJkhEKde2qwHYaQzGxu4ZoDeswuF9w")
                        .version(2)
                        .nonce(3)
                        .sign("this is a top secret passphrase")
                        .transaction;

        assertTrue(actualV2.verify());
    }

    @Test
    void buildSecondSignature() {
        Transaction actualV2 =
                new Ipfs()
                        .ipfsAsset("QmR45FmbVVrixReBwJkhEKde2qwHYaQzGxu4ZoDeswuF9w")
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
