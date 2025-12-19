package com.example.demo.service.impl;

import com.example.demo.dto.MaterialDto;
import com.example.demo.entity.Lesson;
import com.example.demo.entity.Material;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.MaterialMapper;
import com.example.demo.repository.LessonRepository;
import com.example.demo.repository.MaterialRepository;
import com.example.demo.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;
    private final LessonRepository lessonRepository;
    private final MaterialMapper materialMapper;

    @Override
    @Transactional(readOnly = true)
    public List<MaterialDto> getByLesson(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NotFoundException("Lesson not found: " + lessonId));
        return materialRepository.findByLesson(lesson).stream()
                .map(materialMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MaterialDto getById(Long id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Material not found: " + id));
        return materialMapper.toDto(material);
    }

    @Override
    public MaterialDto create(MaterialDto dto) {
        Lesson lesson = lessonRepository.findById(dto.getLessonId())
                .orElseThrow(() -> new NotFoundException("Lesson not found: " + dto.getLessonId()));
        Material material = materialMapper.fromDto(dto, lesson);
        return materialMapper.toDto(materialRepository.save(material));
    }

    @Override
    public MaterialDto update(Long id, MaterialDto dto) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Material not found: " + id));
        Lesson lesson = lessonRepository.findById(dto.getLessonId())
                .orElseThrow(() -> new NotFoundException("Lesson not found: " + dto.getLessonId()));

        material.setFileUrl(dto.getFileUrl());
        material.setType(dto.getType());
        material.setLesson(lesson);

        return materialMapper.toDto(materialRepository.save(material));
    }

    @Override
    public void delete(Long id) {
        if (!materialRepository.existsById(id)) {
            throw new NotFoundException("Material not found: " + id);
        }
        materialRepository.deleteById(id);
    }
}


