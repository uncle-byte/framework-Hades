package com.framework.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.util.List;

/**
 * @author chenhaipeng
 * @version 1.0
 * @date 2016/03/17 15:42
 * @see "https://github.com/alibaba/fastjson/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98"
 */
public abstract class FastJsonUtils {

    /**
     * 将JSON 格式的字符串转换为对象
     *
     * @param content
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> T string2Object(String content, Class<T> clz) {
        try {
            return JSON.parseObject(content, clz);
        } catch (Exception e) {
            FormattingTuple message = MessageFormatter.format("将字符串[{}]转换为对象[{}]时出现异常",
                    new Object[]{content, clz.getSimpleName(), e});
            throw new RuntimeException(message.getMessage(), e);
        }

    }
    /**
     * 将JSON 格式的字符串转换为JsonObject
     * 可以使用 for(java.util.Map.Entry<String,Object> entry:jsonObject.entrySet()) 来遍历
     * ,KEY顺序无序
     * @param content
     * @return
     */
    public static JSONObject string2JSONObject(String content) {
        try {
            return JSONObject.parseObject(content);
        } catch (Exception e) {
            FormattingTuple message = MessageFormatter.format("将字符串[{}]转换为对象[{}]时出现异常",
                    new Object[]{content, "JSONObject", e});
            throw new RuntimeException(message.getMessage(), e);
        }

    }

    /**
     * 将JSON 格式的字符串转换为JsonObject
     * 可以使用 for(int k=0;k<jsonArray.size();k++)  来遍历
     * KEY顺序 有序
     * @param content
     * @return
     */
    public static JSONArray string2JSONArray(String content) {
        try {
            return  JSONArray.parseArray(content);
        } catch (Exception e) {
            FormattingTuple message = MessageFormatter.format("将字符串[{}]转换为对象[{}]时出现异常",
                    new Object[]{content, "JSONArray", e});
            throw new RuntimeException(message.getMessage(), e);
        }

    }

    /**
     * 将对象转换为 JSON 的字符串格式
     *
     * @param object
     * @return
     */
    public static String object2String(Object object) {
        try {
            return JSON.toJSONString(object,SerializerFeature.DisableCircularReferenceDetect);

        } catch (Exception e) {
            FormattingTuple message = MessageFormatter.format("将对象[{}]转换为JSON字符串时发生异常", object, e);
            throw new RuntimeException(message.getMessage(), e);
        }
    }
    /**
     * 将对象转换为 JSON 的字符串格式
     *
     * @param object  SerializerFeature.WriteDateUseDateFormat
     * @param dateFormate 日期格式
     * @return
     */
    public static String object2StringWithDateFormat(Object object,String dateFormate) {
        try {
            return JSON.toJSONStringWithDateFormat(object,dateFormate);

        } catch (Exception e) {
            FormattingTuple message = MessageFormatter.format("将对象[{}]转换为JSON字符串时发生异常", object, e);
            throw new RuntimeException(message.getMessage(), e);
        }
    }

    /**
     * 将对象转换为 JSON 的字符串格式
     *@param prettyFormat 是否美化输出
     * @param object
     * @return
     */
    public static String object2String(Object object,boolean prettyFormat) {
        try {
            return JSON.toJSONString(object,prettyFormat);

        } catch (Exception e) {
            FormattingTuple message = MessageFormatter.format("将对象[{}]转换为JSON字符串时发生异常", object, e);
            throw new RuntimeException(message.getMessage(), e);
        }
    }

    /**
     * 将JSON 格式的字符转换为集合
     * @param content 字符串
     * @param clazz 元素类型
     * @return
     */
    public static <T> List<T> string2List(String content, Class<T> clazz){
        try {
            return JSON.parseArray(content, clazz);
        } catch (Exception e) {
            FormattingTuple message = MessageFormatter.format("将字符串[{}]转换为集合[{}]时出现异常",
                    new Object[] { content, List.class, e });
            throw new RuntimeException(message.getMessage(), e);
        }
    }
    /**
     * 将JSON 格式的字符转换为集合
     * @param content 字符串
     * @param tTypeReference 元素类型  new TypeReference<Map<String, User>>() {};
     * @return
     */
    public static <T> T string2Map(String content, TypeReference<T> tTypeReference){
        try {
            return JSON.parseObject(content, tTypeReference);
        } catch (Exception e) {
            FormattingTuple message = MessageFormatter.format("将字符串[{}]转换为集合[{}]时出现异常",
                    new Object[] { content, List.class, e });
            throw new RuntimeException(message.getMessage(), e);
        }
    }





















}
