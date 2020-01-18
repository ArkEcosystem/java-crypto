package org.arkecosystem.crypto.identities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PrivateKeyTest {

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
}
