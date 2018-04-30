package com.lxzl.erp.common.domain.statistics;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePageParam;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Auther: huahongbin
 * @Date: 2018/4/26 18:32
 * @Description: 长短租详情查询对象
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatisticsRentInfoPageParam extends BasePageParam {
	@NotNull(message = ErrorCode.START_TIME_NOT_NULL)
	private Date startTime;
	@NotNull(message = ErrorCode.END_TIME_NOT_NULL)
	private Date endTime;
	private Integer subCompanyId;
	private String salesmanName;
	private Integer rentLengthType;

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

	public Integer getRentLengthType() {
		return rentLengthType;
	}

	public void setRentLengthType(Integer rentLengthType) {
		this.rentLengthType = rentLengthType;
	}
}
