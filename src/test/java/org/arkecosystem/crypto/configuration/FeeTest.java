package org.arkecosystem.crypto.configuration;

import org.arkecosystem.crypto.enums.TransactionType;
import org.arkecosystem.crypto.enums.TransactionTypeGroup;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FeeTest {

    @Test
    void get() {
        long fee = Fee.getCoreFee(TransactionType.TRANSFER);
        assertEquals(10_000_000, fee);
    }

    @Test
    void set() {
        Map<Integer, Long> fees = new HashMap<>();
        fees.put(500, 1L);
        Fee.set(1001,fees);
        long fee = Fee.getFee(1001,500);
        assertEquals(1L, fee);
    }
}
