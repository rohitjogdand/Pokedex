package com.pokedex.api.service;

import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class PokemonCache {

    private static final long TTL_SECONDS = 600;
    private static final int MAX_ENTRIES = 100;

    // Renamed from 'Entry' to 'CacheItem' to avoid conflict with Map.Entry
    private static class CacheItem {
        Object payload;
        Instant timestamp;

        CacheItem(Object payload) {
            this.payload = payload;
            this.timestamp = Instant.now();
        }
    }

    // Thread-safe LRU implementation with synchronized wrapper
    private final Map<String, CacheItem> store = Collections.synchronizedMap(
        new LinkedHashMap<String, CacheItem>(MAX_ENTRIES, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, CacheItem> eldest) {
                return size() > MAX_ENTRIES;
            }
        }
    );

    public Object get(String key) {
        CacheItem item = store.get(key);
        if (item == null) return null;

        // TTL eviction logic
        if (Instant.now().getEpochSecond() - item.timestamp.getEpochSecond() > TTL_SECONDS) {
            store.remove(key);
            return null;
        }
        return item.payload;
    }

    public void put(String key, Object value) {
        store.put(key, new CacheItem(value));
    }
}
