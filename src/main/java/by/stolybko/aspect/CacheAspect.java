package by.stolybko.aspect;

import by.stolybko.cache.impl.LRUCache;
import by.stolybko.entity.UserEntity;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Optional;

@Aspect
public class CacheAspect {

    LRUCache cache = new LRUCache(3);

    @Pointcut("execution(* by.stolybko.dao.*.findById(..))")
    public void findByIdDao() {
    }

    @Around(value = "findByIdDao()")
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

}
