package org.arkecosystem.crypto.transactions.builder;


import org.arkecosystem.crypto.transactions.types.Transaction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MultiPaymentBuilderTest {
    @Test
    void build() {
        Transaction actual =
            new MultiPaymentBuilder()
                .vendorField("This is a transaction from Java")
                .addPayment("AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25", 1)
                .addPayment("AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25", 2)
                .addPayment("AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25", 3)
                .vendorField("This is a transaction from Java")
                .sign("this is a top secret passphrase")
                .transaction;

        assertTrue(actual.verify());
    }


    @Test
    void buildSecondSignature() {
        Transaction actual =
            new MultiPaymentBuilder()
                .vendorField("This is a transaction from Java")
                .addPayment("AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25", 1)
                .addPayment("AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25", 2)
                .addPayment("AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25", 3)
                .vendorField("This is a transaction from Java")
                .sign("this is a top secret passphrase")
                .secondSign("this is a top secret second passphrase")
                .transaction;

        assertTrue(actual.verify());
    }
}
