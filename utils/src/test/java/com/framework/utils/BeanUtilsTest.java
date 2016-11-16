package com.framework.utils;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * BeanUtils Tester. 
 * 
 * @author chenhaipeng 
 * @since <pre>十一月 16, 2016</pre> 
 * @version 1.0 
*/ 
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
public class BeanUtilsTest {

    @Test
    /**
     * servlet中有这样的使用：
     先定义form表单内容的Info对象(当然你要先写一个bean,这个bean中包含form表单中各个对象的属性)
     InsuranceInfo info = new InsuranceInfo();  （这是一个javabean）
     BeanUtilities.populateBean(info, request);
     ——> populateBean(info, request.getParameterMap());（先将request内容转为Map类型）
     ——>BeanUtils.populate(info, propertyMap);（调用包中方法映射）

     tip : 不能内部类的
     */
    public void testPopulate() throws InvocationTargetException, IllegalAccessException {
        Person person = new Person();
        Map<String,String> map = new HashMap<>();
        map.put("name","2324324");
        BeanUtils.populate(person,map);
        System.out.println(person);

    }



} 
