package com.lxzl.erp.common.domain.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PaymentResult implements Serializable {
    private String code;
    private String description;
    private boolean isSuccess;
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
