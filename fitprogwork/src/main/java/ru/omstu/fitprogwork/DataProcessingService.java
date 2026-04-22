package ru.omstu.fitprogwork;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

@Service
public class DataProcessingService {

    private static final Logger log = LoggerFactory.getLogger(DataProcessingService.class);
    private final Map<String, DataExtractor> extractors;
    private final CacheService cacheService;

    public DataProcessingService(Map<String, DataExtractor> extractorBeans, CacheService cacheService) {
        this.cacheService = cacheService;
        this.extractors = extractorBeans.entrySet().stream()
                .collect(java.util.stream.Collectors.toMap(
                        e -> e.getKey().replace("DataExtractor", "").toLowerCase(),
                        Map.Entry::getValue
                ));
    }

    public String extract(ExtractionRequest request) {
        String valueFromCache = cacheService.get(request);
        if (valueFromCache != null) {
            log.info("Кеш HIT для запроса: type={}, path={}", request.getType(), request.getPath());
            return valueFromCache;
        }

        log.info("Кеш MISS для запроса: type={}, path={}", request.getType(), request.getPath());
        DataExtractor extractor = extractors.get(request.getType().toLowerCase());
        if (extractor == null) {
            throw new IllegalArgumentException("Не поддерживаемый тип: " + request.getType());
        }

        String result = extractor.extractValue(request.getData(), request.getPath());
        cacheService.save(request, result);
        return result;
    }
}
