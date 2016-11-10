package com.framework.redis.tool;

import com.framework.redis.exception.RedisException;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author chenhaipeng
 * @version 1.0
 * @date 2016/07/17 20:42
 */
public interface RedisListTool extends RedisTool<String, Serializable> {

    /**
     * 写入队列，并覆盖旧数据，不能用于分页
     *
     * @param key
     * @param list
     * @param expire   过期时间
     * @param timeUnit 过期时间单位
     */
    void setListData(final String key, final List<? extends Serializable> list, final long expire,
                     final TimeUnit timeUnit);

    /**
     * 写入队列，并覆盖旧数据，不能用于分页
     *
     * @param key
     * @param list
     * @param expire 过期时间，单位为秒
     */
    void setListData(final String key, final List<? extends Serializable> list, final long expire);

    /**
     * 写入队列，并覆盖旧数据，不能用于分页
     *
     * @param key
     * @param list
     */
    void setListData(final String key, final List<? extends Serializable> list);

    /**
     * 读取队列，不能用于分页
     *
     * @param key
     * @return
     */
    <T> List<T> getListData(final String key);

    /**
     * 截取列表，截取结果List[start, end]
     *
     * @param key
     * @param start
     * @param end
     * @return
     * @throws RedisException
     */
    boolean trim(String key, long start, long end) throws RedisException;

}
