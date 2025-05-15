package com.example.ecomjava.service;

import com.example.ecomjava.entity.UserEntity;
import com.example.ecomjava.web.dto.UserDTO;
import org.apache.coyote.BadRequestException;

public interface UserEntityService {
    UserEntity createUser(UserDTO userDTO);

    UserDTO getById(Long id);
}
