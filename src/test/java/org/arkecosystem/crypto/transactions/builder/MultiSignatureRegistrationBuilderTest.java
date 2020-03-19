package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class MultiSignatureRegistrationBuilderTest {

    @Test
    void build() {
        MultiSignatureRegistrationBuilder actual = new MultiSignatureRegistrationBuilder()
            .keysgroup(Arrays.asList("keysgroup"))
            .lifetime(1)
            .lifetime((byte)1)
            .min(1)
            .min((byte)1);

        Throwable exception =
                assertThrows(UnsupportedOperationException.class, () -> actual.sign("passphrase"));
        assertEquals(
                "MultiSignatureRegistration is not supported in java sdk", exception.getMessage());
    }
}
