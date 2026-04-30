package org.arkecosystem.crypto.utils.abi;

import org.arkecosystem.crypto.utils.AbiDecoder;
import org.web3j.utils.Numeric;

public class ArgumentDecoder {

    private final byte[] bytes;

    public ArgumentDecoder(String hex) {
        this.bytes = Numeric.hexStringToByteArray(hex);
    }

    public String decodeString() {
        return (String) AbiDecoder.decodeString(bytes, 0)[0];
    }

    public String decodeAddress() {
        return (String) AbiDecoder.decodeAddress(bytes, 0)[0];
    }

    public String decodeUnsignedInt() {
        return (String) AbiDecoder.decodeNumber(bytes, 0, 256, false)[0];
    }

    public String decodeSignedInt() {
        return (String) AbiDecoder.decodeNumber(bytes, 0, 256, true)[0];
    }

    public boolean decodeBool() {
        return (boolean) AbiDecoder.decodeBool(bytes, 0)[0];
    }
}
