package ru.omstu.fitprogwork;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CacheScheduler {
    private static final Logger log = LoggerFactory.getLogger(CacheScheduler.class);
    private final CacheService cacheService;

    public CacheScheduler(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Scheduled(fixedRate = 30000)
    public void deleteOldRecords() {
        try {
            cacheService.deleteOldRecords();
        } catch (Exception e) {
            log.error("Ошибка при очистке старого кеша", e);
        }
    }

    @Scheduled(cron = "0 0 0 * * SUN")
    public void clearCacheOnSunday() {
        try {
            cacheService.clear();
        } catch (Exception e) {
            log.error("Ошибка при очистке всего кеша", e);
        }
    }
}
