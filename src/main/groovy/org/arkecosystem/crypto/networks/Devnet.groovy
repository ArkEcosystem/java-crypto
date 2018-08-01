package org.arkecosystem.crypto.networks

class Devnet implements INetwork {
    int version() {
        30
    }

    int wif() {
        170
    }

    String epoch() {
        '2017-03-21 13:00:00'
    }
}
