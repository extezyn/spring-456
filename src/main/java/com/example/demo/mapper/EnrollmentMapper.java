package com.example.demo.mapper;

import com.example.demo.dto.EnrollmentDto;
import com.example.demo.entity.Course;
import com.example.demo.entity.Enrollment;
import com.example.demo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {

    @Mappings({
            @Mapping(source = "student.id", target = "studentId"),
            @Mapping(source = "course.id", target = "courseId")
    })
    EnrollmentDto toDto(Enrollment enrollment);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "student", source = "student"),
            @Mapping(target = "course", source = "course")
    })
    Enrollment fromDto(EnrollmentDto dto, User student, Course course);
}


