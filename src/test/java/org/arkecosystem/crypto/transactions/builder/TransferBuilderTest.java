package org.arkecosystem.crypto.transactions.builder;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.Gson;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.arkecosystem.crypto.encoding.Hex;
import org.junit.jupiter.api.Test;

public class TransferBuilderTest {

    @Test
    public void it_should_sign_it_with_a_passphrase() throws Exception {
        Map<String, Object> fixture = loadFixture("transfer");

        Map<String, Object> data = (Map<String, Object>) fixture.get("data");

        // Updated to use BigInteger for large values
        TransferBuilder builder = new TransferBuilder()
                .gasPrice(((Number) data.get("gasPrice")).intValue())
                .nonce(Long.parseLong(data.get("nonce").toString()))
                .network(((Number) data.get("network")).intValue())
                .gasLimit(((Number) data.get("gasLimit")).intValue())
                .recipientAddress((String) data.get("recipientAddress"))
                .value((String) data.get("value"))
                .sign("my super secret passphrase");

        byte[] serializedBytes = builder.transaction.serialize(false);
        String serializedHex = Hex.encode(serializedBytes);
        
        // Compare the serialized transaction
        assertEquals(fixture.get("serialized"), serializedHex);
        assertEquals(data.get("id"), builder.transaction.getId());
        assertTrue(builder.verify());
    }

    private Map<String, Object> loadFixture(String path) throws Exception {
        String resourcePath = "/transactions/" + path + ".json";
        InputStream inputStream = getClass().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new Exception("Fixture not found: " + resourcePath);
        }
        String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        return new Gson().fromJson(json, Map.class);
    }
}
