package de.takacick.secretcraftbase.server.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimedCache<K, V> {

    private final Map<K, TimedEntry<V>> cache = new ConcurrentHashMap<>();

    public TimedCache() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::cleanupExpiredEntries, 0, 5, TimeUnit.SECONDS);
    }

    public void put(K key, V value) {
        cache.put(key, new TimedEntry<>(value));
    }

    public boolean contains(K key) {
        return this.cache.containsKey(key);
    }

    public V getOrDefault(K key, V value) {
        return this.cache.getOrDefault(key, new TimedEntry<>(value)).get();
    }

    public V get(K key) {
        return cache.get(key).get();
    }

    private void cleanupExpiredEntries() {
        long currentTime = System.currentTimeMillis();
        cache.entrySet().removeIf(entry -> entry.getValue().isExpired(currentTime));
    }
}