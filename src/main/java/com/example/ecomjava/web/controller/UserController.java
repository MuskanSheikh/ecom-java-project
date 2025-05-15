package com.example.ecomjava.web.controller;

import com.example.ecomjava.entity.UserEntity;
import com.example.ecomjava.service.UserEntityService;
import com.example.ecomjava.web.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/user")
public class UserController {
    @Autowired
    private UserEntityService userEntityService;

    @PostMapping("/create")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        UserEntity userResponse = userEntityService.createUser(userDTO);
        if(userResponse != null){
            return ResponseEntity.ok(Map.of("status",true));
        }else{
            return ResponseEntity.ok(Map.of("status",false));
        }
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN_USER')")
    public ResponseEntity<?> getUser(@PathVariable Long id){
        UserDTO response = userEntityService.getById(id);
        if(response != null){
            response.setPassword("");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return ResponseEntity.badRequest().body("user not found");
    }
}
