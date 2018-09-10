package com.lxzl.erp.common.domain.statistics;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePageParam;
import com.lxzl.erp.common.util.validate.constraints.In;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\8\29 0029 16:13
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatisticsOperateDataPageParam extends BasePageParam {
    @NotNull(message = ErrorCode.START_TIME_NOT_NULL)
    private Date startTime; //查询起始时间
    private Integer subCompanyId; //分公司ID
    private String salesmanName; //业务员姓名模糊查询
    private Integer statisticalDimension;   //统计维度：1-分公司维度  2-业务员维度
    private Integer statisticalStatus;   //统计时间类型：1-按天统计  2-按周统计  3-按月统计
    private Date createTime;//数据创建时间

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
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

    public Integer getStatisticalDimension() {
        return statisticalDimension;
    }

    public void setStatisticalDimension(Integer statisticalDimension) {
        this.statisticalDimension = statisticalDimension;
    }

    public Integer getStatisticalStatus() {
        return statisticalStatus;
    }

    public void setStatisticalStatus(Integer statisticalStatus) {
        this.statisticalStatus = statisticalStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
