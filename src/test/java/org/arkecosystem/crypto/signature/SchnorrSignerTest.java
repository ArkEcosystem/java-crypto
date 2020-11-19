package org.arkecosystem.crypto.signature;

import org.arkecosystem.crypto.Schnorr;
import org.arkecosystem.crypto.identities.PrivateKey;
import org.bitcoinj.core.ECKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SchnorrSignerTest {

    private Signer signer;

    @BeforeEach
    void setUp() {
        signer = new SchnorrSigner();
    }

    @Test
    void sign() {
        String message = "243F6A8885A308D313198A2E03707344A4093822299F31D0082EFA98EC4E6C89";
        ECKey privateKey = PrivateKey.fromPassphrase("some passphrase");
        String signature = "BCB684D8166A3BCC86BBABA0DF77CC6C5E86769DFF2D6377F71EC25B6AF2C983C57BEA0FE27D1F712CD0CCA0A5B09BFF15D64842DCE0EABA23834F705E73011A";

        assertEquals(
            signature,
            Schnorr.bytesToHex(signer.sign(Schnorr.hexStringToByteArray(message), privateKey))
        );
    }
}
