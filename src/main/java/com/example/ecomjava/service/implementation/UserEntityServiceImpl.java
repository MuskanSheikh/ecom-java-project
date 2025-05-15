package com.example.ecomjava.service.implementation;

import com.example.ecomjava.common.EnumUtils;
import com.example.ecomjava.entity.UserEntity;
import com.example.ecomjava.repository.UserRepository;
import com.example.ecomjava.service.UserEntityService;
import com.example.ecomjava.web.dto.UserDTO;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserEntityServiceImpl implements UserEntityService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public UserEntity createUser(UserDTO userDTO) {
        Optional<UserEntity> userEntity = userRepository.findByEmail(userDTO.getEmail());
        if (userEntity.isPresent()) return null;
        UserEntity users = UserEntity.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .phone(userDTO.getPhone())
                .role(userDTO.getRole())
                .password(encoder.encode(userDTO.getPassword())).build();
        return userRepository.save(users);
    }

    @Override
    public UserDTO getById(Long id) {
        Optional<UserEntity> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return UserDTO.getEntity(userOptional.get());
        }
        return null;
    }
}
