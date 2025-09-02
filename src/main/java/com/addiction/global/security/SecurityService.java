package com.addiction.global.security;

import com.addiction.jwt.dto.LoginUserInfo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    public LoginUserInfo getCurrentLoginUserInfo() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof LoginUserInfo) {
            return (LoginUserInfo) principal;
        }

        throw new RuntimeException("Unknown principal type: " + principal.getClass().getName());
    }
}
