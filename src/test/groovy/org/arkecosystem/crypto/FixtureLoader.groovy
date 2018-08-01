package org.arkecosystem.crypto

import com.google.gson.Gson

class FixtureLoader {
    static Object load(instance, String path) {
        ClassLoader classLoader = instance.getClassLoader()

        URL resource = classLoader.getResource(String.format("%s.json", path))

        File file = new File(resource.getPath())

        return new Gson().fromJson(file.text, Object.class)
    }
}
