package com.addiction.user.users.controller

import com.addiction.global.ApiResponse
import com.addiction.user.users.dto.controller.request.LoginOauthRequest
import com.addiction.user.users.dto.service.response.OAuthLoginResponse
import com.addiction.user.users.service.LoginService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class LoginController(private val loginService: LoginService) {

    @PostMapping("/oauth/login")
    fun oauthLogin(@RequestBody @Valid loginOauthRequest: LoginOauthRequest) : ApiResponse<OAuthLoginResponse> {
        return ApiResponse.ok(loginService.oauthLogin(loginOauthRequest.toServiceRequest()));
    }

}
