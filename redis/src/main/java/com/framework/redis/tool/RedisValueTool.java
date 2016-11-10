package com.framework.redis.tool;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author chenhaipeng
 * @version 1.0
 * @date 2016/07/17 20:42
 */
public interface RedisValueTool extends RedisTool<String, Serializable> {

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
}
