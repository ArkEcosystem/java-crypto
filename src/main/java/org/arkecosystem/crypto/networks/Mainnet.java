package org.arkecosystem.crypto.networks

class Mainnet implements INetwork {
    int version() {
        23
    }

    int wif() {
        170
    }

    String epoch() {
        '2017-03-21 13:00:00'
    }
}
