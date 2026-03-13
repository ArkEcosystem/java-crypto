package org.arkecosystem.crypto;

import com.google.gson.Gson;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.arkecosystem.crypto.configuration.Network;
import org.arkecosystem.crypto.networks.Devnet;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractTest {
    public String passphrase;

    @BeforeEach
    public void setUp() {
        Network.set(new Devnet());
        this.passphrase =
                "found lobster oblige describe ready addict body brave live vacuum display salute lizard combine gift resemble race senior quality reunion proud tell adjust angle";
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
