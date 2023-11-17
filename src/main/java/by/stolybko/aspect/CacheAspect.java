package by.stolybko.aspect;

import by.stolybko.cache.Cache;
import by.stolybko.cache.impl.LFUCache;
import by.stolybko.cache.impl.LRUCache;
import by.stolybko.entity.BaseEntity;
import by.stolybko.entity.UserEntity;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Optional;

@Aspect
public class CacheAspect {

    Cache cache = new LFUCache(3);

    @Pointcut("within(by.stolybko.dao.*Dao)")
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

    //@SuppressWarnings("unchecked")
    @Around(value = "findByIdDaoMethod()")
    public Object cachingFindById(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Long id = (Long) proceedingJoinPoint.getArgs()[0];
        Optional<?> cachedObj = (Optional<?>) cache.getFromCache(id);

        if (cachedObj.isPresent()) {
            System.out.println("cache");
            return cachedObj;
        } else {
            Optional<?> returnObj = (Optional<?>) proceedingJoinPoint.proceed();
            returnObj.ifPresent(o -> cache.putInCache(id, o));

            return returnObj;
        }
    }

    @SuppressWarnings("unchecked")
    @AfterReturning(value = "saveDaoMethod()" , returning = "result")
    public void cachingSave(Object result) {
        Optional<BaseEntity> entity = (Optional<BaseEntity>) result;
        if(entity.isPresent()) {
            cache.putInCache(entity.get().getId(), entity.get());
            System.out.println("put in cache");
        }
    }

}
