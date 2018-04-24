package com.lxzl.erp.common.domain.dingding;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.lang.StringUtils;

/**
 * 钉钉结果数据传输对象
 *
 * @author daiqi
 * @create 2018-04-20 15:52
 */
public class DingdingResultDTO {
    private String code;
    private Object resultMap;
    private Boolean success;
    private String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getResultMap() {
        return resultMap;
    }

    public void setResultMap(Object resultMap) {
        this.resultMap = resultMap;
    }

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JSONField(serialize = false)
    public String getResultMapStr() {
        if (resultMap == null) {
            return null;
        }
        if (resultMap instanceof String) {
            return resultMap.toString();
        }
        return JSONObject.toJSONString(resultMap);
    }

    @JSONField(serialize = false)
    public <T> T getTObj(Class<T> clazz) {
        String jsonStr = getResultMapStr();
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }
        return JSONObject.parseObject(jsonStr, clazz);
    }
}
