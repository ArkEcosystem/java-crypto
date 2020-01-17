package org.arkecosystem.crypto.transactions;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FixtureLoader {

    private static String readFile(String path) throws IOException {
        ClassLoader classLoader = Transaction.class.getClassLoader();
        URL resource = classLoader.getResource(String.format("%s.json", path));
        return new String(
                Files.readAllBytes(Paths.get(resource.getPath())), StandardCharsets.UTF_8);
    }

    public static LinkedTreeMap<String, Object> load(String path) {
        try {
            return new Gson()
                    .fromJson(readFile(path), new LinkedTreeMap<String, Object>().getClass());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
