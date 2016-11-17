package com.framework.json;

public class Monitoring {
	private static ThreadLocal<Long> begin = new ThreadLocal<Long>();
    public static void begin() {
        begin.set(System.currentTimeMillis());
    }
 
    public static void end(String name) {
        double time = (System.currentTimeMillis() - begin.get()) / 1000.0;
        System.out.println(name + "所用时间（秒）：" + time);
    }
}