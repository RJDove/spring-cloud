package com.oppo.autotest.otest.aspect;

import com.google.gson.Gson;
import com.oppo.autotest.otest.common.annotations.LockAction;
import com.oppo.autotest.otest.common.annotations.MultiLockAction;
import com.oppo.autotest.otest.common.enums.common.LockType;
import com.oppo.autotest.otest.common.enums.common.ResultCode;
import com.oppo.autotest.otest.common.result.PlatformResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.RedissonMultiLock;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author yanghuan
 */
@Slf4j
@Aspect
@Component
@Order(1)
public class RedissonLockAspect {

    @Resource(name = "redissonClient")
    private RedissonClient redissonClient;

    @Resource(name = "parser")
    private ExpressionParser parser;

    @Resource(name = "discoverer")
    private LocalVariableTableParameterNameDiscoverer discoverer;

    @Resource(name = "gson")
    private Gson gson;

    @Around(value = "@annotation(com.oppo.autotest.otest.common.annotations.LockAction)")
    public Object syncAction(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
        LockAction lockAction = method.getAnnotation(LockAction.class);
        String key = parse(lockAction.key(), method, joinPoint.getArgs());
        log.debug("lock key is {}.", key);
        RLock lock = getLock(key, lockAction.lockType());
        Exception error;
        if (!lock.tryLock(lockAction.waitTime(), lockAction.leaseTime(), lockAction.unit())) {
            log.error("get lock '{}' failed", key);
            return PlatformResult.failure(ResultCode.GET_LOCK_FAILED);
        }
        log.debug("get lock '{}' success", key);
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            error = e;
            log.error("execute locked method occured an exception", e);
        } finally {
            lock.unlock();
            log.debug("release lock '{}'", key);
        }
        throw error;
    }

    @Around(value = "@annotation(com.oppo.autotest.otest.common.annotations.MultiLockAction) &&" +
            "args(ids,..)")
    public Object multiSyncAction(ProceedingJoinPoint joinPoint, Set<Long> ids) throws Throwable {
        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
        MultiLockAction multiLockAction = method.getAnnotation(MultiLockAction.class);
        String key = parse(multiLockAction.value(), method, joinPoint.getArgs());
        String idListStr = gson.toJson(ids);
        log.debug("key is: {}, id list is: {}", key, idListStr);
        RedissonMultiLock multiLock = getMultiLock(key, multiLockAction, ids);
        Exception error;
        if (!multiLock.tryLock(multiLockAction.waitTime(), multiLockAction.leaseTime(), multiLockAction.unit())) {
            log.error("get multi lock failed. key: {}, id list is: {} ", key, idListStr);
            return PlatformResult.failure(ResultCode.GET_LOCK_FAILED);
        }
        log.debug("get multi lock success. key: {}, id list is: {} ", key, idListStr);
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            error = e;
            log.error("execute multi locked method occured an exception", e);
        } finally {
            multiLock.unlock();
            log.debug("release multi lock. key: {}, id list is: {} ", key, idListStr);
        }
        throw error;
    }

    private RedissonMultiLock getMultiLock(String publicKey, MultiLockAction multiLockAction, Set<Long> ids) {
        List<RLock> locks = new ArrayList<>();
        switch (multiLockAction.parentLockType()) {
            case MULTI_LOCK:
                ids.forEach(id -> {
                    RLock lock = getLock(publicKey + id, multiLockAction.childLockType());
                    locks.add(lock);
                });
                return new RedissonMultiLock(locks.toArray(new RLock[ids.size()]));
            case RED_LOCK:
                ids.forEach(id -> {
                    RLock lock = getLock(publicKey + id, multiLockAction.childLockType());
                    locks.add(lock);
                });
                return new RedissonRedLock(locks.toArray(new RLock[ids.size()]));
            default:
                throw new RuntimeException("do not support lock type:" + multiLockAction.parentLockType().name());
        }
    }

    /**
     * 解析spring EL表达式
     *
     * @param key 表达式
     * @param method 方法
     * @param args 方法参数
     * @return 值
     */
    private String parse(String key, Method method, Object[] args) {
        log.debug("param key is {}", key);
        String[] params = discoverer.getParameterNames(method);
        EvaluationContext context = new StandardEvaluationContext();
        if (params != null) {
            for (int i = 0; i < params.length; i ++) {
                context.setVariable(params[i], args[i]);
            }
        }
        return parser.parseExpression(key).getValue(context, String.class);
    }

    /**
     * 获取锁
     * @param key 值
     * @param lockType 锁类型集合
     * @return 锁对象
     */
    private RLock getLock(String key, LockType lockType) {
        switch (lockType) {
            case REENTRANT_LOCK:
                return redissonClient.getLock(key);
            case FAIR_LOCK:
                return redissonClient.getFairLock(key);
            case READ_LOCK:
                return redissonClient.getReadWriteLock(key).readLock();
            case WRITE_LOCK:
                return redissonClient.getReadWriteLock(key).writeLock();
            default:
                throw new RuntimeException("do not support lock type:" + lockType.name());
        }
    }
}
