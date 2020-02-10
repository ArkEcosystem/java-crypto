package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.arkecosystem.crypto.configuration.Network;
import org.arkecosystem.crypto.networks.Devnet;
import org.arkecosystem.crypto.networks.Testnet;
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
    void buildDevnet() {
        Network.set(new Devnet());
        Transaction actual =
                new MultiPayment()
                        .addPayment("DNA6BqdX2mYw3PtPfGmEz2bGyyCA9hbaED", 1)
                        .addPayment("D65rqoZU7U4FdqLVF9KsEF9GBtvwDEVTBt", 2)
                        .addPayment("D88ZXZ5MaeZFA6xrJPSgJ92qWVWY1462dD", 3)
                        .vendorField("Zan Vendor Field arghhh")
                        .nonce(2)
                        .sign("nurse organ hub theory mad strike desert add heavy deposit immune inform")
                        .transaction;

        System.out.println(actual.toJson());
        System.out.println(actual.serialize());
        assertTrue(actual.verify());
    }

    @Test
    void buildTestnet() {
        Network.set(new Testnet());
        Transaction actual =
            new MultiPayment()
                .addPayment("AHXtmB84sTZ9Zd35h9Y1vfFvPE2Xzqj8ri", 1)
                .addPayment("AZFEPTWnn2Sn8wDZgCRF8ohwKkrmk2AZi1", 2)
                .nonce(1)
                .sign("this is a top secret passphrase")
                .transaction;

        System.out.println(actual.toJson());
        System.out.println(actual.serialize());
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
