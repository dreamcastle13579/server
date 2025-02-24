package com.dreamcastle.server.service.clova;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SystemPromptFactoryTest {

    @InjectMocks
    private SystemPromptFactory systemPromptFactory;

    @Mock
    private FairySystemPrompt fairySystemPrompt;

    @BeforeEach
    void setUp() {
        given(fairySystemPrompt.getType()).willReturn(SystemPromptType.FAIRY);
        systemPromptFactory = new SystemPromptFactory(List.of(fairySystemPrompt));
    }

    @Test
    void 시스템_프롬포트에_맞는_전략을_찾는다() {
        // given
        // when
        SystemPromptStrategy strategy = systemPromptFactory.getStrategy(SystemPromptType.FAIRY);
        // then
        assertThat(strategy).isEqualTo(fairySystemPrompt);
    }

}