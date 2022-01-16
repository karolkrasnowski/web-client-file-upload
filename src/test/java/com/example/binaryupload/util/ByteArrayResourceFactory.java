package com.example.binaryupload.util;

import org.springframework.core.io.ByteArrayResource;

public class ByteArrayResourceFactory {

    public static ByteArrayResource newByteArrayResource(byte[] bytes, int fileIndex) {
        return new ByteArrayResource(bytes) {
            @Override
            public String getFilename() {
                return "filename-" + fileIndex;
            }
        };
    }
}
