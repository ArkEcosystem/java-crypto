package org.arkecosystem.crypto.networks

import static java.lang.Integer.parseInt

abstract class AbstractNetwork {
    Integer version() {
        parseInt(this.addressByte, 16)
    }
}
