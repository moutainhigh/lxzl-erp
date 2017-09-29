package com.lxzl.erp;

import java.util.HashMap;
import java.util.Map;

/**
 * User : LiuKe
 * Date : 2017/1/11
 * Time : 14:21
 */
public class TestResult {
    /**
     * 结果码
     */
    private String code;

    /**
     * 描述
     */
    private String description;

    /**
     * 成功标志
     */
    private boolean isSuccess;

    /**
     * 自定义属性
     */
    private Map<String, Object> resultMap = new HashMap<String, Object>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public Map<String, Object> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<String, Object> resultMap) {
        this.resultMap = resultMap;
    }
}
