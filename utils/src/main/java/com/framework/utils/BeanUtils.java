package com.framework.utils;

import java.util.Collection;
import java.util.Map;

/**
 * Bean 常用工具类，继承apache commons
 *
 * @author chenhaipeng
 * @version 1.0
 * @date 2016/11/16 12:04
 */
public class BeanUtils extends org.apache.commons.beanutils.BeanUtils {

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

}
