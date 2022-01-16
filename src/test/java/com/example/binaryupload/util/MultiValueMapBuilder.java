package com.example.binaryupload.util;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;

import java.util.function.Function;

public class MultiValueMapBuilder {

    public static MultiValueMap<String, HttpEntity<?>> buildMultiValueMap(int numberOfResources,
                                                                          Function<Integer, ByteArrayResource> byteArrayResourceSupplier) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        for (int i = 0; i < numberOfResources; i++) {
            builder.part("item-" + i, byteArrayResourceSupplier.apply(i));
        }
        return builder.build();
    }
}
