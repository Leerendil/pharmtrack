package org.vsu.userservice.controller;

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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.vsu.userservice.dto.UserDto;
import org.vsu.userservice.dto.UserResponse;
import org.vsu.userservice.dto.ErrorResponse;

@Tag(name = "Управление пользователями",
        description = "Отвечает за создание, поиск и деактивацию пользователей в системе PharmTrack.")
public interface UserAPI {

    @Operation(summary = "Создание нового пользователя",
            description = "Создаёт нового пользователя в системе. " +
                    "Пользователю автоматически назначается роль USER. " +
                    "Этот эндпоинт вызывается auth-service после успешной регистрации в Keycloak.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Пользователь успешно создан",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                                 {
                                                   "keycloakId": "550e8400-e29b-41d4-a716-446655440000",
                                                   "username": "john_doe",
                                                   "email": "john@example.com",
                                                   "firstName": "John",
                                                   "lastName": "Doe",
                                                   "role": "USER"
                                                 }
                                                 """
                            )
                    )),
            @ApiResponse(responseCode = "400",
                    description = "Некорректные данные пользователя (email уже существует, " +
                            "неверный формат email и т.д.)",
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
    ResponseEntity<UserResponse> create(
            @Parameter(description = "DTO модель для создания пользователя", required = true)
            @Valid @RequestBody UserDto userDto
    );

    @Operation(summary = "Поиск пользователя по email",
            description = "Возвращает информацию о пользователе по его email адресу. " +
                    "Если пользователь деактивирован, возвращается ошибка.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Пользователь найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                                 {
                                                   "keycloakId": "550e8400-e29b-41d4-a716-446655440000",
                                                   "username": "john_doe",
                                                   "email": "john@example.com",
                                                   "firstName": "John",
                                                   "lastName": "Doe",
                                                   "role": "USER"
                                                 }
                                                 """
                            )
                    )),
            @ApiResponse(responseCode = "404",
                    description = "Пользователь с указанным email не найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
            @ApiResponse(responseCode = "409",
                    description = "Пользователь деактивирован",
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
    ResponseEntity<UserResponse> findByEmail(
            @Parameter(description = "Email пользователя", required = true, example = "john@example.com")
            @PathVariable("email") String email
    );

    @Operation(summary = "Деактивация пользователя",
            description = "Деактивирует текущего авторизованного пользователя. " +
                    "После деактивации пользователь не сможет войти в систему. " +
                    "Пользователь идентифицируется по JWT токену.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Аккаунт успешно деактивирован",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "Your account was successfully deactivated!"
                            )
                    )),
            @ApiResponse(responseCode = "401",
                    description = "Пользователь не авторизован или токен недействителен",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
            @ApiResponse(responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    ResponseEntity<String> deactivate(
            @Parameter(description = "JWT токен пользователя (извлекается автоматически)", hidden = true)
            @AuthenticationPrincipal Jwt jwt
    );
}