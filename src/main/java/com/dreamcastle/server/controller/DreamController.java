package com.dreamcastle.server.controller;

import com.dreamcastle.server.dto.dream.InterpretationRequest;
import com.dreamcastle.server.dto.dream.InterpretationResponse;
import com.dreamcastle.server.service.dream.DreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/dreams")
@RequiredArgsConstructor
public class DreamController {

    private final DreamService dreamService;

    @PostMapping("/interpret")
    public Mono<InterpretationResponse> interpretDream(@RequestBody InterpretationRequest request) {
        return dreamService.interpret(request.promptType(), request.nickname(), request.content());
    }

}
