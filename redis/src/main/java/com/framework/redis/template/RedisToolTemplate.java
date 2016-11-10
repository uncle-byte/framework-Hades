package com.framework.redis.template;

import com.framework.redis.bean.RedisPageData;
import com.framework.redis.exception.RedisException;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author chenhaipeng
 * @version 1.0
 * @date 2016/07/17 20:42
 */
public interface RedisToolTemplate<K, V> {

    public void setRedisDataPrefix(String redisDataPrefix);

    public String getRedisDataPrefix();

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
     * 写数据，并覆盖旧数据
     *
     * @param key
     * @param value
     * @param expire   过期时间
     * @param timeUnit 过期时间单位
     */
    void setData(final String key, final Serializable value, final long expire, final TimeUnit timeUnit);

    /**
     * 写数据，并覆盖旧数据
     *
     * @param key
     * @param value
     * @param expire 过期时间，单位为秒
     */
    void setData(final String key, final Serializable value, final long expire);

    /**
     * 写数据，并覆盖旧数据，永不过期
     *
     * @param key
     * @param value
     */
    void setData(final String key, final Serializable value);

    /**
     * 读取数据
     *
     * @param key
     * @return
     */
    <T> T getData(final String key);

    /**
     * 写入队列，并覆盖旧数据，不能用于分页
     *
     * @param key
     * @param list
     * @param expire   过期时间
     * @param timeUnit 过期时间单位
     */
    void setQueenData(final String key, final List<? extends Serializable> list, final long expire,
                      final TimeUnit timeUnit);

    /**
     * 写入队列，并覆盖旧数据，不能用于分页
     *
     * @param key
     * @param list
     * @param expire 过期时间，单位为秒
     */
    void setQueenData(final String key, final List<? extends Serializable> list, final long expire);

    /**
     * 写入队列，并覆盖旧数据，不能用于分页
     *
     * @param key
     * @param list
     */
    void setQueenData(final String key, final List<? extends Serializable> list);

    /**
     * 读取队列，不能用于分页
     *
     * @param key
     * @return
     */
    <T> List<T> getQueenData(final String key);

    /**
     * 截取列表，截取结果List[start, end]
     *
     * @param key
     * @param start
     * @param end
     * @return
     * @throws RedisException
     */
    boolean trimQueenData(String key, long start, long end) throws RedisException;

    /**
     * 写入集合，并覆盖旧数据
     *
     * @param key
     * @param set
     * @param expire   过期时间
     * @param timeUnit 过期时间单位
     */
    void setSetData(final String key, final Set<? extends Serializable> set, final long expire,
                    final TimeUnit timeUnit);

    /**
     * 写入集合，并覆盖旧数据
     *
     * @param key
     * @param set
     * @param expire 过期时间，单位为秒
     */
    void setSetData(final String key, final Set<? extends Serializable> set, final long expire);

    /**
     * 写入集合，并覆盖旧数据
     *
     * @param key
     * @param set
     */
    void setSetData(final String key, final Set<? extends Serializable> set);

    /**
     * 读取集合
     *
     * @param key
     * @return
     */
    <T> Set<T> getSetData(final String key);

    /**
     * 判断是否集合成员
     *
     * @param key
     * @param value
     * @return
     */
    boolean isSetMember(final String key, final Serializable value);

    /**
     * 写入有序集合，并覆盖旧数据
     *
     * @param key
     * @param list
     * @param expire   过期时间
     * @param timeUnit 过期时间单位
     */
    void setListData(final String key, final List<? extends Serializable> list, final long expire,
                     final TimeUnit timeUnit);

    /**
     * 写入有序集合，并覆盖旧数据
     *
     * @param key
     * @param list
     * @param expire 过期时间，单位为秒
     */
    void setListData(final String key, final List<? extends Serializable> list, final long expire);

    /**
     * 写入有序集合，并覆盖旧数据
     *
     * @param key
     * @param list
     */
    void setListData(final String key, final List<? extends Serializable> list);

    /**
     * 读取有序集合
     *
     * @param key
     * @param isReverse 是否反序
     * @return
     */
    <T> List<T> getListData(final String key, final boolean isReverse);

    /**
     * 读取有序集合，正序
     *
     * @param key
     * @return
     */
    <T> List<T> getListData(final String key);

    /**
     * 读取分页
     *
     * @param pageNo
     * @param pageSize
     * @param key
     * @return
     */
    <T> RedisPageData<T> getPageData(final int pageNo, final int pageSize, final String key);

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
