package org.arkecosystem.crypto.configuration;

import org.arkecosystem.crypto.networks.INetwork;
import org.arkecosystem.crypto.networks.Mainnet;

public class Network {
    private static INetwork network = new Mainnet();

    public static INetwork get() {
        return network;
    }

    public static void set(INetwork network) {
        Network.network = network;
    }
}
