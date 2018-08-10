package com.lxzl.erp.common.domain.dynamicSql.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class DynamicSql extends BasePO {

	@NotNull(message = ErrorCode.DYNAMIC_SQL_ID_NOT_NULL,groups = {IdGroup.class})
	private Integer dynamicSqlId;   //唯一标识
	@NotBlank(message = ErrorCode.SQL_TITLE_NOT_NULL,groups = {AddGroup.class})
	private String sqlTitle;   //sql语句标题
	@NotBlank(message = ErrorCode.SQL_CONTENT_NOT_NULL,groups = {AddGroup.class})
	private String sqlContent;   //sql语句内容
	private Integer sqlType;
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人

	public Integer getDynamicSqlId() {
		return dynamicSqlId;
	}

	public void setDynamicSqlId(Integer dynamicSqlId) {
		this.dynamicSqlId = dynamicSqlId;
	}

	public String getSqlTitle() {
		return sqlTitle;
	}

	public void setSqlTitle(String sqlTitle) {
		this.sqlTitle = sqlTitle;
	}

	public String getSqlContent() {
		return sqlContent;
	}

	public void setSqlContent(String sqlContent) {
		this.sqlContent = sqlContent;
	}

	public Integer getSqlType() {
		return sqlType;
	}

	public void setSqlType(Integer sqlType) {
		this.sqlType = sqlType;
	}

	public Integer getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus) {
		this.dataStatus = dataStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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