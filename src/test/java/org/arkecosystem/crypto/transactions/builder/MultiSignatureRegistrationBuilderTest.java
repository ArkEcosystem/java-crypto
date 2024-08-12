package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.arkecosystem.crypto.enums.Fees;
import org.arkecosystem.crypto.identities.PublicKey;
import org.arkecosystem.crypto.transactions.Deserializer;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
                        .nonce(3)
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
        Exception thrown =
                Assertions.assertThrows(
                        RuntimeException.class,
                        () -> {
                            Deserializer deserializer =
                                    new Deserializer(
                                            "ff011e0100000004000200000000000000023efc1da7f315f3c533a4080e491f32cd4219731cef008976c3876539e1f192d30065cd1d00000000000203029fab3cb2f5e248ae7cbb4de646741da4d73c493b2a03ab5c71507fb2c0dcca9203629f9dbf7f1e91cefa845126189816ceae357bdd1f41bd14787318a7d5b55d48027941d2059f89a26d89e87d3385e261a0ede1234aaeaa487012b69d6b67962dc52b33558cdc62933ff56feb646d1f47a98104bf34b894895d5e816f86e556f87fce8485e55aa32dfa1cd86456a66a58ef7a68dff4af51e2f7fcf75b983540872e000caa6864c71362b369c71107f463f29c43c361e54260cbc54d791b7385dbe76f29d12b9befbe4f98792d7046481afcf0c156408310192a93d8413e5380438f270153a22c5ce2b1894f0a141adc19de567077d2d268f4c7e1476e9558ecb4411d486cd0d7aa3e9c7716739a055a8a1a64a162d0362645d63d13791a9876ef8b5a88021d56997f0c9e21201c59e1b7b6be8d5a609908a4f65bf266144baf5e61e3f14bc2651f62187f4ffa17c8b010fcb0fba94a04df56bc25e5cb20935ec5fb7ad632");
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

        Deserializer deserializer =
                new Deserializer(
                        "ff011e0100000004000200000000000000023efc1da7f315f3c533a4080e491f32cd4219731cef008976c3876539e1f192d30065cd1d00000000000203029fab3cb2f5e248ae7cbb4de646741da4d73c493b2a03ab5c71507fb2c0dcca9203629f9dbf7f1e91cefa845126189816ceae357bdd1f41bd14787318a7d5b55d48027941d2059f89a26d89e87d3385e261a0ede1234aaeaa487012b69d6b67962dc52b33558cdc62933ff56feb646d1f47a98104bf34b894895d5e816f86e556f87fce8485e55aa32dfa1cd86456a66a58ef7a68dff4af51e2f7fcf75b983540872e000caa6864c71362b369c71107f463f29c43c361e54260cbc54d791b7385dbe76f29d12b9befbe4f98792d7046481afcf0c156408310192a93d8413e5380438f270153a22c5ce2b1894f0a141adc19de567077d2d268f4c7e1476e9558ecb4411d486cd0d7aa3e9c7716739a055a8a1a64a162d0362645d63d13791a9876ef8b5a88021d56997f0c9e21201c59e1b7b6be8d5a609908a4f65bf266144baf5e61e3f14bc2651f62187f4ffa17c8b010fcb0fba94a04df56bc25e5cb20935ec5fb7ad632");
        Transaction actual = deserializer.deserialize();
        assertFalse(actual.multiVerify(3, Arrays.asList(key1, key2, key3)));
    }
}
