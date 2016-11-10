package com.framework.core.nutz;

import org.nutz.dao.ConnCallback;
import org.nutz.dao.impl.DaoRunner;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Spring管理Nutz事务时使用
 * @author chenhaipeng
 * @version 1.0
 * @date 2016/07/17 22:23
 */
public class SpringDaoRunner implements DaoRunner {

    public void run(DataSource dataSource, ConnCallback callback) {
        Connection con = DataSourceUtils.getConnection(dataSource);
        try {
            callback.invoke(con);
        } catch (Exception e) {
            if (e instanceof RuntimeException)
                throw (RuntimeException) e;
            else
                throw new RuntimeException(e);
        } finally {
            DataSourceUtils.releaseConnection(con, dataSource);
        }
    }
}