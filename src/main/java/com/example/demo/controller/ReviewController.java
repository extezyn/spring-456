package com.example.demo.controller;

import com.example.demo.dto.ReviewDto;
import com.example.demo.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ReviewDto>> getByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(reviewService.getByCourse(courseId));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ReviewDto>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(reviewService.getByStudent(studentId));
    }

    @PostMapping
    public ResponseEntity<ReviewDto> create(@RequestBody @Valid ReviewDto dto) {
        return ResponseEntity.ok(reviewService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDto> update(@PathVariable Long id, @RequestBody @Valid ReviewDto dto) {
        return ResponseEntity.ok(reviewService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


