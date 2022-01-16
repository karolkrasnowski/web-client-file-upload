package com.example.binaryupload;

import com.example.binaryupload.util.Bytes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.example.binaryupload.util.ByteArrayResourceFactory.newByteArrayResource;
import static com.example.binaryupload.util.MultiValueMapBuilder.buildMultiValueMap;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * These tests should be executed against running application
 */
public class BinaryFilesUploadE2eTest {

    private final List<String> sentBytes = new CopyOnWriteArrayList<>();

    @BeforeEach
    void before() {
        sentBytes.clear();
    }

    /**
     * This test passes all the time
     */
    @Test
    void shouldUpload5Files() {
        // given
        MultiValueMap<String, HttpEntity<?>> body = buildMultiValueMap(5, this::createByteArrayResource);

        // when
        List<String> receivedBytes = sendPostRequest(body);

        // then
        assertEquals(sentBytes, receivedBytes);
    }

    /**
     * This test fails most of the time
     */
    @RepeatedTest(10)
    void shouldUpload1000Files() {
        // given
        MultiValueMap<String, HttpEntity<?>> body = buildMultiValueMap(1000, this::createByteArrayResource);

        // when
        List<String> receivedBytes = sendPostRequest(body);

        // then
        assertEquals(sentBytes, receivedBytes);
    }

    private List<String> sendPostRequest(MultiValueMap<String, HttpEntity<?>> body) {
        return WebClient.builder().build().post()
                .uri("http://localhost:8080/files")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {
                })
                .block();
    }

    private ByteArrayResource createByteArrayResource(int index) {
        byte[] bytes = Bytes.random();
        sentBytes.add(Base64.getEncoder().encodeToString(bytes));
        return newByteArrayResource(bytes, index);
    }
}
