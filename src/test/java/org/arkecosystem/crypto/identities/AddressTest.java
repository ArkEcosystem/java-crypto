package org.arkecosystem.crypto.identities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.bitcoinj.core.ECKey;
import org.junit.jupiter.api.Test;

public class AddressTest {

    @Test
    public void fromPassphrase() {
        String actual = Address.fromPassphrase("this is a top secret passphrase");
        assertEquals("0xb0FF9213f7226bBB72b84dE16af86e56f1f38B01", actual);
    }

    @Test
    public void fromPublicKey() {
        String actual =
                Address.fromPublicKey(
                        "034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed192");
        assertEquals("0xb0FF9213f7226bBB72b84dE16af86e56f1f38B01", actual);
    }

    @Test
    public void fromPrivateKey() {
        ECKey privateKey = PrivateKey.fromPassphrase("this is a top secret passphrase");
        String actual = Address.fromPrivateKey(privateKey);
        assertEquals("0xb0FF9213f7226bBB72b84dE16af86e56f1f38B01", actual);
    }

    @Test
    public void validate_accepts_valid_checksum_address() {
        assertTrue(Address.validate("0xb0FF9213f7226bBB72b84dE16af86e56f1f38B01"));
    }

    @Test
    public void validate_accepts_lowercase_address() {
        assertTrue(Address.validate("0xb0ff9213f7226bbb72b84de16af86e56f1f38b01"));
    }

    @Test
    public void validate_rejects_missing_prefix() {
        assertFalse(Address.validate("b0FF9213f7226bBB72b84dE16af86e56f1f38B01"));
    }

    @Test
    public void validate_rejects_wrong_length() {
        assertFalse(Address.validate("0xb0FF9213f7226bBB72b84dE16af86e56f1f38B0"));
        assertFalse(Address.validate("0xb0FF9213f7226bBB72b84dE16af86e56f1f38B011"));
    }

    @Test
    public void validate_rejects_non_hex_characters() {
        assertFalse(Address.validate("0xb0FF9213f7226bBB72b84dE16af86e56f1f38BZZ"));
    }

    @Test
    public void validate_rejects_null() {
        assertFalse(Address.validate(null));
    }
}
