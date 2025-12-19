package com.example.demo.controller;

import com.example.demo.dto.EnrollmentDto;
import com.example.demo.service.EnrollmentService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
@Tag(name = "Записи на курсы", description = "API для управления записями студентов на курсы")
@SecurityRequirement(name = "Bearer Authentication")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Operation(summary = "Получить записи студента", description = "Возвращает список всех курсов, на которые записан студент")
    @ApiResponse(responseCode = "200", description = "Список записей студента")
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentDto>> getByStudent(
            @Parameter(description = "ID студента") @PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentService.getByStudent(studentId));
    }

    @Operation(summary = "Получить записи по курсу", description = "Возвращает список всех студентов, записанных на курс")
    @ApiResponse(responseCode = "200", description = "Список записей на курс")
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EnrollmentDto>> getByCourse(
            @Parameter(description = "ID курса") @PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getByCourse(courseId));
    }

    @Operation(summary = "Записаться на курс", description = "Создаёт запись студента на курс")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Студент успешно записан на курс"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации или студент уже записан")
    })
    @PostMapping
    public ResponseEntity<EnrollmentDto> enroll(@RequestBody @Valid EnrollmentDto dto) {
        return ResponseEntity.ok(enrollmentService.enroll(dto));
    }

    @Operation(summary = "Отписаться от курса", description = "Удаляет запись студента на курс")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Студент успешно отписан от курса"),
            @ApiResponse(responseCode = "404", description = "Запись не найдена")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> unenroll(
            @Parameter(description = "ID записи") @PathVariable Long id) {
        enrollmentService.unenroll(id);
        return ResponseEntity.noContent().build();
    }
}
