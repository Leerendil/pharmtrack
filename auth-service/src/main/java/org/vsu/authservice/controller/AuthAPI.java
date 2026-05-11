package org.vsu.authservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.vsu.authservice.dto.AssignRoleDto;
import org.vsu.authservice.dto.LoginDto;
import org.vsu.authservice.dto.RegisterDto;
import org.vsu.authservice.dto.UserResponse;
import org.vsu.authservice.dto.ErrorResponse;

@Tag(name = "Управление аутентификацией и авторизацией",
        description = "Отвечает за регистрацию пользователей, вход в систему и управление ролями.")
public interface AuthAPI {

    @Operation(summary = "Регистрация нового пользователя",
            description = "Создаёт нового пользователя в системе и возвращает его данные. " +
                    "По умолчанию пользователю назначается роль USER.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Пользователь успешно зарегистрирован.",
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
                                              "role": USER 
                                            }
                                            """
                            )
                    )),
            @ApiResponse(responseCode = "400",
                    description = "Некорректные данные регистрации (email уже существует, " +
                            "неверный формат email, пароль слишком слабый и т.д.)",
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
    ResponseEntity<UserResponse> register(
            @Parameter(description = "DTO модель для регистрации пользователя", required = true)
            @Valid RegisterDto registerDto
    );

    @Operation(summary = "Аутентификация пользователя",
            description = "Выполняет вход пользователя в систему по username/email и паролю. " +
                    "Возвращает JWT токены (access и refresh).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Успешный вход. Возвращены JWT токены.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AccessTokenResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJkR...",
                                              "expires_in": 300,
                                              "refresh_token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiw...",
                                              "refresh_expires_in": 1800,
                                              "token_type": "Bearer"
                                            }
                                            """
                            )
                    )),
            @ApiResponse(responseCode = "401",
                    description = "Неверные учетные данные (username/email или пароль не совпадают)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
            @ApiResponse(responseCode = "400",
                    description = "Некорректные данные для входа (пустые поля или неверный формат)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    ResponseEntity<AccessTokenResponse> login(
            @Parameter(description = "DTO модель для входа пользователя (username/email и пароль)",
                    required = true)
            @Valid LoginDto loginDto
    );

    @Operation(summary = "Назначение роли пользователю",
            description = "Назначает указанную роль пользователю по его Keycloak ID. " +
                    "Доступно только для пользователей с ролью ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Роль успешно назначена пользователю.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "Role 'MANAGER' assigned to user successfully"
                            )
                    )),
            @ApiResponse(responseCode = "400",
                    description = "Некорректные данные (роль не существует или пользователь не найден)",
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
                    description = "Пользователь с указанным Keycloak ID не найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    ResponseEntity<String> assignRoleToUser(
            @Parameter(description = "DTO модель для назначения роли пользователю",
                    required = true)
            @Valid AssignRoleDto roleDto
    );
}