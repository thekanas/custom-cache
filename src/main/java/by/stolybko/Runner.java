package by.stolybko;

import by.stolybko.cache.impl.LFUCache;
import by.stolybko.cache.impl.LRUCache;
import by.stolybko.entity.BaseEntity;
import by.stolybko.entity.UserEntity;

public class Runner {
    public static void main(String[] args) {
        LRUCache<Long, BaseEntity> lfuCache = new LRUCache<>(3);

        UserEntity user1 = UserEntity.builder().id(1L).build();
        UserEntity user2 = UserEntity.builder().id(2L).build();
        UserEntity user3 = UserEntity.builder().id(3L).build();
        UserEntity user4 = UserEntity.builder().id(4L).build();

        lfuCache.putInCache(user1.getId(), user1);
        lfuCache.putInCache(user2.getId(), user2);
        lfuCache.putInCache(user3.getId(), user3);
        lfuCache.putInCache(user4.getId(), user4);

        lfuCache.getFromCache(2L);
        lfuCache.getFromCache(3L);
        lfuCache.getFromCache(4L);
        lfuCache.getFromCache(4L);
        lfuCache.getFromCache(2L);
        System.out.println();

    }
}
