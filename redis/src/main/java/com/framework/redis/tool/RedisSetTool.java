package com.framework.redis.tool;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author chenhaipeng
 * @version 1.0
 * @date 2016/07/17 20:42
 */
public interface RedisSetTool extends RedisTool<String, Serializable> {
	
	/**
	 * 写入集合，并覆盖旧数据
	 * @param key
	 * @param set
	 * @param expire 过期时间
	 * @param timeUnit 过期时间单位
	 */
	void setSetData(final String key, final Set<? extends Serializable> set, final long expire,
					final TimeUnit timeUnit);
	
	/**
	 * 写入集合，并覆盖旧数据
	 * @param key
	 * @param set
	 * @param expire 过期时间，单位为秒
	 */
	void setSetData(final String key, final Set<? extends Serializable> set, final long expire);
	
	/**
	 * 写入集合，并覆盖旧数据
	 * @param key
	 * @param set
	 */
	void setSetData(final String key, final Set<? extends Serializable> set);
	
	/**
	 * 读取集合
	 * @param key
	 * @return
	 */
	<T> Set<T> getSetData(final String key);

	/**
	 * 判断是否集合成员
	 * @param key
	 * @param value
	 * @return
	 */
	boolean isMember(final String key, final Serializable value);

}
