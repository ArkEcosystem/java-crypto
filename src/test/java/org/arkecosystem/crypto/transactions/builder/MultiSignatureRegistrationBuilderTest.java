package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class MultiSignatureRegistrationBuilderTest {

    @Test
    void build() {
        MultiSignatureRegistrationBuilder actual = new MultiSignatureRegistrationBuilder();
        Throwable exception =
                assertThrows(UnsupportedOperationException.class, () -> actual.sign("passphrase"));
        assertEquals(
                "MultiSignatureRegistration is not supported in java sdk", exception.getMessage());
    }
}
