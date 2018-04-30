package com.lxzl.erp.common.domain.dingding.member;

import com.lxzl.erp.common.domain.dingding.DingdingBaseDTO;
import com.lxzl.erp.common.domain.user.pojo.User;

/**
 * 钉钉用户数据传输对象
 *
 * @author daiqi
 * @create 2018-04-20 13:40
 */
public class DingdingUserDTO extends DingdingBaseDTO{
    private String mobile;
    private String name;
    private Integer userId;

    public DingdingUserDTO() {

    }

    public DingdingUserDTO(User user) {
        buildDingdingUserDataByUser(user);
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public DingdingUserDTO buildDingdingUserDataByUser(User user) {
        if (user == null) {
            return null;
        }
        this.setMobile(user.getPhone());
        this.setUserId(user.getUserId());
        this.setName(user.getRealName());
        return this;
    }
}
