package org.arkecosystem.crypto.configuration;

import org.arkecosystem.crypto.networks.Devnet;
import org.arkecosystem.crypto.networks.INetwork;

public class Network {
    public static INetwork get() {
        return network;
    }

    public static void set(INetwork network) {
        Network.network = network;
    }

    private static INetwork network = new Devnet();
}
