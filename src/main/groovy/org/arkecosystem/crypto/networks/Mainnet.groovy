package org.arkecosystem.crypto.networks

class Mainnet implements INetwork {
    byte addressByte() {
        0x17
    }

    byte wif() {
        0xaa
    }

    String epoch() {
        '2017-03-21T13:00:00.000Z'
    }
}
