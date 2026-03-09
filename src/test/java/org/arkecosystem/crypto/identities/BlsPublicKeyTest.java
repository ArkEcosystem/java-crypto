package org.arkecosystem.crypto.identities;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class BlsPublicKeyTest {

    @Test
    public void validBlsPublicKeyFromMnemonic() {
        assertTrue(
                BlsPublicKey.validate(
                        "b4865127896c3c5286296a7b26e7c8002586a3ecf5832bfb59e689336f1f4c75e10491b9dfaed8dfb2c2fbe22d11fa93"));
    }

    @Test
    public void validG1GeneratorPoint() {
        assertTrue(
                BlsPublicKey.validate(
                        "97f1d3a73197d7942695638c4fa9ac0fc3688c4f9774b905a14e3a3f171bac586c55e83ff97a1aeffb3af00adb22c6bb"));
    }

    @Test
    public void validSecondBlsPublicKey() {
        assertTrue(
                BlsPublicKey.validate(
                        "95af988701a6fb60e09da41d2ca1a9e0b49e43501bda4255b3ca01073f490c34102b6bbcafde6333185e9980745d72cb"));
    }

    @Test
    public void validBlsPublicKeyWith0xPrefix() {
        assertTrue(
                BlsPublicKey.validate(
                        "0xb4865127896c3c5286296a7b26e7c8002586a3ecf5832bfb59e689336f1f4c75e10491b9dfaed8dfb2c2fbe22d11fa93"));
    }

    @Test
    public void emptyString() {
        assertFalse(BlsPublicKey.validate(""));
    }

    @Test
    public void singleCharacter() {
        assertFalse(BlsPublicKey.validate("0"));
    }

    @Test
    public void nonHexString() {
        assertFalse(BlsPublicKey.validate("NOT A VALID PUBLICKEY"));
    }

    @Test
    public void invalidHexCharactersMixedWithValid() {
        assertFalse(BlsPublicKey.validate("02b5Gf"));
    }

    @Test
    public void tooShort33BytesZeroPadded() {
        assertFalse(
                BlsPublicKey.validate(
                        "000000000000000000000000000000000000000000000000000000000000000000"));
    }

    @Test
    public void secp256k1CompressedPublicKeyPrefix02() {
        assertFalse(
                BlsPublicKey.validate(
                        "02e0f7449c5588f24492c338f2bc8f7865f755b958d48edb0f2d0056e50c3fd5b7"));
    }

    @Test
    public void secp256k1CompressedPublicKeyPrefix03() {
        assertFalse(
                BlsPublicKey.validate(
                        "038c14b793cb19137e323a6d2e2a870bca2e7a493ec1153b3a95feb8a4873f8d08"));
    }

    @Test
    public void secp256k1CompressedKeyWithInvalidHex() {
        assertFalse(
                BlsPublicKey.validate(
                        "02b5Gf00d9de5a3ace28913fe78a15afcfe242926e94d9b517d06d2705b261f992"));
    }

    @Test
    public void secp256k1UncompressedLengthKeyPrefix32() {
        assertFalse(
                BlsPublicKey.validate(
                        "32337416a26d8d49ec27059bd0589c49bb474029c3627715380f4df83fb431aece"));
    }

    @Test
    public void secp256k1UncompressedLengthKeyPrefix22() {
        assertFalse(
                BlsPublicKey.validate(
                        "22337416a26d8d49ec27059bd0589c49bb474029c3627715380f4df83fb431aece"));
    }

    @Test
    public void correctLengthButInvalidPoint() {
        assertFalse(
                BlsPublicKey.validate(
                        "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001"));
    }

    @Test
    public void tooLongBy1Byte() {
        assertFalse(
                BlsPublicKey.validate(
                        "b4865127896c3c5286296a7b26e7c8002586a3ecf5832bfb59e689336f1f4c75e10491b9dfaed8dfb2c2fbe22d11fa9300"));
    }

    @Test
    public void nullInput() {
        assertFalse(BlsPublicKey.validate(null));
    }
}
