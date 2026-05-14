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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.vsu.catalogservice.dto.manufacturer.ManageApplication;
import org.vsu.catalogservice.dto.manufacturer.ManufacturerCreateRequest;
import org.vsu.catalogservice.entity.Manufacturer;
import org.vsu.catalogservice.dto.ErrorResponse;

import java.util.UUID;

@Tag(name = "Управление производителями",
        description = "Отвечает за подачу заявок на добавление производителей, их одобрение администратором и получение информации о производителях.")
public interface ManufacturerAPI {

    @Operation(summary = "Подача заявки на регистрацию производителя",
            description = "Позволяет представителю производителя подать заявку на добавление компании в каталог. " +
                    "Заявка получает статус PENDING и ожидает решения администратора. " +
                    "Требует авторизации пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Заявка успешно создана и ожидает рассмотрения",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UUID.class),
                            examples = @ExampleObject(
                                    value = """
                                            "550e8400-e29b-41d4-a716-446655440000"
                                            """
                            )
                    )),
            @ApiResponse(responseCode = "400",
                    description = "Некорректные данные (название пустое, неверный формат email и т.д.)",
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
            @ApiResponse(responseCode = "409",
                    description = "Производитель с таким названием или email уже существует",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    ResponseEntity<UUID> apply(
            @Parameter(description = "DTO с данными производителя для регистрации", required = true)
            @Valid @RequestBody ManufacturerCreateRequest createRequest,
            @Parameter(description = "JWT токен пользователя (извлекается автоматически)", hidden = true)
            @AuthenticationPrincipal Jwt jwt
    );

    @Operation(summary = "Рассмотрение заявки производителя (ADMIN)",
            description = "Позволяет администратору одобрить или отклонить заявку на регистрацию производителя. " +
                    "При одобрении производитель добавляется в каталог. " +
                    "Доступно только для пользователей с ролью ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Заявка успешно обработана",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(
                                    value = """
                                            "Application approved successfully"
                                            """
                            )
                    )),
            @ApiResponse(responseCode = "400",
                    description = "Некорректные данные (неверный токен заявки или решение)",
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
                    description = "Заявка с указанным токеном не найдена",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    ResponseEntity<String> manage(
            @Parameter(description = "DTO с токеном заявки и решением (APPROVED/REJECTED)", required = true)
            @Valid @RequestBody ManageApplication manageApplication
    );

    @Operation(summary = "Получение производителя по названию",
            description = "Возвращает полную информацию о производителе по его точному названию.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Производитель найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Manufacturer.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "name": "Байер",
                                              "country": "Германия",
                                              "companyMail": "info@bayer.de"
                                            }
                                            """
                            )
                    )),
            @ApiResponse(responseCode = "404",
                    description = "Производитель с указанным названием не найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    ResponseEntity<Manufacturer> getByName(
            @Parameter(description = "Название производителя", required = true, example = "Байер")
            @PathVariable("name") String name
    );
}