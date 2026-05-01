package org.vsu.catalogservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vsu.catalogservice.dto.category.CategoryCreateRequest;
import org.vsu.catalogservice.dto.category.CategoryResponse;
import org.vsu.catalogservice.service.CategoryService;

import static org.vsu.catalogservice.utils.constants.CatalogConstants.API_V1_CATEGORIES;
import static org.vsu.catalogservice.utils.constants.CatalogConstants.NAME;

@RestController
@RequestMapping(API_V1_CATEGORIES)
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@RequestBody @Valid CategoryCreateRequest createRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(createRequest));
    }

    @GetMapping(NAME)
    public ResponseEntity<CategoryResponse> getByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(categoryService.getByName(name));
    }
}
