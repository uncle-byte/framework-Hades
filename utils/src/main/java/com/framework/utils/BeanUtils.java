package com.framework.utils;

import org.apache.commons.beanutils.*;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * Bean 常用工具类，继承apache commons
 *
 * @author chenhaipeng
 * @version 1.0
 * @date 2016/11/16 12:04
 */
public class BeanUtils extends org.apache.commons.beanutils.BeanUtils {

	private static Logger logger = LoggerFactory.getLogger(BeanUtils.class);
	/**
	 * BeanUtil类型转换器
	 */
	public static ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();

	private static BeanUtilsBean beanUtilsBean = new BeanUtilsBean(convertUtilsBean, new PropertyUtilsBean());

	static {
		convertUtilsBean.register(new DateConverter(), Date.class);
		convertUtilsBean.register(new LongConverter(), Long.class);
	}


	/**
	 * 可以用于判断 Map,Collection,String,Array,Long是否为空
	 *
	 * @param o java.lang.Object.
	 * @return boolean.
	 */
	@SuppressWarnings("unused")
	public static boolean isEmpty(Object o) {
		if (o == null) return true;
		if (o instanceof String) {
			if (((String) o).trim().length() == 0) {
				return true;
			}
		} else if (o instanceof Collection) {
			if (((Collection) o).isEmpty()) {
				return true;
			}
		} else if (o.getClass().isArray()) {
			if (((Object[]) o).length == 0) {
				return true;
			}
		} else if (o instanceof Map) {
			if (((Map) o).isEmpty()) {
				return true;
			}
		} else if (o instanceof Long) {
			Long lEmpty = 0L;
			if (o == null || lEmpty.equals(o)) {
				return true;
			}
		} else if (o instanceof Short) {
			Short sEmpty = 0;
			if (o == null || sEmpty.equals(o)) {
				return true;
			}
		} else if (o instanceof Integer) {
			Integer sEmpty = 0;
			if (o == null || sEmpty.equals(o)) {
				return true;
			}
		}

		return false;

	}

	/**
	 * 拷贝一个bean中的非空属性于另一个bean中
	 *
	 * @param dest 目标bean
	 * @param orig 源bean
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void copyNotNullProperties(Object dest, Object orig) {

		// Validate existence of the specified beans
		if (dest == null) {
			logger.error("No destination bean specified");
			return;
		}
		if (orig == null) {
			logger.error("No origin bean specified");
			return;
		}

		try {
			// Copy the properties, converting as necessary
			if (orig instanceof DynaBean) {
				DynaProperty[] origDescriptors = ((DynaBean) orig).getDynaClass()
						.getDynaProperties();
				for (int i = 0; i < origDescriptors.length; i++) {
					String name = origDescriptors[i].getName();
					if (beanUtilsBean.getPropertyUtils().isReadable(orig, name)
							&& beanUtilsBean.getPropertyUtils().isWriteable(dest, name)) {
						Object value = ((DynaBean) orig).get(name);
						beanUtilsBean.copyProperty(dest, name, value);
					}
				}
			} else if (orig instanceof Map) {
				Iterator entries = ((Map) orig).entrySet().iterator();
				while (entries.hasNext()) {
					Map.Entry entry = (Map.Entry) entries.next();
					String name = (String) entry.getKey();
					if (beanUtilsBean.getPropertyUtils().isWriteable(dest, name)) {
						beanUtilsBean.copyProperty(dest, name, entry.getValue());
					}
				}
			} else /* if (orig is a standard JavaBean) */ {
				PropertyDescriptor[] origDescriptors = beanUtilsBean.getPropertyUtils()
						.getPropertyDescriptors(orig);
				for (int i = 0; i < origDescriptors.length; i++) {
					String name = origDescriptors[i].getName();
					if ("class".equals(name)) {
						continue; // No point in trying to set an object's class
					}
					if (beanUtilsBean.getPropertyUtils().isReadable(orig, name)
							&& beanUtilsBean.getPropertyUtils().isWriteable(dest, name)) {
						try {
							Object value = beanUtilsBean.getPropertyUtils().getSimpleProperty(orig, name);
							if (value != null && !value.toString().equals("null")) {
								beanUtilsBean.copyProperty(dest, name, value);
							}
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException("copyNotNullProperties error!",ex);
		}


	}

}
