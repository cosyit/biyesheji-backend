package com.edu.college.common.util.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
@SuppressWarnings("unused")
public class RedisUtil {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public void put(final String key, final Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void putHash(final String key, final Object hashKay, final Object hashValue) {
        redisTemplate.opsForHash().put(key, hashKay, hashValue);
    }

    public void delete(final String key) {
        redisTemplate.delete(key);
    }

    public void deleteHash(final String key, final Object hashKay) {
        redisTemplate.opsForHash().delete(key, hashKay);
    }

    public void putAndExpire(final String key, final Object value, final long seconds) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

    public <T> T getAndExpire(final String key, final long seconds) {
        @SuppressWarnings("ALL") final T value = (T) redisTemplate.opsForValue().get(key);
        if (value != null) {
            redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
        }
        return value;
    }

    public <T> T get(final String key) {
        @SuppressWarnings("ALL") final T value = (T) redisTemplate.opsForValue().get(key);
        return value;
    }

    public Long getExpire(final String key) {
        return redisTemplate.getExpire(key);
    }

    public <T> T getHash(final String key, final Object hashKey) {
        @SuppressWarnings("ALL") final T value = (T) redisTemplate.opsForHash().get(key, hashKey);
        return value;
    }

    public <T extends Number> T incrementHash(final String key, final Object hashKey, T number) {
        final T value;
        if (number instanceof Double) {
            return (T) redisTemplate.opsForHash().increment(key, hashKey, number.doubleValue());
        } else {
            return (T) redisTemplate.opsForHash().increment(key, hashKey, number.longValue());
        }
    }

    public <T> T getOrDefault(final String key, final T defaultValue) {
        @SuppressWarnings("ALL") final T value = (T) redisTemplate.opsForValue().get(key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
}
