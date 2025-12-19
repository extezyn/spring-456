package com.example.demo.service;

import com.example.demo.dto.ReviewDto;

import java.util.List;

public interface ReviewService {

    List<ReviewDto> getByCourse(Long courseId);

    List<ReviewDto> getByStudent(Long studentId);

    ReviewDto create(ReviewDto dto);

    ReviewDto update(Long id, ReviewDto dto);

    void delete(Long id);
}


