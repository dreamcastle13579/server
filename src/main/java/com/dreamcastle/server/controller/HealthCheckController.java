package com.dreamcastle.server.controller;

import com.dreamcastle.server.dto.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

    @GetMapping
    public ApiResponse<String> healthCheck() {
        return ApiResponse.success("OK");
    }
}
