package com.framework.redis.tool.impl;

import com.framework.redis.tool.RedisSetTool;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.SetOperations;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author chenhaipeng
 * @version 1.0
 * @date 2016/07/17 20:42
 */
public class RedisSetToolImpl extends RedisToolImpl implements RedisSetTool {

    public RedisSetToolImpl() {
    }

    public RedisSetToolImpl(RedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    public void setSetData(final String key, final Set<? extends Serializable> set,
                           final long expire, final TimeUnit timeUnit) {

        redisTemplate.execute(new SessionCallback<Object>() {

            public Object execute(RedisOperations operations)
                    throws DataAccessException {
                operations.multi();
                //1.删除旧数据
                operations.delete(key);

                //2.写入新数据
                SetOperations<String, Serializable> ops = operations.opsForSet();
                Iterator<? extends Serializable> it = set.iterator();

                while (it.hasNext()) {
                    ops.add(key, it.next());
                }

                //3.设置过期时间
                if (expire > 0) {
                    operations.expire(key, expire, timeUnit);
                }

                operations.exec();
                return null;
            }

        });

    }

    public void setSetData(String key, Set<? extends Serializable> set, long expire) {
        this.setSetData(key, set, expire, TimeUnit.SECONDS);
    }

    public void setSetData(String key, Set<? extends Serializable> set) {
        this.setSetData(key, set, 0L);
    }

    public <T> Set<T> getSetData(String key) {
        SetOperations<String, Serializable> ops = redisTemplate.opsForSet();

        Set<Serializable> set = ops.members(key);

        return changeDataType(set);
    }

    public boolean isMember(final String key, final Serializable value) {
        SetOperations<String, Serializable> ops = redisTemplate.opsForSet();

        return ops.isMember(key, value);
    }

}
