package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.junit.jupiter.api.Test;

class MultiPaymentBuilderTest {
    @Test
    void build() {
        Transaction actual =
                new MultiPaymentBuilder()
                        .addPayment("AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25", 1)
                        .addPayment("AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25", 2)
                        .addPayment("AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25", 3)
                        .vendorField("This is a transaction from Java")
                        .sign("this is a top secret passphrase")
                        .transaction;

        HashMap actualHashMap = actual.toHashMap();
        HashMap actualAsset = (HashMap) actualHashMap.get("asset");
        ArrayList payments = (ArrayList) actualAsset.get("payments");
        HashMap payment = (HashMap) payments.get(0);
        assertEquals(payment.get("amount"), "1");
        assertEquals(payment.get("recipientId"), "AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25");

        assertTrue(actual.verify());
    }

    @Test
    void buildSecondSignature() {
        Transaction actual =
                new MultiPaymentBuilder()
                        .addPayment("AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25", 1)
                        .addPayment("AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25", 2)
                        .addPayment("AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25", 3)
                        .vendorField("This is a transaction from Java")
                        .sign("this is a top secret passphrase")
                        .secondSign("this is a top secret second passphrase")
                        .transaction;

        assertTrue(actual.verify());
        assertTrue(
                actual.secondVerify(
                        "03699e966b2525f9088a6941d8d94f7869964a000efe65783d78ac82e1199fe609"));
    }

    @Test
    void testMaxPayments() {
        MultiPaymentBuilder actual = new MultiPaymentBuilder();
        for (int i = 0; i < 64; i++) {
            actual.addPayment("AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25", 1);
        }
        Throwable exception =
                assertThrows(
                        MaximumPaymentCountExceededError.class,
                        () -> actual.addPayment("AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25", 1));
        assertEquals("Expected a maximum of 64 payments", exception.getMessage());
    }
}
