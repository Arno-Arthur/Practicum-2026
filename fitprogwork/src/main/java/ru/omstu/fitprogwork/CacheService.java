package ru.omstu.fitprogwork;

public interface CacheService {
    String get(ExtractionRequest request);

    void save(ExtractionRequest request, String result);

    void deleteOldRecords();

    void clear();
}
