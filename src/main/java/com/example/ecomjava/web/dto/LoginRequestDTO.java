package com.example.ecomjava.web.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private long id;
    private String userName;
    private String password;
}
