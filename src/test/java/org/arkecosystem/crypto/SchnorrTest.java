package org.arkecosystem.crypto;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SchnorrTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/utils/test-vectors.csv", numLinesToSkip = 1)
    void name(String index, String seckey, String publickey, String message, String sig, boolean result, String comment) {

        byte[] pubkey = Schnorr.hexStringToByteArray(publickey);
        byte[] msg = Schnorr.hexStringToByteArray(message);

        if (seckey != null) {
            BigInteger seckeyNum = new BigInteger(seckey, 16);
            String sig_actual = Schnorr.bytesToHex(Schnorr.schnorr_sign(msg, seckeyNum));
            assertEquals(sig, sig_actual);
        }
        boolean result_actual = Schnorr.schnorr_verify(msg, pubkey, Schnorr.hexStringToByteArray(sig));
        assertEquals(result, result_actual, comment);
    }
}
