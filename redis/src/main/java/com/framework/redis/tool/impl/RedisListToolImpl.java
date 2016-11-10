package com.framework.redis.tool.impl;

import com.framework.redis.exception.RedisException;
import com.framework.redis.tool.RedisListTool;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RedisListToolImpl extends RedisToolImpl implements RedisListTool {
	
	public RedisListToolImpl() {}
	public RedisListToolImpl(RedisTemplate redisTemplate) {
		super(redisTemplate);
	}

	public void setListData(final String key, final List<? extends Serializable> list,
			final long expire, final TimeUnit timeUnit) {
		//执行事务
		super.redisTemplate.execute(new SessionCallback<Object>() {

			public Object execute(RedisOperations operations)
					throws DataAccessException {
				operations.multi();
				//1.删除旧数据
				operations.delete(key);
				//2.写入新数据
				ListOperations<String, Serializable> ops = operations.opsForList();
				for (Serializable value : list) {
					ops.rightPush(key, value);
				}
				
				if (expire > 0) {
					operations.expire(key, expire, timeUnit);
				}
				
				operations.exec();
				return null;
			}
		});
	}
	
	public void setListData(final String key, final List<? extends Serializable> list,
			final long expire) {
		this.setListData(key, list, expire, TimeUnit.SECONDS);
	}

	public void setListData(final String key, final List<? extends Serializable> list) {
		this.setListData(key, list, 0L);
	}

	public <T> List<T> getListData(final String key) {
		List<Serializable> list = new ArrayList<Serializable>();
		
		if (redisTemplate.hasKey(key)) {
			ListOperations<String, Serializable> ops = redisTemplate.opsForList();
			list = ops.range(key, 0, -1);
		}
		
		return changeDataType(list);
	}

	public boolean trim(final String key, final long start, final long end) throws RedisException {
		boolean flag = false;
		ListOperations<String, Serializable> ops = redisTemplate.opsForList();
		try {
			ops.trim(key, start, end);
			flag = true;
		} catch (Exception e) {
			flag = false;
			throw new RedisException("截取Redis列表失败！", e);
		}
		return flag;
	}

}
