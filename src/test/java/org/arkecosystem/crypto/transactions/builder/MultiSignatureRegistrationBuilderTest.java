package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.Fees;
import org.arkecosystem.crypto.identities.PublicKey;
import org.arkecosystem.crypto.transactions.Deserializer;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MultiSignatureRegistrationBuilderTest {

    @Test
    @SuppressWarnings("unchecked")
    void build() {

        String key1 = PublicKey.fromPassphrase("secret 1");
        String key2 = PublicKey.fromPassphrase("secret 2");
        String key3 = PublicKey.fromPassphrase("secret 3");

        List<String> publicKeys = Arrays.asList(key1, key2, key3);

        Transaction actual =
            new MultiSignatureRegistrationBuilder()
                .version(2)
                .nonce(3)
                .network(23)
                .fee(Fees.MULTI_SIGNATURE_REGISTRATION.getValue())
                .publicKeys(publicKeys)
                .min(3)
                .multiSign("secret 1", 0)
                .multiSign("secret 2", 1)
                .multiSign("secret 3", 2)
                .sign("secret 1")
                .transaction;

        assertTrue(actual.verify());
        assertTrue(actual.multiVerify(3, publicKeys));

        HashMap actualHashMap = actual.toHashMap();

        assertNotNull(actualHashMap.get("asset"));
        HashMap actualAsset = (HashMap) actualHashMap.get("asset");

        assertNotNull(actualAsset.get("multiSignature"));
        HashMap actualMultisignature = (HashMap) actualAsset.get("multiSignature");

        byte actualMin = (byte) actualMultisignature.get("min");
        List<String> actualPublicKeys = (List<String>) actualMultisignature.get("publicKeys");

        assertEquals(publicKeys, actualPublicKeys);
        assertEquals(3, actualMin);
    }

    @Test
    void checkMultiSignaturePassingInvalidMultiSignatureAsset() {
        Exception thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            Deserializer deserializer = new Deserializer("ff0217010000000000010000000000000003fb92a2c3efaa177d8a51fc0cdf41905098d8b2cd900cbac94617492827e5f01580969800000000000000c2eb0b0000000000000000170995750207ecaf0ccf251c1265b92ad84f55366200c01c44bf33bea20a74d5acc12c5d6aafe82240f3571121382b77c871f4b33d6da2b62fdc6ca2cc6bc583abb2a69e7975be29e8d80a59c52bcff8d54514cf999e01830a2e319f2070f3519bc22c2d449acdb7691bf9dd25e3649a72ac843e08ce26e0b87e30e65b73f853cf8e375dbc495cc75c6fd199bf4b15327bf8c5ec4bfac50292c299b739f0cb5e36133d85f51fff2fcc7745e8af4d0778908560c0874e3f2303e0436eabce1c09d88efab0004e61c5d6e47768aa8d2ab0cb9f14d523a38308");
            Transaction actual = deserializer.deserialize();
            assertTrue(actual.multiVerify(3, Collections.emptyList()));
        });
        assertEquals("The multi signature asset is invalid.", thrown.getMessage());
    }

    @Test
    void checkMultiSignaturePassingInvalidMultiSignatureAssets() {
        String key1 = PublicKey.fromPassphrase("this is a top secret passphrase 1");
        String key2 = PublicKey.fromPassphrase("this is a top secret passphrase 2");
        String key3 = PublicKey.fromPassphrase("this is a top secret passphrase 3");

        Deserializer deserializer = new Deserializer("ff0217010000000000010000000000000003fb92a2c3efaa177d8a51fc0cdf41905098d8b2cd900cbac94617492827e5f01580969800000000000000c2eb0b0000000000000000170995750207ecaf0ccf251c1265b92ad84f55366200c01c44bf33bea20a74d5acc12c5d6aafe82240f3571121382b77c871f4b33d6da2b62fdc6ca2cc6bc583abb2a69e7975be29e8d80a59c52bcff8d54514cf999e0192c299b739f0cb5e36133d85f51fff2fcc7745e8af4d0778908560c0874e3f2303e0436eabce1c09d88efab0004e61c5d6e47768aa8d2ab0cb9f14d523a3830802830a2e319f2070f3519bc22c2d449acdb7691bf9dd25e3649a72ac843e08ce26e0b87e30e65b73f853cf8e375dbc495cc75c6fd199bf4b15327bf8c5ec4bfac5");
        Transaction actual = deserializer.deserialize();
        assertFalse(actual.multiVerify(3, Arrays.asList(key1, key2, key3)));
    }
}
