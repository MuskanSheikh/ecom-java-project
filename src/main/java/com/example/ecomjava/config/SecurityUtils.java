package com.example.ecomjava.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SecurityUtils {
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    private final PasswordEncoder passwordEncoder;

    public SecurityUtils(UserDetailsServiceImpl userDetailsServiceImpl, PasswordEncoder passwordEncoder) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.passwordEncoder = passwordEncoder;
    }


    public static Long getCurrentUserId() {
        CustomUserDetail user = getCurrentUser();
        if(user != null && user.getUser() != null){
            return user.getUser().getId();
        }
        return null;
    }

    public static CustomUserDetail getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            return (CustomUserDetail) authentication.getPrincipal();
        }
        return null;
    }
}
