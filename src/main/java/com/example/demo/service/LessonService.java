package com.example.demo.service;

import com.example.demo.dto.LessonDto;

import java.util.List;

public interface LessonService {

    List<LessonDto> getByCourse(Long courseId);

    LessonDto getById(Long id);

    LessonDto create(LessonDto dto);

    LessonDto update(Long id, LessonDto dto);

    void delete(Long id);
}


