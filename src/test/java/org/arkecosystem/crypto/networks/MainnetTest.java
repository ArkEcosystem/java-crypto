package org.arkecosystem.crypto.networks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MainnetTest {

    @Test
    void version() {
        assertEquals(0x17, new Mainnet().version());
    }

    @Test
    void wif() {
        assertEquals(170, new Mainnet().wif());
    }

    @Test
    void epoch() {
        assertEquals("2017-03-21 13:00:00", new Mainnet().epoch());
    }
}
