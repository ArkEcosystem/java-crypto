package org.arkecosystem.crypto.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.arkecosystem.crypto.enums.CoreTransactionTypes;
import org.junit.jupiter.api.Test;

class FeeTest {

    @Test
    void get() {
        long fee = Fee.getCoreFee(CoreTransactionTypes.TRANSFER.getValue());
        assertEquals(10_000_000, fee);
    }

    @Test
    void set() {
        Map<Integer, Long> fees = new HashMap<>();
        fees.put(500, 1L);
        Fee.set(1001, fees);
        long fee = Fee.getFee(1001, 500);
        assertEquals(1L, fee);
    }
}
