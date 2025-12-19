package com.example.demo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReviewDto {

    private Long id;

    @Min(1)
    @Max(5)
    private int rating;

    @Size(max = 4000)
    private String comment;

    @NotNull
    private Long studentId;

    @NotNull
    private Long courseId;
}


