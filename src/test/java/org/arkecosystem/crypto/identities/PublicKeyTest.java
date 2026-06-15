package org.arkecosystem.crypto.identities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PublicKeyTest {
    @Test
    public void fromPassphrase() {
        String actual = PublicKey.fromPassphrase("this is a top secret passphrase");
        Assertions.assertEquals(
                "034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed192", actual);
    }

    @Test
    public void fromHex_round_trips_to_same_compressed_hex() {
        String hex = "034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed192";

        String actual = PublicKey.fromHex(hex).getPublicKeyAsHex();

        Assertions.assertEquals(hex, actual);
    }

    @Test
    public void fromHex_accepts_uncompressed_public_key() {
        String compressed = "034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed192";
        String uncompressed =
                org.arkecosystem.crypto.encoding.Hex.encode(
                        PublicKey.fromHex(compressed).getPubKeyPoint().getEncoded(false));

        String actual = PublicKey.fromHex(uncompressed).getPublicKeyAsHex();

        Assertions.assertEquals(compressed, actual);
    }

    @Test
    public void recover_round_trips_with_PrivateKey_sign() {
        byte[] message = org.bitcoinj.core.Sha256Hash.hash("Hello World".getBytes());
        byte[] signature = PrivateKey.sign(message, "this is a top secret passphrase");

        org.bitcoinj.core.ECKey recovered = PublicKey.recover(message, signature);

        Assertions.assertEquals(
                "034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed192",
                recovered.getPublicKeyAsHex());
    }

    @Test
    public void recover_throws_on_invalid_signature_length() {
        byte[] message = new byte[32];
        Assertions.assertThrows(
                IllegalArgumentException.class, () -> PublicKey.recover(message, new byte[64]));
    }
}
