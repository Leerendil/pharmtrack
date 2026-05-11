package org.vsu.orderservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.vsu.orderservice.entity.CartItem;
import org.vsu.orderservice.dto.ErrorResponse;

import java.util.List;

@Tag(name = "Управление корзиной",
        description = "Отвечает за управление корзиной покупок пользователя: добавление, удаление товаров, очистка корзины и получение списка товаров.")
public interface CartAPI {

    @Operation(summary = "Получение всех товаров в корзине",
            description = "Возвращает список всех товаров, находящихся в корзине текущего авторизованного пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Список товаров успешно получен",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CartItem.class)),
                            examples = @ExampleObject(
                                    value = """
                                            [
                                              {
                                                "medicineId": 1,
                                                "name": "Аспирин",
                                                "quantity": 2,
                                                "price": 150.50
                                              },
                                              {
                                                "medicineId": 2,
                                                "name": "Нурофен",
                                                "quantity": 1,
                                                "price": 250.00
                                              }
                                            ]
                                            """
                            )
                    )),
            @ApiResponse(responseCode = "401",
                    description = "Пользователь не авторизован",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    ResponseEntity<List<CartItem>> getAllItems(
            @Parameter(description = "JWT токен пользователя (извлекается автоматически)", hidden = true)
            @AuthenticationPrincipal Jwt jwt
    );

    @Operation(summary = "Добавление товара в корзину",
            description = "Добавляет указанный товар в корзину текущего пользователя. " +
                    "Если товар уже есть в корзине, увеличивается его количество.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Товар успешно добавлен в корзину",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class),
                            examples = @ExampleObject(value = "true")
                    )),
            @ApiResponse(responseCode = "400",
                    description = "Некорректные данные товара (отрицательное количество, некорректная цена и т.д.)",
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
            @ApiResponse(responseCode = "404",
                    description = "Товар с указанным ID не найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    ResponseEntity<Boolean> addItem(
            @Parameter(description = "Товар для добавления в корзину", required = true)
            @Valid @RequestBody CartItem item,
            @Parameter(description = "JWT токен пользователя (извлекается автоматически)", hidden = true)
            @AuthenticationPrincipal Jwt jwt
    );

    @Operation(summary = "Удаление нескольких товаров из корзины",
            description = "Удаляет список товаров из корзины по их ID. " +
                    "Полезно при оформлении заказа или массовом удалении.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Товары успешно удалены из корзины",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class),
                            examples = @ExampleObject(value = "true")
                    )),
            @ApiResponse(responseCode = "400",
                    description = "Некорректный список ID товаров",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
            @ApiResponse(responseCode = "401",
                    description = "Пользователь не авторизован",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    ResponseEntity<Boolean> removeListOfItems(
            @Parameter(description = "JWT токен пользователя (извлекается автоматически)", hidden = true)
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(description = "Массив ID товаров для удаления", required = true, example = "[1, 2, 3]")
            @RequestBody int[] ids
    );

    @Operation(summary = "Удаление одного товара из корзины",
            description = "Удаляет указанный товар из корзины текущего пользователя по ID медикамента.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Товар успешно удалён из корзины",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class),
                            examples = @ExampleObject(value = "true")
                    )),
            @ApiResponse(responseCode = "401",
                    description = "Пользователь не авторизован",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
            @ApiResponse(responseCode = "404",
                    description = "Товар с указанным ID не найден в корзине",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    ResponseEntity<Boolean> removeItem(
            @Parameter(description = "JWT токен пользователя (извлекается автоматически)", hidden = true)
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(description = "ID медикамента для удаления", required = true, example = "123")
            @PathVariable("medicineId") String medicineId
    );

    @Operation(summary = "Очистка корзины",
            description = "Полностью очищает корзину текущего пользователя, удаляя все товары.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Корзина успешно очищена",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class),
                            examples = @ExampleObject(value = "true")
                    )),
            @ApiResponse(responseCode = "401",
                    description = "Пользователь не авторизован",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    ResponseEntity<Boolean> clearCart(
            @Parameter(description = "JWT токен пользователя (извлекается автоматически)", hidden = true)
            @AuthenticationPrincipal Jwt jwt
    );
}