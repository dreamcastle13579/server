package com.dreamcastle.server.service.dream;

import com.dreamcastle.server.dto.dream.Category;
import com.dreamcastle.server.dto.dream.InterpretationResponse;
import com.dreamcastle.server.exception.ClovaApiException;
import com.dreamcastle.server.exception.ErrorCode;
import com.dreamcastle.server.service.clova.ClovaStudioChatService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DreamServiceTest {

    @Mock
    private ClovaStudioChatService clovaStudioChatService;
    @InjectMocks
    private DreamService dreamService;

    @Test
    void 해몽_성공() {
        // Given
        String promptType = "dream";
        String nickname = "테스트유저";
        String dreamContent = "어제 꾼 꿈입니다.";
        String formattedContent = nickname + ": " + dreamContent;
        String clovaResponse = """
            {
                "messages": ["해몽 결과1", "해몽 결과2", "해몽 결과3", "해몽 결과4", "해몽 결과5", "해몽 결과6"],
                "category": "기쁨"
            }
        """;

        given(clovaStudioChatService.sendRequest(promptType, formattedContent))
                .willReturn(Mono.just(clovaResponse));

        // When
        Mono<InterpretationResponse> result = dreamService.interpret(promptType, nickname, dreamContent);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(response -> {
                    // 실제 response 객체의 messages 필드가 예상한 값과 일치하는지 확인
                    return response.messages() != null &&
                            response.messages().size() == 6 &&
                            response.messages().get(0).equals("해몽 결과1") &&
                            response.messages().get(1).equals("해몽 결과2") &&
                            response.messages().get(2).equals("해몽 결과3") &&
                            response.messages().get(3).equals("해몽 결과4") &&
                            response.messages().get(4).equals("해몽 결과5") &&
                            response.messages().get(5).equals("해몽 결과6") &&
                            response.category().equals(Category.JOY);
                })
                .verifyComplete();
    }

    @Test
    void 해몽시_시간이_정한_limit을_넘기면_예외가_발생한다() {
        // Given
        String promptType = "dream";
        String nickname = "테스트유저";
        String dreamContent = "어제 꾼 꿈입니다.";
        String formattedContent = nickname + ": " + dreamContent;

        given(clovaStudioChatService.sendRequest(promptType, formattedContent))
                .willReturn(Mono.delay(Duration.ofSeconds(11)).then(Mono.empty()));

        // when & then
        StepVerifier.withVirtualTime(() -> dreamService.interpret(promptType, nickname, dreamContent))
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(10))
                .expectErrorMatches(exception ->
                        exception instanceof ClovaApiException &&
                                ((ClovaApiException) exception).getErrorCode() == ErrorCode.CLOVA_API_TIME_OUT_ERROR)
                .verify();
    }

    @Test
    void 해몽결과가_6개가_아닌_경우_예외가_발생한다() {
        // given
        String clovaResponse = """
            {
                "messages": ["해몽 결과1", "해몽 결과2", "해몽 결과3", "해몽 결과4", "해몽 결과5"],
                "category": "기쁨"
            }
        """;
        given(clovaStudioChatService.sendRequest(anyString(), anyString()))
                .willReturn(Mono.just(clovaResponse));

        // when & then
        StepVerifier.create(dreamService.interpret("fairy", "nickname", "dream"))
                .expectErrorSatisfies(throwable -> {
                    assertThat(throwable).isInstanceOf(ClovaApiException.class);
                    // 캐스팅 후 세부 정보 검증
                    ClovaApiException exception = (ClovaApiException) throwable;
                    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CLOVA_API_INVALID_RESPONSE_MESSAGE);
                })
                .verify();
    }

    @Test
    void 해몽결과가_null인_경우_예외가_발생한다() {
        // given
        String clovaResponse = """
            {
                "category": "기쁨"
            }
        """;
        given(clovaStudioChatService.sendRequest(anyString(), anyString()))
                .willReturn(Mono.just(clovaResponse));

        // when & then
        StepVerifier.create(dreamService.interpret("fairy", "nickname", "dream"))
                .expectErrorSatisfies(throwable -> {
                    assertThat(throwable).isInstanceOf(ClovaApiException.class);
                    // 캐스팅 후 세부 정보 검증
                    ClovaApiException exception = (ClovaApiException) throwable;
                    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CLOVA_API_INVALID_RESPONSE_MESSAGE);
                })
                .verify();
    }

    @Test
    void 카테고리_결과가_null인_경우_예외가_발생한다() {
        // given
        String clovaResponse = """
            {
                "messages": ["해몽 결과1", "해몽 결과2", "해몽 결과3", "해몽 결과4", "해몽 결과5", "해몽 결과6"]
            }
        """;
        given(clovaStudioChatService.sendRequest(anyString(), anyString()))
                .willReturn(Mono.just(clovaResponse));

        // when & then
        StepVerifier.create(dreamService.interpret("fairy", "nickname", "dream"))
                .expectErrorSatisfies(throwable -> {
                    assertThat(throwable).isInstanceOf(ClovaApiException.class);
                    ClovaApiException exception = (ClovaApiException) throwable;
                    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CLOVA_API_INVALID_RESPONSE_CATEGORY);
                })
                .verify();
    }

    @Test
    void 카테고리_결과가_없는_카테고리인_경우_예외가_발생한다() {
        // given
        String clovaResponse = """
            {
                "messages": ["해몽 결과1", "해몽 결과2", "해몽 결과3", "해몽 결과4", "해몽 결과5", "해몽 결과6"],
                "category": "없는카테고리"
            }
        """;
        given(clovaStudioChatService.sendRequest(anyString(), anyString()))
                .willReturn(Mono.just(clovaResponse));

        // when & then
        StepVerifier.create(dreamService.interpret("fairy", "nickname", "dream"))
                .expectErrorSatisfies(throwable -> {
                    assertThat(throwable).isInstanceOf(ClovaApiException.class);
                    ClovaApiException exception = (ClovaApiException) throwable;
                    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CLOVA_API_INVALID_RESPONSE_CATEGORY);
                })
                .verify();
    }
}
