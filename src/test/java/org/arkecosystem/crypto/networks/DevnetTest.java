package org.arkecosystem.crypto.networks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DevnetTest {

    @Test
    void version() {
        assertEquals(0x1E, new Devnet().version());
    }

    @Test
    void wif() {
        assertEquals(170, new Devnet().wif());
    }

    @Test
    void epoch() {
        assertEquals("2017-03-21 13:00:00", new Devnet().epoch());
    }
}
