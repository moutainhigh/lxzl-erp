package com.lxzl.erp.common.domain.replace;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.ReturnOrChangeMode;
import com.lxzl.erp.common.domain.base.BasePageParam;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.common.util.validate.constraints.In;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\9\11 0011 10:46
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReplaceOrderQueryParam extends BasePageParam {
    private Integer replaceOrderId;   //唯一标识
    private String replaceOrderNo;   //换货编号
    private String orderNo;   //原订单编号
    private String customerNo;   //客户编号
    private String customerName; //客户名称
    private Integer replaceReasonType;   //换货原因类型,0-升级 ，1-损坏，2-其他
    private Integer replaceMode;   //换货方式，1-上门取件，2邮寄
    private Integer replaceOrderStatus;   //换货订单状态，0-待提交，4-审核中，8-待发货，12-处理中，16-已发货，20-已完成，24-取消
    private Integer dataStatus;   //状态：0不可用；1可用；2删除
    private String createUserName;   //添加人
    private String updateUserName;   //修改人
    private String confirmReplaceUserName;   //确认换货人
    private String consigneeName;   //联系人
    private Date replaceStartTime;   //换货开始时间
    private Date replaceEndTime;   //换货结束时间
    private Date createStartTime;   //添加开始时间
    private Date createEndTime;   //添加结束时间
    private Date updateStartTime;   //修改开始时间
    private Date updateEndTime;   //修改结束时间
    private Date confirmReplaceStartTime;   //确认换货时间
    private Date confirmReplaceEndTime;   //确认换货时间

    public Integer getReplaceOrderId() {
        return replaceOrderId;
    }

    public void setReplaceOrderId(Integer replaceOrderId) {
        this.replaceOrderId = replaceOrderId;
    }

    public String getReplaceOrderNo() {
        return replaceOrderNo;
    }

    public void setReplaceOrderNo(String replaceOrderNo) {
        this.replaceOrderNo = replaceOrderNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }


    public Integer getReplaceReasonType() {
        return replaceReasonType;
    }

    public void setReplaceReasonType(Integer replaceReasonType) {
        this.replaceReasonType = replaceReasonType;
    }

    public Integer getReplaceMode() {
        return replaceMode;
    }

    public void setReplaceMode(Integer replaceMode) {
        this.replaceMode = replaceMode;
    }

    public Integer getReplaceOrderStatus() {
        return replaceOrderStatus;
    }

    public void setReplaceOrderStatus(Integer replaceOrderStatus) {
        this.replaceOrderStatus = replaceOrderStatus;
    }

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public String getConfirmReplaceUserName() {
        return confirmReplaceUserName;
    }

    public void setConfirmReplaceUserName(String confirmReplaceUserName) {
        this.confirmReplaceUserName = confirmReplaceUserName;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public Date getReplaceStartTime() {
        return replaceStartTime;
    }

    public void setReplaceStartTime(Date replaceStartTime) {
        this.replaceStartTime = replaceStartTime;
    }

    public Date getReplaceEndTime() {
        return replaceEndTime;
    }

    public void setReplaceEndTime(Date replaceEndTime) {
        this.replaceEndTime = replaceEndTime;
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

    public Date getUpdateStartTime() {
        return updateStartTime;
    }

    public void setUpdateStartTime(Date updateStartTime) {
        this.updateStartTime = updateStartTime;
    }

    public Date getUpdateEndTime() {
        return updateEndTime;
    }

    public void setUpdateEndTime(Date updateEndTime) {
        this.updateEndTime = updateEndTime;
    }

    public Date getConfirmReplaceStartTime() {
        return confirmReplaceStartTime;
    }

    public void setConfirmReplaceStartTime(Date confirmReplaceStartTime) {
        this.confirmReplaceStartTime = confirmReplaceStartTime;
    }

    public Date getConfirmReplaceEndTime() {
        return confirmReplaceEndTime;
    }

    public void setConfirmReplaceEndTime(Date confirmReplaceEndTime) {
        this.confirmReplaceEndTime = confirmReplaceEndTime;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
