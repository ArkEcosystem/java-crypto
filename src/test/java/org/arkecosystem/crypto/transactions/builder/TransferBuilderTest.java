package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.arkecosystem.crypto.enums.Fees;
import org.arkecosystem.crypto.identities.PublicKey;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.junit.jupiter.api.Test;

class TransferBuilderTest {

    @Test
    void build() {
        Transaction actual =
                new TransferBuilder()
                        .recipient("0xb693449AdDa7EFc015D87944EAE8b7C37EB1690A")
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
        assertEquals(
                actualHashMap.get("recipientId"), "0xb693449AdDa7EFc015D87944EAE8b7C37EB1690A");
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
                        .recipient("0xb693449AdDa7EFc015D87944EAE8b7C37EB1690A")
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

    @Test
    void buildMultiSignature() {
        Transaction actual = new TransferBuilder()
                .recipient("0xb693449AdDa7EFc015D87944EAE8b7C37EB1690A")
                .amount(133380000000L)
                .expiration(100000)
                .vendorField("This is a transaction from Java")
                .nonce(3)
                .fee(Fees.TRANSFER.getValue())
                .multiSign("secret 1", 0)
                .multiSign("secret 2", 1)
                .multiSign("secret 3", 2)
                .sign("secret 1")
                .transaction;

        assertTrue(actual.verify());

        HashMap actualHashMap = actual.toHashMap();

        assertNotNull(actualHashMap.get("signatures"));

        List<String> actualSignatures = (List<String>) actualHashMap.get("signatures");

        assertEquals(3, actualSignatures.size());
    }
}
