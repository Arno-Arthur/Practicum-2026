# Practicum 2026

Учебный репозиторий с практическими работами по разработке Java/Spring Boot приложения. Работы оформлены отдельными ветками и показывают постепенное развитие проекта: от простого извлечения данных из файлов до REST-сервиса с кэшированием и хранением результатов в базе данных.

## Что внутри

| Ветка | Содержание |
| --- | --- |
| `develop` | ЛР 1-2: базовая структура проекта, извлечение данных из JSON и XML, фабрика парсеров. |
| `lr-3` | ЛР 3: REST API, DTO запроса, сервис обработки данных, поддержка JSON/XML/YAML. |
| `lr-4` | ЛР 4: кэширование результатов запросов в памяти, логирование cache hit/cache miss. |
| `lr-5` | ЛР 5: выделенный сервис кэша, плановая очистка, JPA, EclipseLink и HSQLDB. |
| `main` | Навигационный README и общая точка входа в репозиторий. |

## Проект

Основное приложение находится в каталоге `fitprogwork` на ветках лабораторных работ. Это Spring Boot сервис, который принимает документ и путь до нужного значения, выбирает подходящий парсер и возвращает найденный результат.

Поддерживаемые форматы:

- JSON
- XML
- YAML

Ключевой endpoint:

```http
POST /api/data/extract
Content-Type: application/json
```

Пример тела запроса:

```json
{
  "type": "json",
  "data": "{\"user\":{\"name\":\"Arthur\",\"skills\":[\"Java\",\"Spring\"]}}",
  "path": "user/name"
}
```

Пример ответа:

```json
{
  "value": "Arthur"
}
```

## Технологии

- Java 21
- Spring Boot 3.5
- Spring Web
- Spring Data JPA
- EclipseLink
- HSQLDB
- Jackson Databind
- Jackson XML
- Jackson YAML / SnakeYAML
- Maven

## Как запустить лабораторную

Переключитесь на нужную ветку, например на самую полную версию:

```bash
git checkout lr-5
cd fitprogwork
./mvnw spring-boot:run
```

После запуска приложение будет доступно локально на стандартном порту Spring Boot:

```text
http://localhost:8080
```

Проверить endpoint можно через `curl`:

```bash
curl -X POST http://localhost:8080/api/data/extract \
  -H "Content-Type: application/json" \
  -d '{
    "type": "json",
    "data": "{\"user\":{\"name\":\"Arthur\"}}",
    "path": "user/name"
  }'
```

## Архитектура

```text
Client
  |
  v
DataExtractorController
  |
  v
DataProcessingService
  |
  +-- JsonDataExtractor
  +-- XmlDataExtractor
  +-- YamlDataExtractor
  |
  v
CacheService
  |
  +-- MapCacheService
  +-- DbCacheService
        |
        v
      HSQLDB
```

## Развитие по работам

1. Реализованы интерфейс `DataExtractor` и парсеры для структурированных форматов.
2. Добавлен выбор обработчика по типу данных.
3. Проект превращен в REST API на Spring Boot.
4. Добавлено кэширование одинаковых запросов.
5. Кэш вынесен в отдельный сервис, добавлена автоматическая очистка и хранение через JPA.

## Структура ветки `lr-5`

```text
fitprogwork/
  pom.xml
  src/main/java/ru/omstu/fitprogwork/
    DataExtractor.java
    DataExtractorController.java
    DataProcessingService.java
    JsonDataExtractor.java
    XmlDataExtractor.java
    YamlDataExtractor.java
    CacheService.java
    MapCacheService.java
    CacheScheduler.java
    db/
      CacheRecord.java
      CacheRecordRepository.java
      DbCacheService.java
      JpaConfig.java
  src/main/resources/application.properties
```

## Назначение

Репозиторий используется для практики:

- проектирования сервисного слоя;
- работы с REST API;
- обработки JSON, XML и YAML;
- подключения Spring-компонентов через DI;
- кэширования результатов;
- настройки JPA и in-memory базы данных;
- ведения лабораторных работ в отдельных Git-ветках.
