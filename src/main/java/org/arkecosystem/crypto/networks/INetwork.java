package org.arkecosystem.crypto.networks;

public interface INetwork {

    int chainId();

    int version();

    int wif();

    String epoch();
}
