package com.framework.redis.tool;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author chenhaipeng
 * @version 1.0
 * @date 2016/07/17 20:42
 */
public interface RedisHashTool extends RedisTool<String, Serializable> {

    /**
     * 写入哈希表，覆盖旧数据
     *
     * @param key
     * @param hash
     * @param expire   过期时间
     * @param timeUnit 过期时间单位
     */
    void setHashData(String key, Map<String, ? extends Serializable> hash,
                     long expire, TimeUnit timeUnit);

    /**
     * 写入哈希表，覆盖旧数据
     *
     * @param key
     * @param hash
     * @param expire 过期时间，单位为秒
     */
    void setHashData(String key, Map<String, ? extends Serializable> hash, long expire);

    /**
     * 写入哈希表，覆盖旧数据
     *
     * @param key
     * @param hash
     */
    void setHashData(String key, Map<String, ? extends Serializable> hash);

    /**
     * 读取哈希表实体
     *
     * @param key
     * @return
     */
    <T> Map<String, T> getHashEntity(String key);

    /**
     * 读取哈希表所有属性
     *
     * @param key
     * @return
     */
    <T> List<T> getHashValueList(String key);

    /**
     * 读取哈希表指定属性
     *
     * @param key
     * @param field 属性名
     * @return
     */
    <T> T getHashValue(String key, String field);
}
