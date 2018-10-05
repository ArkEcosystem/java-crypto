package org.arkecosystem.crypto.networks

class Testnet implements INetwork {
    int version() {
        23
    }

    int wif() {
        186
    }

    String epoch() {
        '2017-03-21 13:00:00'
    }
}
