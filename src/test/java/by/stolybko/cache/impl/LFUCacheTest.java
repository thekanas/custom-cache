package by.stolybko.cache.impl;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class LFUCacheTest {

    @Test
    void getFromCacheTest() {
        // given
        LFUCache lfuCache = new LFUCache(3);
        lfuCache.putInCache(1L, "first");
        lfuCache.putInCache(2L, "second");
        lfuCache.putInCache(3L, "third");

        // when
        Optional<String> actual1 = (Optional<String>) lfuCache.getFromCache(1L);

        // then
        assertThat(actual1.get()).isEqualTo("first");

        // when
        lfuCache.putInCache(4L, "fourth");
        Optional<String> actual2 = (Optional<String>) lfuCache.getFromCache(2L);

        // then
        assertThat(actual2).isEmpty();

    }

    @Test
    void putInCacheTest() {
        // given
        LFUCache lfuCache = new LFUCache(3);
        lfuCache.putInCache(1L, "first");
        lfuCache.putInCache(2L, "second");
        lfuCache.putInCache(3L, "third");

        // when
        lfuCache.putInCache(1L, "first+");
        Optional<String> actual = (Optional<String>) lfuCache.getFromCache(1L);

        // then
        assertThat(actual.get()).isEqualTo("first+");
    }

    @Test
    void removeFromCacheTest() {
        // given
        LFUCache lfuCache = new LFUCache(3);
        lfuCache.putInCache(1L, "first");

        // when
        lfuCache.removeFromCache(1L);
        Optional<String> actual = (Optional<String>) lfuCache.getFromCache(1L);

        // then
        assertThat(actual).isEmpty();
    }
}