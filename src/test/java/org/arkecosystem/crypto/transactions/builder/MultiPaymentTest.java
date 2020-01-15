package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.arkecosystem.crypto.transactions.Transaction;
import org.junit.jupiter.api.Test;

public class MultiPaymentTest {
    @Test
    void build() {
        Transaction actual =
                new MultiPayment()
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
                new MultiPayment()
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
