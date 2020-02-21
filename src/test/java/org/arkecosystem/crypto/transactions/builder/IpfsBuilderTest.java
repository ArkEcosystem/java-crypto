package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.transactions.types.Transaction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class IpfsBuilderTest {

    @Test
    void passphrase() {
        Transaction actualV2 = new IpfsBuilder()
                .ipfsAsset("QmR45FmbVVrixReBwJkhEKde2qwHYaQzGxu4ZoDeswuF9w")
                .version(2)
                .nonce(3)
                .sign("this is a top secret passphrase")
                .transaction;

        assertTrue(actualV2.verify());
    }
}
