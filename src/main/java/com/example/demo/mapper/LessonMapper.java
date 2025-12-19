package com.example.demo.mapper;

import com.example.demo.dto.LessonDto;
import com.example.demo.entity.Course;
import com.example.demo.entity.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(source = "course.id", target = "courseId")
    LessonDto toDto(Lesson lesson);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "course", source = "course"),
            @Mapping(target = "materials", ignore = true),
            @Mapping(target = "title", source = "dto.title"),
            @Mapping(target = "content", source = "dto.content")
    })
    Lesson fromDto(LessonDto dto, Course course);
}


