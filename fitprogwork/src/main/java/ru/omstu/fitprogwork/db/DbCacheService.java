package ru.omstu.fitprogwork.db;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.omstu.fitprogwork.CacheService;
import ru.omstu.fitprogwork.ExtractionRequest;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Primary
public class DbCacheService implements CacheService {
    private final CacheRecordRepository repository;

    public DbCacheService(CacheRecordRepository repository) {
        this.repository = repository;
    }

    @Override
    public String get(ExtractionRequest request) {
        return repository.findById(createKey(request))
                .map(CacheRecord::getResult)
                .orElse(null);
    }

    @Override
    @Transactional
    public void save(ExtractionRequest request, String result) {
        CacheRecord record = new CacheRecord(
                createKey(request),
                request.getType(),
                request.getData(),
                request.getPath(),
                result,
                LocalDateTime.now()
        );
        repository.save(record);
    }

    @Override
    @Transactional
    public void deleteOldRecords() {
        repository.deleteByCreatedAtBefore(LocalDateTime.now().minusSeconds(60));
    }

    @Override
    @Transactional
    public void clear() {
        repository.deleteAll();
    }

    private String createKey(ExtractionRequest request) {
        return String.valueOf(Objects.hash(request.getType(), request.getData(), request.getPath()));
    }
}
