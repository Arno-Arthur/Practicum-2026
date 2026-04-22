package ru.omstu.fitprogwork.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface CacheRecordRepository extends JpaRepository<CacheRecord, String> {
    void deleteByCreatedAtBefore(LocalDateTime date);
}
