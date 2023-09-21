package com.smu.odowan.dto;

import com.smu.odowan.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@ToString
@Builder
public class UserDTO {
    private Long userIdx;

    private String email;

    private String password;

    private String name;

    private String phone;

    private String address;

    public User toEntity() {
        return User.builder()
                .email(email)
                .password(password)
                .name(name)
                .phone(phone)
                .address(address)
                .build();
    }
}
