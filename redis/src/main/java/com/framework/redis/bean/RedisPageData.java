package com.framework.redis.bean;

import java.util.List;

/**
 * @author chenhaipeng
 * @version 1.0
 * @date 2016/07/17 20:42
 */
public class RedisPageData<T> {

    private int pageNo;

    private int pageSize;

    private long count;

    private List<T> data;

    public RedisPageData() {
    }

    public RedisPageData(int pageNo, int pageSize, long count) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.count = count;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> list) {
        this.data = list;
    }


}