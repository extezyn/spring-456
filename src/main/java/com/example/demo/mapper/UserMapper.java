package com.example.demo.mapper;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mappings({
            @Mapping(source = "role.name", target = "roleName"),
            @Mapping(target = "password", ignore = true)
    })
    UserDto toDto(User user);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "role", source = "role"),
            @Mapping(target = "enabled", expression = "java(true)")
    })
    User fromDto(UserDto dto, Role role);
}


