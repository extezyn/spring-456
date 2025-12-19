package com.example.demo.dto;

import com.example.demo.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {

    private Long id;

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Email
    private String email;

    // Только при создании/обновлении, не возвращаем наружу в ответах
    @ValidPassword
    private String password;

    private boolean enabled;

    @NotBlank
    private String roleName;
}


