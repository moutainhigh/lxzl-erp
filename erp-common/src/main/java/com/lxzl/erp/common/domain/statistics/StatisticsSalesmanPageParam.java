package com.lxzl.erp.common.domain.statistics;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePageParam;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Auther: huahongbin
 * @Date: 2018/4/23 13:38
 * @Description: 业务员提成统计数据分页查询
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatisticsSalesmanPageParam extends BasePageParam {

	@NotNull(message = ErrorCode.START_TIME_NOT_NULL)
	private Date startTime; // 查询开始时间
	@NotNull(message = ErrorCode.END_TIME_NOT_NULL)
	private Date endTime; // 查询结束时间
	private Integer subCompanyId; //所属分公司id
	private String salesmanName; //业务员姓名模糊查询

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getSubCompanyId() {
		return subCompanyId;
	}

	public void setSubCompanyId(Integer subCompanyId) {
		this.subCompanyId = subCompanyId;
	}

	public String getSalesmanName() {
		return salesmanName;
	}

	public void setSalesmanName(String salesmanName) {
		this.salesmanName = salesmanName;
	}
}
