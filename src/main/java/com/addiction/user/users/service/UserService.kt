package com.addiction.user.users.service

import com.addiction.global.security.SecurityService
import com.addiction.user.users.dto.service.request.UserSaveServiceRequest
import com.addiction.user.users.dto.service.request.UserUpdateServiceRequest
import com.addiction.user.users.dto.service.response.UserSaveResponse
import com.addiction.user.users.dto.service.response.UserUpdateResponse
import com.addiction.user.users.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
open class UserService(
    private val userRepository: UserRepository,
    private val securityService: SecurityService
) {
    fun save(userSaveServiceRequest: UserSaveServiceRequest): UserSaveResponse {
        return UserSaveResponse.createResponse(userRepository.save(userSaveServiceRequest.toEntity()))
    }

    fun update(userUpdateServiceRequest: UserUpdateServiceRequest): UserUpdateResponse {
        val user = userRepository.findById(securityService.currentLoginUserInfo.userId)
        user.update(
            userUpdateServiceRequest.sex,
            userUpdateServiceRequest.birthDay
        )
        return UserUpdateResponse.createResponse(user);
    }
}
