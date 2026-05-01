package org.vsu.catalogservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vsu.catalogservice.dto.category.CategoryCreateRequest;
import org.vsu.catalogservice.dto.category.CategoryResponse;
import org.vsu.catalogservice.entity.Category;
import org.vsu.catalogservice.mapper.CatalogMapper;
import org.vsu.catalogservice.repository.CategoryRepository;
import org.vsu.catalogservice.utils.exceptions.CategoryAlreadyExistsException;
import org.vsu.catalogservice.utils.exceptions.CategoryNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CatalogMapper mapper;

    public CategoryResponse create(CategoryCreateRequest createRequest) {
        if (categoryRepository.existsByName(createRequest.getName())) {
            throw new CategoryAlreadyExistsException(createRequest.getName());
        }

        Category entity = mapper.mapToEntity(createRequest);

        return mapper.mapToResponse(categoryRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public Category getByName(String name) {
        Category entity = categoryRepository.findByName(name)
                .orElseThrow(() -> new CategoryNotFoundException(name));

        return entity;
    }

}
