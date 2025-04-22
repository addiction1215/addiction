package com.addiction.global.security.config

import com.addiction.jwt.JwtTokenProvider
import com.addiction.jwt.filter.JwtAuthenticationFilter
import com.addiction.jwt.filter.JwtExceptionHandlerFilter
import com.addiction.user.users.entity.enums.Role
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher

@Configuration
@EnableWebSecurity
class SecurityConfig(
    val jwtTokenProvider: JwtTokenProvider
) {

    @Bean
    fun encoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    protected fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf(AbstractHttpConfigurer<*, *>::disable)
            .headers { headers ->
                headers.contentSecurityPolicy { policy ->
                    policy.policyDirectives("frame-ancestors 'self'")
                }
            }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(*getPublicMatchers()).permitAll()
                    .anyRequest().hasAnyAuthority(Role.USER.name, Role.ADMIN.name)
            }
            .logout { logout -> logout.logoutSuccessUrl("/") }
            .addFilterBefore(JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(JwtExceptionHandlerFilter(), JwtAuthenticationFilter::class.java) // JwtAuthenticationFilter에서 발생한 에러를 처리하기 위한 필터

        return http.build()
    }

    private fun getPublicMatchers(): Array<RequestMatcher> {
        return arrayOf(
            AntPathRequestMatcher("/api/v1/jwt/**"),
            AntPathRequestMatcher("/api/v1/auth/**"),
            AntPathRequestMatcher("/docs/**"),
            AntPathRequestMatcher("/health-check"),
            AntPathRequestMatcher("/favicon.ico")
        )
    }

}
