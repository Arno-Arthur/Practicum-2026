# Practicum 2026: ЛР 4

Ветка `lr-4` развивает REST-сервис из предыдущей работы и добавляет кэширование результатов. Повторный запрос с теми же `type`, `data` и `path` возвращается из памяти без повторного парсинга.

## Что реализовано

- REST endpoint `POST /api/data/extract`;
- поддержка JSON, XML и YAML;
- `ExtractionRequest` с корректными `equals` и `hashCode`;
- in-memory кэш на `ConcurrentHashMap`;
- логирование `Кеш HIT` и `Кеш MISS`;
- обработка неподдерживаемого типа данных через ошибку в JSON-ответе.

## Как устроен кэш

Кэш находится внутри `DataProcessingService`:

```text
Map<ExtractionRequest, String> cache = new ConcurrentHashMap<>();
```

Ключом является весь запрос:

- `type`;
- `data`;
- `path`.

Если такой запрос уже был обработан, сервис возвращает сохраненное значение. Если записи нет, сервис выбирает подходящий `DataExtractor`, извлекает значение и кладет результат в кэш.

## API

```http
POST /api/data/extract
Content-Type: application/json
```

Пример JSON-запроса:

```json
{
  "type": "json",
  "data": "{\"book\":{\"title\":\"Clean Code\"}}",
  "path": "book/title"
}
```

Ответ:

```json
{
  "value": "Clean Code"
}
```

Повтор этого же запроса должен пройти как cache hit и отразиться в логах приложения.

## Структура

```text
fitprogwork/
  pom.xml
  src/main/java/ru/omstu/fitprogwork/
    DataExtractor.java
    DataExtractorController.java
    DataProcessingService.java
    ExtractionRequest.java
    JsonDataExtractor.java
    XmlDataExtractor.java
    YamlDataExtractor.java
    FitprogworkApplication.java
```

## Технологии

- Java 21
- Spring Boot 3.5.10
- Spring Web
- Maven
- Jackson Databind
- Jackson XML
- Jackson YAML
- SnakeYAML
- `ConcurrentHashMap`

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
    "type": "json",
    "data": "{\"book\":{\"title\":\"Clean Code\"}}",
    "path": "book/title"
  }'
```

## Результат работы

ЛР 4 показывает, как добавить к REST-сервису простой потокобезопасный кэш. Это уменьшает повторную обработку одинаковых запросов и подготавливает проект к следующему шагу: вынесению кэша в отдельный сервис и хранению записей в базе данных.
