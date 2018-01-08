package com.lxzl.erp.common.domain.assembleOder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.util.Date;

/**
 * User : XiaoLuYu
 * Date : Created in ${Date}
 * Time : Created in ${Time}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssembleOrderQueryParam extends BasePageParam {

    private Integer assembleOrderId;  //组装单id
    private Date createStartTime;   //创建起始时间
    private Date createEndTime;     //创建结束时间
    private Integer assembleOrderNo;  //组装单编号

    public Integer getAssembleOrderNo() {
        return assembleOrderNo;
    }

    public void setAssembleOrderNo(Integer assembleOrderNo) {
        this.assembleOrderNo = assembleOrderNo;
    }

    public Integer getAssembleOrderId() {
        return assembleOrderId;
    }

    public void setAssembleOrderId(Integer assembleOrderId) {
        this.assembleOrderId = assembleOrderId;
    }

    public Date getCreateStartTime() {
        return createStartTime;
    }

    public void setCreateStartTime(Date createStartTime) {
        this.createStartTime = createStartTime;
    }

    public Date getCreateEndTime() {
        return createEndTime;
    }

    public void setCreateEndTime(Date createEndTime) {
        this.createEndTime = createEndTime;
    }
}
