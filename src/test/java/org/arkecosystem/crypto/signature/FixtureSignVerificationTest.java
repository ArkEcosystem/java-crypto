package org.arkecosystem.crypto.signature;

import com.google.gson.internal.LinkedTreeMap;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.identities.PublicKey;
import org.arkecosystem.crypto.transactions.Deserializer;
import org.arkecosystem.crypto.transactions.FixtureLoader;
import org.arkecosystem.crypto.transactions.Serializer;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FixtureSignVerificationTest {

    private final String passphrase = "this is a top secret passphrase";
    private final String secondPassphrase = "this is a top secret second passphrase";
    private final String musigPassphrase1 = "this is a top secret passphrase 1";
    private final String musigPassphrase2 = "this is a top secret passphrase 2";
    private final String musigPassphrase3 = "this is a top secret passphrase 3";

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
    void checkSchnorrSignature(String file) {
        LinkedTreeMap<String, Object> fixture = FixtureLoader.load(file);

        Transaction actual = new Deserializer(fixture.get("serialized").toString()).deserialize();

        if (actual.signature != null)
            assertTrue(actual.verify());

        if (actual.secondSignature != null) {
            checkSecondSignature(actual);
        }

        if (actual.signatures != null) {
            checkMultiSignature(actual);
        }
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
    void checkSigningAgainProducesSameSignature(String file) {
        LinkedTreeMap<String, Object> fixture = FixtureLoader.load(file);

        Transaction actual = new Deserializer(fixture.get("serialized").toString()).deserialize();

        // Remove the signatures from original transaction
        Transaction withoutSignatures = new Deserializer(
            Hex.encode(Serializer.serialize(actual, true, true, true))
        ).deserialize();

        // Ensure only the signatures were removed
        assertThat(fixture.get("serialized").toString(), startsWith(Hex.encode(Serializer.serialize(withoutSignatures))));

        reSignUnsigned(actual, withoutSignatures);

        if (withoutSignatures.signature != null) {
            assertTrue(withoutSignatures.verify());
        }

        if (withoutSignatures.secondSignature != null) {
            checkSecondSignature(withoutSignatures);
        }

        if (withoutSignatures.signatures != null) {
            checkMultiSignature(withoutSignatures);
        }

        assertThat(Hex.encode(Serializer.serialize(withoutSignatures)), is(fixture.get("serialized").toString()));
    }

    private void reSignUnsigned(Transaction actual, Transaction withoutSignatures) {
        if (actual.signatures != null) {
            int i = 0;
            for (String passphrase : Arrays.asList(musigPassphrase1, musigPassphrase2, musigPassphrase3)) {
                withoutSignatures.multiSign(passphrase, i++);
            }
            if (actual.signature != null) {
                withoutSignatures.sign(musigPassphrase1);
            }
            if (actual.secondSignature != null) {
                withoutSignatures.secondSign(secondPassphrase);
            }
        } else if (actual.secondSignature != null) {
            withoutSignatures.sign(passphrase);
            withoutSignatures.secondSign(secondPassphrase);
        } else if (actual.signature != null) {
            withoutSignatures.sign(passphrase);
        }
    }

    private void checkSecondSignature(Transaction actual) {
        String secondPublicKey = PublicKey.fromPassphrase(secondPassphrase);
        assertTrue(actual.secondVerify(secondPublicKey));
    }

    private void checkMultiSignature(Transaction actual) {
        String key1 = PublicKey.fromPassphrase(musigPassphrase1);
        String key2 = PublicKey.fromPassphrase(musigPassphrase2);
        String key3 = PublicKey.fromPassphrase(musigPassphrase3);

        List<String> publicKeys = Arrays.asList(key1, key2, key3);

        assertTrue(actual.multiVerify(2, publicKeys));
    }
}
