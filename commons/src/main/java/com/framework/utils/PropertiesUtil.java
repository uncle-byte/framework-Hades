package com.framework.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {

	public static final String CONFIG_PATH = "config.properties";

	/* 持有私有静态实例，防止被引用，此处赋值为null，目的是实现延迟加载 */
	private static Properties instance = null;


	/**
	 * 单例
	 *
	 * @param filepath
	 * @return
	 * @throws Exception
	 */
	public static Properties getProperties(String filepath) throws Exception {
		return getInstances(filepath);
	}

	private static Properties getInstances(String filepath) throws IOException {
		if (instance == null) {
			synchronized (PropertiesUtil.class) {
				if (instance == null) {
					instance = new Properties();
					FileInputStream fis = new FileInputStream(filepath);
					instance.load(fis);
					return instance;
				}
			}
		}
		return instance;
	}

	/**
	 * 单例
	 *
	 * @throws Exception
	 */
	public static Properties getProperties() throws Exception {
		return getInstances(CONFIG_PATH);
	}

	/**
	 * 获取key
	 *
	 * @param key
	 * @return
	 */
	public static String getValue(String key) {
		try {
			if (instance.get(key) != null) {
				return instance.get(key).toString();
			}
		} catch (Exception e) {
			throw new RuntimeException("获取: " + key + " 失败");
		}
		return "";
	}

	/**
	 * 设置key/value
	 *
	 * @param key
	 * @param value
	 */
	public static void setValue(String key, String value) {
		try {
			instance.setProperty(key, value);
		} catch (Exception e) {
			throw new RuntimeException("设置: " + key + "value: " + value + " 失败");
		}
	}
}