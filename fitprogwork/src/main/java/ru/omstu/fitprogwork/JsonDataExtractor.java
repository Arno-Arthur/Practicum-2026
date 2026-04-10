package ru.omstu.fitprogwork;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component("json")
public class JsonDataExtractor implements DataExtractor {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String extractValue(String data, String path) {
        try {
            JsonNode root = mapper.readTree(data);
            return navigate(root, path).asText();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при парсинге JSON", e);
        }
    }

    private JsonNode navigate(JsonNode node, String path) {
        if (path == null || path.isEmpty()) return node;
        String[] parts = path.split("/");
        JsonNode current = node;
        for (String part : parts) {
            if (part.startsWith("[") && part.endsWith("]")) {
                int index = Integer.parseInt(part.substring(1, part.length() - 1));
                current = current.get(index);
            } else {
                current = current.get(part);
            }
            if (current == null) return mapper.nullNode();
        }
        return current;
    }
}