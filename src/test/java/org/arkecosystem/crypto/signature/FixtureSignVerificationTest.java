package org.arkecosystem.crypto.signature;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.internal.LinkedTreeMap;
import java.util.Arrays;
import java.util.List;
import org.arkecosystem.crypto.encoding.Hex;
import org.arkecosystem.crypto.identities.PublicKey;
import org.arkecosystem.crypto.transactions.Deserializer;
import org.arkecosystem.crypto.transactions.FixtureLoader;
import org.arkecosystem.crypto.transactions.Serializer;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class FixtureSignVerificationTest {

    private final String passphrase = "my super secret passphrase";
    private final String secondPassphrase = "this is a top secret second passphrase";
    private final String musigPassphrase1 =
            "album pony urban cheap small blade cannon silent run reveal luxury glad predict excess fire beauty hollow reward solar egg exclude leaf sight degree";
    private final String musigPassphrase2 =
            "hen slogan retire boss upset blame rocket slender area arch broom bring elder few milk bounce execute page evoke once inmate pear marine deliver";
    private final String musigPassphrase3 =
            "top visa use bacon sun infant shrimp eye bridge fantasy chair sadness stable simple salad canoe raw hill target connect avoid promote spider category";

    @ParameterizedTest
    @ValueSource(
            strings = {
                "transactions/transfer/transfer-sign",
                "transactions/transfer/transfer-with-vendor-field-sign",
                // "transactions/transfer/transfer-multi-sign",

                "transactions/vote/vote-sign",
                "transactions/vote/unvote-sign",
                // "transactions/vote/vote-multi-sign",

                "transactions/validator_registration/validator-registration-sign",
                // "transactions/validator_registration/validator-registration-multi-sign",

                "transactions/validator_resignation/validator-resignation-sign",
                // "transactions/validator_resignation/validator-resignation-multi-sign",

                "transactions/multi_payment/multi-payment-sign",
                "transactions/multi_payment/multi-payment-with-vendor-field-sign",
                // "transactions/multi_payment/multi-payment-multi-sign",

                // "transactions/username_resignation/username-resignation-sign",
                // "transactions/username_resignation/username-resignation-multi-sign",

                // "transactions/username_registration/username-registration-multi-sign",
                // "transactions/username_registration/username-registration-sign",

                "transactions/multi_signature_registration/multi-signature-registration-sign",
            })
    void checkSchnorrSignature(String file) {
        LinkedTreeMap<String, Object> fixture = FixtureLoader.load(file);

        Transaction actual = new Deserializer(fixture.get("serialized").toString()).deserialize();

        if (actual.signature != null) assertTrue(actual.verify());

        if (actual.secondSignature != null) {
            checkSecondSignature(actual);
        }

        if (actual.signatures != null) {
            checkMultiSignature(actual);
        }
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "transactions/transfer/transfer-sign",
                "transactions/transfer/transfer-with-vendor-field-sign",
                // "transactions/transfer/transfer-multi-sign",

                "transactions/vote/vote-sign",
                "transactions/vote/unvote-sign",
                // "transactions/vote/vote-multi-sign",

                "transactions/validator_registration/validator-registration-sign",
                // "transactions/validator_registration/validator-registration-multi-sign",

                "transactions/validator_resignation/validator-resignation-sign",
                // "transactions/validator_resignation/validator-resignation-multi-sign",

                "transactions/multi_payment/multi-payment-sign",
                "transactions/multi_payment/multi-payment-with-vendor-field-sign",
                // "transactions/multi_payment/multi-payment-multi-sign",

                // "transactions/username_resignation/username-resignation-sign",
                // "transactions/username_resignation/username-resignation-multi-sign",

                // "transactions/username_registration/username-registration-multi-sign",
                // "transactions/username_registration/username-registration-sign",

                // "transactions/multi_signature_registration/multi-signature-registration-sign",
            })
    void checkSigningAgainProducesSameSignature(String file) {
        LinkedTreeMap<String, Object> fixture = FixtureLoader.load(file);

        Transaction actual = new Deserializer(fixture.get("serialized").toString()).deserialize();

        // Remove the signatures from original transaction
        Transaction withoutSignatures =
                new Deserializer(Hex.encode(Serializer.serialize(actual, true, true, true)))
                        .deserialize();

        // Ensure only the signatures were removed
        assertThat(
                fixture.get("serialized").toString(),
                startsWith(Hex.encode(Serializer.serialize(withoutSignatures))));

        reSignUnsigned(actual, withoutSignatures);

        if (withoutSignatures.signature != null) {
            assertTrue(withoutSignatures.verify());
        }

        if (withoutSignatures.secondSignature != null) {
            checkSecondSignature(withoutSignatures);
        }

        int signatureLength = 128;

        if (withoutSignatures.signatures != null) {
            checkMultiSignature(withoutSignatures);

            signatureLength = 128 + (withoutSignatures.signatures.size() * 130);
        }

        String serializedWithoutSignatures = Hex.encode(Serializer.serialize(withoutSignatures));
        String serializedFixture = fixture.get("serialized").toString();

        // Exclude the last 128 characters (signature) for final comparison
        assertThat(
                serializedWithoutSignatures.substring(
                        0, serializedWithoutSignatures.length() - signatureLength),
                is(serializedFixture.substring(0, serializedFixture.length() - signatureLength)));
    }

    private void reSignUnsigned(Transaction actual, Transaction withoutSignatures) {
        if (actual.signatures != null) {
            int i = 0;
            for (String passphrase :
                    Arrays.asList(musigPassphrase1, musigPassphrase2, musigPassphrase3)) {
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
