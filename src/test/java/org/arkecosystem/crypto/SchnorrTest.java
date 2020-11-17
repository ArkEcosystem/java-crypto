package org.arkecosystem.crypto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

    @Test
    public void addPointNoP1() {
        BigInteger[] p2 = {
            new BigInteger("1"), new BigInteger("2"),
        };

        assertEquals(p2, Schnorr.addPoint(null, p2));
    }

    @Test
    public void addPointNoP2() {
        BigInteger[] p1 = {
            new BigInteger("1"), new BigInteger("2"),
        };

        assertEquals(p1, Schnorr.addPoint(p1, null));
    }

    @Test
    public void bytesToPointInvalidBytes() {
        byte[] bytes = new byte[] { 1 };

        assertNull(Schnorr.bytesToPoint(bytes));
    }

    @Test
    public void cannotSignMessageIfNot32BytesLong() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            byte[] bytes = new byte[] { 1 };

            Schnorr.schnorrSign(bytes, new BigInteger("1"));
        });
    }

    @Test
    public void cannotSignMessageIfSecKeyZero() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            byte[] bytes = Schnorr.hexStringToByteArray("243F6A8885A308D313198A2E03707344A4093822299F31D0082EFA98EC4E6C89");

            Schnorr.schnorrSign(bytes, new BigInteger("-1"));
        });
    }

    @Test
    public void cannotVerifyIfMessageNot32BytesLong() {
        Exception thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            byte[] message = Schnorr.hexStringToByteArray("01");
            byte[] publicKey = Schnorr.hexStringToByteArray("02DFF1D77F2A671C5F36183726DB2341BE58FEAE1DA2DECED843240F7B502BA659");
            byte[] signature = Schnorr.hexStringToByteArray("787A848E71043D280C50470E8E1532B2DD5D20EE912A45DBDD2BD1DFBF187EF67031A98831859DC34DFFEEDDA86831842CCD0079E1F92AF177F7F22CC1DCED05");

            Schnorr.schnorrVerify(message, publicKey, signature);
        });
        assertEquals("The message must be a 32-byte array.", thrown.getMessage());
    }

    @Test
    public void cannotVerifyIfPublicKeyNot33BytesLong() {
        Exception thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            byte[] message = Schnorr.hexStringToByteArray("243F6A8885A308D313198A2E03707344A4093822299F31D0082EFA98EC4E6C89");
            byte[] publicKey = Schnorr.hexStringToByteArray("00");
            byte[] signature = Schnorr.hexStringToByteArray("787A848E71043D280C50470E8E1532B2DD5D20EE912A45DBDD2BD1DFBF187EF67031A98831859DC34DFFEEDDA86831842CCD0079E1F92AF177F7F22CC1DCED05");

            Schnorr.schnorrVerify(message, publicKey, signature);
        });
        assertEquals("The public key must be a 33-byte array.", thrown.getMessage());
    }

    @Test
    public void cannotVerifyIfSignatureNot64BytesLong() {
        Exception thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            byte[] message = Schnorr.hexStringToByteArray("243F6A8885A308D313198A2E03707344A4093822299F31D0082EFA98EC4E6C89");
            byte[] publicKey = Schnorr.hexStringToByteArray("02DFF1D77F2A671C5F36183726DB2341BE58FEAE1DA2DECED843240F7B502BA659");
            byte[] signature = Schnorr.hexStringToByteArray("00");

            Schnorr.schnorrVerify(message, publicKey, signature);
        });
        assertEquals("The signature must be a 64-byte array.", thrown.getMessage());

    }

}
