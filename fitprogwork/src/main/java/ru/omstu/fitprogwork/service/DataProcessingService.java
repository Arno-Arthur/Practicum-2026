package ru.omstu.fitprogwork.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.HashMap;

@Service
public class DataProcessingService {

    private final Map<String, DataExtractor> extractors;

    @Autowired
    public DataProcessingService(Map<String, DataExtractor> extractorBeans) {
        this.extractors = new HashMap<>();
        for (Map.Entry<String, DataExtractor> entry : extractorBeans.entrySet()) {
            String key = entry.getKey().replace("DataExtractor", "").toLowerCase();
            this.extractors.put(key, entry.getValue());
        }
    }

    public String extract(String type, String data, String path) {
        DataExtractor extractor = extractors.get(type.toLowerCase());
        if (extractor == null) {
            throw new IllegalArgumentException("Не поддерживаемый тип: " + type);
        }
        return extractor.extractValue(data, path);
    }
}