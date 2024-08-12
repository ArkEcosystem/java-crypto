package org.arkecosystem.crypto.signature;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.internal.LinkedTreeMap;
import org.arkecosystem.crypto.identities.PublicKey;
import org.arkecosystem.crypto.transactions.Deserializer;
import org.arkecosystem.crypto.transactions.FixtureLoader;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class FixtureSignVerificationTest {

    private final String secondPassphrase = "this is a top secret second passphrase";

    @ParameterizedTest
    @ValueSource(
            strings = {
                "transactions/transfer/transfer-sign",
                "transactions/vote/vote-sign",
                "transactions/vote/unvote-sign",
                "transactions/validator_registration/validator-registration-sign",
                "transactions/validator_resignation/validator-resignation-sign",
                "transactions/multi_payment/multi-payment-sign",
                "transactions/multi_payment/multi-payment-with-vendor-field-sign",
                "transactions/username_resignation/username-resignation-sign",
                "transactions/username_registration/username-registration-sign",
                "transactions/multi_signature_registration/multi-signature-registration-sign",
            })
    void checkSchnorrSignature(String file) {
        LinkedTreeMap<String, Object> fixture = FixtureLoader.load(file);

        Transaction actual = new Deserializer(fixture.get("serialized").toString()).deserialize();

        if (actual.signature != null) assertTrue(actual.verify());

        if (actual.secondSignature != null) {
            checkSecondSignature(actual);
        }
    }

    private void checkSecondSignature(Transaction actual) {
        String secondPublicKey = PublicKey.fromPassphrase(secondPassphrase);
        assertTrue(actual.secondVerify(secondPublicKey));
    }
}
