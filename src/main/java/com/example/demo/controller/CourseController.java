package com.example.demo.controller;

import com.example.demo.dto.CourseDto;
import com.example.demo.service.CourseService;
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
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
@Tag(name = "Курсы", description = "API для управления курсами")
@SecurityRequirement(name = "Bearer Authentication")
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "Получить список курсов", description = "Возвращает пагинированный список всех курсов")
    @ApiResponse(responseCode = "200", description = "Список курсов")
    @GetMapping
    public ResponseEntity<Page<CourseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(courseService.getAll(pageable));
    }

    @Operation(summary = "Получить курсы преподавателя", description = "Возвращает список курсов конкретного преподавателя")
    @ApiResponse(responseCode = "200", description = "Список курсов преподавателя")
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<Page<CourseDto>> getByTeacher(
            @Parameter(description = "ID преподавателя") @PathVariable Long teacherId,
            Pageable pageable) {
        return ResponseEntity.ok(courseService.getByTeacher(teacherId, pageable));
    }

    @Operation(summary = "Получить курс по ID", description = "Возвращает информацию о курсе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Курс найден"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getById(
            @Parameter(description = "ID курса") @PathVariable Long id) {
        return ResponseEntity.ok(courseService.getById(id));
    }

    @Operation(summary = "Создать курс", description = "Создаёт новый курс. Требуется роль TEACHER или ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Курс успешно создан"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    })
    @PostMapping
    public ResponseEntity<CourseDto> create(@RequestBody @Valid CourseDto dto) {
        return ResponseEntity.ok(courseService.create(dto));
    }

    @Operation(summary = "Обновить курс", description = "Обновляет информацию о курсе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Курс успешно обновлён"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> update(
            @Parameter(description = "ID курса") @PathVariable Long id,
            @RequestBody @Valid CourseDto dto) {
        return ResponseEntity.ok(courseService.update(id, dto));
    }

    @Operation(summary = "Удалить курс", description = "Удаляет курс по ID. Требуется роль TEACHER или ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Курс успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID курса") @PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
