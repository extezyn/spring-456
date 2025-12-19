package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "API для управления пользователями")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Получить список пользователей", description = "Возвращает пагинированный список всех пользователей")
    @ApiResponse(responseCode = "200", description = "Список пользователей")
    @GetMapping
    public ResponseEntity<Page<UserDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(userService.getAll(pageable));
    }

    @Operation(summary = "Получить пользователя по ID", description = "Возвращает информацию о пользователе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(
            @Parameter(description = "ID пользователя") @PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @Operation(summary = "Создать пользователя", description = "Создаёт нового пользователя. Требуется роль ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно создан"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    })
    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody @Valid UserDto dto) {
        return ResponseEntity.ok(userService.create(dto));
    }

    @Operation(summary = "Обновить пользователя", description = "Обновляет информацию о пользователе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлён"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(
            @Parameter(description = "ID пользователя") @PathVariable Long id,
            @RequestBody @Valid UserDto dto) {
        return ResponseEntity.ok(userService.update(id, dto));
    }

    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя по ID. Требуется роль ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пользователь успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID пользователя") @PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


