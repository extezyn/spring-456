package com.example.demo.service.impl;

import com.example.demo.dto.CourseDto;
import com.example.demo.entity.Category;
import com.example.demo.entity.Course;
import com.example.demo.entity.User;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.CourseMapper;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CourseMapper courseMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<CourseDto> getAll(Pageable pageable) {
        return courseRepository.findAll(pageable).map(courseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseDto> getByTeacher(Long teacherId, Pageable pageable) {
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundException("Teacher not found: " + teacherId));
        return courseRepository.findByTeacher(teacher, pageable).map(courseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDto getById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Course not found: " + id));
        return courseMapper.toDto(course);
    }

    @Override
    public CourseDto create(CourseDto dto) {
        User teacher = userRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new NotFoundException("Teacher not found: " + dto.getTeacherId()));
        Category category = null;
        if (dto.getCategoryId() != null) {
            category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category not found: " + dto.getCategoryId()));
        }
        Course course = courseMapper.fromDto(dto, teacher, category);
        return courseMapper.toDto(courseRepository.save(course));
    }

    @Override
    public CourseDto update(Long id, CourseDto dto) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Course not found: " + id));

        User teacher = userRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new NotFoundException("Teacher not found: " + dto.getTeacherId()));
        Category category = null;
        if (dto.getCategoryId() != null) {
            category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category not found: " + dto.getCategoryId()));
        }

        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setPrice(dto.getPrice());
        course.setTeacher(teacher);
        course.setCategory(category);

        return courseMapper.toDto(courseRepository.save(course));
    }

    @Override
    public void delete(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new NotFoundException("Course not found: " + id);
        }
        courseRepository.deleteById(id);
    }
}


