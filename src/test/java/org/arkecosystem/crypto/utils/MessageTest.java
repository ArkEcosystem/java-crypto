package org.arkecosystem.crypto.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import org.junit.jupiter.api.Test;

class MessageTest {

    @Test
    void sign() {
        Message message = Message.sign("Hello World", "this is a top secret passphrase");
        assertEquals(
                "034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed192",
                message.getPublickey());
        assertEquals(
                "304402200fb4adddd1f1d652b544ea6ab62828a0a65b712ed447e2538db0caebfa68929e02205ecb2e1c63b29879c2ecf1255db506d671c8b3fa6017f67cfd1bf07e6edd1cc8",
                message.getSignature());
        assertEquals("Hello World", message.getMessage());
    }

    @Test
    void verify() {
        Message message = Message.sign("Hello World", "this is a top secret passphrase");
        assertEquals(true, message.verify());
    }

    @Test
    void toMap() {
        Message message = Message.sign("Hello World", "this is a top secret passphrase");
        Map map = message.toMap();
        assertEquals(
                "034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed192",
                map.get("publickey"));
        assertEquals(
                "304402200fb4adddd1f1d652b544ea6ab62828a0a65b712ed447e2538db0caebfa68929e02205ecb2e1c63b29879c2ecf1255db506d671c8b3fa6017f67cfd1bf07e6edd1cc8",
                map.get("signature"));
        assertEquals("Hello World", map.get("message"));
    }

    @Test
    void toJson() {
        Message message = Message.sign("Hello World", "this is a top secret passphrase");
        String json = message.toJson();
        assertEquals(
                "{\"signature\":\"304402200fb4adddd1f1d652b544ea6ab62828a0a65b712ed447e2538db0caebfa68929e02205ecb2e1c63b29879c2ecf1255db506d671c8b3fa6017f67cfd1bf07e6edd1cc8\",\"publickey\":\"034151a3ec46b5670a682b0a63394f863587d1bc97483b1b6c70eb58e7f0aed192\",\"message\":\"Hello World\"}",
                json);
    }
}
