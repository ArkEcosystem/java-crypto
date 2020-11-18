package org.arkecosystem.crypto.transactions.builder;

import org.arkecosystem.crypto.enums.Fees;
import org.arkecosystem.crypto.identities.PrivateKey;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.bitcoinj.core.ECKey;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MultiSignatureRegistrationBuilderTest {

    @Test
    void build() {

        ECKey key1 = PrivateKey.fromPassphrase("secret 1");
        ECKey key2 = PrivateKey.fromPassphrase("secret 2");
        ECKey key3 = PrivateKey.fromPassphrase("secret 3");

        List<String> publicKeys = Arrays.asList(
            key1.getPublicKeyAsHex(),
            key2.getPublicKeyAsHex(),
            key3.getPublicKeyAsHex()
        );
        System.out.println(publicKeys);
//        System.out.println(Arrays.asList(key1.getPrivateKeyAsHex(), key2.getPrivateKeyAsHex(), key3.getPrivateKeyAsHex()));
        Transaction actual =
            new MultiSignatureRegistrationBuilder()
                .version(2)
                .nonce(3)
                .network(23)
                .fee(Fees.MULTI_SIGNATURE_REGISTRATION.getValue())
//                .amount(20)
                .publicKeys(publicKeys)
                .min(3)
                .multiSign("secret 1", 0)
                .multiSign("secret 2", 1)
                .multiSign("secret 3", 2)
                .sign("secret 1")
                .transaction;

        assertTrue(actual.verify());

        HashMap actualHashMap = actual.toHashMap();

        assertNotNull(actualHashMap.get("asset"));
        HashMap actualAsset = (HashMap) actualHashMap.get("asset");

        assertNotNull(actualAsset.get("multisignature"));
        HashMap actualMultisignature = (HashMap) actualAsset.get("multisignature");

        byte actualMin = (byte) actualMultisignature.get("min");
        List<String> actualPublicKeys = (List<String>) actualMultisignature.get("publicKeys");

        assertEquals(publicKeys, actualPublicKeys);
        assertEquals(3, actualMin);
    }
}
