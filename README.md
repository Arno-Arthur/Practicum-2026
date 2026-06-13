# Practicum 2026: ЛР 1-2

Ветка `develop` содержит раннюю версию проекта `fitprogwork`: приложение на Java/Spring Boot для извлечения значений из структурированных файлов. На этом этапе основное внимание уделено общему интерфейсу парсеров, выбору обработчика по расширению файла и проверке работы на примерах JSON/XML.

## Что реализовано

- общий интерфейс `DataExtractor`;
- парсер JSON на Jackson Databind;
- парсер XML на Jackson XML;
- фабрика `ExtractorFactory`, выбирающая обработчик по расширению `.json` или `.xml`;
- демонстрационный класс `Main`;
- тестовые ресурсы `sample.json` и `sample.xml`;
- базовый Spring Boot проект с `HelloController`.

## Структура

```text
fitprogwork/
  pom.xml
  src/main/java/ru/omstu/fitprogwork/
    DataExtractor.java
    ExtractorFactory.java
    JsonDataExtractor.java
    XmlDataExtractor.java
    Main.java
    HelloController.java
    FitprogworkApplication.java
  src/main/resources/
    sample.json
    sample.xml
    application.properties
```

## Как работает

`Main` создает парсеры через `ExtractorFactory` и извлекает значения по путям:

```text
/name
/relation/[1]/name
```

Пример логики:

```java
DataExtractor jsonExtractor = ExtractorFactory.getExtractor("sample.json");
String value = jsonExtractor.extractValue("sample.json", "/name");
```

Если передать файл с неподдерживаемым расширением, фабрика выбросит `IllegalArgumentException`.

## Технологии

- Java 21
- Spring Boot 3.5.9
- Maven
- Jackson Databind
- Jackson XML
- SnakeYAML подключен как зависимость, но YAML-парсер появляется в следующих работах

## Запуск

```bash
cd fitprogwork
./mvnw spring-boot:run
```

Для запуска демонстрационного класса `Main` можно использовать IDE или Maven exec-плагин, если добавить его в конфигурацию проекта.

## Результат работы

Эта ветка фиксирует основу проекта: единый контракт для извлечения данных и первые реализации для JSON/XML. Дальше на этой базе добавляются REST API, YAML, кэширование и хранение кэша в базе данных.
