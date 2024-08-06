package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import org.arkecosystem.crypto.transactions.types.Transaction;
import org.junit.jupiter.api.Test;

class UsernameRegistrationBuilderTest {
    @Test
    void build() {
        Transaction actual =
                new UsernameRegistrationBuilder()
                        .usernameAsset("alfonsobries")
                        .nonce(3)
                        .sign("this is a top secret passphrase")
                        .transaction;

        HashMap actualHashMap = actual.toHashMap();

        HashMap asset = (HashMap) actualHashMap.get("asset");

        assertEquals(asset.get("username"), "alfonsobries");

        assertTrue(actual.verify());
    }
}
