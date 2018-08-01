package org.arkecosystem.crypto.networks

interface INetwork {
    byte addressByte()
    byte wif()
    String epoch()
}
