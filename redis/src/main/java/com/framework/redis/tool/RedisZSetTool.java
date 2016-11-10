package com.framework.redis.tool;

import com.framework.redis.bean.RedisPageData;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis有序集合，相当于Java List，但会过滤重复数据
 * @author chenhaipeng
 * @version 1.0
 * @date 2016/07/17 20:42
 */
public interface RedisZSetTool extends RedisTool<String, Serializable> {

	/**
	 * 写入有序集合，并覆盖旧数据
	 * @param key
	 * @param list
	 * @param expire 过期时间
	 * @param timeUnit 过期时间单位
	 */
	void setZSetData(final String key, final List<? extends Serializable> list, final long expire,
					 final TimeUnit timeUnit);
	
	/**
	 * 写入有序集合，并覆盖旧数据
	 * @param key
	 * @param list
	 * @param expire 过期时间，单位为秒
	 */
	void setZSetData(final String key, final List<? extends Serializable> list, final long expire);
	
	/**
	 * 写入有序集合，并覆盖旧数据
	 * @param key
	 * @param list
	 */
	void setZSetData(final String key, final List<? extends Serializable> list);
	
	/**
	 * 插入有序集合
	 * @param key
	 * @param score
	 * @param member
	 */
	void addZSetData(final String key, double score, Serializable member);
	
	/**
	 * 插入有序集合，score默认当前时间戳
	 * @param key
	 * @param member
	 */
	void addZSetData(final String key, Serializable member);
	
	/**
	 * 批量插入有序集合
	 * @param key
	 * @param tuples
	 */
	void addZSetData(final String key, Set<TypedTuple<Serializable>> tuples);
	
	/**
	 * 批量插入有序集合，score默认当前时间戳
	 * @param key
	 * @param members
	 */
	void addZSetData(final String key, List<Serializable> members);
	
	/**
	 * 读取有序集合
	 * @param key
	 * @param isReverse 是否反序
	 * @return
	 */
	<T> List<T> getZSetData(final String key, final boolean isReverse);
	
	/**
	 * 读取有序集合，正序
	 * @param key
	 * @return
	 */
	<T> List<T> getZSetData(final String key);

	/**
	 * 读取分页
	 * @param pageNo
	 * @param pageSize
	 * @param key 
	 * @return
	 */
	<T> RedisPageData<T> getPageData(final int pageNo, final int pageSize, final String key);
}
