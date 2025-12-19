package com.example.demo.controller;

import com.example.demo.dto.CategoryDto;
import com.example.demo.service.CategoryService;
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
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "Категории", description = "API для управления категориями курсов")
@SecurityRequirement(name = "Bearer Authentication")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Получить список категорий", description = "Возвращает пагинированный список всех категорий")
    @ApiResponse(responseCode = "200", description = "Список категорий")
    @GetMapping
    public ResponseEntity<Page<CategoryDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(categoryService.getAll(pageable));
    }

    @Operation(summary = "Получить категорию по ID", description = "Возвращает информацию о категории")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Категория найдена"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getById(
            @Parameter(description = "ID категории") @PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @Operation(summary = "Создать категорию", description = "Создаёт новую категорию. Требуется роль ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Категория успешно создана"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    })
    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody @Valid CategoryDto dto) {
        return ResponseEntity.ok(categoryService.create(dto));
    }

    @Operation(summary = "Обновить категорию", description = "Обновляет информацию о категории")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Категория успешно обновлена"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> update(
            @Parameter(description = "ID категории") @PathVariable Long id,
            @RequestBody @Valid CategoryDto dto) {
        return ResponseEntity.ok(categoryService.update(id, dto));
    }

    @Operation(summary = "Удалить категорию", description = "Удаляет категорию по ID. Требуется роль ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Категория успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID категории") @PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
