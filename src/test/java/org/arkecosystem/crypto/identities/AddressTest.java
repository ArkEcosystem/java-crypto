package org.arkecosystem.crypto.identities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.arkecosystem.crypto.configuration.Network;
import org.arkecosystem.crypto.networks.Devnet;
import org.bitcoinj.core.ECKey;
import org.junit.jupiter.api.Test;

public class AddressTest {

    @Test
    public void fromPassphrase() {
        Network.set(new Devnet());
        String actual = Address.fromPassphrase("this is a top secret passphrase");
        assertEquals("D61mfSggzbvQgTUe6JhYKH2doHaqJ3Dyib", actual);
    }

    @Test
    public void fromPublicKey() {
        String actual =
                Address.fromPublicKey(
                        "034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed192");
        assertEquals("D61mfSggzbvQgTUe6JhYKH2doHaqJ3Dyib", actual);
    }

    @Test
    public void fromPrivateKey() {
        ECKey privateKey = PrivateKey.fromPassphrase("this is a top secret passphrase");
        String actual = Address.fromPrivateKey(privateKey);
        assertEquals("D61mfSggzbvQgTUe6JhYKH2doHaqJ3Dyib", actual);
    }

    @Test
    public void validate() {
        Network.set(new Devnet());
        assertTrue(Address.validate("D61mfSggzbvQgTUe6JhYKH2doHaqJ3Dyib"));
    }
}
