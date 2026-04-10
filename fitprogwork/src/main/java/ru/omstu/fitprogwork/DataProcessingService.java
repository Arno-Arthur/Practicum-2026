package ru.omstu.fitprogwork;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DataProcessingService {

    private static final Logger log = LoggerFactory.getLogger(DataProcessingService.class);
    private final Map<String, DataExtractor> extractors;
    private final Map<ExtractionRequest, String> cache = new ConcurrentHashMap<>();

    public DataProcessingService(Map<String, DataExtractor> extractorBeans) {
        this.extractors = extractorBeans.entrySet().stream()
                .collect(java.util.stream.Collectors.toMap(
                        e -> e.getKey().replace("DataExtractor", "").toLowerCase(),
                        Map.Entry::getValue
                ));
    }

    public String extract(ExtractionRequest request) {
        if (cache.containsKey(request)) {
            log.info("Кеш HIT для запроса: type={}, path={}", request.getType(), request.getPath());
            return cache.get(request);
        }

        log.info("Кеш MISS для запроса: type={}, path={}", request.getType(), request.getPath());
        DataExtractor extractor = extractors.get(request.getType().toLowerCase());
        if (extractor == null) {
            throw new IllegalArgumentException("Не поддерживаемый тип: " + request.getType());
        }

        String result = extractor.extractValue(request.getData(), request.getPath());
        cache.put(request, result);
        return result;
    }
}
