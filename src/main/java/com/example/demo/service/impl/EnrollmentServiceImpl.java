package com.example.demo.service.impl;

import com.example.demo.dto.EnrollmentDto;
import com.example.demo.entity.Course;
import com.example.demo.entity.Enrollment;
import com.example.demo.entity.User;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.EnrollmentMapper;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.EnrollmentRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper enrollmentMapper;

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentDto> getByStudent(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found: " + studentId));
        return enrollmentRepository.findByStudent(student).stream()
                .map(enrollmentMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentDto> getByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found: " + courseId));
        return enrollmentRepository.findByCourse(course).stream()
                .map(enrollmentMapper::toDto)
                .toList();
    }

    @Override
    public EnrollmentDto enroll(EnrollmentDto dto) {
        User student = userRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new NotFoundException("Student not found: " + dto.getStudentId()));
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found: " + dto.getCourseId()));

        enrollmentRepository.findByStudentAndCourse(student, course)
                .ifPresent(e -> {
                    throw new BadRequestException("Student already enrolled to this course");
                });

        Enrollment enrollment = enrollmentMapper.fromDto(dto, student, course);
        enrollment.setEnrollmentDate(LocalDateTime.now());
        return enrollmentMapper.toDto(enrollmentRepository.save(enrollment));
    }

    @Override
    public void unenroll(Long enrollmentId) {
        if (!enrollmentRepository.existsById(enrollmentId)) {
            throw new NotFoundException("Enrollment not found: " + enrollmentId);
        }
        enrollmentRepository.deleteById(enrollmentId);
    }
}


