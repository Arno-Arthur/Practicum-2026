package ru.omstu.fitprogwork;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

public class JsonDataExtractor implements DataExtractor {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String extractValue(String filePath, String fieldPath) {
        try (InputStream is = getClass().getResourceAsStream("/" + filePath)) {
            JsonNode root = mapper.readTree(is);
            JsonNode node = navigateJson(root, fieldPath);
            return node != null ? node.asText() : null;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при чтении JSON", e);
        }
    }

    private JsonNode navigateJson(JsonNode node, String path) {
        if (path == null || path.isEmpty() || "/".equals(path)) return node;
        String[] parts = path.startsWith("/") ? path.substring(1).split("/") : path.split("/");
        JsonNode current = node;
        for (String part : parts) {
            if (part.isEmpty()) continue;
            if (part.startsWith("[") && part.endsWith("]")) {
                int index = Integer.parseInt(part.substring(1, part.length() - 1));
                current = current.get(index);
            } else {
                current = current.get(part);
            }
            if (current == null) return null;
        }
        return current;
    }
}