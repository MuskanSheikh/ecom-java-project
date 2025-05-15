package com.example.ecomjava.web.dto;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String token;

    private String type = " Bearer";

    private String role;
}
