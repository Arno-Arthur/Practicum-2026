package ru.omstu.fitprogwork.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "cache_records")
public class CacheRecord {
    @Id
    @Column(name = "cache_key")
    private String cacheKey;

    private String type;

    @Column(length = 10000)
    private String data;

    private String path;

    @Column(length = 10000)
    private String result;

    private LocalDateTime createdAt;

    public CacheRecord() {
    }

    public CacheRecord(String cacheKey, String type, String data, String path, String result, LocalDateTime createdAt) {
        this.cacheKey = cacheKey;
        this.type = type;
        this.data = data;
        this.path = path;
        this.result = result;
        this.createdAt = createdAt;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
