package com.lxzl.erp.common.domain.dingding.approve;

/**
 * 钉钉FormComponentValue数据传输对象
 * @author daiqi
 * @create 2018-04-23 17:01
 */
public class DingdingFormComponentValueDTO {
    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
