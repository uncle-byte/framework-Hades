package com.framework.redis.tool.impl;


import com.framework.redis.bean.RedisPageData;
import com.framework.redis.tool.RedisZSetTool;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author chenhaipeng
 * @version 1.0
 * @date 2016/07/17 20:42
 */
public class RedisZSetToolImpl extends RedisToolImpl implements RedisZSetTool {

    public RedisZSetToolImpl() {
    }

    public RedisZSetToolImpl(RedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    public void setZSetData(final String key, final List<? extends Serializable> list,
                            final long expire, final TimeUnit timeUnit) {
        //执行事务
        redisTemplate.execute(new SessionCallback<Object>() {

            public Object execute(RedisOperations operations)
                    throws DataAccessException {
                operations.multi();
                //1.删除旧数据
                operations.delete(key);
                //2.写入新数据
                ZSetOperations<String, Serializable> ops = operations.opsForZSet();
                long timestamp = new Date().getTime();
                for (int i = 0, len = list.size(); i < len; i++) {
                    ops.add(key, list.get(i), timestamp++);
                }

                if (expire > 0) {
                    operations.expire(key, expire, timeUnit);
                }

                operations.exec();
                return null;
            }
        });
    }

    public void setZSetData(final String key, final List<? extends Serializable> list,
                            final long expire) {
        this.setZSetData(key, list, expire, TimeUnit.SECONDS);
    }

    public void setZSetData(final String key, final List<? extends Serializable> list) {
        this.setZSetData(key, list, 0L);
    }

    public void addZSetData(final String key, double score, Serializable member) {
        ZSetOperations<String, Serializable> ops = redisTemplate.opsForZSet();
        ops.add(key, member, score);
    }

    public void addZSetData(final String key, Serializable member) {
        this.addZSetData(key, new Date().getTime(), member);
    }

    public void addZSetData(String key, Set<TypedTuple<Serializable>> tuples) {
        ZSetOperations<String, Serializable> ops = redisTemplate.opsForZSet();
        ops.add(key, tuples);
    }

    public void addZSetData(String key, List<Serializable> members) {
        Set<TypedTuple<Serializable>> tuples = new HashSet<TypedTuple<Serializable>>();
        TypedTuple<Serializable> tuple = null;
        long timestamp = new Date().getTime();
        for (Serializable member : members) {
            tuple = new DefaultTypedTuple(member, (double) (timestamp++));
            tuples.add(tuple);
        }

        this.addZSetData(key, tuples);
    }

    public <T> List<T> getZSetData(final String key, final boolean isReverse) {
        List<Serializable> list = new ArrayList<Serializable>();

        if (redisTemplate.hasKey(key)) {
            ZSetOperations<String, Serializable> ops = redisTemplate.opsForZSet();
            Set<Serializable> set = new HashSet<Serializable>();
            if (isReverse) {
                set = ops.reverseRange(key, 0, -1);
            } else {
                set = ops.range(key, 0, -1);
            }

            for (Serializable value : set) {
                list.add(value);
            }
        }

        return changeDataType(list);
    }

    public <T> List<T> getZSetData(final String key) {
        return this.getZSetData(key, false);
    }

    public <T> RedisPageData<T> getPageData(final int pageNo, final int pageSize, final String key) {
        RedisPageData<T> pageData = null;

        if (redisTemplate.hasKey(key)) {
            ZSetOperations<String, Serializable> ops = redisTemplate.opsForZSet();

            int min = (pageNo - 1) * pageSize;
            int max = min + pageSize - 1;
            Long count = ops.size(key);

//			Set<Serializable> set = ops.rangeByScore(key, min, max);
            Set<Serializable> set = ops.range(key, min, max);

            List<T> list = new ArrayList<T>();
            for (Serializable value : set) {
                list.add((T) value);
            }

            pageData = new RedisPageData<T>();
            pageData.setPageNo(pageNo);
            pageData.setPageSize(pageSize);
            pageData.setCount(count);
            pageData.setData(list);
        }

        return pageData;

    }

}
