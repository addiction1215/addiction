package com.addiction.user.users.controller;

import com.addiction.global.ApiResponse;
import com.addiction.user.users.controller.request.LoginOauthRequest;
import com.addiction.user.users.controller.request.LoginRequest;
import com.addiction.user.users.controller.request.SendAuthCodeRequest;
import com.addiction.user.users.controller.request.UserSaveRequest;
import com.addiction.user.users.service.LoginService;
import com.addiction.user.users.service.UserService;
import com.addiction.user.users.service.response.LoginResponse;
import com.addiction.user.users.service.response.OAuthLoginResponse;
import com.addiction.user.users.service.response.SendAuthCodeResponse;
import com.addiction.user.users.service.response.UserSaveResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class LoginController {

    private final LoginService loginService;
    private final UserService userService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) throws JsonProcessingException {
        return ApiResponse.ok(loginService.normalLogin(loginRequest.toServiceRequest()));
    }

    @PostMapping("/oauth/login")
    public ApiResponse<OAuthLoginResponse> oauthLogin(@Valid @RequestBody LoginOauthRequest loginOauthRequest) throws JsonProcessingException {
        return ApiResponse.ok(loginService.oauthLogin(loginOauthRequest.toServiceRequest()));
    }

    @PostMapping("/join")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserSaveResponse> save(@RequestBody @Valid UserSaveRequest userSaveRequest) {
        return ApiResponse.created(userService.save(userSaveRequest.toServiceRequest()));
    }

    @PostMapping("/mail")
    public ApiResponse<SendAuthCodeResponse> sendMail(@Valid @RequestBody SendAuthCodeRequest sendAuthCodeRequest) {
        return ApiResponse.ok(loginService.sendMail(sendAuthCodeRequest.toServiceRequest()));
    }

}
