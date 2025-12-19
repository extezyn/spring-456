package com.example.demo.mapper;

import com.example.demo.dto.MaterialDto;
import com.example.demo.entity.Lesson;
import com.example.demo.entity.Material;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface MaterialMapper {

    @Mapping(source = "lesson.id", target = "lessonId")
    MaterialDto toDto(Material material);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "lesson", source = "lesson")
    })
    Material fromDto(MaterialDto dto, Lesson lesson);
}


