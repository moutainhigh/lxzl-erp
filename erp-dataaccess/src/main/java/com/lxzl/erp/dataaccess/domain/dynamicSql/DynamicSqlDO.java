package com.lxzl.erp.dataaccess.domain.dynamicSql;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class DynamicSqlDO  extends BaseDO {

	private Integer id;
	private String sqlTitle;
	private String sqlContent;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getSqlTitle(){
		return sqlTitle;
	}

	public void setSqlTitle(String sqlTitle){
		this.sqlTitle = sqlTitle;
	}

	public String getSqlContent(){
		return sqlContent;
	}

	public void setSqlContent(String sqlContent){
		this.sqlContent = sqlContent;
	}

	public Integer getDataStatus(){
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus){
		this.dataStatus = dataStatus;
	}

	public String getRemark(){
		return remark;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

}
