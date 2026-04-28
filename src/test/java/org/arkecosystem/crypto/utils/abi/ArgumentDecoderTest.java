package org.arkecosystem.crypto.utils.abi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ArgumentDecoderTest {

    @Test
    void it_should_decode_string() {
        String payload =
                "0000000000000000000000000000000000000000000000000000000000000020"
                        + "000000000000000000000000000000000000000000000000000000000000000D"
                        + "48656C6C6F2C20776F726C642100000000000000000000000000000000000000";

        ArgumentDecoder decoder = new ArgumentDecoder(payload);

        assertEquals("Hello, world!", decoder.decodeString());
    }

    @Test
    void it_should_decode_address() {
        String payload = "000000000000000000000000512F366D524157BcF734546eB29a6d687B762255";

        ArgumentDecoder decoder = new ArgumentDecoder(payload);

        assertEquals("0x512F366D524157BcF734546eB29a6d687B762255", decoder.decodeAddress());
    }

    @Test
    void it_should_decode_unsigned_int() {
        String payload = "000000000000000000000000000000000000000000000000016345785d8a0000";

        ArgumentDecoder decoder = new ArgumentDecoder(payload);

        assertEquals("100000000000000000", decoder.decodeUnsignedInt());
    }

    @Test
    void it_should_decode_signed_int() {
        String payload = "000000000000000000000000000000000000000000000000016345785d8a0000";

        ArgumentDecoder decoder = new ArgumentDecoder(payload);

        assertEquals("100000000000000000", decoder.decodeSignedInt());
    }

    @Test
    void it_should_decode_negative_signed_int() {
        String payload = "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";

        ArgumentDecoder decoder = new ArgumentDecoder(payload);

        assertEquals("-1", decoder.decodeSignedInt());
    }

    @Test
    void it_should_decode_bool_as_true() {
        String payload = "0000000000000000000000000000000000000000000000000000000000000001";

        ArgumentDecoder decoder = new ArgumentDecoder(payload);

        assertTrue(decoder.decodeBool());
    }

    @Test
    void it_should_decode_bool_as_false() {
        String payload = "0000000000000000000000000000000000000000000000000000000000000000";

        ArgumentDecoder decoder = new ArgumentDecoder(payload);

        assertFalse(decoder.decodeBool());
    }
}
