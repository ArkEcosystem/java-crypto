package org.arkecosystem.crypto.signature.bls;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class JNIEnv {
    byte[] cache;
    List<String> sources;

    public JNIEnv() {
        cache = new byte[1024];
        sources = new LinkedList<String>();
        String OS = System.getProperty("os.name").toLowerCase();
        String ARCH = System.getProperty(("os.arch")).toLowerCase();
        if (OS.contains("mac") && (ARCH.contains("x86_64") || ARCH.contains("aarch64"))) {
            sources.add("libmcljava.dylib");
        } else if (OS.contains("linux") && ARCH.contains("amd64")) {
            sources.add("libmcljava.so");
        }
    }

    private Boolean sourceExist(String sourceName) {
        String[] libraryPaths = System.getProperty("java.library.path").split(File.pathSeparator);
        for (String path : libraryPaths) {
            File f = new File(path, sourceName);
            if (f.exists()) {
                return true;
            }
        }
        return false;
    }

    public void prepare() {
        for (String s : sources) {
            copy(s);
        }
    }

    public Boolean copy(String sourceName) {
        if (sourceExist(sourceName)) {
            return true;
        } else {
            try {
                String[] libraryPaths =
                        System.getProperty("java.library.path").split(File.pathSeparator);
                File f =
                        new File(
                                libraryPaths[0],
                                sourceName); // Using the first path in java.library.path
                if (!f.exists()) {
                    f.createNewFile();
                    System.out.println("[JNIDEV]:DEFAULT JNI INITION:" + sourceName);
                }
                FileOutputStream os = new FileOutputStream(f);

                InputStream is =
                        getClass()
                                .getResourceAsStream(
                                        "/" + sourceName); // Modified to add leading slash
                if (is == null) {
                    os.close();
                    return false;
                }
                Arrays.fill(cache, (byte) 0);
                int realRead = is.read(cache);
                while (realRead != -1) {
                    os.write(cache, 0, realRead);
                    realRead = is.read(cache);
                }
                os.close();
            } catch (Exception e) {
                System.out.println("[JNIDEV]:ERROR IN COPY JNI LIB!");
                return false;
            }
        }
        return true;
    }
}
