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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.vsu.catalogservice.dto.medicie.MedicineBasicInfo;
import org.vsu.catalogservice.dto.medicie.MedicineCreateRequest;
import org.vsu.catalogservice.dto.medicie.MedicineResponse;
import org.vsu.catalogservice.dto.ErrorResponse;

import java.math.BigDecimal;

@Tag(name = "Управление медикаментами",
        description = "Отвечает за создание, поиск и получение информации о медикаментах в каталоге аптеки.")
public interface MedicineAPI {

    @Operation(summary = "Создание нового медикамента",
            description = "Добавляет новый препарат в каталог. Требует прав администратора. " +
                    "Автоматически связывается с существующей категорией и производителем по их названиям.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Медикамент успешно создан",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MedicineResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "id": 1,
                                              "name": "Аспирин",
                                              "description": "Противовоспалительное, жаропонижающее средство",
                                              "price": 150.50,
                                              "dosage": 500,
                                              "prescriptionRequired": false,
                                              "category": {
                                                "id": 1,
                                                "name": "Обезболивающие"
                                              },
                                              "manufacturer": {
                                                "id": 1,
                                                "name": "Байер",
                                                "country": "Германия"
                                              }
                                            }
                                            """
                            )
                    )),
            @ApiResponse(responseCode = "400",
                    description = "Некорректные данные (название пустое, цена отрицательная и т.д.)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
            @ApiResponse(responseCode = "401",
                    description = "Пользователь не авторизован",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
            @ApiResponse(responseCode = "403",
                    description = "Доступ запрещён. Требуется роль ADMIN.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
            @ApiResponse(responseCode = "404",
                    description = "Категория или производитель с указанным названием не найдены",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    ResponseEntity<MedicineResponse> create(
            @Parameter(description = "DTO для создания медикамента", required = true)
            @Valid @RequestBody MedicineCreateRequest createRequest,
            @Parameter(description = "JWT токен пользователя (извлекается автоматически)", hidden = true)
            @AuthenticationPrincipal Jwt jwt
    );

    @Operation(summary = "Получение медикамента по ID",
            description = "Возвращает базовую информацию о медикаменте по его идентификатору. " +
                    "Используется для быстрого отображения в списках и корзине.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Медикамент найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MedicineBasicInfo.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "id": 1,
                                              "name": "Аспирин",
                                              "price": 150.50
                                            }
                                            """
                            )
                    )),
            @ApiResponse(responseCode = "404",
                    description = "Медикамент с указанным ID не найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    ResponseEntity<MedicineBasicInfo> getById(
            @Parameter(description = "ID медикамента", required = true, example = "1")
            @PathVariable("id") Long id
    );

    @Operation(summary = "Получение медикамента по названию",
            description = "Возвращает полную информацию о медикаменте по его точному названию.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Медикамент найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MedicineResponse.class)
                    )),
            @ApiResponse(responseCode = "404",
                    description = "Медикамент с указанным названием не найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    ResponseEntity<MedicineResponse> getByName(
            @Parameter(description = "Название медикамента", required = true, example = "Аспирин")
            @PathVariable("name") String name
    );

    @Operation(summary = "Поиск медикаментов по фильтрам",
            description = "Выполняет поиск медикаментов с фильтрацией по названию, категории, производителю, стране и цене. " +
                    "Поддерживает пагинацию и сортировку. Все параметры фильтрации опциональны.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Страница с результатами поиска",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Page.class)
                    )),
            @ApiResponse(responseCode = "400",
                    description = "Некорректные параметры фильтрации (минимальная цена больше максимальной и т.д.)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    ResponseEntity<Page<MedicineResponse>> search(
            @Parameter(description = "Название медикамента (частичное совпадение)", example = "спирин")
            @RequestParam(value = "name", required = false) String name,

            @Parameter(description = "Название категории", example = "Обезболивающие")
            @RequestParam(value = "categoryName", required = false) String categoryName,

            @Parameter(description = "Название производителя", example = "Байер")
            @RequestParam(value = "manufacturerName", required = false) String manufacturerName,

            @Parameter(description = "Страна производителя", example = "Германия")
            @RequestParam(value = "manufacturerCountry", required = false) String manufacturerCountry,

            @Parameter(description = "Максимальная цена", example = "500.00")
            @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,

            @Parameter(description = "Минимальная цена", example = "100.00")
            @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,

            @Parameter(description = "Параметры пагинации и сортировки")
            @PageableDefault(
                    page = 0,
                    size = 15,
                    sort = "price",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    );
}