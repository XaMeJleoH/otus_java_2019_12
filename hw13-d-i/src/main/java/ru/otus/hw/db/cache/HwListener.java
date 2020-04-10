package ru.otus.hw.db.cache;

/**
 * @author sergey
 * created on 14.12.18.
 */
public interface HwListener<K, V> {
  void notify(K key, V value, String action);
}
