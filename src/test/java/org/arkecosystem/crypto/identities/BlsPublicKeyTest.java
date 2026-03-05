package org.arkecosystem.crypto.identities;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class BlsPublicKeyTest {

    @ParameterizedTest
    @ValueSource(
            strings = {
                "b4865127896c3c5286296a7b26e7c8002586a3ecf5832bfb59e689336f1f4c75e10491b9dfaed8dfb2c2fbe22d11fa93",
                "97f1d3a73197d7942695638c4fa9ac0fc3688c4f9774b905a14e3a3f171bac586c55e83ff97a1aeffb3af00adb22c6bb",
                "95af988701a6fb60e09da41d2ca1a9e0b49e43501bda4255b3ca01073f490c34102b6bbcafde6333185e9980745d72cb",
            })
    public void validBlsPublicKeys(String key) {
        assertTrue(BlsPublicKey.validate(key));
    }

    @Test
    public void validBlsPublicKeyWith0xPrefix() {
        assertTrue(
                BlsPublicKey.validate(
                        "0xb4865127896c3c5286296a7b26e7c8002586a3ecf5832bfb59e689336f1f4c75e10491b9dfaed8dfb2c2fbe22d11fa93"));
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "",
                "0",
                "02b5Gf",
                "NOT A VALID PUBLICKEY",
                "000000000000000000000000000000000000000000000000000000000000000000",
                "02b5Gf00d9de5a3ace28913fe78a15afcfe242926e94d9b517d06d2705b261f992",
                "02e0f7449c5588f24492c338f2bc8f7865f755b958d48edb0f2d0056e50c3fd5b7",
                "026f969d90fd494b04913eda9e0cf23f66eea5a70dfd5fb3e48f393397421c2b02",
                "038c14b793cb19137e323a6d2e2a870bca2e7a493ec1153b3a95feb8a4873f8d08",
                "32337416a26d8d49ec27059bd0589c49bb474029c3627715380f4df83fb431aece",
                "22337416a26d8d49ec27059bd0589c49bb474029c3627715380f4df83fb431aece",
                "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001",
                "b4865127896c3c5286296a7b26e7c8002586a3ecf5832bfb59e689336f1f4c75e10491b9dfaed8dfb2c2fbe22d11fa9300",
            })
    public void invalidBlsPublicKeys(String key) {
        assertFalse(BlsPublicKey.validate(key));
    }
}
