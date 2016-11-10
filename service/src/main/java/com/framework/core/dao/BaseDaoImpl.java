package com.framework.core.dao;

import com.framework.core.page.DataTableColumn;
import com.framework.core.page.DataTableOrder;
import com.framework.core.page.OffsetPager;
import com.framework.core.page.Pagination;
import org.apache.commons.collections4.CollectionUtils;
import org.nutz.dao.*;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.entity.Record;
import org.nutz.dao.impl.sql.SqlTemplate;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.Daos;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.service.EntityService;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 封装NutzDao
 * 其他Dao继承此Dao时，需要使用范型指明实体类
 *
 * @author chenhaipeng
 * @version 1.0
 * @date 2016/07/18 1:15
 */
public class BaseDaoImpl<T> extends EntityService<T> implements BaseDao<T> {

    protected final static int DEFAULT_PAGE_NUMBER = 10;

    @Resource
    protected Dao dao;    //自动注入Dao

    @Resource
    protected SqlTemplate sqlTemplate;    //自动注入sqlTemplate

    protected Class<T> entityClass;

    /**
     * 用于Dao层子类使用的构造函数.
     * 通过子类的泛型定义取得对象类型Class.
     * eg.
     * public class UserDao extends BaseDao<User>
     */
    public BaseDaoImpl() {
        Class cls = Object.class;
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (!(params[0] instanceof Class)) {
        } else {
            cls = (Class) params[0];
        }
        this.entityClass = cls;
    }


//*********************  增-begin  *********************//

    /**
     * 插入数据
     * 会把插入数据后的ID返回
     * 性能较fastInsert低
     *
     * @param classOfT
     * @return {@code
     * xxxDao.insert(classOfT)
     * }
     */
    public T insert(T classOfT) {
        return dao.insert(classOfT);
    }

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
    public T fastInsert(T classOfT) {

        return dao.fastInsert(classOfT);

    }

    /**
     * 插入多条数据
     * 出入后，Nutz会查询当前插入的数据的ID
     *
     * @param listOfT
     * @return
     */
    public boolean insert(List<T> listOfT) {
        boolean isSaved = false;
        try {
            dao.insert(listOfT);
            isSaved = true;
        } catch (Exception e) {
            isSaved = false;
            throw new DaoException(e);
        }

        return isSaved;
    }

    /**
     * 插入多条数据
     * 出入后，Nutz不会查询当前插入的数据的ID
     *
     * @param listOfT
     * @return
     */
    public boolean fastInsert(List<T> listOfT) {
        boolean isSaved = false;
        try {
            dao.fastInsert(listOfT);
            isSaved = true;
        } catch (Exception e) {
            isSaved = false;
            throw new DaoException(e);
        }

        return isSaved;
    }

//*********************  增-end  *********************//

//*********************  删-begin  *********************//

    /**
     * 根据数字主键（ID）删除
     * DAO层需使用范型指明实体类
     *
     * @param id
     * @return {@code
     * xxxDao.deleteById(1) ;
     * }
     */
    public boolean deleteById(Long id) {
        try {
            boolean isDeleted = false;
            if (dao.delete(entityClass, id) > 0) {
                isDeleted = true;
            }
            return isDeleted;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * 根据字符串主键（Name）删除
     * DAO层需使用范型指明实体类
     *
     * @param pk
     * @return {@code
     * xxxDao.deleteByStrPrimaryKey("zhangsan")
     * }
     */
    public boolean deleteByStrPrimaryKey(String pk) {
        try {
            boolean isDeleted = false;
            if (dao.delete(entityClass, pk) > 0) {
                isDeleted = true;
            }
            return isDeleted;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * 根据对象删除
     *
     * @param classOfT
     * @return {@code
     * xxxDao.deleteByEntity(classOfT) ;
     * }
     */
    public boolean deleteByEntity(T classOfT) {
        try {
            boolean isDeleted = false;
            if (dao.delete(classOfT) > 0) {
                isDeleted = true;
            }
            return isDeleted;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

//*********************  删-end  *********************//

//*********************  改-begin  *********************//

    /**
     * 更新实体类
     *
     * @param classOfT {@code
     *                 xxxDao.updateEntity(classOfT) ;
     *                 }
     */
    public boolean updateEntity(T classOfT) {
        try {
            boolean isUpdated = false;

            int updated = dao.update(classOfT);
            if (updated > 0) {
                isUpdated = true;
            }

            return isUpdated;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * 更新一个实体类List
     * 注：实体类的主键必须不为空
     *
     * @param list
     * @return
     */
    public boolean updateEntity(List<T> list) {
        try {
            boolean isUpdated = false;

            int updated = dao.update(list);
            if (updated > 0) {
                isUpdated = true;
            }

            return isUpdated;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

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
    public T getEntityById(Long id) {
        try {
            return dao.fetch(entityClass, id);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

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
    public T getEntityById(Class<T> clazz, Long id) {
        return (T) dao.fetch(clazz, id);
    }

    /**
     * 根据字符串主键（Name）查询
     *
     * @param key
     * @return {@code
     * T t = tDao.getEntityByStrPrimaryKey("zhangsan") ;
     * }
     */
    public T getEntityByStrPrimaryKey(String key) {
        return dao.fetch(entityClass, key);
    }

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
    public T getEntityByStrPrimaryKey(Class<T> clazz, String pk) {
        return (T) dao.fetch(clazz, pk);
    }

    /**
     * 根据复合主键查询实体类
     *
     * @param pk1
     * @param pk2
     * @return
     */
    public T getEntityByPK(String pk1, String pk2) {
        if (null == pk1 || null == pk2 || "".equals(pk1.trim()) || "".equals(pk2.trim())) {
            return null;
        }

        return (T) dao.fetchx(entityClass, pk1, pk2);
    }

    /**
     * 根据复合主键查询实体类
     *
     * @param pk1
     * @param pk2
     * @return
     */
    public T getEntityByPK(Long pk1, Long pk2) {
        if (0 == pk1 || 0 == pk2) {
            return null;
        }

        return (T) dao.fetchx(entityClass, pk1, pk2);
    }

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
    public List<T> getListByPager(Class<T> clazz, Condition cond, int currentPageNum, int pageSize) {
        List<T> list = new ArrayList<T>();

        if (null == clazz) {
            return list;
        }

        Pager pager = dao.createPager(currentPageNum, pageSize);    //创建Pager对象，设置当前页和pageSize
        pager.setRecordCount(dao.count(clazz, cond));        //设置数据总条数

        list = dao.query(clazz, cond, pager);    //分页查询

        return list;
    }

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
    public <T> QueryResult getListByPager2(Class<T> clazz, Condition cond, int currentPageNum, int pageSize) {
        try {
            List<T> list = new ArrayList<T>();

            if (null == clazz) {
                return new QueryResult();
            }

            Pager pager = dao.createPager(currentPageNum, pageSize);    //创建Pager对象，设置当前页和pageSize
            pager.setRecordCount(dao.count(clazz, cond));        //设置数据总条数

            list = dao.query(clazz, cond, pager);    //分页查询

            return new QueryResult(list, pager);
        } catch (Exception e) {
            throw new DaoException(e);
        }

    }

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
    public List<T> getListByPager(Condition cond, int currentPageNum, int pageSize) throws Exception {
        List<T> list = new ArrayList<T>();

        Pager pager = new Pager();
        if (0 < currentPageNum && 0 < pageSize) {
            dao.createPager(currentPageNum, pageSize);    //创建Pager对象，设置当前页和pageSize
            pager.setRecordCount(dao.count(entityClass, cond));        //设置数据总条数
        }

        list = dao.query(entityClass, cond, pager);    //分页查询

        return list;
    }

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
    public QueryResult getListByPager2(Condition cond, int currentPageNum, int pageSize) {
        List<T> list = new ArrayList<T>();

        Pager pager = dao.createPager(currentPageNum, pageSize);    //创建Pager对象，设置当前页和pageSize
        pager.setRecordCount(dao.count(entityClass, cond));        //设置数据总条数

        list = dao.query(entityClass, cond, pager);    //分页查询

        return new QueryResult(list, pager);
    }

    /**
     * 分页查询所有记录
     *
     * @param currentPageNum 当前页
     * @param pageSize
     * @return new QueryResult(List,Pager) ;
     */
    public QueryResult getListByPager2(int currentPageNum, int pageSize) {

        Pager pager = dao.createPager(currentPageNum, pageSize); // 创建Pager对象，设置当前页和pageSize
        pager.setRecordCount(dao.count(entityClass, null)); // 设置数据总条数

        List<T> list = dao.query(entityClass, null, pager); // 分页查询
        if (list == null) {
            list = new ArrayList<T>();
        }
        return new QueryResult(list, pager);
    }

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
    public List<T> getEntityList(Condition cond) {
        List<T> list = new ArrayList<T>();

        list = dao.query(entityClass, cond);    //条件查询

        return list;
    }

    /**
     * 返回单个entity
     *
     * @param cond
     * @return
     */
    @Override
    public T getEntity(Condition cond) {
        List<T> list = new ArrayList<T>();

        list = dao.query(entityClass, cond);    //条件查询
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public void insert(Chain chain) {
        super.insert(chain);
    }

    /**
     * 按查询条件翻页查询
     *
     * @param cond
     * @param pager
     * @return
     */
    public List<T> getEntityList(Condition cond, Pager pager) {
        List<T> list = new ArrayList<T>();

        list = dao.query(entityClass, cond, pager);    //条件查询

        return list;
    }

    /**
     * 条件查询-不分页
     * 需要指定具体的实体类
     *
     * @param clazz 必须是实体类的class
     * @param cond
     * @return
     */
    public List getEntityList(Class clazz, Condition cond) {
        List list = new ArrayList();

        list = dao.query(clazz, cond);    //条件查询

        return list;
    }

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
    public int getCount(Class<T> clazz, Condition cond) {
        if (null == clazz) {
            return 0;
        }
        return dao.count(clazz, cond);
    }

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
    public int getCountBySql(String sql) {
        return sqlTemplate.queryForInt(sql, null);
    }

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
    public int getCount(Condition cond) {
        return dao.count(entityClass, cond);
    }


    /**
     * 多表查询，可使用任意POJO对象接收查询结果
     * 非翻页查询
     *
     * @param clazz
     * @param sql
     * @return
     * @throws Exception
     */
    public QueryResult getEntityListBySql(Class clazz, Sql sql) {
        if (null == sql) {
            return null;
        }
        Pager pager = new Pager();
        QueryResult qr = new QueryResult();

        if (null != sql) {
            pager.setRecordCount((int) Daos.queryCount(this.dao, sql.toString()));// 记录数需手动设置
            sql.setCallback(Sqls.callback.entities());    //返回一组对象
            Entity<T> entity = dao.getEntity(clazz);
            sql.setEntity(entity);
            dao.execute(sql);

            qr.setList(sql.getList(clazz));
            qr.setPager(pager);
        }

        return qr;
    }

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
    public QueryResult getEntityListBySql(Class clazz, Sql sql, int currentPageNum, int pageSize) {
        if (null == sql) {
            return null;
        }

        Pager pager = null;
        QueryResult qr = new QueryResult();

        if (null != sql) {
            pager = dao.createPager(currentPageNum, pageSize);    //创建Pager对象，设置当前页和pageSize
            pager.setRecordCount((int) Daos.queryCount(this.dao, sql.toString()));// 记录数需手动设置
            sql.setPager(pager);
            sql.setCallback(Sqls.callback.entities());    //返回一组对象
            Entity<T> entity = dao.getEntity(clazz);
            sql.setEntity(entity);
            dao.execute(sql);

            qr.setList(sql.getList(clazz));
            qr.setPager(pager);
        }

        return qr;
    }

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
    public QueryResult getListBySql(Sql sql, int currentPageNum, int pageSize) {
        if (null == sql) {
            return null;
        }
        Pager pager = dao.createPager(currentPageNum, pageSize);    //创建Pager对象，设置当前页和pageSize
        pager.setRecordCount((int) Daos.queryCount(dao, sql.toString()));    //查询并设置数据总条数
        sql.setPager(pager);    //设置分页对象
        sql.setCallback(Sqls.callback.records());
        dao.execute(sql);    //执行sql

        return new QueryResult(sql.getList(Map.class), pager);
    }

    /**
     * 执行自定义sql
     * 非翻页查询
     *
     * @param sql
     * @return
     * @throws Exception
     */
    public QueryResult getListBySql(Sql sql) {
        if (null == sql) {
            return null;
        }

        Pager pager = null;
        QueryResult qr = new QueryResult();
        if (null != sql) {
            pager = new Pager();    //创建Pager对象
            pager.setRecordCount((int) Daos.queryCount(dao, sql.toString()));    //查询并设置数据总条数
            sql.setPager(pager);
            sql.setCallback(Sqls.callback.records());
            dao.execute(sql);

            qr.setList(sql.getList(Map.class));
            qr.setPager(pager);
        }

        return qr;
    }

    /**
     * 使用原始SQL查询
     *
     * @param clazz
     * @param sql
     * @return
     * @throws Exception
     */
    public List getEntityListBySql2(Class clazz, Sql sql) {
        if (null == sql) {
            return null;
        }
        List list = null;

        if (null != sql) {
            sql.setCallback(Sqls.callback.entities());    //返回一组对象
            Entity<T> entity = dao.getEntity(clazz);
            sql.setEntity(entity);
            dao.execute(sql);

            list = sql.getList(clazz);
        }

        return list;
    }

//*********************  查-end  *********************//

//以下为官方项目样例

    /**
     * 批量删除
     *
     * @param ids
     */
    @Override
    public void delete(Integer[] ids) {
        this.dao.clear(getEntityClass(), Cnd.where("id", "in", ids));
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    @Override
    public void delete(Long[] ids) {
        this.dao.clear(getEntityClass(), Cnd.where("id", "in", ids));
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    @Override
    public void delete(String[] ids) {
        this.dao.clear(getEntityClass(), Cnd.where("id", "in", ids));
    }

    /**
     * 通过LONG主键获取部分字段值
     *
     * @param fieldName
     * @param id
     * @return
     */
    @Override
    public T getField(String fieldName, long id) {
        return Daos.ext(this.dao, FieldFilter.create(getEntityClass(), fieldName))
                .fetch(getEntityClass(), id);
    }

    /**
     * 通过INT主键获取部分字段值
     *
     * @param fieldName
     * @param id
     * @return
     */
    @Override
    public T getField(String fieldName, int id) {
        return Daos.ext(this.dao, FieldFilter.create(getEntityClass(), fieldName))
                .fetch(getEntityClass(), id);
    }


    /**
     * 通过NAME主键获取部分字段值
     *
     * @param fieldName 支持通配符 ^(a|b)$
     * @param name
     * @return
     */
    @Override
    public T getField(String fieldName, String name) {
        return Daos.ext(this.dao, FieldFilter.create(getEntityClass(), fieldName))
                .fetch(getEntityClass(), name);
    }

    /**
     * 通过NAME主键获取部分字段值
     *
     * @param fieldName 支持通配符 ^(a|b)$
     * @param cnd
     * @return
     */
    @Override
    public T getField(String fieldName, Condition cnd) {
        return Daos.ext(this.dao, FieldFilter.create(getEntityClass(), fieldName))
                .fetch(getEntityClass(), cnd);
    }

    /**
     * 自定义SQL返回Record记录集，Record是个MAP但不区分大小写
     * 别返回Map对象，因为MySql和Oracle中字段名有大小写之分
     *
     * @param sql
     * @param <T>
     * @return
     */
    @Override
    public <T> List<Record> getEntityRecordBySQL(Sql sql) {
        sql.setCallback(Sqls.callback.records());
        this.dao.execute(sql);
        return sql.getList(Record.class);

    }

    /**
     * 分页查询
     *
     * @param pageNumber
     * @param cnd
     * @return
     */
    @Override
    public Pagination getEntityListPage(Integer pageNumber, Condition cnd) {
        return getEntityListPage(pageNumber, DEFAULT_PAGE_NUMBER, cnd);
    }

    /**
     * 分页查询
     *
     * @param pageNumber
     * @param sql
     * @return
     */
    @Override
    public Pagination getEntityListPage(Integer pageNumber, Sql sql) {
        return getEntityListPage(pageNumber, DEFAULT_PAGE_NUMBER, sql);
    }

    /**
     * 分页查询
     *
     * @param pageNumber
     * @param tableName
     * @param cnd
     * @return
     */
    @Override
    public Pagination getEntityListPage(Integer pageNumber, String tableName, Condition cnd) {
        return getEntityListPage(pageNumber, DEFAULT_PAGE_NUMBER, tableName, cnd);
    }

    /**
     * 分页查询(cnd)
     *
     * @param pageNumber
     * @param pageSize
     * @param cnd
     * @return
     */
    @Override
    public Pagination getEntityListPage(Integer pageNumber, int pageSize, Condition cnd) {
        pageNumber = getPageNumber(pageNumber);
        pageSize = getPageSize(pageSize);
        Pager pager = this.dao.createPager(pageNumber, pageSize);
        List<T> list = this.dao.query(getEntityClass(), cnd, pager);
        pager.setRecordCount(this.dao.count(getEntityClass(), cnd));
        return new Pagination(pageNumber, pageSize, pager.getRecordCount(), list);
    }

    /**
     * 分页查询,获取部分字段(cnd)
     *
     * @param pageNumber
     * @param pageSize
     * @param cnd
     * @param fieldName  支持通配符 ^(a|b)$
     * @return
     */
    @Override
    public Pagination getEntityListPage(Integer pageNumber, int pageSize, Condition cnd, String fieldName) {
        pageNumber = getPageNumber(pageNumber);
        pageSize = getPageSize(pageSize);
        Pager pager = this.dao.createPager(pageNumber, pageSize);
        List<T> list = Daos.ext(this.dao, FieldFilter.create(getEntityClass(), fieldName)).query(getEntityClass(), cnd);
        pager.setRecordCount(this.dao.count(getEntityClass(), cnd));
        return new Pagination(pageNumber, pageSize, pager.getRecordCount(), list);
    }

    /**
     * 分页查询(tabelName)
     *
     * @param pageNumber
     * @param pageSize
     * @param tableName
     * @param cnd
     * @return
     */
    @Override
    public Pagination getEntityListPage(Integer pageNumber, int pageSize, String tableName, Condition cnd) {
        pageNumber = getPageNumber(pageNumber);
        pageSize = getPageSize(pageSize);
        Pager pager = this.dao.createPager(pageNumber, pageSize);
        List<Record> list = this.dao.query(tableName, cnd, pager);
        pager.setRecordCount(this.dao.count(tableName, cnd));
        return new Pagination(pageNumber, pageSize, pager.getRecordCount(), list);
    }

    /**
     * 分页查询(sql)
     *
     * @param pageNumber
     * @param pageSize
     * @param sql
     * @return
     */
    @Override
    public Pagination getEntityListPage(Integer pageNumber, int pageSize, Sql sql) {
        pageNumber = getPageNumber(pageNumber);
        pageSize = getPageSize(pageSize);
        Pager pager = this.dao.createPager(pageNumber, pageSize);
        pager.setRecordCount((int) Daos.queryCount(this.dao, sql.toString()));// 记录数需手动设置
        sql.setPager(pager);
        sql.setCallback(Sqls.callback.records());
        dao().execute(sql);
        return new Pagination(pageNumber, pageSize, pager.getRecordCount(), sql.getList(Record.class));
    }

    /**
     * 默认页码
     *
     * @param pageNumber
     * @return
     */
    @Override
    public int getPageNumber(Integer pageNumber) {
        return Lang.isEmpty(pageNumber) ? 1 : pageNumber;
    }

    /**
     * 默认页大小
     *
     * @param pageSize
     * @return
     */
    protected int getPageSize(int pageSize) {
        return pageSize == 0 ? DEFAULT_PAGE_NUMBER : pageSize;
    }

    /**
     * DataTable Page
     *
     * @param length   页大小
     * @param start    start
     * @param draw     draw
     * @param orders   排序
     * @param columns  字段
     * @param cnd      查询条件
     * @param linkname 关联查询
     * @return
     */
    @Override
    public NutMap getEntityMapdata(int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, Cnd cnd, String linkname) {
        NutMap re = new NutMap();
        if (orders != null && orders.size() > 0) {
            for (DataTableOrder order : orders) {
                DataTableColumn col = columns.get(order.getColumn());
                cnd.orderBy(Sqls.escapeSqlFieldValue(col.getData()).toString(), order.getDir());
            }
        }
        Pager pager = new OffsetPager(start, length);
        re.put("recordsFiltered", this.dao.count(getEntityClass(), cnd));
        List<?> list = this.dao.query(getEntityClass(), cnd, pager);
        if (!Strings.isBlank(linkname)) {
            this.dao.fetchLinks(list, linkname);
        }
        re.put("data", list);
        re.put("draw", draw);
        re.put("recordsTotal", length);
        return re;
    }

    /**
     * DataTable Page SQL
     *
     * @param length   页大小
     * @param start    start
     * @param draw     draw
     * @param countSql 查询条件
     * @param orderSql 排序语句
     * @return
     */
    @Override
    public NutMap getEntityMapdata(int length, int start, int draw, Sql countSql, Sql orderSql) {
        NutMap re = new NutMap();
        Pager pager = new OffsetPager(start, length);
        pager.setRecordCount((int) Daos.queryCount(this.dao, countSql.toString()));// 记录数需手动设置
        orderSql.setPager(pager);
        orderSql.setCallback(Sqls.callback.records());
        this.dao.execute(orderSql);
        re.put("recordsFiltered", pager.getRecordCount());
        re.put("data", orderSql.getList(Record.class));
        re.put("draw", draw);
        re.put("recordsTotal", length);
        return re;
    }
}