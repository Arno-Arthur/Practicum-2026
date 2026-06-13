# Practicum 2026: ЛР 3

Ветка `lr-3` содержит REST-версию приложения `fitprogwork`. Проект принимает документ в теле HTTP-запроса, выбирает нужный обработчик по типу данных и возвращает значение по указанному пути.

## Что реализовано

- REST endpoint `POST /api/data/extract`;
- DTO `ExtractionRequest` с полями `type`, `data`, `path`;
- сервис `DataProcessingService`;
- интерфейс `DataExtractor`;
- обработчики JSON, XML и YAML;
- разделение кода по пакетам `controller`, `dto`, `service`;
- автоматический выбор Spring-компонента по типу данных.

## API

```http
POST /api/data/extract
Content-Type: application/json
```

Тело запроса:

```json
{
  "type": "json",
  "data": "{\"user\":{\"name\":\"Arthur\",\"skills\":[\"Java\",\"Spring\"]}}",
  "path": "user/name"
}
```

Успешный ответ:

```json
{
  "value": "Arthur"
}
```

Ответ при ошибке:

```json
{
  "error": "Не поддерживаемый тип: csv"
}
```

## Поддерживаемые типы

| Type | Класс |
| --- | --- |
| `json` | `JsonDataExtractor` |
| `xml` | `XmlDataExtractor` |
| `yaml` | `YamlDataExtractor` |

Путь передается через `/`:

```text
user/name
items/[0]/title
```

## Структура

```text
fitprogwork/
  pom.xml
  src/main/java/ru/omstu/fitprogwork/
    FitprogworkApplication.java
    controller/DataExtractorController.java
    dto/ExtractionRequest.java
    service/
      DataExtractor.java
      DataProcessingService.java
      JsonDataExtractor.java
      XmlDataExtractor.java
      YamlDataExtractor.java
```

## Технологии

- Java 21
- Spring Boot 3.5.9
- Spring Web
- Maven
- Jackson XML
- Jackson YAML
- SnakeYAML

## Запуск

```bash
cd fitprogwork
./mvnw spring-boot:run
```

После запуска сервис доступен по адресу:

```text
http://localhost:8080
```

Проверка через `curl`:

```bash
curl -X POST http://localhost:8080/api/data/extract \
  -H "Content-Type: application/json" \
  -d '{
    "type": "yaml",
    "data": "user:\n  name: Arthur\n",
    "path": "user/name"
  }'
```

## Результат работы

ЛР 3 превращает набор парсеров в HTTP-сервис. Пользователь больше не запускает парсинг через `Main`, а отправляет данные в REST API и получает результат в JSON-ответе.
