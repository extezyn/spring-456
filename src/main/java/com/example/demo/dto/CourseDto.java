package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseDto {

    private Long id;

    @NotBlank
    @Size(max = 200)
    private String title;

    @Size(max = 4000)
    private String description;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Long teacherId;

    private Long categoryId;
}


