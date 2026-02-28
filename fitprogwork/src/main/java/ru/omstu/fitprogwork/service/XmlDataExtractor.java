package ru.omstu.fitprogwork.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Component;

@Component("xml")
public class XmlDataExtractor implements DataExtractor {
    private final XmlMapper mapper = new XmlMapper();

    @Override
    public String extractValue(String data, String path) {
        try {
            JsonNode root = mapper.readTree(data);
            JsonNode actualRoot = root.has("root") ? root.get("root") : root;
            return navigate(actualRoot, path).asText();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при парсинге XML", e);
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