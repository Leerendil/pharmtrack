package org.vsu.authservice.utils.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthServiceConstants {

    // Swagger
    public static final String SERVER_URL = "http://localhost:";
    public static final String SERVER_DESCRIPTION = "Local dev server";
    public static final String SERVICE_TITLE = "Auth Service API";
    public static final String SERVICE_VERSION = "1.0.0";
    public static final String SERVICE_DESCRIPTION = """
            ## Сервис аутентификации и авторизации PharmTrack
            
            Auth Service отвечает за:
            - Регистрацию новых пользователей
            - Аутентификацию (вход в систему)
            - Управление ролями пользователей
            - Взаимодействие с Keycloak для управления пользователями
            
            ### Основные возможности:
            - **Регистрация** — создание нового пользователя
            - **Логин** — получение JWT токена
            - **Назначение ролей** — выдача ролей пользователям (ADMIN, MANAGER, USER)
            
            ### Роли в системе:
            - **ADMIN** — полный доступ ко всем операциям
            - **MANAGER** — управление заказами и товарами, но не пользователями
            - **USER** — базовые операции (просмотр товаров, оформление заказов)
            
            ### Примечания:
            - Для защищённых эндпоинтов требуется JWT токен
            - Токен передаётся в заголовке: `Authorization: Bearer <token>`
            - Некоторые эндпоинты доступны только администраторам
            """;
}