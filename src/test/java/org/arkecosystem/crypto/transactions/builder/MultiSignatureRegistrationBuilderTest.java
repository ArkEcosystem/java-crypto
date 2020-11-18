package org.arkecosystem.crypto.transactions.builder;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MultiSignatureRegistrationBuilderTest {

    @Test
    void build() {
        MultiSignatureRegistrationBuilder actual =
                new MultiSignatureRegistrationBuilder()
                        .publicKeys(Arrays.asList("keysgroup"))
                        .min(1)
                        .min((byte) 1);

        Throwable exception =
                assertThrows(UnsupportedOperationException.class, () -> actual.sign("passphrase"));
        assertEquals(
                "MultiSignatureRegistration is not supported in java sdk", exception.getMessage());
    }
}
