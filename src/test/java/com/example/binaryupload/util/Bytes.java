package com.example.binaryupload.util;

import java.util.concurrent.ThreadLocalRandom;

public class Bytes {

    public static byte[] random() {
        byte[] bytes = new byte[ThreadLocalRandom.current().nextInt(16, 32)];
        ThreadLocalRandom.current().nextBytes(bytes);
        return bytes;
    }
}
