package org.arkecosystem.crypto.configuration;

import org.arkecosystem.crypto.networks.*;
import org.junit.jupiter.api.Test;
import org.testng.reporters.jq.Main;

import static org.junit.jupiter.api.Assertions.*;

class NetworkTest {

    @Test
    public void get() {
        INetwork network = Network.get();
        assertEquals(new Devnet().version(), network.version());
    }

    @Test
    public void set() {
        Network.set(new Testnet());
        assertEquals(new Testnet().version(), Network.get().version());
        Network.set(new Devnet());
        assertEquals(new Devnet().version(), Network.get().version());
    }
}
