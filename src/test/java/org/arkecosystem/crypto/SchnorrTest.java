package org.arkecosystem.crypto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.arkecosystem.crypto.encoding.Hex;
import org.junit.jupiter.api.Test;

class SchnorrTest {
    @Test
    public void convertHexStringToByteArray() {
        assertEquals(
                Hex.encode(
                        Schnorr.hexStringToByteArray("b693449AdDa7EFc015D87944EAE8b7C37EB1690A")),
                "b693449adda7efc015d87944eae8b7c37eb1690a");
    }
}
