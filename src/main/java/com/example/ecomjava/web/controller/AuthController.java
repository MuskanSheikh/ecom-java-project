package com.example.ecomjava.web.controller;

import com.example.ecomjava.repository.UserTokenRepository;
import com.example.ecomjava.service.AuthService;
import com.example.ecomjava.web.dto.LoginRequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final UserTokenRepository userTokenRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        return authService.login(loginRequest);
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No JWT token found in request headers");
        }

        String jwt = authHeader.substring(7);

        return userTokenRepository.findByToken(jwt)
                .map(token -> {
                    token.setExpired(true);
                    userTokenRepository.save(token);
                    return ResponseEntity.ok("Logged out successfully");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token not found"));
    }
}
