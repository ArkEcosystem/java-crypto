package org.arkecosystem.crypto.transactions;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FixtureLoader {

    private static String readFile(String path) throws IOException {
        InputStream inputStream =
                FixtureLoader.class
                        .getClassLoader()
                        .getResourceAsStream(String.format("%s.json", path));
        if (inputStream == null) {
            throw new IOException("Resource not found: " + path);
        }
        byte[] bytes = inputStream.readAllBytes();
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static LinkedTreeMap<String, Object> load(String path) {
        try {
            return new Gson().fromJson(readFile(path), LinkedTreeMap.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
