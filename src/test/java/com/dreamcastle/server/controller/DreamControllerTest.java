package com.dreamcastle.server.controller;

import com.dreamcastle.server.dto.ApiResponse;
import com.dreamcastle.server.dto.dream.Category;
import com.dreamcastle.server.dto.dream.InterpretationRequest;
import com.dreamcastle.server.dto.dream.InterpretationResponse;
import com.dreamcastle.server.exception.ClovaApiException;
import com.dreamcastle.server.exception.ErrorCode;
import com.dreamcastle.server.service.dream.DreamService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

@WebFluxTest(DreamController.class)
@ExtendWith(MockitoExtension.class)
class DreamControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private DreamService dreamService;

    @Test
    void 꿈_해몽_API_성공() {
        // given
        InterpretationRequest request = new InterpretationRequest("fairy", "닉네임", "꿈 내용");
        List<String> messages = List.of("해몽 결과 1", "해몽 결과 2", "해몽 결과 3", "해몽 결과 4", "해몽 결과 5", "해몽 결과 6");
        Category category = Category.JOY;

        InterpretationResponse interpretationResponse = new InterpretationResponse(messages, category);

        given(dreamService.interpret(request.promptType(), request.nickname(), request.content()))
                .willReturn(Mono.just(interpretationResponse));

        // when & then
        webTestClient.post()
                .uri("/dreams/interpretation")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.result.category").isEqualTo(category.getValue())
                .jsonPath("$.result.messages[0]").isEqualTo(messages.get(0))
                .jsonPath("$.result.messages[1]").isEqualTo(messages.get(1))
                .jsonPath("$.result.messages[2]").isEqualTo(messages.get(2))
                .jsonPath("$.result.messages[3]").isEqualTo(messages.get(3))
                .jsonPath("$.result.messages[4]").isEqualTo(messages.get(4))
                .jsonPath("$.result.messages[5]").isEqualTo(messages.get(5));
    }

    @ParameterizedTest
    @MethodSource("provideClovaErrorCases")
    void 꿈_해몽시_예외가_발생하면_적절한_응답을_반환한다(ErrorCode errorCode, HttpStatus expectedStatus) {
        // given
        InterpretationRequest request = new InterpretationRequest("fairy", "닉네임", "꿈 내용");
        given(dreamService.interpret(request.promptType(), request.nickname(), request.content()))
                .willReturn(Mono.error(new ClovaApiException(errorCode)));

        // when & then
        webTestClient.post()
                .uri("/dreams/interpretation")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectBody(ApiResponse.class)
                .value(response -> {
                    assertNotNull(response);
                    assertThat(response.code()).isEqualTo(errorCode.getStatus().value());
                    assertThat(response.message()).isEqualTo(errorCode.getMessage());
                });
    }

    static Stream<Arguments> provideClovaErrorCases() {
        return Stream.of(
                Arguments.of(ErrorCode.CLOVA_API_TIME_OUT_ERROR, HttpStatus.GATEWAY_TIMEOUT),
                Arguments.of(ErrorCode.CLOVA_API_RESPONSE_PARSING_ERROR, HttpStatus.INTERNAL_SERVER_ERROR),
                Arguments.of(ErrorCode.CLOVA_API_INVALID_REQUEST, HttpStatus.BAD_REQUEST),
                Arguments.of(ErrorCode.INVALID_PROMPT_TYPE, HttpStatus.BAD_REQUEST),
                Arguments.of(ErrorCode.CLOVA_API_INVALID_RESPONSE_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR),
                Arguments.of(ErrorCode.CLOVA_API_INVALID_RESPONSE_CATEGORY, HttpStatus.INTERNAL_SERVER_ERROR)
        );
    }
}
