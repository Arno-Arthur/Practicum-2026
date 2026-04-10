package ru.omstu.fitprogwork;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FitprogworkApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testJsonExtractionAndCaching() {
        // Подготовка запроса
        String jsonBody = """
            {
              "type": "json",
              "data": "{\\"user\\":{\\"name\\":\\"Alex\\"}}",
              "path": "user/name"
            }
            """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

        // Первый запрос — обработать и вернуть значение
        ResponseEntity<String> response1 = restTemplate.postForEntity("/api/data/extract", request, String.class);
        assertThat(response1.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response1.getBody()).contains("\"value\":\"Alex\"");

        // Второй запрос — кеширование
        ResponseEntity<String> response2 = restTemplate.postForEntity("/api/data/extract", request, String.class);
        assertThat(response2.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response2.getBody()).contains("\"value\":\"Alex\"");

        // Ответы должны быть идентичны
        assertThat(response2.getBody()).isEqualTo(response1.getBody());
    }

    @Test
    void testXmlExtraction() {
        String xmlBody = """
            {
              "type": "xml",
              "data": "<root><name>Petr</name></root>",
              "path": "name"
            }
            """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(xmlBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/data/extract", request, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("\"value\":\"Petr\"");
    }

    @Test
    void testYamlExtraction() {
        String yamlBody = """
            {
              "type": "yaml",
              "data": "name: Anna\\nage: 30",
              "path": "name"
            }
            """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(yamlBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/data/extract", request, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("\"value\":\"Anna\"");
    }

    @Test
    void testUnsupportedTypeReturnsError() {
        String badBody = """
            {
              "type": "csv",
              "data": "id,name\\n1,Alex",
              "path": "name"
            }
            """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(badBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/data/extract", request, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue(); // всё ещё 200, но с ошибкой
        assertThat(response.getBody()).contains("\"error\"");
    }
}