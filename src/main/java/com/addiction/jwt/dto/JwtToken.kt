package com.addiction.jwt.dto

data class JwtToken(
    val accessToken: String,
    val refreshToken: String,
    val grantType: String,
    val expiresIn: Long
) {
    companion object {
        fun of(accessToken: String, refreshToken: String, grantType: String, expiresIn: Long): JwtToken {
            return JwtToken(accessToken, refreshToken, grantType, expiresIn)
        }
    }
}
