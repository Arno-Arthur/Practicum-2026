package ru.omstu.fitprogwork;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

@Component("yaml")
public class YamlDataExtractor implements DataExtractor {
    private final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

    @Override
    public String extractValue(String data, String path) {
        try {
            JsonNode root = yamlMapper.readTree(data);
            return navigate(root, path).asText();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при парсинге YAML", e);
        }
    }

    private JsonNode navigate(JsonNode node, String path) {
        if (path == null || path.isEmpty()) return node;
        String[] parts = path.split("/");
        JsonNode current = node;
        for (String part : parts) {
            if (part.isEmpty()) continue;
            if (part.startsWith("[") && part.endsWith("]")) {
                int index = Integer.parseInt(part.substring(1, part.length() - 1));
                current = current.get(index);
            } else {
                current = current.get(part);
            }
            if (current == null) return yamlMapper.nullNode();
        }
        return current;
    }
}
