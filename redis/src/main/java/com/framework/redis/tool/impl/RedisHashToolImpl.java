package com.framework.redis.tool.impl;

import com.framework.redis.tool.RedisHashTool;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RedisHashToolImpl extends RedisToolImpl implements RedisHashTool {
	
	public RedisHashToolImpl() {}
	public RedisHashToolImpl(RedisTemplate redisTemplate) {
		super(redisTemplate);
	}

	public void setHashData(final String key, final Map<String, ? extends Serializable> hash,
			final long expire, final TimeUnit timeUnit) {
		
		redisTemplate.execute(new SessionCallback<Object>() {

			public Object execute(RedisOperations operations)
					throws DataAccessException {
				
				operations.multi();
				//1.删除旧数据
				operations.delete(key);
				//2.写入新数据
				HashOperations<String, String, Serializable> ops = operations.opsForHash();
				ops.putAll(key, hash);
				
				//3.设置过期时间
				if (expire > 0) {
					operations.expire(key, expire, timeUnit);
				}
				operations.exec();
				return null;
			}
		});
		
	}
	
	public void setHashData(final String key, final Map<String, ? extends Serializable> hash,
			final long expire) {
		this.setHashData(key, hash, expire, TimeUnit.SECONDS);
	}
	
	public void setHashData(final String key, final Map<String, ? extends Serializable> hash) {
		this.setHashData(key, hash, 0L, TimeUnit.SECONDS);
	}
	
	public <T> Map<String, T> getHashEntity(final String key) {
		Map<String, Serializable> entity = null;
		
		if (redisTemplate.hasKey(key)) {
			HashOperations<String, String, Serializable> ops = redisTemplate.opsForHash();
			entity = ops.entries(key);
		}
		
		return changeDataType(entity);
	}
	
	public <T> List<T> getHashValueList(final String key) {
		List<Serializable> list = new ArrayList<Serializable>();
		
		if (redisTemplate.hasKey(key)) {
			HashOperations<String, String, Serializable> ops = redisTemplate.opsForHash();
			list = ops.values(key);
		}
		
		return changeDataType(list);
		
	}
	
	public <T> T getHashValue(final String key, final String field) {
		Serializable result = null;
		
		HashOperations<String, String, Serializable> ops = redisTemplate.opsForHash();
		if (ops.hasKey(key, field)) {
			result = ops.get(key, field);
		}
		
		return (T)result;
	}
}
