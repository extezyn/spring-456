package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LessonDto {

    private Long id;

    @NotBlank
    @Size(max = 200)
    private String title;

    @Size(max = 10000)
    private String content;

    @NotNull
    private Long courseId;
}


