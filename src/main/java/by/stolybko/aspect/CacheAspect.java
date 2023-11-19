package by.stolybko.aspect;

import by.stolybko.cache.Cache;
import by.stolybko.cache.CacheFactory;
import by.stolybko.entity.BaseEntity;
import by.stolybko.util.PropertiesManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import java.util.Optional;

/**
 * Класс внедряет функционал кеширования
 * с помощью АОП.
 */
@Aspect
public class CacheAspect {

    private final Cache<Long, BaseEntity> cache = CacheFactory.getCache(PropertiesManager.get("cacheAlgorithm"));

    /**
     * Метод отфильтровывает точки соединения
     * по названию класса.
     */
    @Pointcut("within(by.stolybko.dao.impl.*DaoImpl)")
    public void isDaoLayer() {
    }

    @Pointcut("isDaoLayer() && execution( * by.stolybko.dao.*.findById(..))")
    public void findByIdDaoMethod() {
    }

    @Pointcut("isDaoLayer() && execution( * by.stolybko.dao.*.save(..))")
    public void saveDaoMethod() {
    }

    @Pointcut("isDaoLayer() && execution( * by.stolybko.dao.*.update(..))")
    public void updateDaoMethod() {
    }

    @Pointcut("isDaoLayer() && execution( * by.stolybko.dao.*.delete(..))")
    public void deleteDaoMethod() {
    }

    /**
     * Возвращает объект из кэша при его наличии.
     * Иначе, возвращает объект из базы данных
     * и помещает его в кэш.
     *
     */
    @SuppressWarnings("unchecked")
    @Around(value = "findByIdDaoMethod()")
    public Object cachingFindById(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Long id = (Long) proceedingJoinPoint.getArgs()[0];
        Optional<BaseEntity> cachedObj = cache.getFromCache(id);

        if (cachedObj.isPresent()) {
            System.out.println("cache");
            return cachedObj;
        } else {
            Optional<BaseEntity> returnObj = (Optional<BaseEntity>) proceedingJoinPoint.proceed();
            returnObj.ifPresent(o -> cache.putInCache(id, o));

            return returnObj;
        }
    }

    /**
     * Помещает объект в кэш после его сохранения в базе данных.
     *
     */
    @SuppressWarnings("unchecked")
    @AfterReturning(value = "saveDaoMethod()", returning = "result")
    public void cachingSave(Object result) {
        Optional<BaseEntity> entity = (Optional<BaseEntity>) result;
        if (entity.isPresent()) {
            cache.putInCache(entity.get().getId(), entity.get());
            System.out.println("put in cache");
        }
    }

    /**
     * Помещает объект в кэш после его обновления в базе данных.
     *
     */
    @SuppressWarnings("unchecked")
    @AfterReturning(value = "updateDaoMethod()", returning = "result")
    public void cachingUpdate(Object result) {
        Optional<BaseEntity> entity = (Optional<BaseEntity>) result;
        if (entity.isPresent()) {
            cache.putInCache(entity.get().getId(), entity.get());
            System.out.println("put in cache");
        }
    }

    /**
     * Удаляет объект из кэша после его удаления из базы данных.
     *
     */
    @AfterReturning(value = "deleteDaoMethod() && args(id) ", argNames = "id")
    public void cachingUpdate(Long id) {
        cache.removeFromCache(id);
        System.out.println("remove from cache");
    }
}
