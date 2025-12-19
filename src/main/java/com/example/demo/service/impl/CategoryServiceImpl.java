package com.example.demo.service.impl;

import com.example.demo.dto.CategoryDto;
import com.example.demo.entity.Category;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDto> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(categoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found: " + id));
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto create(CategoryDto dto) {
        Category category = categoryMapper.fromDto(dto);
        category.setId(null);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto update(Long id, CategoryDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found: " + id));
        category.setName(dto.getName());
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException("Category not found: " + id);
        }
        categoryRepository.deleteById(id);
    }
}


