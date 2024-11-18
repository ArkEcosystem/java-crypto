package org.arkecosystem.crypto.transactions.builder;

import com.google.gson.Gson;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractTransactionBuilderTest {
    protected String passphrase;

    @BeforeEach
    public void setUp() {
        this.passphrase = "my super secret passphrase";
    }

    protected Map<String, Object> loadFixture(String name) throws Exception {
        String resourcePath = "/transactions/" + name + ".json";
        InputStream inputStream = getClass().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new Exception("Fixture not found: " + resourcePath);
        }
        String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        return new Gson().fromJson(json, Map.class);
    }
}
