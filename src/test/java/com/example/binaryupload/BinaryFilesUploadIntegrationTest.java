package com.example.binaryupload;

import com.example.binaryupload.util.Bytes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Base64;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.example.binaryupload.util.ByteArrayResourceFactory.newByteArrayResource;
import static com.example.binaryupload.util.MultiValueMapBuilder.buildMultiValueMap;

@SpringBootTest
class BinaryFilesUploadIntegrationTest {

    private final List<String> sentBytes = new CopyOnWriteArrayList<>();

    private WebTestClient webTestClient;

    @BeforeEach
    void before(ApplicationContext applicationContext) {
        webTestClient = WebTestClient.bindToApplicationContext(applicationContext).build();
    }

    /**
     * The problem described does not occur when using WebTestClient so the following test passes
     */
    @Test
    void shouldUpload1000Files() {
        // given
        MultiValueMap<String, HttpEntity<?>> body = buildMultiValueMap(1000, this::createByteArrayResource);

        // expect
        sendPostRequest(body).isEqualTo(sentBytes);
    }

    private WebTestClient.BodySpec<Object, ?> sendPostRequest(MultiValueMap<String, HttpEntity<?>> body) {
        return webTestClient.post()
                .uri("/files")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body))
                .exchange()
                .expectBody(new ParameterizedTypeReference<>() {
                });
    }

    private ByteArrayResource createByteArrayResource(int index) {
        byte[] bytes = Bytes.random();
        sentBytes.add(Base64.getEncoder().encodeToString(bytes));
        return newByteArrayResource(bytes, index);
    }
}
