package ru.otus.hw.cache;

import lombok.extern.slf4j.Slf4j;

/**
 * @author sergey
 * created on 14.12.18.
 */

@Slf4j
public class HWCacheDemo {

  public static void main(String[] args) {
    new HWCacheDemo().demo();
  }

  private void demo() {
    HwCache<Integer, Integer> cache = new MyCache<>();

    // пример, когда Idea предлагает упростить код, при этом может появиться "спец"-эффект
    HwListener<Integer, Integer> listener = new HwListener<Integer, Integer>() {
      @Override
      public void notify(Integer key, Integer value, String action) {
        log.info("key:{}, value:{}, action: {}", key, value, action);
      }
    };

    cache.addListener(listener);
    cache.put(1, 1);

    log.info("getValue:{}", cache.get(1));
    cache.remove(1);
    cache.removeListener(listener);
  }
}
