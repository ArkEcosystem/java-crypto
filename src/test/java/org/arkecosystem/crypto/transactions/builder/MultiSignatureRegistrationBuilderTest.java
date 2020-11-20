package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.Fees;
import org.arkecosystem.crypto.identities.PublicKey;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
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
}
