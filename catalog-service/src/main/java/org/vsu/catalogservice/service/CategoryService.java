package org.vsu.catalogservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vsu.catalogservice.dto.category.CategoryCreateRequest;
import org.vsu.catalogservice.dto.category.CategoryResponse;
import org.vsu.catalogservice.entity.Category;
import org.vsu.catalogservice.mapper.CatalogMapper;
import org.vsu.catalogservice.repository.CategoryRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CatalogMapper mapper;

    public CategoryResponse create(CategoryCreateRequest createRequest) {
        if (categoryRepository.existsByName(createRequest.getName())) {
            //TODO: Заменить на свой CategoryAlreadyExistsException()
            throw new RuntimeException("409");
        }

        Category entity = mapper.mapToEntity(createRequest);

        return mapper.mapToResponse(categoryRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public Category getByName(String name) {
        Category entity = categoryRepository.findByName(name)
                //TODO: Заменить на свйо CategoryNotFoundException()
                .orElseThrow(() -> new RuntimeException("404"));

        return entity;
    }

}
