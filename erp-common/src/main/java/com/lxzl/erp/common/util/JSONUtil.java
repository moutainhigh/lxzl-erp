package com.lxzl.erp.common.util;

import com.lxzl.se.common.util.StringUtil;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: gaochao
 * Date: 2016/5/4.
 * Time: 8:43.
 */
public class JSONUtil {

    /**
     * JSON字符串转对象类型
     */
    public static <T> T convertJSONToBean(String str, Class<T> type) {
        if (StringUtil.isBlank(str)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        T result;
        try {
            result = mapper.readValue(str, type);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return result;
    }

    /**
     * 通用转JSON
     */
    public static <T> String convertTToJSON(T t) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(t);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * List转成json
     */
    public static <T> String convertListToJSON(List<T> list) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(list);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Bean转成json
     */
    public static <T> String convertBeanToJSON(T bean) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(bean);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 将map转成json
     */
    public static String convertMapToJSON(Map map) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(map);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
