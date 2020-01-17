package org.arkecosystem.crypto.identities;

import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WIFTest {

    @Test
    public void fromPassphrase() throws IOException {
        String actual = WIF.fromPassphrase("this is a top secret passphrase");
        Assertions.assertEquals("SGq4xLgZKCGxs7bjmwnBrWcT4C1ADFEermj846KC97FSv1WFD1dA", actual);
    }
}
