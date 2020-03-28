package ru.otus.hw.cache;

import java.util.*;

/**
 * @author sergey
 * created on 14.12.18.
 */
public class MyCache<K, V> implements HwCache<K, V> {
//Надо реализовать эти методы

    private final Map<K, V> cache = new WeakHashMap<>();
    private final List<HwListener> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
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
        listeners.removeIf(hwListener -> Objects.equals(hwListener,listener));
    }
}
