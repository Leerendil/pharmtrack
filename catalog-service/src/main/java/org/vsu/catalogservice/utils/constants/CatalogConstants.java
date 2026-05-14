package org.vsu.catalogservice.utils.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CatalogConstants {
    public static final String SERVER_URL = "http://localhost:";
    public static final String SERVER_DESCRIPTION = "Local dev server";
    public static final String SERVICE_TITLE = "Catalog Service API";
    public static final String SERVICE_VERSION = "1.0.0";
    public static final String SERVICE_DESCRIPTION = """
            ## Сервис каталога PharmTrack
            
            Catalog Service отвечает за управление каталогом лекарственных препаратов, категорий и производителей.
            
            ---
            
            ### 💊 Управление медикаментами
            
            - Создание новых препаратов (требует прав ADMIN)
            - Поиск по ID для получения базовой информации
            - Поиск по названию для получения полной информации
            - Гибкий поиск с фильтрацией по категории, производителю, цене
            
            ---
            
            ### 📂 Управление категориями
            
            Категории используются для группировки лекарственных препаратов по их назначению и действию.
            
            - **Создание категорий** — добавление новых категорий в каталог
            - **Поиск по названию** — получение информации о категории
            
            **Примеры категорий:**
            - Обезболивающие
            - Антибиотики
            - Противовирусные
            - Витамины
            - Сердечно-сосудистые
            
            ---
            
            ### 🏭 Управление производителями
            
            Сервис предоставляет возможность регистрации производителей через систему заявок:
            
            **Процесс регистрации производителя:**
            1. Представитель производителя подаёт заявку с данными компании
            2. Заявка получает уникальный токен и статус PENDING
            3. Администратор рассматривает заявку и принимает решение
            4. При одобрении производитель добавляется в каталог
            
            ---
            
            ### 📊 Структура данных:
            
            **Категория (Category):**
            | Поле | Тип | Описание |
            |------|-----|----------|
            | name | String | Название категории |
            | description | String | Описание категории |
            
            **Производитель (Manufacturer):**
            | Поле | Тип | Описание |
            |------|-----|----------|
            | name | String | Название компании |
            | country | String | Страна производителя |
            | companyMail | String | Контактный email |
            
            **Медикамент (Medicine):**
            | Поле | Тип | Описание |
            |------|-----|----------|
            | name | String | Название препарата |
            | description | String | Описание и показания |
            | price | BigDecimal | Цена за упаковку |
            | dosage | Long | Дозировка в мг |
            | isPrescriptionRequired | boolean | Требуется ли рецепт |
            | category | Category | Категория препарата |
            | manufacturer | Manufacturer | Производитель |
            
            ---
            
            ### 🔐 Правила доступа:
            
            - **USER** — просмотр каталога, поиск, подача заявок на производителей
            - **MANAGER** — просмотр, создание категорий, редактирование медикаментов
            - **ADMIN** — полный доступ: создание, редактирование, удаление, одобрение заявок
            
            ---
            
            ### 📝 Примечания:
            
            - Названия категорий и производителей должны быть уникальными
            - При создании медикамента категория и производитель должны существовать
            - Поиск по названию работает по принципу точного совпадения
            - Поиск медикаментов поддерживает частичное совпадение по названию
            - Цена медикамента не может быть отрицательной
            """;


    public static final String API_V1_CATEGORIES = "/api/v1/categories";
    public static final String API_V1_MANUFACTURERS = "/api/v1/manufacturers";
    public static final String API_V1_MEDICINES = "/api/v1/medicines";

    public static final String NAME = "/{name}";
    public static final String SEARCH = "/search";
    public static final String APPLICATION_MANAGE = "/applications/manage";
}
