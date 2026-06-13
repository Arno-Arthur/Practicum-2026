# Practicum 2026: ЛР 5

Ветка `lr-5` содержит самую полную версию проекта `fitprogwork`: REST-сервис для извлечения данных из JSON/XML/YAML с выделенным слоем кэширования, плановой очисткой и хранением кэша через JPA в HSQLDB.

## Что реализовано

- REST endpoint `POST /api/data/extract`;
- обработчики JSON, XML и YAML;
- общий интерфейс `DataExtractor`;
- сервис обработки `DataProcessingService`;
- интерфейс кэша `CacheService`;
- реализация кэша в памяти `MapCacheService`;
- основная реализация кэша в базе `DbCacheService`;
- сущность `CacheRecord`;
- репозиторий `CacheRecordRepository`;
- ручная конфигурация JPA через `JpaConfig`;
- плановая очистка старых записей через `CacheScheduler`;
- включенный scheduling через `@EnableScheduling`.

## API

```http
POST /api/data/extract
Content-Type: application/json
```

Пример запроса:

```json
{
  "type": "xml",
  "data": "<root><user><name>Arthur</name></user></root>",
  "path": "user/name"
}
```

Пример ответа:

```json
{
  "value": "Arthur"
}
```

При ошибке контроллер возвращает JSON с полем `error`.

## Кэширование

`DataProcessingService` сначала обращается к `CacheService`. Если запись найдена, возвращается сохраненный результат и в логах появляется `Кеш HIT`. Если записи нет, сервис выполняет парсинг, сохраняет результат и логирует `Кеш MISS`.

Ветка содержит две реализации:

| Реализация | Назначение |
| --- | --- |
| `MapCacheService` | Потокобезопасный кэш в памяти на `ConcurrentHashMap`. |
| `DbCacheService` | Основной кэш в HSQLDB через Spring Data JPA, помечен `@Primary`. |

Очистка:

- каждые 30 секунд удаляются записи старше 60 секунд;
- каждое воскресенье в 00:00 выполняется полная очистка кэша.

## База данных

Настройки находятся в `src/main/resources/application.properties`:

```properties
db.url=jdbc:hsqldb:mem:testdb
db.username=sa
db.password=
```

`JpaConfig` создает:

- `DataSource`;
- `LocalContainerEntityManagerFactoryBean`;
- `JpaTransactionManager`;
- настройки EclipseLink для HSQLDB.

## Структура

```text
fitprogwork/
  pom.xml
  src/main/java/ru/omstu/fitprogwork/
    CacheScheduler.java
    CacheService.java
    DataExtractor.java
    DataExtractorController.java
    DataProcessingService.java
    ExtractionRequest.java
    JsonDataExtractor.java
    MapCacheService.java
    XmlDataExtractor.java
    YamlDataExtractor.java
    db/
      CacheRecord.java
      CacheRecordRepository.java
      DbCacheService.java
      JpaConfig.java
  src/main/resources/application.properties
```

## Технологии

- Java 21
- Spring Boot 3.5.10
- Spring Web
- Spring Data JPA
- EclipseLink 4.0.8
- HSQLDB
- Maven
- Jackson Databind
- Jackson XML
- Jackson YAML
- SnakeYAML

## Запуск

```bash
cd fitprogwork
./mvnw spring-boot:run
```

Проверка через `curl`:

```bash
curl -X POST http://localhost:8080/api/data/extract \
  -H "Content-Type: application/json" \
  -d '{
    "type": "xml",
    "data": "<root><user><name>Arthur</name></user></root>",
    "path": "user/name"
  }'
```

Повторите запрос дважды: первый вызов пройдет как `Кеш MISS`, второй должен вернуться из кэша как `Кеш HIT`.

## Результат работы

ЛР 5 завершает цепочку практических работ: парсинг данных оформлен как REST API, повторные запросы кэшируются, кэш вынесен в отдельный слой, а основное хранилище реализовано через JPA и in-memory базу HSQLDB.
