package com.example.demo.service;

import com.example.demo.dto.MaterialDto;

import java.util.List;

public interface MaterialService {

    List<MaterialDto> getByLesson(Long lessonId);

    MaterialDto getById(Long id);

    MaterialDto create(MaterialDto dto);

    MaterialDto update(Long id, MaterialDto dto);

    void delete(Long id);
}


