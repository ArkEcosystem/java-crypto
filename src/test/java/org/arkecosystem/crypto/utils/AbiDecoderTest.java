package org.arkecosystem.crypto.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.web3j.utils.Numeric;

class AbiDecoderTest {

    private AbiDecoder decoder;

    @BeforeEach
    void setUp() throws Exception {
        decoder = new AbiDecoder();
    }

    @Test
    void it_should_expose_decode_address_as_static() {
        byte[] bytes =
                Numeric.hexStringToByteArray(
                        "000000000000000000000000512f366d524157bcf734546eb29a6d687b762255");
        Object[] result = AbiDecoder.decodeAddress(bytes, 0);

        assertEquals("0x512F366D524157BcF734546eB29a6d687B762255", result[0]);
        assertEquals(32, result[1]);
    }

    @Test
    void it_should_expose_decode_bool_as_static() {
        byte[] truthy =
                Numeric.hexStringToByteArray(
                        "0000000000000000000000000000000000000000000000000000000000000001");
        byte[] falsy =
                Numeric.hexStringToByteArray(
                        "0000000000000000000000000000000000000000000000000000000000000000");

        assertEquals(true, AbiDecoder.decodeBool(truthy, 0)[0]);
        assertEquals(false, AbiDecoder.decodeBool(falsy, 0)[0]);
    }

    @Test
    void it_should_expose_decode_number_as_static() {
        byte[] bytes =
                Numeric.hexStringToByteArray(
                        "00000000000000000000000000000000000000000000000000000000000000ff");

        assertEquals("255", AbiDecoder.decodeNumber(bytes, 0, 256, false)[0]);
    }

    @Test
    void it_should_expose_read_uint_as_static() {
        byte[] bytes =
                Numeric.hexStringToByteArray(
                        "0000000000000000000000000000000000000000000000000000000000000020");

        assertEquals(BigInteger.valueOf(32), AbiDecoder.readUInt(bytes, 0));
    }

    @Test
    void it_should_decode_vote_payload() throws Exception {
        String functionName = "vote";
        List<Object> args = Arrays.asList("0x512F366D524157BcF734546eB29a6d687B762255");
        String data = "0x6dd7d8ea000000000000000000000000512f366d524157bcf734546eb29a6d687b762255";

        Map<String, Object> decodedData = decoder.decodeFunctionData(data);

        Map<String, Object> expectedData = new HashMap<>();
        expectedData.put("functionName", functionName);
        expectedData.put("args", args);

        assertEquals(expectedData, decodedData);
    }
}
