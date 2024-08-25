package com.ecommerce.app.cache;

import java.util.List;


public interface CacheManagerService {

    void clearCacheByName(String key);

    void clearAllCachedKeys();

    List<String> getAllCachedKeys();

}