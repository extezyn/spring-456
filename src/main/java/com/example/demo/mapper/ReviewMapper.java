package com.example.demo.mapper;

import com.example.demo.dto.ReviewDto;
import com.example.demo.entity.Course;
import com.example.demo.entity.Review;
import com.example.demo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mappings({
            @Mapping(source = "student.id", target = "studentId"),
            @Mapping(source = "course.id", target = "courseId")
    })
    ReviewDto toDto(Review review);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "student", source = "student"),
            @Mapping(target = "course", source = "course")
    })
    Review fromDto(ReviewDto dto, User student, Course course);
}


