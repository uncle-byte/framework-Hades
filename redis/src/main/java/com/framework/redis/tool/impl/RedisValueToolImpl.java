package com.framework.redis.tool.impl;

import com.framework.redis.tool.RedisValueTool;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ValueOperations;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author chenhaipeng
 * @version 1.0
 * @date 2016/07/17 20:42
 */
public class RedisValueToolImpl extends RedisToolImpl implements RedisValueTool {

    public RedisValueToolImpl() {
    }

    public RedisValueToolImpl(RedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    public void setData(final String key, final Serializable value,
                        final long expire, final TimeUnit timeUnit) {

        redisTemplate.execute(new SessionCallback<Object>() {

            public Object execute(RedisOperations operations)
                    throws DataAccessException {


                operations.multi();

                ValueOperations<String, Serializable> ops = operations.opsForValue();
                ops.set(key, value);
                if (expire > 0) {
                    redisTemplate.expire(key, expire, timeUnit);
                }

                operations.exec();
                return null;
            }
        });

    }

    public void setData(final String key, final Serializable value,
                        final long expire) {
        this.setData(key, value, expire, TimeUnit.SECONDS);
    }

    public void setData(final String key, final Serializable value) {
        this.setData(key, value, 0L);
    }

    public <T> T getData(final String key) {
        Serializable result = null;

        if (redisTemplate.hasKey(key)) {
            ValueOperations<String, Serializable> ops = redisTemplate.opsForValue();
            result = ops.get(key);
        }
        return (T) result;
    }
}
