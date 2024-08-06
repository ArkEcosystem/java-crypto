package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.arkecosystem.crypto.transactions.types.Transaction;
import org.junit.jupiter.api.Test;

class MultiPaymentBuilderTest {
    @Test
    void build() {
        Transaction actual =
                new MultiPaymentBuilder()
                        .addPayment("0xb693449AdDa7EFc015D87944EAE8b7C37EB1690A", 1)
                        .addPayment("0xb693449AdDa7EFc015D87944EAE8b7C37EB1690A", 2)
                        .addPayment("0xb693449AdDa7EFc015D87944EAE8b7C37EB1690A", 3)
                        .vendorField("This is a transaction from Java")
                        .sign("this is a top secret passphrase")
                        .transaction;

        HashMap actualHashMap = actual.toHashMap();
        HashMap actualAsset = (HashMap) actualHashMap.get("asset");
        ArrayList payments = (ArrayList) actualAsset.get("payments");
        HashMap payment = (HashMap) payments.get(0);
        assertEquals(payment.get("amount"), "1");
        assertEquals(payment.get("recipientId"), "0xb693449AdDa7EFc015D87944EAE8b7C37EB1690A");

        assertTrue(actual.verify());
    }

    @Test
    void buildSecondSignature() {
        Transaction actual =
                new MultiPaymentBuilder()
                        .addPayment("0xb693449AdDa7EFc015D87944EAE8b7C37EB1690A", 1)
                        .addPayment("0xb693449AdDa7EFc015D87944EAE8b7C37EB1690A", 2)
                        .addPayment("0xb693449AdDa7EFc015D87944EAE8b7C37EB1690A", 3)
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
            actual.addPayment("0xb693449AdDa7EFc015D87944EAE8b7C37EB1690A", 1);
        }
        Throwable exception =
                assertThrows(
                        MaximumPaymentCountExceededError.class,
                        () -> actual.addPayment("0xb693449AdDa7EFc015D87944EAE8b7C37EB1690A", 1));
        assertEquals("Expected a maximum of 64 payments", exception.getMessage());
    }

    @Test
    void buildMultiSignature() {
        Transaction actual =
                new MultiPaymentBuilder()
                        .addPayment("0xb693449AdDa7EFc015D87944EAE8b7C37EB1690A", 1)
                        .addPayment("0xb693449AdDa7EFc015D87944EAE8b7C37EB1690A", 2)
                        .addPayment("0xb693449AdDa7EFc015D87944EAE8b7C37EB1690A", 3)
                        .vendorField("This is a transaction from Java")
                        .multiSign("secret 1", 0)
                        .multiSign("secret 2", 1)
                        .multiSign("secret 3", 2)
                        .sign("this is a top secret passphrase")
                        .transaction;

        assertTrue(actual.verify());

        HashMap actualHashMap = actual.toHashMap();

        assertNotNull(actualHashMap.get("signatures"));

        List<String> actualSignatures = (List<String>) actualHashMap.get("signatures");

        assertEquals(3, actualSignatures.size());
    }
}
