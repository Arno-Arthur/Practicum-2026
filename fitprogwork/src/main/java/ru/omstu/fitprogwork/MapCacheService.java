package ru.omstu.fitprogwork;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MapCacheService implements CacheService {
    private final Map<ExtractionRequest, CacheValue> cache = new ConcurrentHashMap<>();

    @Override
    public String get(ExtractionRequest request) {
        CacheValue value = cache.get(request);
        if (value == null) {
            return null;
        }
        return value.getResult();
    }

    @Override
    public void save(ExtractionRequest request, String result) {
        cache.put(request, new CacheValue(result, LocalDateTime.now()));
    }

    @Override
    public void deleteOldRecords() {
        LocalDateTime oldDate = LocalDateTime.now().minusSeconds(60);
        cache.entrySet().removeIf(entry -> entry.getValue().getCreatedAt().isBefore(oldDate));
    }

    @Override
    public void clear() {
        cache.clear();
    }

    private static class CacheValue {
        private final String result;
        private final LocalDateTime createdAt;

        public CacheValue(String result, LocalDateTime createdAt) {
            this.result = result;
            this.createdAt = createdAt;
        }

        public String getResult() {
            return result;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }
    }
}
