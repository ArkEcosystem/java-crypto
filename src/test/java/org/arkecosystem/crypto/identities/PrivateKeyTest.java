package org.arkecosystem.crypto.identities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import org.arkecosystem.crypto.configuration.Network;
import org.arkecosystem.crypto.networks.Devnet;
import org.arkecosystem.crypto.networks.Mainnet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PrivateKeyTest {

    @BeforeEach
    void setUp() {
        Network.set(new Devnet());
    }

    @AfterEach
    void tearDown() {
        Network.set(new Devnet());
    }

    @Test
    public void fromPassphrase() {
        String actual =
                PrivateKey.fromPassphrase("this is a top secret passphrase").getPrivateKeyAsHex();
        assertEquals("d8839c2432bfd0a67ef10a804ba991eabba19f154a3d707917681d45822a5712", actual);
    }

    @Test
    public void fromHex() {
        String actual =
                PrivateKey.fromHex(
                                "d8839c2432bfd0a67ef10a804ba991eabba19f154a3d707917681d45822a5712")
                        .getPrivateKeyAsHex();
        assertEquals("d8839c2432bfd0a67ef10a804ba991eabba19f154a3d707917681d45822a5712", actual);
    }

    @Test
    public void fromWif_round_trips_with_fromPassphrase() throws IOException {
        String wif = WIF.fromPassphrase("this is a top secret passphrase");

        String fromWif = PrivateKey.fromWif(wif).getPrivateKeyAsHex();
        String fromPassphrase =
                PrivateKey.fromPassphrase("this is a top secret passphrase").getPrivateKeyAsHex();

        assertEquals(fromPassphrase, fromWif);
    }

    @Test
    public void sign_produces_a_65_byte_signature_recoverable_to_the_signer_public_key() {
        byte[] message = org.bitcoinj.core.Sha256Hash.hash("payload".getBytes());

        byte[] signature = PrivateKey.sign(message, "this is a top secret passphrase");

        assertEquals(65, signature.length);
        assertEquals(
                "034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed192",
                PublicKey.recover(message, signature).getPublicKeyAsHex());
    }

    @Test
    public void sign_with_eckey_overload_matches_passphrase_overload() {
        byte[] message = org.bitcoinj.core.Sha256Hash.hash("payload".getBytes());
        org.bitcoinj.core.ECKey key = PrivateKey.fromPassphrase("this is a top secret passphrase");

        byte[] viaPassphrase = PrivateKey.sign(message, "this is a top secret passphrase");
        byte[] viaEcKey = PrivateKey.sign(message, key);

        assertEquals(
                org.arkecosystem.crypto.encoding.Hex.encode(viaPassphrase),
                org.arkecosystem.crypto.encoding.Hex.encode(viaEcKey));
    }

    @Test
    public void fromWif_rejects_when_version_byte_belongs_to_another_network() throws IOException {
        String mainnetWif;
        try {
            Network.set(new Mainnet());
            mainnetWif = WIF.fromPassphrase("this is a top secret passphrase");
        } finally {
            Network.set(new Devnet());
        }

        // Mainnet and Devnet share the same wif byte (170) so this branch only
        // triggers when the prefix differs, e.g. against testnet (186).
        Network.set(new org.arkecosystem.crypto.networks.Testnet());
        try {
            assertThrows(IllegalArgumentException.class, () -> PrivateKey.fromWif(mainnetWif));
        } finally {
            Network.set(new Devnet());
        }
    }
}
