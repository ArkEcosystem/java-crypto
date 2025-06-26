package org.arkecosystem.crypto.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AbiEncoderTest {

    private AbiEncoder encoder;

    @BeforeEach
    void setUp() throws Exception {
        encoder = new AbiEncoder();
    }

    @Test
    void it_should_encode_vote_function_call() throws Exception {
        String functionName = "vote";
        List<Object> args = Arrays.asList("0x512F366D524157BcF734546eB29a6d687B762255");
        String expectedEncodedData =
                "0x6dd7d8ea000000000000000000000000512f366d524157bcf734546eb29a6d687b762255";

        String encodedData = encoder.encodeFunctionCall(functionName, args);

        assertEquals(expectedEncodedData, encodedData);
    }
}
