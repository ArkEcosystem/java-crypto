package org.arkecosystem.crypto.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void it_should_expose_decode_signed_negative_number_as_static() {
        byte[] bytes =
                Numeric.hexStringToByteArray(
                        "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");

        assertEquals("-1", AbiDecoder.decodeNumber(bytes, 0, 256, true)[0]);
    }

    @Test
    void it_should_expose_decode_string_as_static() {
        byte[] bytes =
                Numeric.hexStringToByteArray(
                        "0000000000000000000000000000000000000000000000000000000000000020"
                                + "000000000000000000000000000000000000000000000000000000000000000b"
                                + "68656c6c6f20776f726c64000000000000000000000000000000000000000000");

        Object[] result = AbiDecoder.decodeString(bytes, 0);

        assertEquals("hello world", result[0]);
        assertEquals(32, result[1]);
    }

    @Test
    void it_should_expose_decode_dynamic_bytes_as_static() {
        byte[] bytes =
                Numeric.hexStringToByteArray(
                        "0000000000000000000000000000000000000000000000000000000000000020"
                                + "0000000000000000000000000000000000000000000000000000000000000004"
                                + "deadbeef00000000000000000000000000000000000000000000000000000000");

        Object[] result = AbiDecoder.decodeDynamicBytes(bytes, 0);

        assertEquals("0xdeadbeef", result[0]);
        assertEquals(32, result[1]);
    }

    @Test
    void it_should_expose_decode_fixed_bytes_as_static() {
        byte[] bytes =
                Numeric.hexStringToByteArray(
                        "deadbeef00000000000000000000000000000000000000000000000000000000");

        Object[] result = AbiDecoder.decodeFixedBytes(bytes, 0, 4);

        assertEquals("0xdeadbeef", result[0]);
        assertEquals(32, result[1]);
    }

    @Test
    void it_should_expose_decode_fixed_array_as_static() throws Exception {
        byte[] bytes =
                Numeric.hexStringToByteArray(
                        "0000000000000000000000000000000000000000000000000000000000000001"
                                + "0000000000000000000000000000000000000000000000000000000000000002"
                                + "0000000000000000000000000000000000000000000000000000000000000003");
        Map<String, Object> param = new HashMap<>();
        param.put("type", "uint256");

        Object[] result = AbiDecoder.decodeArray(bytes, 0, param, 3);

        assertEquals(Arrays.asList("1", "2", "3"), result[0]);
    }

    @Test
    void it_should_expose_decode_dynamic_array_as_static() throws Exception {
        byte[] bytes =
                Numeric.hexStringToByteArray(
                        "0000000000000000000000000000000000000000000000000000000000000020"
                                + "0000000000000000000000000000000000000000000000000000000000000003"
                                + "000000000000000000000000000000000000000000000000000000000000000a"
                                + "0000000000000000000000000000000000000000000000000000000000000014"
                                + "000000000000000000000000000000000000000000000000000000000000001e");
        Map<String, Object> param = new HashMap<>();
        param.put("type", "uint256");

        Object[] result = AbiDecoder.decodeArray(bytes, 0, param, null);

        assertEquals(Arrays.asList("10", "20", "30"), result[0]);
    }

    @Test
    void it_should_expose_decode_static_tuple_as_static() throws Exception {
        byte[] bytes =
                Numeric.hexStringToByteArray(
                        "000000000000000000000000000000000000000000000000000000000000002a"
                                + "000000000000000000000000512f366d524157bcf734546eb29a6d687b762255");
        Map<String, Object> uintComponent = new HashMap<>();
        uintComponent.put("type", "uint256");
        uintComponent.put("name", "amount");
        Map<String, Object> addrComponent = new HashMap<>();
        addrComponent.put("type", "address");
        addrComponent.put("name", "recipient");
        Map<String, Object> param = new HashMap<>();
        param.put("components", Arrays.asList(uintComponent, addrComponent));

        Object[] result = AbiDecoder.decodeTuple(bytes, 0, param);

        Map<String, Object> tuple = (Map<String, Object>) result[0];
        assertEquals("42", tuple.get("amount"));
        assertEquals("0x512F366D524157BcF734546eB29a6d687B762255", tuple.get("recipient"));
    }

    @Test
    void it_should_throw_when_function_selector_is_not_found() {
        String unknownSelector = "deadbeef";

        Exception thrown =
                assertThrows(
                        Exception.class, () -> decoder.decodeFunctionData("0x" + unknownSelector));

        assertEquals("Function selector not found in ABI: " + unknownSelector, thrown.getMessage());
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
