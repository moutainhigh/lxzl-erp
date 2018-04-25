package com.lxzl.erp.common.domain.dingding.member;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 钉钉用户数据传输对象
 *
 * @author daiqi
 * @create 2018-04-20 13:40
 */
public class DingdingUserDTO {
    private String unionId;
    private String openId;
    private String email;
    private String mobile;
    private String name;
    private String userId;

    public String getUnionId() {
        return unionId;
    }

    @JSONField(name = "unionid")
    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    @JSONField(name = "userid")
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
