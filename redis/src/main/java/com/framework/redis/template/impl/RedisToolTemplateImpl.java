package com.framework.redis.template.impl;

import com.framework.redis.bean.RedisPageData;
import com.framework.redis.exception.RedisException;
import com.framework.redis.template.RedisToolTemplate;
import com.framework.redis.tool.*;
import com.framework.redis.tool.impl.*;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author chenhaipeng
 * @version 1.0
 * @date 2016/07/17 20:42
 */
public class RedisToolTemplateImpl implements RedisToolTemplate<String, Serializable> {

    private RedisHashTool redisHashTool;
    private RedisListTool redisListTool;
    private RedisSetTool redisSetTool;
    private RedisValueTool redisValueTool;
    private RedisZSetTool redisZSetTool;

    private String redisDataPrefix = "";

    public void setRedisDataPrefix(String redisDataPrefix) {
        this.redisDataPrefix = redisDataPrefix;
    }

    public String getRedisDataPrefix() {
        return redisDataPrefix;
    }

    public RedisToolTemplateImpl() {
    }

    public RedisToolTemplateImpl(RedisTemplate redisTemplate) {
        this.redisHashTool = new RedisHashToolImpl(redisTemplate);
        this.redisListTool = new RedisListToolImpl(redisTemplate);
        this.redisSetTool = new RedisSetToolImpl(redisTemplate);
        this.redisValueTool = new RedisValueToolImpl(redisTemplate);
        this.redisZSetTool = new RedisZSetToolImpl(redisTemplate);
    }

    public boolean hasKey(String key) {
        return redisValueTool.hasKey(redisDataPrefix + key);
    }

    public boolean delete(String key) throws RedisException {
        return redisValueTool.delete(redisDataPrefix + key);
    }

    public boolean delete(Collection<String> keys) throws RedisException {
        Collection<String> keysTmp = new HashSet<String>();
        for (String key : keys) {
            keysTmp.add(redisDataPrefix + key);
        }
        return redisValueTool.delete(keysTmp);
    }

    public <T> T executeScript(Class<T> resultType, String script,
                               List<String> keys, Object... args) {
        return redisValueTool.executeScript(resultType, script, keys, args);
    }

    public void setData(String key, Serializable value, long expire,
                        TimeUnit timeUnit) {
        redisValueTool.setData(redisDataPrefix + key, value, expire, timeUnit);
    }

    public void setData(String key, Serializable value, long expire) {
        redisValueTool.setData(redisDataPrefix + key, value, expire);
    }

    public void setData(String key, Serializable value) {
        redisValueTool.setData(redisDataPrefix + key, value);
    }

    public <T> T getData(String key) {
        return redisValueTool.getData(redisDataPrefix + key);
    }

    public void setQueenData(String key, List<? extends Serializable> list, long expire,
                             TimeUnit timeUnit) {
        redisListTool.setListData(redisDataPrefix + key, list, expire, timeUnit);
    }

    public void setQueenData(String key, List<? extends Serializable> list, long expire) {
        redisListTool.setListData(redisDataPrefix + key, list, expire);
    }

    public void setQueenData(String key, List<? extends Serializable> list) {
        redisListTool.setListData(redisDataPrefix + key, list);
    }

    public <T> List<T> getQueenData(String key) {
        return redisListTool.getListData(redisDataPrefix + key);
    }

    public boolean trimQueenData(String key, long start, long end)
            throws RedisException {
        return redisListTool.trim(redisDataPrefix + key, start, end);
    }

    public void setSetData(String key, Set<? extends Serializable> set, long expire,
                           TimeUnit timeUnit) {
        redisSetTool.setSetData(redisDataPrefix + key, set, expire, timeUnit);
    }

    public void setSetData(String key, Set<? extends Serializable> set, long expire) {
        redisSetTool.setSetData(redisDataPrefix + key, set, expire);
    }

    public void setSetData(String key, Set<? extends Serializable> set) {
        redisSetTool.setSetData(redisDataPrefix + key, set);
    }

    public <T> Set<T> getSetData(String key) {
        return redisSetTool.getSetData(redisDataPrefix + key);
    }

    public boolean isSetMember(String key, Serializable value) {
        return redisSetTool.isMember(redisDataPrefix + key, value);
    }

    public void setListData(String key, List<? extends Serializable> list, long expire,
                            TimeUnit timeUnit) {
        redisZSetTool.setZSetData(redisDataPrefix + key, list, expire, timeUnit);
    }

    public void setListData(String key, List<? extends Serializable> list, long expire) {
        redisZSetTool.setZSetData(redisDataPrefix + key, list, expire);
    }

    public void setListData(String key, List<? extends Serializable> list) {
        redisZSetTool.setZSetData(redisDataPrefix + key, list);
    }

    public <T> List<T> getListData(String key, boolean isReverse) {
        return redisZSetTool.getZSetData(redisDataPrefix + key, isReverse);
    }

    public <T> List<T> getListData(String key) {
        return redisZSetTool.getZSetData(redisDataPrefix + key);
    }

    public <T> RedisPageData<T> getPageData(int pageNo, int pageSize, String key) {
        return redisZSetTool.getPageData(pageNo, pageSize, redisDataPrefix + key);
    }

    public void setHashData(String key, Map<String, ? extends Serializable> hash,
                            long expire, TimeUnit timeUnit) {
        redisHashTool.setHashData(redisDataPrefix + key, hash, expire, timeUnit);
    }

    public void setHashData(String key, Map<String, ? extends Serializable> hash,
                            long expire) {
        redisHashTool.setHashData(redisDataPrefix + key, hash, expire);
    }

    public void setHashData(String key, Map<String, ? extends Serializable> hash) {
        redisHashTool.setHashData(redisDataPrefix + key, hash);
    }

    public <T> Map<String, T> getHashEntity(String key) {
        return redisHashTool.getHashEntity(redisDataPrefix + key);
    }

    public <T> List<T> getHashValueList(String key) {
        return redisHashTool.getHashValueList(redisDataPrefix + key);
    }

    public <T> T getHashValue(String key, String field) {
        return redisHashTool.getHashValue(redisDataPrefix + key, field);
    }


}
