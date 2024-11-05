package org.arkecosystem.crypto.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AbiDecoderTest {

    private AbiDecoder decoder;

    @BeforeEach
    void setUp() throws Exception {
        decoder = new AbiDecoder();
    }

    @Test
    void it_should_decode_vote_payload() throws Exception {
        String functionName = "vote";
        List<Object> args = Arrays.asList("0x512F366D524157BcF734546eB29a6d687B762255");
        String data = "0x6dd7d8ea000000000000000000000000512f366d524157bcf734546eb29a6d687b762255";

        Map<String, Object> decodedData = decoder.decodeFunctionData(data);

        assertEquals(functionName, decodedData.get("functionName"));
        assertEquals(args, decodedData.get("args"));
    }
}
