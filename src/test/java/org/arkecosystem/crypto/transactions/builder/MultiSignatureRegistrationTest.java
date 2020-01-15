package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import org.arkecosystem.crypto.transactions.Transaction;
import org.junit.jupiter.api.Test;

public class MultiSignatureRegistrationTest {

    @Test
    void build() {
        Transaction actualV1 =
                new MultiSignatureRegistration()
                        .min(2)
                        .lifetime(255)
                        .keysgroup(
                                Arrays.asList(
                                        "03a02b9d5fdd1307c2ee4652ba54d492d1fd11a7d1bb3f3a44c4a05e79f19de933",
                                        "13a02b9d5fdd1307c2ee4652ba54d492d1fd11a7d1bb3f3a44c4a05e79f19de933",
                                        "23a02b9d5fdd1307c2ee4652ba54d492d1fd11a7d1bb3f3a44c4a05e79f19de933"))
                        .sign("this is a top secret passphrase")
                        .transaction;

        assertTrue(actualV1.verify());
    }

    @Test
    void buildSecondSignature() {
        Transaction actual =
                new MultiSignatureRegistration()
                        .min(2)
                        .lifetime(255)
                        .keysgroup(
                                Arrays.asList(
                                        "03a02b9d5fdd1307c2ee4652ba54d492d1fd11a7d1bb3f3a44c4a05e79f19de933",
                                        "13a02b9d5fdd1307c2ee4652ba54d492d1fd11a7d1bb3f3a44c4a05e79f19de933",
                                        "23a02b9d5fdd1307c2ee4652ba54d492d1fd11a7d1bb3f3a44c4a05e79f19de933"))
                        .sign("this is a top secret passphrase")
                        .secondSign("this is a top secret second passphrase")
                        .transaction;

        assertTrue(actual.verify());
        assertTrue(
                actual.secondVerify(
                        "03699e966b2525f9088a6941d8d94f7869964a000efe65783d78ac82e1199fe609"));
    }
}
