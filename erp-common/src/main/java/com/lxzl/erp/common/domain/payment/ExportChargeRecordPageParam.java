package com.lxzl.erp.common.domain.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePageParam;
import com.lxzl.erp.common.domain.validGroup.QueryGroup;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 21:12 2018/4/9
 * @Modified By:
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExportChargeRecordPageParam extends BasePageParam {
    private String customerNo;
    private Date startTime;
    private Date endTime;

    private Integer subCompanyId;
    @NotNull(message = ErrorCode.CHARGE_ORDER_NO_IS_NULL, groups = {QueryGroup.class})
    private String chargeOrderNo;  //支付系统充值订单号
    private String channelNo;  //充值渠道编号 1:快付通 其他待拓展

    public String getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(String channelNo) {
        this.channelNo = channelNo;
    }

    public String getChargeOrderNo() {
        return chargeOrderNo;
    }

    public void setChargeOrderNo(String chargeOrderNo) {
        this.chargeOrderNo = chargeOrderNo;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

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
}
