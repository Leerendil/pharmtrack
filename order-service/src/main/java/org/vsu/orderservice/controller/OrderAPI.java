package org.vsu.orderservice.controller;

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.vsu.orderservice.dto.OrderResponse;
import org.vsu.orderservice.dto.OrderStatusUpdate;
import org.vsu.orderservice.dto.ErrorResponse;

import java.util.UUID;

@Tag(name = "Управление заказами",
        description = "Отвечает за создание заказов, управление их статусами и получение информации о заказах.")
public interface OrderAPI {

    @Operation(summary = "Создание нового заказа",
            description = "Создаёт новый заказ для текущего авторизованного пользователя. " +
                    "Заказ создаётся на основе текущей корзины пользователя. " +
                    "Начальный статус заказа — PENDING.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Заказ успешно создан",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                                 {
                                                   "id": "550e8400-e29b-41d4-a716-446655440000",
                                                   "buyerEmail": "john@example.com",
                                                   "status": "PENDING",
                                                   "createdAt": "2026-05-11T17:30:10.521"
                                                 }
                                                 """
                            )
                    )),
            @ApiResponse(responseCode = "400",
                    description = "Корзина пуста или произошла ошибка валидации",
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
    ResponseEntity<OrderResponse> create(
            @Parameter(description = "JWT токен пользователя (извлекается автоматически)", hidden = true)
            @AuthenticationPrincipal Jwt jwt
    );

    @Operation(summary = "Изменение статуса заказа (администратор)",
            description = "Позволяет администратору изменять статус любого заказа. " +
                    "Доступно только для пользователей с ролью ADMIN. " +
                    "Статус можно изменить на любой из допустимых: PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Статус заказа успешно обновлён администратором",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                                 {
                                                   "id": "550e8400-e29b-41d4-a716-446655440000",
                                                   "buyerEmail": "john@example.com",
                                                   "status": "CONFIRMED",
                                                   "createdAt": "2026-05-11T17:30:10.521"
                                                 }
                                                 """
                            )
                    )),
            @ApiResponse(responseCode = "400",
                    description = "Некорректные данные (статус не указан или заказ не найден)",
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
                    description = "Заказ с указанным ID не найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    ResponseEntity<OrderResponse> changeOrderStatus(
            @Parameter(description = "DTO с ID заказа и новым статусом", required = true)
            @Valid @RequestBody OrderStatusUpdate updateDto
    );

    @Operation(summary = "Управление статусом заказа (пользователь)",
            description = "Позволяет пользователю управлять статусом своего заказа. " +
                    "Доступные переходы статусов: " +
                    "- PENDING → CANCELLED (отменить ожидающий заказ) " +
                    "- CONFIRMED → DELIVERED (подтвердить доставку)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Статус заказа успешно обновлён пользователем",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class)
                    )),
            @ApiResponse(responseCode = "400",
                    description = "Некорректные данные или недопустимый переход статуса",
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
                    description = "Пользователь не является владельцем заказа",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
            @ApiResponse(responseCode = "404",
                    description = "Заказ с указанным ID не найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    ResponseEntity<OrderResponse> manageOrderStatus(
            @Parameter(description = "DTO с ID заказа и новым статусом", required = true)
            @Valid @RequestBody OrderStatusUpdate updateDto
    );

    @Operation(summary = "Получение заказа по ID",
            description = "Возвращает информацию о заказе по его уникальному идентификатору. " +
                    "Обычный пользователь может видеть только свои заказы. " +
                    "Администратор видит все заказы.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Заказ найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                                 {
                                                   "id": "550e8400-e29b-41d4-a716-446655440000",
                                                   "buyerEmail": "john@example.com",
                                                   "status": "PENDING",
                                                   "createdAt": "2026-05-11T17:30:10.521"
                                                 }
                                                 """
                            )
                    )),
            @ApiResponse(responseCode = "404",
                    description = "Заказ с указанным ID не найден",
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
                    description = "Доступ запрещён (пользователь не владелец заказа и не администратор)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    ResponseEntity<OrderResponse> getById(
            @Parameter(description = "UUID заказа", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable("id") UUID id
    );
}