package com.lxzl.erp.common.domain.bank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.util.Date;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 20:03 2018/3/20
 * @Modified By:
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankSlipQueryParam extends BasePageParam {

    private String subCompanyName;   //分公司名称
    private Integer subCompanyId;   //分公司id
    private Integer bankType;   //银行类型，1-支付宝，2-中国银行，3-交通银行，4-南京银行，5-农业银行，6-工商银行，7-建设银行，8-平安银行，9-招商银行，10-浦发银行
    private Date slipDayStart;   //导入开始时间日期
    private Date slipDayEnd;   //导入结束时间日期
    private Integer slipStatus;   //单据状态：0-初始化，1-已下推，2-部分认领，3-全部认领
    private Integer bankSlipId;   //银行对公流水id

    public Integer getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(Integer subCompanyId) {
        this.subCompanyId = subCompanyId;
    }

    public Integer getBankSlipId() {
        return bankSlipId;
    }

    public void setBankSlipId(Integer bankSlipId) {
        this.bankSlipId = bankSlipId;
    }

    public String getSubCompanyName() {
        return subCompanyName;
    }

    public void setSubCompanyName(String subCompanyName) {
        this.subCompanyName = subCompanyName;
    }

    public Integer getBankType() {
        return bankType;
    }

    public void setBankType(Integer bankType) {
        this.bankType = bankType;
    }

    public Date getSlipDayStart() {
        return slipDayStart;
    }

    public void setSlipDayStart(Date slipDayStart) {
        this.slipDayStart = slipDayStart;
    }

    public Date getSlipDayEnd() {
        return slipDayEnd;
    }

    public void setSlipDayEnd(Date slipDayEnd) {
        this.slipDayEnd = slipDayEnd;
    }

    public Integer getSlipStatus() {
        return slipStatus;
    }

    public void setSlipStatus(Integer slipStatus) {
        this.slipStatus = slipStatus;
    }
}
