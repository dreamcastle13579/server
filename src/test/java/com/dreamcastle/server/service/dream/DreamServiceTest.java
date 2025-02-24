package com.dreamcastle.server.service.dream;

import com.dreamcastle.server.dto.clova.ClovaStudioChatResponse;
import com.dreamcastle.server.dto.clova.Message;
import com.dreamcastle.server.dto.clova.Result;
import com.dreamcastle.server.dto.clova.Role;
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
        String clovaResponse = "{\"messages\": [\"해몽 결과\", \"해몽 결과1\"], \"category\" : \"슬픔\"}";

        ClovaStudioChatResponse mockResponse = new ClovaStudioChatResponse(
                new Result(new Message(Role.assistant, clovaResponse)));

        given(clovaStudioChatService.sendRequest(promptType, formattedContent))
                .willReturn(Mono.just(mockResponse));

        // When
        Mono<InterpretationResponse> result = dreamService.interpret(promptType, nickname, dreamContent);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(response -> {
                    // 실제 response 객체의 messages 필드가 예상한 값과 일치하는지 확인
                    return response.messages() != null &&
                            response.messages().size() == 2 &&
                            response.messages().get(0).equals("해몽 결과") &&
                            response.messages().get(1).equals("해몽 결과1") &&
                            response.category().equals(Category.슬픔);
                })
                .verifyComplete();
    }

    @Test
    void 해몽시_시간이_정한_limit을_넘기면_실패한다() {
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
    void 잘못된_해몽_결과가_나올시_실패한다() {
        // given
        String promptType = "dream";
        String nickname = "테스트유저";
        String dreamContent = "어제 꾼 꿈입니다.";
        String formattedContent = nickname + ": " + dreamContent;
        String clovaResponse = "{\"messages\": [\"해몽 결과\", \"해몽 결과1\"], \"category\" : \"슬픔,\"}";

        ClovaStudioChatResponse mockResponse = new ClovaStudioChatResponse(
                new Result(new Message(Role.assistant, clovaResponse)));

        given(clovaStudioChatService.sendRequest(promptType, formattedContent))
                .willReturn(Mono.just(mockResponse));

        // when
        Mono<InterpretationResponse> result = dreamService.interpret(promptType, nickname, dreamContent);

        // then
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof ClovaApiException &&
                                ((ClovaApiException) throwable).getErrorCode() == ErrorCode.CLOVA_API_ERROR)
                .verify();
    }
}
