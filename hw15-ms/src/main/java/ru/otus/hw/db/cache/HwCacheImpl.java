package ru.otus.hw.db.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class HwCacheImpl<K, V> implements HwCache<K, V> {
//Надо реализовать эти методы

    private final Map<K, V> cache = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        notifyListeners(key, value, "put");
        log.info("Cache size: " + cache.size());
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
        notifyListeners(key, cache.get(key), "remove");
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.removeIf(hwListener -> Objects.equals(hwListener, listener));
    }

    private void notifyListeners(K key, V value, String action) {
        for (HwListener<K, V> hwListener : listeners) {
            try {
                hwListener.notify(key, value, action);
            } catch (Exception ex) {
                log.error("Error message: {}. Exception: ", ex.getMessage(), ex);
            }
        }
    }
}
