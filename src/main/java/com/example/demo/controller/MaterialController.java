package com.example.demo.controller;

import com.example.demo.dto.MaterialDto;
import com.example.demo.service.MaterialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/materials")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;

    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<List<MaterialDto>> getByLesson(@PathVariable Long lessonId) {
        return ResponseEntity.ok(materialService.getByLesson(lessonId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(materialService.getById(id));
    }

    @PostMapping
    public ResponseEntity<MaterialDto> create(@RequestBody @Valid MaterialDto dto) {
        return ResponseEntity.ok(materialService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaterialDto> update(@PathVariable Long id, @RequestBody @Valid MaterialDto dto) {
        return ResponseEntity.ok(materialService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        materialService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


