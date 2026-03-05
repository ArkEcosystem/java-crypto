package org.arkecosystem.crypto.identities;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class BlsPublicKeyTest {

    @Test
    public void validBlsPublicKey() {
        assertTrue(
                BlsPublicKey.validate(
                        "b4865127896c3c5286296a7b26e7c8002586a3ecf5832bfb59e689336f1f4c75e10491b9dfaed8dfb2c2fbe22d11fa93"));
    }

    @Test
    public void validBlsPublicKeyWith0xPrefix() {
        assertTrue(
                BlsPublicKey.validate(
                        "0xb4865127896c3c5286296a7b26e7c8002586a3ecf5832bfb59e689336f1f4c75e10491b9dfaed8dfb2c2fbe22d11fa93"));
    }

    @Test
    public void validG1GeneratorPoint() {
        assertTrue(
                BlsPublicKey.validate(
                        "97f1d3a73197d7942695638c4fa9ac0fc3688c4f9774b905a14e3a3f171bac586c55e83ff97a1aeffb3af00adb22c6bb"));
    }

    @Test
    public void invalidLengthTooShort() {
        assertFalse(BlsPublicKey.validate("b4865127896c3c5286296a7b26e7c800"));
    }

    @Test
    public void invalidLengthTooLong() {
        assertFalse(
                BlsPublicKey.validate(
                        "b4865127896c3c5286296a7b26e7c8002586a3ecf5832bfb59e689336f1f4c75e10491b9dfaed8dfb2c2fbe22d11fa9300"));
    }

    @Test
    public void invalidHexCharacters() {
        assertFalse(
                BlsPublicKey.validate(
                        "zz865127896c3c5286296a7b26e7c8002586a3ecf5832bfb59e689336f1f4c75e10491b9dfaed8dfb2c2fbe22d11fa93"));
    }

    @Test
    public void invalidPointNotOnCurve() {
        assertFalse(
                BlsPublicKey.validate(
                        "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001"));
    }

    @Test
    public void emptyString() {
        assertFalse(BlsPublicKey.validate(""));
    }
}
