package org.arkecosystem.crypto.networks

class Devnet implements INetwork {
    byte addressByte() {
        0x1e
    }

    byte wif() {
        0xaa
    }

    String epoch() {
        '2017-03-21 13:00:00'
    }
}
