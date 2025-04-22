package com.addiction.jwt.dto

data class LoginUserInfo(
    val userId: Int
) {
    companion object {
        fun of(userId: Int): LoginUserInfo {
            return LoginUserInfo(userId)
        }
    }
}
