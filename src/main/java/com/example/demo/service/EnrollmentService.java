package com.example.demo.service;

import com.example.demo.dto.EnrollmentDto;

import java.util.List;

public interface EnrollmentService {

    List<EnrollmentDto> getByStudent(Long studentId);

    List<EnrollmentDto> getByCourse(Long courseId);

    EnrollmentDto enroll(EnrollmentDto dto);

    void unenroll(Long enrollmentId);
}


