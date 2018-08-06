package com.lxzl.erp.common.domain.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/7/26
 * @Time : Created in 15:31
 */
public class TaskExecutorResult  implements Serializable {
    private String code;
    private String description;
    private boolean success;
    private Map<String, Object> resultMap = new HashMap();

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
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Map<String, Object> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<String, Object> resultMap) {
        this.resultMap = resultMap;
    }

}
