package org.arkecosystem.crypto.networks

interface INetwork {
    int version()

    int wif()

    String epoch()
}
