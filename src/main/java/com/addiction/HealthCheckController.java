package com.addiction;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.addiction.global.ApiResponse;

@RestController
public class HealthCheckController {

	@GetMapping("/health-check")
	public ApiResponse<String> healthCheck() {
		return ApiResponse.ok("OK");
	}
}

