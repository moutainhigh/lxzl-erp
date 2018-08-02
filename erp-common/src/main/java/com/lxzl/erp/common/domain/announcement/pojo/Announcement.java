package com.lxzl.erp.common.domain.announcement.pojo;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

public class Announcement extends BasePO {
    @NotNull(message = ErrorCode.ID_NOT_NULL, groups = {IdGroup.class})
    private Integer id;
    @NotBlank(message = ErrorCode.ANNOUNCEMENT_TITLE_NOT_NULL, groups = {AddGroup.class, UpdateGroup.class})
    @Size(max = 100,message = ErrorCode.ANNOUNCEMENT_TITLE_TOO_LARGE, groups = {AddGroup.class, UpdateGroup.class})
    private String title;
    @NotBlank(message = ErrorCode.ANNOUNCEMENT_CONTENT_NOT_NULL, groups = {AddGroup.class, UpdateGroup.class})
    @Size(max = 500,message = ErrorCode.ANNOUNCEMENT_CONTENT_TOO_LARGE, groups = {AddGroup.class, UpdateGroup.class})
    private String content;
    @Size(max = 500,message = ErrorCode.ANNOUNCEMENT_REMARK_TOO_LARGE, groups = {AddGroup.class, UpdateGroup.class})
    private String remark;
    private Integer dataStatus;
    private Date createTime;   //添加时间
    private String createUser;   //添加人
    private Date updateTime;   //添加时间
    private String updateUser;   //修改人

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}
