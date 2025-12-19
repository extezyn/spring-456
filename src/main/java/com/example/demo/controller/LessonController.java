package com.example.demo.controller;

import com.example.demo.dto.LessonDto;
import com.example.demo.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<LessonDto>> getByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(lessonService.getByCourse(courseId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(lessonService.getById(id));
    }

    @PostMapping
    public ResponseEntity<LessonDto> create(@RequestBody @Valid LessonDto dto) {
        return ResponseEntity.ok(lessonService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonDto> update(@PathVariable Long id, @RequestBody @Valid LessonDto dto) {
        return ResponseEntity.ok(lessonService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        lessonService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


