package org.vsu.userservice.utils.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserServiceConstants {
    // Swagger
    public static final String SERVER_URL = "http://localhost:";
    public static final String SERVER_DESCRIPTION = "Local dev server";
    public static final String SERVICE_TITLE = "User Service API";
    public static final String SERVICE_VERSION = "1.0.0";
    public static final String SERVICE_DESCRIPTION = """
            ## Сервис управления пользователями PharmTrack
            
            User Service отвечает за: 
            - Создание пользователей в базе данных после регистрации через Keycloak
            - Поиск пользователей по email
            - Деактивацию пользователей
            - Хранение дополнительной информации о пользователях
            
            ### Основные возможности:
            - **Создание пользователя** — сохранение информации о пользователе после успешной регистрации в auth-service
            - **Поиск по email** — получение информации о пользователе по его email адресу
            - **Деактивация аккаунта** — мягкое удаление пользователя (блокировка доступа)
            
            ### Структура пользователя:
            - **keycloakId** — идентификатор из Keycloak (UUID)
            - **username** — уникальное имя пользователя
            - **email** — уникальный email пользователя
            - **firstName / lastName** — имя и фамилия
            - **role** — роль пользователя (ADMIN, MANAGER, USER)
            - **isDeactivated** — статус деактивации аккаунта
            - **createdAt** — дата создания
            
            ### Роли в системе:
            - **ADMIN** — полный доступ ко всем операциям
            - **MANAGER** — управление заказами и товарами
            - **USER** — базовые операции (просмотр товаров, оформление заказов)
            
            ### Примечания:
            - Пользователь создаётся через auth-service при регистрации
            - Поиск доступен только для активных пользователей (isDeactivated = false)
            - Деактивированный пользователь не может войти в систему
            - JWT токен извлекается автоматически из заголовка Authorization
            """;
}