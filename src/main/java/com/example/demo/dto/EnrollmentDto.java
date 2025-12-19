package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EnrollmentDto {

    private Long id;

    @NotNull
    private Long studentId;

    @NotNull
    private Long courseId;

    private LocalDateTime enrollmentDate;
}


