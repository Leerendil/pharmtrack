package org.vsu.catalogservice.controller.swagger_api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vsu.catalogservice.dto.category.CategoryCreateRequest;
import org.vsu.catalogservice.dto.category.CategoryResponse;
import org.vsu.catalogservice.entity.Category;
import org.vsu.catalogservice.dto.ErrorResponse;

@Tag(name = "Управление категориями",
        description = "Отвечает за создание и получение категорий лекарственных препаратов.")
public interface CategoryAPI {

    @Operation(summary = "Создание новой категории",
            description = "Добавляет новую категорию лекарственных препаратов в каталог.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Категория успешно создана",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoryResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "name": "Обезболивающие",
                                              "description": "Препараты для снятия боли различного происхождения"
                                            }
                                            """
                            )
                    )),
            @ApiResponse(responseCode = "400",
                    description = "Некорректные данные (название или описание пустые, категория уже существует)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    ResponseEntity<CategoryResponse> create(
            @Parameter(description = "DTO для создания категории", required = true)
            @Valid @RequestBody CategoryCreateRequest createRequest
    );

    @Operation(summary = "Получение категории по названию",
            description = "Возвращает информацию о категории по её точному названию.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Категория найдена",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Category.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "name": "Обезболивающие",
                                              "description": "Препараты для снятия боли различного происхождения"
                                            }
                                            """
                            )
                    )),
            @ApiResponse(responseCode = "404",
                    description = "Категория с указанным названием не найдена",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    ResponseEntity<Category> getByName(
            @Parameter(description = "Название категории", required = true, example = "Обезболивающие")
            @PathVariable("name") String name
    );
}