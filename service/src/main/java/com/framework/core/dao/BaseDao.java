package com.framework.core.dao;

import com.framework.core.page.DataTableColumn;
import com.framework.core.page.DataTableOrder;
import com.framework.core.page.Pagination;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.QueryResult;
import org.nutz.dao.entity.Record;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.util.NutMap;

import java.util.List;

/**
 * 封装NutzDao
 * 其他Dao继承此Dao时，需要使用范型指明实体类
 *
 * @author chenhaipeng
 * @version 1.0
 * @date 2016/07/17 22:23
 */
public interface BaseDao<T> {

    public T insert(T classOfT);

    /**
     * 快速插入数据
     * 不返回ID
     * 性能较insert高
     *
     * @param classOfT
     * @return {@code
     * xxxDao.fastInsert(classOfT)
     * }
     */
    public T fastInsert(T classOfT);

    /**
     * 插入多条数据
     * 出入后，Nutz会查询当前插入的数据的ID
     *
     * @param listOfT
     * @return
     */
    public boolean insert(List<T> listOfT);

    /**
     * 插入多条数据
     * 出入后，Nutz不会查询当前插入的数据的ID
     *
     * @param listOfT
     * @return
     */
    public boolean fastInsert(List<T> listOfT);

    /**
     * 根据数字主键（ID）删除
     * DAO层需使用范型指明实体类
     *
     * @param id
     * @return {@code
     * xxxDao.deleteById(1) ;
     * }
     */
    public boolean deleteById(Long id);

    /**
     * 根据字符串主键（Name）删除
     * DAO层需使用范型指明实体类
     *
     * @param pk
     * @return {@code
     * xxxDao.deleteByStrPrimaryKey("zhangsan")
     * }
     */
    public boolean deleteByStrPrimaryKey(String pk);

    /**
     * 根据对象删除
     *
     * @param classOfT
     * @return {@code
     * xxxDao.deleteByEntity(classOfT) ;
     * }
     */
    public boolean deleteByEntity(T classOfT);

    /**
     * 更新实体类
     *
     * @param classOfT {@code
     *                 xxxDao.updateEntity(classOfT) ;
     *                 }
     */
    public boolean updateEntity(T classOfT);

    /**
     * 更新一个实体类List
     * 注：实体类的主键必须不为空
     *
     * @param list
     * @return
     */
    public boolean updateEntity(List<T> list);
//*********************  改-end  *********************//


//*********************  查-begin  *********************//

    /**
     * 根据数字主键（ID）查询
     * 不必指明实体类，但相应的Dao继承BaseDao时必须使用范型指明
     *
     * @param id
     * @return {@code
     * T t = tDao.getEntityById(1) ;
     * }
     */
    public T getEntityById(Long id);

    /**
     * 根据数字主键（ID）查询
     * 需指明实体类
     *
     * @param clazz
     * @param id
     * @return {@code
     * T t = xxxDao.getEntityById(T.class, 1) ;
     * }
     */
    public T getEntityById(Class<T> clazz, Long id);

    /**
     * 根据字符串主键（Name）查询
     *
     * @param key
     * @return {@code
     * T t = tDao.getEntityByStrPrimaryKey("zhangsan") ;
     * }
     */
    public T getEntityByStrPrimaryKey(String key);

    /**
     * 根据字符串主键（Name）查询
     * 需指明实体类
     *
     * @param clazz
     * @param pk
     * @return {@code
     * T t = xxxDao.getEntityByStrPrimaryKey(T.class, "zhangsan") ;
     * }
     */
    public T getEntityByStrPrimaryKey(Class<T> clazz, String pk);

    /**
     * 根据复合主键查询实体类
     *
     * @param pk1
     * @param pk2
     * @return
     */
    public T getEntityByPK(String pk1, String pk2);

    /**
     * 根据复合主键查询实体类
     *
     * @param pk1
     * @param pk2
     * @return
     */
    public T getEntityByPK(Long pk1, Long pk2);

    /**
     * 条件分页查询
     * 需明确指定实体类，根据条件分页查询
     * 只返回查询的实体List，不返回pager
     *
     * @param clazz          明确指定实体类
     * @param cond           查询条件
     * @param currentPageNum 当前页
     * @param pageSize
     * @return {@code
     * ------------------------------------------------------------------------------------------------------------
     * 简单条件查询（查询name中包含“zhang”，且sex为“男”的前10条数据，按照name升序、id降序排列）：
     * Condition cond = Cnd.where("name", "like", "%zhang%").and("sex", "=", "男").asc("name").desc("id");
     * List<T> list = xxxDao.getListByPager(T.class, cond, 1, 10) ;
     * ------------------------------------------------------------------------------------------------------------
     * ------------------------------------------------------------------------------------------------------------
     * 复杂条件查询
     * SqlExpressionGroup e1 = Cnd.exps("name", "like", "%zhang%").and("id", ">", "15") ;
     * SqlExpressionGroup e2 = Cnd.exps("name", "like", "%zh%").and("sex", "=", "女") ;
     * Condition cond = Cnd.where(e1).or(e2).asc("name");
     * List<T> list = xxxDao.getListByPager(T.class, cond, 1, 10) ;
     * ------------------------------------------------------------------------------------------------------------
     * }
     */
    public List<T> getListByPager(Class<T> clazz, Condition cond, int currentPageNum, int pageSize);

    /**
     * 条件分页查询
     * 需明确指定实体类，根据条件分页查询
     * 讲查询结果（List和Pager）一并返回
     *
     * @param clazz          明确指定实体类
     * @param cond           查询条件
     * @param currentPageNum 当前页
     * @param pageSize
     * @return new QueryResult(List,Pager) ;
     * {@code
     * ------------------------------------------------------------------------------------------------------------
     * 查询返回QueryResult对象，QueryResult对象中有一个List<T>和一个Pager对象
     * <p>
     * Condition cond = Cnd.where("name", "like", "%zhang%").and("sex", "=", "男").asc("name").desc("id");
     * QueryResult qr = xxxDao.getListByPager(T.class, cond, 1, 10) ;
     * ------------------------------------------------------------------------------------------------------------
     * }
     */
    public <T> QueryResult getListByPager2(Class<T> clazz, Condition cond, int currentPageNum, int pageSize) throws Exception;

    /**
     * 条件分页查询
     * pager为null，则查询全部
     * 不用指定实体类，但是调用此方法的Dao在继承BaseDao时必须指定范型（实体类），根据条件分页查询
     * 只返回查询的实体List，不返回pager
     *
     * @param cond           查询条件
     * @param currentPageNum 当前页
     * @param pageSize
     * @return {@code
     * ------------------------------------------------------------------------------------------------------------
     * 简单条件查询（查询name中包含“zhang”，且sex为“男”的前10条数据，按照name升序、id降序排列）：
     * Condition cond = Cnd.where("name", "like", "%zhang%").and("sex", "=", "男").asc("name").desc("id");
     * List<T> list = xxxDao.getListByPager(cond, 1, 10) ;
     * ------------------------------------------------------------------------------------------------------------
     * ------------------------------------------------------------------------------------------------------------
     * 复杂条件查询
     * SqlExpressionGroup e1 = Cnd.exps("name", "like", "%zhang%").and("id", ">", "15") ;
     * SqlExpressionGroup e2 = Cnd.exps("name", "like", "%zh%").and("sex", "=", "女") ;
     * Condition cond = Cnd.where(e1).or(e2).asc("name");
     * List<T> list = xxxDao.getListByPager(cond, 1, 10) ;
     * ------------------------------------------------------------------------------------------------------------
     * }
     */
    public List<T> getListByPager(Condition cond, int currentPageNum, int pageSize) throws Exception;

    /**
     * 条件分页查询
     * 不用指定实体类，但是调用此方法的Dao在继承BaseDao时必须指定范型（实体类），根据条件分页查询
     * 只返回查询的实体List，不返回pager
     *
     * @param cond           查询条件
     * @param currentPageNum 当前页
     * @param pageSize
     * @return new QueryResult(List,Pager) ;
     * {@code
     * ------------------------------------------------------------------------------------------------------------
     * 查询返回QueryResult对象，QueryResult对象中有一个List<T>和一个Pager对象
     * <p>
     * Condition cond = Cnd.where("name", "like", "%zhang%").and("sex", "=", "男").asc("name").desc("id");
     * QueryResult qr = xxxDao.getListByPager(cond, 1, 10) ;
     * ------------------------------------------------------------------------------------------------------------
     * }
     */
    public QueryResult getListByPager2(Condition cond, int currentPageNum, int pageSize);

    /**
     * 分页查询所有记录
     *
     * @param currentPageNum 当前页
     * @param pageSize
     * @return new QueryResult(List,Pager) ;
     */
    public QueryResult getListByPager2(int currentPageNum, int pageSize);

    /**
     * 条件查询-不分页
     * 不用指定实体类，但是调用此方法的Dao在继承BaseDao时必须指定范型（实体类）
     *
     * @param cond 查询条件
     * @return List;
     * {@code
     * ------------------------------------------------------------------------------------------------------------
     * 查询返回List<T>
     * <p>
     * Condition cond = Cnd.where("name", "like", "%zhang%").and("sex", "=", "男").asc("name").desc("id");
     * ------------------------------------------------------------------------------------------------------------
     * }
     */
    public List<T> getEntityList(Condition cond);

    T getEntity(Condition cond);

    /**
     * 按查询条件翻页查询
     *
     * @param cond
     * @param pager
     * @return
     */
    public List<T> getEntityList(Condition cond, Pager pager);

    /**
     * 条件查询-不分页
     * 需要指定具体的实体类
     *
     * @param clazz 必须是实体类的class
     * @param cond
     * @return
     */
    public List getEntityList(Class clazz, Condition cond);

    /**
     * 根据条件查询数据条数
     * 需指明实体类
     *
     * @param clazz
     * @param cond
     * @return {@code
     * Condition cond = Cnd.where("name", "like", "%zhang%").and("sex", "=", "男").asc("name").desc("id");
     * int totalCount = xxxDao.count(T.class, cond) ;
     * }
     */
    public int getCount(Class<T> clazz, Condition cond);

    /**
     * 使用Sql查询数据条数
     *
     * @param sql
     * @return {@code
     * String sql = "select count(distinct loan_id) from TAA_CAPITAL_RECORD where user_id='" + userId + "'";
     * int count = xxxDao.getCountBySql(sql) ;
     * }
     */
    @Deprecated
    public int getCountBySql(String sql);

    /**
     * 根据条件查询数据条数
     * 不用指明实体类
     *
     * @param cond
     * @return {@code
     * Condition cond = Cnd.where("name", "like", "%zhang%").and("sex", "=", "男").asc("name").desc("id");
     * int totalCount = xxxDao.count(cond) ;
     * }
     */
    public int getCount(Condition cond);

    /**
     * 多表查询，可使用任意POJO对象接收查询结果
     * 非翻页查询
     *
     * @param clazz
     * @param sql
     * @return
     * @throws Exception
     */
    public QueryResult getEntityListBySql(Class clazz, Sql sql);

    /**
     * 多表查询，可使用任意POJO对象接收查询结果
     * 翻页查询
     * @param clazz
     * @param sqlStr
     * @return
     * {@code
     * 		String sqlStr = "select li.* from tap_loan_info li where li.status='6' and li.wehter_generation=0 and li.loan_id in (select distinct loan_id from taa_capital_record cr where cr.user_id='" + userId + "' and cr.type_code=7)" ;
     * 		QueryResult qr = xxxDao.getEntityListBySql(clazz, sqlStr, 1, 10) ;
     * }
     */

    /**
     * 多表查询，可使用任意POJO对象接收查询结果
     * 翻页查询
     *
     * @param clazz
     * @param sql
     * @param currentPageNum
     * @param pageSize
     * @return
     * @throws Exception
     */
    public QueryResult getEntityListBySql(Class clazz, Sql sql, int currentPageNum, int pageSize);

    /**
     * 执行自定义sql
     * 返回查询结果和Pager
     * 需要创建Sql对象	Sql sql = Sqls.create("select x.xx,o.oo from X x, O o where x.x = o.o");
     *
     * @param sql            查询数据的sql对象
     * @param currentPageNum
     * @param pageSize
     * @return {@code
     * ------------------------------------------------------------------------------------------------------------
     * 多表查询需指明查询的字段及别名，查询结果（QueryResult）中存放一个List<Map>，map的key即为sql中的字段别名（全小写）
     * Sql sql = Sqls.create("select x.xx,o.oo from X x, O o where x.x = o.o");
     * QueryResult qr = xxxDao.getListBySql(sql, 1, 10) ;
     * ------------------------------------------------------------------------------------------------------------
     * }
     */
    public QueryResult getListBySql(Sql sql, int currentPageNum, int pageSize);

    /**
     * 执行自定义sql
     * 非翻页查询
     *
     * @param sql
     * @return
     * @throws Exception
     */
    public QueryResult getListBySql(Sql sql);


    /**
     * 使用原始SQL查询
     *
     * @param clazz
     * @param sql
     * @return
     * @throws Exception
     */
    public List getEntityListBySql2(Class clazz, Sql sql);


    void delete(Integer[] ids);

    void delete(Long[] ids);

    void delete(String[] ids);

    T getField(String fieldName, long id);

    T getField(String fieldName, int id);

    T getField(String fieldName, String name);

    T getField(String fieldName, Condition cnd);

    <T> List<Record> getEntityRecordBySQL(Sql sql);

    Pagination getEntityListPage(Integer pageNumber, Condition cnd);

    Pagination getEntityListPage(Integer pageNumber, Sql sql);

    Pagination getEntityListPage(Integer pageNumber, String tableName, Condition cnd);

    Pagination getEntityListPage(Integer pageNumber, int pageSize, Condition cnd);

    Pagination getEntityListPage(Integer pageNumber, int pageSize, Condition cnd, String fieldName);

    Pagination getEntityListPage(Integer pageNumber, int pageSize, String tableName, Condition cnd);

    Pagination getEntityListPage(Integer pageNumber, int pageSize, Sql sql);

    int getPageNumber(Integer pageNumber);

    NutMap getEntityMapdata(int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, Cnd cnd, String linkname);

    NutMap getEntityMapdata(int length, int start, int draw, Sql countSql, Sql orderSql);
}

