package com.example.ecomjava.service;

import com.example.ecomjava.config.JwtTokenProvider;
import com.example.ecomjava.entity.UserEntity;
import com.example.ecomjava.repository.UserRepository;
import com.example.ecomjava.web.dto.LoginRequestDTO;
import com.example.ecomjava.web.dto.LoginResponseDTO;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository  userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public ResponseEntity<?> login(LoginRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUserName(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails users = (UserDetails) authentication.getPrincipal();
            String token = jwtTokenProvider.generateToken(users);
            LoginResponseDTO loginResponse = new LoginResponseDTO();
            Optional<UserEntity> optionalEntity = userRepository.findByEmail(loginRequest.getUserName());
            loginResponse.setToken(token);
            loginResponse.setType("Bearer");
            loginResponse.setRole(optionalEntity.get().getRole());
            return ResponseEntity.ok(loginResponse);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
