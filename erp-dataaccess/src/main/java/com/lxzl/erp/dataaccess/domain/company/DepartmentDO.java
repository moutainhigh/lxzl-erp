package com.lxzl.erp.dataaccess.domain.company;

import com.lxzl.erp.dataaccess.domain.user.RoleDO;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.util.List;


public class DepartmentDO  extends BaseDO {

	private Integer id;
	private String departmentName;
	private Integer departmentType;
	private Integer parentDepartmentId;
	private Integer subCompanyId;
	private Integer dataOrder;
	private Integer dataStatus;
	private String remark;
	private List<DepartmentDO> children;

	private List<RoleDO> roleDOList;
	private List<UserDO> userDOList;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getDepartmentName(){
		return departmentName;
	}

	public void setDepartmentName(String departmentName){
		this.departmentName = departmentName;
	}

	public Integer getDepartmentType(){
		return departmentType;
	}

	public void setDepartmentType(Integer departmentType){
		this.departmentType = departmentType;
	}

	public Integer getParentDepartmentId(){
		return parentDepartmentId;
	}

	public void setParentDepartmentId(Integer parentDepartmentId){
		this.parentDepartmentId = parentDepartmentId;
	}

	public Integer getSubCompanyId(){
		return subCompanyId;
	}

	public void setSubCompanyId(Integer subCompanyId){
		this.subCompanyId = subCompanyId;
	}

	public Integer getDataOrder(){
		return dataOrder;
	}

	public void setDataOrder(Integer dataOrder){
		this.dataOrder = dataOrder;
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

	public List<DepartmentDO> getChildren() {
		return children;
	}

	public void setChildren(List<DepartmentDO> children) {
		this.children = children;
	}

	public List<RoleDO> getRoleDOList() {
		return roleDOList;
	}

	public void setRoleDOList(List<RoleDO> roleDOList) {
		this.roleDOList = roleDOList;
	}

	public List<UserDO> getUserDOList() {
		return userDOList;
	}

	public void setUserDOList(List<UserDO> userDOList) {
		this.userDOList = userDOList;
	}
}