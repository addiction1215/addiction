package com.addiction.global.security

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.addiction.jwt.dto.LoginUserInfo;

@Service
class SecurityService(@Value("\${aes.key-value}") keyValue: String) {

    private val iv: String = keyValue.substring(0, 16)

    companion object {
        private const val ALG = "AES/CBC/PKCS5Padding"
    }

    fun getCurrentLoginUserInfo(): LoginUserInfo {
        val principal = SecurityContextHolder.getContext().authentication.principal

        if (principal is LoginUserInfo) {
            return principal
        }

        throw RuntimeException("Unknown principal type: ${principal::class.java.name}")
    }

    fun encrypt(text: String): String {
        return try {
            val cipher = createCipher(Cipher.ENCRYPT_MODE)
            val encrypted = cipher.doFinal(text.toByteArray(StandardCharsets.UTF_8))
            Base64.getEncoder().withoutPadding().encodeToString(encrypted)
        } catch (e: Exception) {
            throw RuntimeException("Error occurred during encryption: ${e.message}", e)
        }
    }

    fun decrypt(cipherText: String): String {
        return try {
            val cipher = createCipher(Cipher.DECRYPT_MODE)
            val decodedBytes = Base64.getDecoder().decode(cipherText)
            val decrypted = cipher.doFinal(decodedBytes)
            String(decrypted, StandardCharsets.UTF_8)
        } catch (e: Exception) {
            throw RuntimeException("Error occurred during decryption: ${e.message}", e)
        }
    }

    private fun createCipher(cipherMode: Int): Cipher {
        val cipher = Cipher.getInstance(ALG)
        val keySpec = SecretKeySpec(iv.toByteArray(StandardCharsets.UTF_8), "AES")
        val ivParamSpec = IvParameterSpec(iv.toByteArray(StandardCharsets.UTF_8))
        cipher.init(cipherMode, keySpec, ivParamSpec)
        return cipher
    }
}
