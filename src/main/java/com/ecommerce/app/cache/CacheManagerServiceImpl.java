package com.ecommerce.app.cache;

import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
@AllArgsConstructor
public class CacheManagerServiceImpl implements CacheManagerService {

    private final CacheManager cacheManager;

    public void clearCacheByName(String key) {
        Objects.requireNonNull(cacheManager.getCache(key)).clear();
    }

    public void clearAllCachedKeys() {
        cacheManager.getCacheNames().forEach(key -> cacheManager.getCache(key).clear());
    }

    public List<String> getAllCachedKeys() {
        return cacheManager.getCacheNames().stream().toList();
    }

}