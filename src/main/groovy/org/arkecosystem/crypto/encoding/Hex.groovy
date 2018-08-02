package org.arkecosystem.crypto.encoding

import static com.google.common.io.BaseEncoding.base16

class Hex {
    static String encode(byte[] value) {
        base16().lowerCase().encode value
    }

    static byte[] decode(String value) {
        base16().lowerCase().decode value
    }
}
