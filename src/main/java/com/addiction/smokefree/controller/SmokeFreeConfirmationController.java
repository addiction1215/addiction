package com.addiction.smokefree.controller;

import com.addiction.global.ApiResponse;
import com.addiction.smokefree.controller.request.SmokeFreeConfirmationRequest;
import com.addiction.smokefree.service.SmokeFreeConfirmationService;
import com.addiction.smokefree.service.response.SmokeFreeConfirmationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/smoke-free-confirmations")
public class SmokeFreeConfirmationController {

    private final SmokeFreeConfirmationService smokeFreeConfirmationService;

    @PostMapping
    public ApiResponse<SmokeFreeConfirmationResponse> confirm(
            @RequestBody @Valid SmokeFreeConfirmationRequest request
    ) {
        return ApiResponse.ok(smokeFreeConfirmationService.confirm(request.toServiceRequest()));
    }
}
