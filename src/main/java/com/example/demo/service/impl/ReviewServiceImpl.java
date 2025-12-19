package com.example.demo.service.impl;

import com.example.demo.dto.ReviewDto;
import com.example.demo.entity.Course;
import com.example.demo.entity.Review;
import com.example.demo.entity.User;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.ReviewMapper;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ReviewMapper reviewMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDto> getByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found: " + courseId));
        return reviewRepository.findByCourse(course).stream()
                .map(reviewMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDto> getByStudent(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found: " + studentId));
        return reviewRepository.findByStudent(student).stream()
                .map(reviewMapper::toDto)
                .toList();
    }

    @Override
    public ReviewDto create(ReviewDto dto) {
        User student = userRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new NotFoundException("Student not found: " + dto.getStudentId()));
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found: " + dto.getCourseId()));
        Review review = reviewMapper.fromDto(dto, student, course);
        return reviewMapper.toDto(reviewRepository.save(review));
    }

    @Override
    public ReviewDto update(Long id, ReviewDto dto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review not found: " + id));
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        return reviewMapper.toDto(reviewRepository.save(review));
    }

    @Override
    public void delete(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new NotFoundException("Review not found: " + id);
        }
        reviewRepository.deleteById(id);
    }
}


