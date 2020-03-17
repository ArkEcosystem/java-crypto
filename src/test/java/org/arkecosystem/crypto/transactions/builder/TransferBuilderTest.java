package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import org.arkecosystem.crypto.enums.Fees;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.junit.jupiter.api.Test;

class TransferBuilderTest {

    @Test
    void build() {
        Transaction actual =
                new TransferBuilder()
                        .recipient("AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25")
                        .amount(133380000000L)
                        .expiration(100000)
                        .vendorField("This is a transaction from Java")
                        .version(2)
                        .nonce(3)
                        .network(23)
                        .fee(Fees.TRANSFER.getValue())
                        .sign("this is a top secret passphrase")
                        .transaction;

        assertTrue(actual.verify());

        HashMap actualHashMap = actual.toHashMap();
        assertEquals(actualHashMap.get("recipientId"), "AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25");
        assertEquals(actualHashMap.get("amount"), "133380000000");
        assertEquals(actualHashMap.get("expiration"), 100000);
        assertEquals(actualHashMap.get("vendorField"), "This is a transaction from Java");
        assertEquals(actualHashMap.get("version"), 2);
        assertEquals(actualHashMap.get("nonce"), "3");
        assertEquals(actualHashMap.get("network"), 23);
        assertEquals(actualHashMap.get("fee"), Fees.TRANSFER.getValue().toString());
        assertEquals(actualHashMap.get("id"), actual.id);
    }

    @Test
    void buildSecondSignature() {
        Transaction actual =
                new TransferBuilder()
                        .recipient("AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25")
                        .amount(133380000000L)
                        .expiration(100000)
                        .vendorField("This is a transaction from Java")
                        .version(2)
                        .nonce(3)
                        .sign("this is a top secret passphrase")
                        .secondSign("this is a top secret second passphrase")
                        .transaction;

        assertTrue(actual.verify());
        assertTrue(
                actual.secondVerify(
                        "03699e966b2525f9088a6941d8d94f7869964a000efe65783d78ac82e1199fe609"));

        HashMap actualHashMap = actual.toHashMap();
        assertEquals(actualHashMap.get("secondSignature"), actual.secondSignature);
    }
}
