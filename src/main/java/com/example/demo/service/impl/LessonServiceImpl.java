package com.example.demo.service.impl;

import com.example.demo.dto.LessonDto;
import com.example.demo.entity.Course;
import com.example.demo.entity.Lesson;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.LessonMapper;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.LessonRepository;
import com.example.demo.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final LessonMapper lessonMapper;

    @Override
    @Transactional(readOnly = true)
    public List<LessonDto> getByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found: " + courseId));
        return lessonRepository.findByCourse(course).stream()
                .map(lessonMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public LessonDto getById(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Lesson not found: " + id));
        return lessonMapper.toDto(lesson);
    }

    @Override
    public LessonDto create(LessonDto dto) {
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found: " + dto.getCourseId()));
        Lesson lesson = lessonMapper.fromDto(dto, course);
        return lessonMapper.toDto(lessonRepository.save(lesson));
    }

    @Override
    public LessonDto update(Long id, LessonDto dto) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Lesson not found: " + id));
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found: " + dto.getCourseId()));

        lesson.setTitle(dto.getTitle());
        lesson.setContent(dto.getContent());
        lesson.setCourse(course);

        return lessonMapper.toDto(lessonRepository.save(lesson));
    }

    @Override
    public void delete(Long id) {
        if (!lessonRepository.existsById(id)) {
            throw new NotFoundException("Lesson not found: " + id);
        }
        lessonRepository.deleteById(id);
    }
}


