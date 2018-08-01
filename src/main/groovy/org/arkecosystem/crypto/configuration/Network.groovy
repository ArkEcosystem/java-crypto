package org.arkecosystem.crypto.configuration

import org.arkecosystem.crypto.networks.*

class Network {
    private static INetwork network;

    static
    {
        network = new Mainnet()
    }

    static INetwork get() {
        return network
    }

    static void set(INetwork network) {
        Network.network = network
    }
}
