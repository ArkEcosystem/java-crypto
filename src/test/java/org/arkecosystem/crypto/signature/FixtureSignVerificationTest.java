package org.arkecosystem.crypto.signature;

import com.google.gson.internal.LinkedTreeMap;
import org.arkecosystem.crypto.identities.PrivateKey;
import org.arkecosystem.crypto.transactions.Deserializer;
import org.arkecosystem.crypto.transactions.FixtureLoader;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.bitcoinj.core.ECKey;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FixtureSignVerificationTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "transactions/v2-ecdsa/second-signature-registration",
        "transactions/v2-ecdsa/transfer-with-vendor-field-secondSign",
        "transactions/v2-ecdsa/transfer-secondSign",
        "transactions/v2-ecdsa/transfer-sign",
        "transactions/v2-ecdsa/multi-payment-secondSign",
        "transactions/v2-ecdsa/delegate-resignation-secondSign",
        "transactions/v2-ecdsa/htlc-claim-sign",
        "transactions/v2-ecdsa/htlc-claim-secondSign",
        "transactions/v2-ecdsa/htlc-lock-sign",
        "transactions/v2-ecdsa/htlc-refund-secondSign",
        "transactions/v2-ecdsa/delegate-registration-sign",
        "transactions/v2-ecdsa/vote-sign",
        "transactions/v2-ecdsa/multi-payment-sign",
        "transactions/v2-ecdsa/multi-payment-with-vendor-field-sign",
        "transactions/v2-ecdsa/delegate-registration-secondSign",
        "transactions/v2-ecdsa/ipfs-secondSign",
        "transactions/v2-ecdsa/htlc-lock-secondSign",
        "transactions/v2-ecdsa/unvote-sign",
        "transactions/v2-ecdsa/vote-secondSign",
        "transactions/v2-ecdsa/delegate-resignation-sign",
        "transactions/v2-ecdsa/htlc-lock-with-vendor-field-sign",
        "transactions/v2-ecdsa/ipfs-sign",
        "transactions/v2-ecdsa/htlc-lock-with-vendor-field-secondSign",
        "transactions/v2-ecdsa/unvote-secondSign",
        "transactions/v2-ecdsa/transfer-with-vendor-field-sign",
        "transactions/v2-ecdsa/multi-payment-with-vendor-field-secondSign",
        "transactions/v2-ecdsa/htlc-refund-sign",
    })
    void checkEcdsa(String file) {
        LinkedTreeMap<String, Object> fixture = FixtureLoader.load(file);

        Transaction actual = new Deserializer(fixture.get("serialized").toString()).deserialize();

        assertTrue(actual.verify());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "transactions/v2-schnorr/second-signature-registration",
        "transactions/v2-schnorr/transfer-with-vendor-field-secondSign",
        "transactions/v2-schnorr/transfer-secondSign",
        "transactions/v2-schnorr/multi-signature-registration",
        "transactions/v2-schnorr/transfer-sign",
        "transactions/v2-schnorr/multi-payment-secondSign",
        "transactions/v2-schnorr/delegate-resignation-secondSign",
        "transactions/v2-schnorr/htlc-claim-sign",
        "transactions/v2-schnorr/htlc-claim-secondSign",
        "transactions/v2-schnorr/htlc-lock-sign",
        "transactions/v2-schnorr/htlc-refund-secondSign",
        "transactions/v2-schnorr/delegate-registration-sign",
        "transactions/v2-schnorr/vote-sign",
        "transactions/v2-schnorr/multi-payment-sign",
        "transactions/v2-schnorr/multi-payment-with-vendor-field-sign",
        "transactions/v2-schnorr/delegate-registration-secondSign",
        "transactions/v2-schnorr/ipfs-secondSign",
        "transactions/v2-schnorr/htlc-lock-secondSign",
        "transactions/v2-schnorr/unvote-sign",
        "transactions/v2-schnorr/vote-secondSign",
        "transactions/v2-schnorr/delegate-resignation-sign",
        "transactions/v2-schnorr/htlc-lock-with-vendor-field-sign",
        "transactions/v2-schnorr/ipfs-sign",
        "transactions/v2-schnorr/htlc-lock-with-vendor-field-secondSign",
        "transactions/v2-schnorr/unvote-secondSign",
        "transactions/v2-schnorr/transfer-with-vendor-field-sign",
        "transactions/v2-schnorr/multi-payment-with-vendor-field-secondSign",
        "transactions/v2-schnorr/htlc-refund-sign",
    })
    void checkSchnorrSignature(String file) {
        LinkedTreeMap<String, Object> fixture = FixtureLoader.load(file);

        Transaction actual = new Deserializer(fixture.get("serialized").toString()).deserialize();

        if (actual.signature != null)
            assertTrue(actual.verify());

        if (actual.secondSignature != null) {
            ECKey secondPublicKey = PrivateKey.fromPassphrase("this is a top secret second passphrase");
            assertTrue(actual.secondVerify(secondPublicKey.getPublicKeyAsHex()));
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "transactions/v2-schnorr/htlc-lock-multiSign",
        "transactions/v2-schnorr/htlc-claim-multiSign",
        "transactions/v2-schnorr/htlc-refund-multiSign",
        "transactions/v2-schnorr/ipfs-multiSign",
        "transactions/v2-schnorr/htlc-lock-with-vendor-field-multiSign",
        "transactions/v2-schnorr/vote-multiSign",
        "transactions/v2-schnorr/multi-payment-multiSign",
        "transactions/v2-schnorr/transfer-with-vendor-field-multiSign",
        "transactions/v2-schnorr/unvote-multiSign",
        "transactions/v2-schnorr/delegate-registration-multiSign",
        "transactions/v2-schnorr/transfer-multiSign",
        "transactions/v2-schnorr/multi-payment-with-vendor-field-multiSign",
        "transactions/v2-schnorr/delegate-resignation-multiSign",
    })
    void checkSchnorrMultiSignature(String file) {
        ECKey key1 = PrivateKey.fromPassphrase("this is a top secret passphrase 1");
        ECKey key2 = PrivateKey.fromPassphrase("this is a top secret passphrase 2");
        ECKey key3 = PrivateKey.fromPassphrase("this is a top secret passphrase 3");

        LinkedTreeMap<String, Object> fixture = FixtureLoader.load(file);

        Transaction actual = new Deserializer(fixture.get("serialized").toString()).deserialize();

        List<String> publicKeys = Arrays.asList(
            key1.getPublicKeyAsHex(),
            key2.getPublicKeyAsHex(),
            key3.getPublicKeyAsHex()
        );

        if (actual.signatures != null)
            assertTrue(actual.multiVerify(3, publicKeys));
    }

}
