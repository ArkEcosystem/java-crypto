package org.arkecosystem.crypto.identities.bls;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class KeyPairFactoryTest {

    private static final String PASSPHRASE =
            "question measure debris increase false feature journey height fun agent coach office only shell nation skill track upset distance behave easy devote floor shy";
    private static final String EXPECTED_PRIVATE_KEY =
            "3e99d30b3816f60077b1fdb4535ce0e9f9c715e42d1647edc3361fc531fb618f";
    private static final String EXPECTED_PUBLIC_KEY =
            "b4865127896c3c5286296a7b26e7c8002586a3ecf5832bfb59e689336f1f4c75e10491b9dfaed8dfb2c2fbe22d11fa93";

    @Test
    public void fromPassphrase() {
        KeyPairFactory factory = new KeyPairFactory();

        KeyPairFactory.KeyPair keyPair = factory.fromPassphrase(PASSPHRASE);

        assertEquals(EXPECTED_PRIVATE_KEY, keyPair.getPrivateKey());
        assertEquals(EXPECTED_PUBLIC_KEY, keyPair.getPublicKey());
    }
}
