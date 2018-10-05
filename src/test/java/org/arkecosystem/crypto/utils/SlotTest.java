package org.arkecosystem.crypto.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SlotTest {

    @Test
    void time() {
        assertEquals(true, Integer.class.isInstance(Slot.time()));
    }

    @Test
    void epoch() {
        assertEquals(1_490_101_200_000L, Slot.epoch());
    }
}
