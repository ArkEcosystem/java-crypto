package org.arkecosystem.crypto.configuration


import org.arkecosystem.crypto.networks.AbstractNetwork
import org.arkecosystem.crypto.networks.Mainnet

class Network {
    private static AbstractNetwork network;

    static
    {
        network = new Mainnet()
    }

    static AbstractNetwork get() {
        return network
    }

    static void set(AbstractNetwork network) {
        Network.network = network
    }
}
