package org.arkecosystem.crypto.signature;

import org.arkecosystem.crypto.Schnorr;
import org.arkecosystem.crypto.identities.PrivateKey;
import org.bitcoinj.core.ECKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ECDSASignerTest {

    private Signer signer;

    @BeforeEach
    void setUp() {
        signer = new ECDSASigner();
    }

    @Test
    void sign() {
        String message = "243F6A8885A308D313198A2E03707344A4093822299F31D0082EFA98EC4E6C89";
        ECKey privateKey = PrivateKey.fromPassphrase("some passphrase");
        String signature = "304402207856D22D9C1E146492117B61D83F0A8D2E046C5A75F471172689A0A0C26907C6022035A1368F38EC63BC9B7BEEC7BCE49BD2C1AE28179EE83EBE7FD9C17824E2514B";

        assertEquals(
            signature,
            Schnorr.bytesToHex(signer.sign(Schnorr.hexStringToByteArray(message), privateKey))
        );
    }
}
