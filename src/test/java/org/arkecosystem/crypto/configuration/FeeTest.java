package org.arkecosystem.crypto.configuration;

import org.arkecosystem.crypto.enums.TransactionType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FeeTest {

    @Test
    void get() {
        long fee = Fee.get(TransactionType.TRANSFER);
        assertEquals(10_000_000, fee);
    }

    @Test
    void set() {
        Fee.set(TransactionType.TRANSFER, 20_000_000L);
        long fee = Fee.get(TransactionType.TRANSFER);
        assertEquals(20_000_000, fee);
    }
}
