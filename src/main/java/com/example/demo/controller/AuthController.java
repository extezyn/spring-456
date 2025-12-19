package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация", description = "API для входа, регистрации и обновления токенов")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final CustomUserDetailsService userDetailsService;

    @Operation(summary = "Вход в систему", description = "Аутентификация пользователя и получение JWT токена")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный вход",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Неверные учетные данные"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);
        long expiresIn = jwtTokenProvider.getExpirationInSeconds();
        return ResponseEntity.ok(new AuthResponse(token, refreshToken, expiresIn));
    }

    @Operation(summary = "Обновление токена", description = "Получение нового JWT токена с помощью refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Токен успешно обновлён",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Невалидный refresh token")
    })
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody @Valid RefreshTokenRequest request) {
        if (!jwtTokenProvider.validateToken(request.getRefreshToken())) {
            return ResponseEntity.status(401).build();
        }
        String username = jwtTokenProvider.getUsernameFromToken(request.getRefreshToken());
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        String token = jwtTokenProvider.generateToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);
        long expiresIn = jwtTokenProvider.getExpirationInSeconds();
        return ResponseEntity.ok(new AuthResponse(token, refreshToken, expiresIn));
    }

    @Operation(summary = "Регистрация студента", description = "Создание нового аккаунта студента. Пароль должен соответствовать требованиям безопасности.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован",
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации или пользователь уже существует")
    })
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Valid RegisterRequest request) {
        UserDto dto = new UserDto();
        dto.setUsername(request.getUsername());
        dto.setEmail(request.getEmail());
        dto.setPassword(request.getPassword());
        dto.setRoleName("STUDENT");
        dto.setEnabled(true);
        return ResponseEntity.ok(userService.create(dto));
    }
}


