package com.framework.redis.tool.impl;

import com.framework.redis.exception.RedisException;
import com.framework.redis.tool.RedisTool;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.io.Serializable;
import java.util.*;

/**
 * @author chenhaipeng
 * @version 1.0
 * @date 2016/07/17 20:42
 */
public class RedisToolImpl implements RedisTool<String, Serializable> {

    protected RedisTemplate<String, Serializable> redisTemplate;

    public void setRedisTemplate(
            RedisTemplate<String, Serializable> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public RedisTemplate<String, Serializable> getRedisTemplate() {
        return redisTemplate;
    }

    public RedisToolImpl() {
    }

    public RedisToolImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public boolean hasKey(final String key) {
        return redisTemplate.hasKey(key);
    }

    public boolean delete(final String key) throws RedisException {
        boolean flag = false;
        try {
            redisTemplate.delete(key);
            flag = true;
        } catch (Exception e) {
            flag = false;
            throw new RedisException("删除Redis数据失败！", e);
        }
        return flag;
    }

    public boolean delete(final Collection<String> keys) throws RedisException {
        Collection<String> keysTmp = new HashSet<String>();
        for (String key : keys) {
            keysTmp.add(key);
        }

        boolean flag = false;
        try {
            redisTemplate.delete(keysTmp);
            flag = true;
        } catch (Exception e) {
            flag = false;
            throw new RedisException("删除Redis数据失败！", e);
        }
        return flag;
    }

    public <T> T executeScript(final Class<T> resultType, final String script,
                               final List<String> keys, final Object... args) {

        RedisScript<T> redisScript = new DefaultRedisScript<T>(script, resultType);

        T result = redisTemplate.execute(redisScript, keys, args);

        return result;
    }

    public <T> List<T> changeDataType(List<? extends Serializable> data) {
        if (data == null) {
            return null;
        }

        List<T> changeData = new ArrayList<T>();

        for (Serializable ele : data) {
            T t = (T) ele;
            changeData.add(t);
        }

        return changeData;
    }

    public <T> Set<T> changeDataType(Set<? extends Serializable> data) {
        if (data == null) {
            return null;
        }

        Set<T> changeData = new HashSet<T>();

        for (Serializable ele : data) {
            T t = (T) ele;
            changeData.add(t);
        }

        return changeData;

    }

    public <T> Map<String, T> changeDataType(Map<String, ? extends Serializable> data) {
        if (data == null) {
            return null;
        }

        Map<String, T> changeData = new HashMap<String, T>();

        changeData.putAll((Map<String, T>) data);

        return changeData;
    }
}








