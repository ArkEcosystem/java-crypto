package org.arkecosystem.crypto.networks;

import org.arkecosystem.crypto.configuration.Network;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
