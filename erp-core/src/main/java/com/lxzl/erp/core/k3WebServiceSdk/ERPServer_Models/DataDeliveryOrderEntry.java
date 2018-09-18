package com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 14:17 2018/9/12
 * @Modified By:
 */
public class DataDeliveryOrderEntry {
    private Integer OrderItemId;//ERP ID号
    private String Number;//设备代码
    private String Name;//设备名称
    private BigDecimal Qty;//数量
    private BigDecimal LeaseMonthCount;//租赁月数
    private BigDecimal Price;//含税单价
    private BigDecimal AddRate;//税率
    private BigDecimal Amount;//含税租赁金额
    private String Date;//租赁开始日期
    private String EndDate;//租赁结束日期
    private BigDecimal YJMonthCount;//押金月数
    private BigDecimal SFMonthCount;//首付月数
    private BigDecimal PayMonthCount;//付款月数
    private BigDecimal SFAmount;//首付租金
    private String EQConfigNumber;//设备配置代码
    private String StartDate;//起算日期
    private BigDecimal YJAmount;//押金金额
    private BigDecimal PayAmountTotal;//首付合计
    private BigDecimal EQPrice;//单台设备价值
    private BigDecimal EQAmount;//设备价值
    private String SupplyNumber;//同行供应商
    private BigDecimal EQYJAmount;//设备押金
    private BigDecimal StdPrice;//设备标准租金
    private String EQType;//新旧属性（ N 新，O 次新）
    private String PayMethodNumber;//付款方式
    private String Note;//备注
    private String OriginalFBillNo;//换货前订单号
    private Integer OriginalFEntryId;//换货前订单行号
    private BigDecimal TransferQty;//转入数量

    public Integer getOrderItemId() {
        return OrderItemId;
    }

    public void setOrderItemId(Integer orderItemId) {
        OrderItemId = orderItemId;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public BigDecimal getQty() {
        return Qty;
    }

    public void setQty(BigDecimal qty) {
        Qty = qty;
    }

    public BigDecimal getLeaseMonthCount() {
        return LeaseMonthCount;
    }

    public void setLeaseMonthCount(BigDecimal leaseMonthCount) {
        LeaseMonthCount = leaseMonthCount;
    }

    public BigDecimal getPrice() {
        return Price;
    }

    public void setPrice(BigDecimal price) {
        Price = price;
    }

    public BigDecimal getAddRate() {
        return AddRate;
    }

    public void setAddRate(BigDecimal addRate) {
        AddRate = addRate;
    }

    public BigDecimal getAmount() {
        return Amount;
    }

    public void setAmount(BigDecimal amount) {
        Amount = amount;
    }

    public BigDecimal getYJMonthCount() {
        return YJMonthCount;
    }

    public void setYJMonthCount(BigDecimal YJMonthCount) {
        this.YJMonthCount = YJMonthCount;
    }

    public BigDecimal getSFMonthCount() {
        return SFMonthCount;
    }

    public void setSFMonthCount(BigDecimal SFMonthCount) {
        this.SFMonthCount = SFMonthCount;
    }

    public BigDecimal getPayMonthCount() {
        return PayMonthCount;
    }

    public void setPayMonthCount(BigDecimal payMonthCount) {
        PayMonthCount = payMonthCount;
    }

    public BigDecimal getSFAmount() {
        return SFAmount;
    }

    public void setSFAmount(BigDecimal SFAmount) {
        this.SFAmount = SFAmount;
    }

    public String getEQConfigNumber() {
        return EQConfigNumber;
    }

    public void setEQConfigNumber(String EQConfigNumber) {
        this.EQConfigNumber = EQConfigNumber;
    }

    public BigDecimal getYJAmount() {
        return YJAmount;
    }

    public void setYJAmount(BigDecimal YJAmount) {
        this.YJAmount = YJAmount;
    }

    public BigDecimal getPayAmountTotal() {
        return PayAmountTotal;
    }

    public void setPayAmountTotal(BigDecimal payAmountTotal) {
        PayAmountTotal = payAmountTotal;
    }

    public BigDecimal getEQPrice() {
        return EQPrice;
    }

    public void setEQPrice(BigDecimal EQPrice) {
        this.EQPrice = EQPrice;
    }

    public BigDecimal getEQAmount() {
        return EQAmount;
    }

    public void setEQAmount(BigDecimal EQAmount) {
        this.EQAmount = EQAmount;
    }

    public String getSupplyNumber() {
        return SupplyNumber;
    }

    public void setSupplyNumber(String supplyNumber) {
        SupplyNumber = supplyNumber;
    }

    public BigDecimal getEQYJAmount() {
        return EQYJAmount;
    }

    public void setEQYJAmount(BigDecimal EQYJAmount) {
        this.EQYJAmount = EQYJAmount;
    }

    public BigDecimal getStdPrice() {
        return StdPrice;
    }

    public void setStdPrice(BigDecimal stdPrice) {
        StdPrice = stdPrice;
    }

    public String getEQType() {
        return EQType;
    }

    public void setEQType(String EQType) {
        this.EQType = EQType;
    }

    public String getPayMethodNumber() {
        return PayMethodNumber;
    }

    public void setPayMethodNumber(String payMethodNumber) {
        PayMethodNumber = payMethodNumber;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getOriginalFBillNo() {
        return OriginalFBillNo;
    }

    public void setOriginalFBillNo(String originalFBillNo) {
        OriginalFBillNo = originalFBillNo;
    }

    public Integer getOriginalFEntryId() {
        return OriginalFEntryId;
    }

    public void setOriginalFEntryId(Integer originalFEntryId) {
        OriginalFEntryId = originalFEntryId;
    }

    public BigDecimal getTransferQty() {
        return TransferQty;
    }

    public void setTransferQty(BigDecimal transferQty) {
        TransferQty = transferQty;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }
}
