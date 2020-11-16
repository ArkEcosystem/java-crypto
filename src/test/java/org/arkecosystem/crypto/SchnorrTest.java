package org.arkecosystem.crypto;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SchnorrTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/utils/test-vectors.csv", numLinesToSkip = 1)
    void schnorrSignAndVerify(String secretKey, String publickey, String message, String signature, boolean expected, String comment) {

        byte[] pubkey = Schnorr.hexStringToByteArray(publickey);
        byte[] msg = Schnorr.hexStringToByteArray(message);

        if (secretKey != null) {
            BigInteger seckeyNum = new BigInteger(secretKey, 16);
            String sig_actual = Schnorr.bytesToHex(Schnorr.schnorrSign(msg, seckeyNum));
            assertEquals(signature, sig_actual);
        }
        boolean result_actual = Schnorr.schnorrVerify(msg, pubkey, Schnorr.hexStringToByteArray(signature));
        assertEquals(expected, result_actual, comment);
    }
}
