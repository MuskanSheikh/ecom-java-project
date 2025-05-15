package com.example.ecomjava.web.dto;
import com.example.ecomjava.common.EnumUtils;
import com.example.ecomjava.entity.UserEntity;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String password;

    private String role;

    public static UserDTO getEntity(UserEntity entity) {
        UserDTO dto = new UserDTO();
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setPassword(entity.getPassword());
        dto.setRole(String.valueOf(EnumUtils.USER));
        return dto;
    }
}
