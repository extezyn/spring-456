package com.example.demo.dto;

import com.example.demo.entity.MaterialType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MaterialDto {

    private Long id;

    @NotBlank
    private String fileUrl;

    @NotNull
    private MaterialType type;

    @NotNull
    private Long lessonId;
}


