package com.example.demo.service;

import com.example.demo.dto.CourseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseService {

    Page<CourseDto> getAll(Pageable pageable);

    Page<CourseDto> getByTeacher(Long teacherId, Pageable pageable);

    CourseDto getById(Long id);

    CourseDto create(CourseDto dto);

    CourseDto update(Long id, CourseDto dto);

    void delete(Long id);
}


