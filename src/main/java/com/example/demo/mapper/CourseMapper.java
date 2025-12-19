package com.example.demo.mapper;

import com.example.demo.dto.CourseDto;
import com.example.demo.entity.Category;
import com.example.demo.entity.Course;
import com.example.demo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mappings({
            @Mapping(source = "teacher.id", target = "teacherId"),
            @Mapping(source = "category.id", target = "categoryId")
    })
    CourseDto toDto(Course course);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "teacher", source = "teacher"),
            @Mapping(target = "category", source = "category"),
            @Mapping(target = "lessons", ignore = true)
    })
    Course fromDto(CourseDto dto, User teacher, Category category);
}


