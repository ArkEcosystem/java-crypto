package org.arkecosystem.crypto.networks;

import org.arkecosystem.crypto.configuration.Network;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DevnetTest {

    @Test
    void version() {
        assertEquals(0x1e, Network.get().version());
    }

    @Test
    void wif() {
        assertEquals(170, Network.get().wif());
    }

    @Test
    void epoch() {
        assertEquals("2017-03-21 13:00:00", Network.get().epoch());
    }
}
