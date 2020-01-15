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
    public void fromHex() {
        String actual =
                PrivateKey.fromHex(
                                "d8839c2432bfd0a67ef10a804ba991eabba19f154a3d707917681d45822a5712")
                        .getPrivateKeyAsHex();
        Assertions.assertEquals(
                "d8839c2432bfd0a67ef10a804ba991eabba19f154a3d707917681d45822a5712", actual);
    }
}
