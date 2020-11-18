package org.arkecosystem.crypto.signature;

import com.google.gson.internal.LinkedTreeMap;
import org.arkecosystem.crypto.transactions.Deserializer;
import org.arkecosystem.crypto.transactions.FixtureLoader;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FixtureSignVerificationTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "transactions/V1/transfer/second-passphrase",
        "transactions/V1/transfer/second-passphrase-with-vendor-field",
        "transactions/V1/transfer/passphrase-with-vendor-field",
        "transactions/V1/transfer/passphrase",
        "transactions/V1/transfer/passphrase-with-vendor-field-hex",
        "transactions/V1/transfer/second-passphrase-with-vendor-field-hex",
        "transactions/V1/second_signature_registration/second-passphrase",
        "transactions/V1/multi_signature_registration/passphrase",
        "transactions/V1/vote/second-passphrase",
        "transactions/V1/vote/passphrase",
        "transactions/V1/delegate_registration/second-passphrase",
        "transactions/V1/delegate_registration/passphrase",
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
    void name(String file) {
        LinkedTreeMap<String, Object> fixture = FixtureLoader.load(file);

        Transaction actual = new Deserializer(fixture.get("serialized").toString()).deserialize();

        assertTrue(actual.verify());
    }
}
