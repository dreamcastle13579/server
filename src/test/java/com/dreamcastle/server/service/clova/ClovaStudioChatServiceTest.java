package com.dreamcastle.server.service.clova;//package com.dreamcastle.server.service.clova;
//
//import com.dreamcastle.server.config.ClovaProperties;
//import com.dreamcastle.server.dto.clova.ClovaStudioChatRequest;
//import com.dreamcastle.server.dto.clova.ClovaStudioChatResponse;
//import com.dreamcastle.server.exception.ServerException;
//import okhttp3.mockwebserver.MockResponse;
//import okhttp3.mockwebserver.MockWebServer;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.reactive.function.client.WebClient;
//import org.springframework.web.reactive.function.client.WebClientResponseException;
//import reactor.core.publisher.Mono;
//import reactor.test.StepVerifier;
//
//import java.io.IOException;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//
//@ExtendWith(MockitoExtension.class)
//class ClovaStudioChatServiceTest {
//    @Mock
//    private SystemPromptFactory systemPromptFactory;
//
//    @Mock
//    private ClovaProperties clovaProperties;
//
//    @Mock
//    private WebClient webClient;
//
//    @InjectMocks
//    private ClovaStudioChatService clovaStudioChatService;
//
////    private MockWebServer mockWebServer;
//
////    private String mockClovaStudioApiUrl;
//
////    @BeforeEach
////    void setup() throws IOException {
////        mockWebServer = new MockWebServer();
////        mockWebServer.start();
////        mockClovaStudioApiUrl = mockWebServer.url("http://clova.studio.com").toString();
////    }
////
////    @AfterEach
////    void close() throws IOException {
////        mockWebServer.shutdown();
////    }
//
//    private MockWebServer mockWebServer;
////    private ClovaStudioChatService clovaStudioChatService;
//
//    @BeforeEach
//    void setUp() throws IOException {
//        // MockWebServer 초기화
//        mockWebServer = new MockWebServer();
//        mockWebServer.start();
//
//        // WebClient 설정
//        WebClient webClient = WebClient.builder()
//                .baseUrl(mockWebServer.url("/").toString()) // MockWebServer의 URL을 WebClient에 설정
//                .build();
//
//        // ClovaStudioChatService 인스턴스 생성

import com.dreamcastle.server.config.ClovaProperties;
import com.dreamcastle.server.dto.clova.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ClovaStudioChatServiceTest {

    private MockWebServer mockWebServer;

    @Mock
    private ClovaProperties clovaProperties;

    @Mock
    private SystemPromptFactory systemPromptFactory;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private ClovaStudioChatService clovaStudioChatService;

    private WebClient webClient;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        clovaStudioChatService = new ClovaStudioChatService(webClient, clovaProperties, systemPromptFactory);
        given(clovaProperties.getStudioApiUrl()).willReturn("/api/studio/v1/chat");
        given(clovaProperties.getApiKey()).willReturn("test-api-key");
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void sendRequest_성공() throws Exception {
        // given
        String promptType = "fairy";
        String content = "테스트유저: 어제 꾼 꿈입니다.";

        SystemPromptType mockPromptType = SystemPromptType.FAIRY;
        SystemPromptStrategy mockStrategy = mock(SystemPromptStrategy.class);
        ClovaStudioChatRequest mockRequest = mock(ClovaStudioChatRequest.class);

        given(systemPromptFactory.getStrategy(mockPromptType)).willReturn(mockStrategy);
        given(mockStrategy.createChatRequest(content)).willReturn(mockRequest);

        String clovaResponse = "{\"messages\": [\"해몽 결과\", \"해몽 결과1\"], \"category\" : \"슬픔\"}";

        ClovaStudioChatResponse expectedResponse = new ClovaStudioChatResponse(
                new Result(new Message(Role.assistant, clovaResponse)));

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(objectMapper.writeValueAsString(expectedResponse)));

        // when
        Mono<ClovaStudioChatResponse> result = clovaStudioChatService.sendRequest(promptType, content);

        // then
        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.result().message().content().equals(clovaResponse)
                )
                .verifyComplete();

        RecordedRequest recordedRequest = mockWebServer.takeRequest(5, TimeUnit.SECONDS);
        assertThat(recordedRequest.getMethod()).isEqualTo("POST");
        assertThat(recordedRequest.getPath()).isEqualTo("/api/studio/v1/chat");
        assertThat(recordedRequest.getHeader(HttpHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(recordedRequest.getHeader(HttpHeaders.AUTHORIZATION)).isEqualTo("Bearer test-api-key");

        verify(systemPromptFactory).getStrategy(mockPromptType);
        verify(mockStrategy).createChatRequest(content);
    }

    @Test
    void sendRequest_서버_에러() {
        // given
        String promptType = "fairy";
        String content = "테스트유저: 어제 꾼 꿈입니다.";

        SystemPromptType mockPromptType = SystemPromptType.FAIRY;
        SystemPromptStrategy mockStrategy = mock(SystemPromptStrategy.class);
        ClovaStudioChatRequest mockRequest = mock(ClovaStudioChatRequest.class);

        given(systemPromptFactory.getStrategy(mockPromptType)).willReturn(mockStrategy);
        given(mockStrategy.createChatRequest(content)).willReturn(mockRequest);

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error"));

        // when
        Mono<ClovaStudioChatResponse> result = clovaStudioChatService.sendRequest(promptType, content);

        // then
        StepVerifier.create(result)
                .expectError()
                .verify();
    }
}