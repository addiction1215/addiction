package com.addiction.user.users.controller

import com.addiction.global.ApiResponse
import com.addiction.user.users.dto.controller.request.UserSaveRequest
import com.addiction.user.users.dto.controller.request.UserUpdateRequest
import com.addiction.user.users.dto.service.response.UserSaveResponse
import com.addiction.user.users.dto.service.response.UserUpdateResponse
import com.addiction.user.users.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class UserController(private val userService: UserService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun save(@RequestBody @Valid userSaveRequest: UserSaveRequest): ApiResponse<UserSaveResponse> {
        return ApiResponse.created(userService.save(userSaveRequest.toServiceRequest()))
    }

    @PatchMapping
    fun update(@RequestBody @Valid userUpdateRequest: UserUpdateRequest): ApiResponse<UserUpdateResponse> {
        return ApiResponse.ok(userService.update(userUpdateRequest.toServiceRequest()))
    }
}
