package com.framework.redis.tool;

import com.framework.redis.exception.RedisException;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author chenhaipeng
 * @version 1.0
 * @date 2016/07/17 20:42
 */
public interface RedisTool<K, V> {

    /**
     * 设置redisTemplate
     *
     * @param redisTemplate
     */
    void setRedisTemplate(RedisTemplate<K, V> redisTemplate);

    /**
     * 获取redisTemplate
     *
     * @return
     */
    RedisTemplate<K, V> getRedisTemplate();

    /**
     * 判断是否存在key
     *
     * @param key
     * @return
     */
    boolean hasKey(final K key);

    /**
     * 删除key
     *
     * @param key
     * @return true表示成功执行del命令，即使实际上没有删除对应的key（可能不存在）
     * @throws RedisException
     */
    boolean delete(final K key) throws RedisException;

    /**
     * 批量删除key
     *
     * @param keys
     * @return true表示成功执行del命令，即使实际上没有删除对应的key（可能不存在）
     * @throws RedisException
     */
    boolean delete(Collection<K> keys) throws RedisException;

    /**
     * 执行lua脚本，并返回执行结果
     *
     * @param resultType
     * @param script     lua脚本
     * @param keys       参数名，KEYS[1], KEYS[2]....
     * @param args       参数值
     * @return
     */
    <T> T executeScript(final Class<T> resultType, final String script, final List<K> keys,
                        final Object... args);

    /**
     * List数据类型转换
     *
     * @param data
     * @return
     */
    <T> List<T> changeDataType(List<? extends Serializable> data);

    /**
     * Set数据类型转换
     *
     * @param data
     * @return
     */
    <T> Set<T> changeDataType(Set<? extends Serializable> data);

    /**
     * Hash数据类型转换
     *
     * @param data
     * @return
     */
    <T> Map<String, T> changeDataType(Map<String, ? extends Serializable> data);

}
