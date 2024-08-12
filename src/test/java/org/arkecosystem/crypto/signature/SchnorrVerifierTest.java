package org.arkecosystem.crypto.signature;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.arkecosystem.crypto.identities.PrivateKey;
import org.arkecosystem.crypto.utils.Schnorr;
import org.bitcoinj.core.ECKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SchnorrVerifierTest {

    private Verifier verifier;

    @BeforeEach
    void setUp() {
        verifier = new SchnorrVerifier();
    }

    @Test
    void verify() {
        byte[] message =
                Schnorr.hexStringToByteArray(
                        "243F6A8885A308D313198A2E03707344A4093822299F31D0082EFA98EC4E6C89");
        ECKey privateKey = PrivateKey.fromPassphrase("some passphrase");
        String signature =
                "d39f6c989c185699c2f3a8674dcdd86944f9e11debc57e179bf51de3f3604546c565a37302417ab0914aa45b46f66154fbd845f883942a4cdd8654d1aaecb5c3";

        assertTrue(verifier.verify(message, privateKey, Schnorr.hexStringToByteArray(signature)));
    }
}
