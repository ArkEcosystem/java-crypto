package org.arkecosystem.crypto.identities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.bitcoinj.core.ECKey;
import org.junit.jupiter.api.Test;

public class LegacyAddressTest {

    private static final int PUB_KEY_HASH = 30;
    private static final String PASSPHRASE =
            "enact busy minimum fantasy endless shoot reduce few inject ostrich snow promote";
    private static final String PUBLIC_KEY =
            "02a7c5ca78f6abbced169cb883aec3ffc0a0950affc0de575fb211873b5846e668";
    private static final String PRIVATE_KEY =
            "c7a0df6e1c42268946af49af28c49c6da64419f0203fa970b6e9be9f85a44875";
    private static final String ADDRESS = "D6WFwqYDRiFkSf4ezzWRt3jCsUp2sRmDMi";

    @Test
    public void fromPassphrase() {
        assertEquals(ADDRESS, LegacyAddress.fromPassphrase(PASSPHRASE, PUB_KEY_HASH));
    }

    @Test
    public void fromPublicKey() {
        assertEquals(ADDRESS, LegacyAddress.fromPublicKey(PUBLIC_KEY, PUB_KEY_HASH));
    }

    @Test
    public void fromPrivateKey() {
        ECKey privateKey = PrivateKey.fromHex(PRIVATE_KEY);
        assertEquals(ADDRESS, LegacyAddress.fromPrivateKey(privateKey, PUB_KEY_HASH));
    }

    @Test
    public void validate() {
        assertTrue(LegacyAddress.validate(ADDRESS, PUB_KEY_HASH));
    }

    @Test
    public void validateFailsWithIncorrectPubKeyHash() {
        assertFalse(LegacyAddress.validate(ADDRESS, 32));
    }

    @Test
    public void validateFailsWithInvalidAddress() {
        assertFalse(LegacyAddress.validate("D2WFnqYDRiFkSf4ezzWRt3jCsUp2sRmDMifwd", PUB_KEY_HASH));
    }

    @Test
    public void validateFailsWithDecodingError() {
        assertFalse(LegacyAddress.validate("invalid", PUB_KEY_HASH));
    }
}
