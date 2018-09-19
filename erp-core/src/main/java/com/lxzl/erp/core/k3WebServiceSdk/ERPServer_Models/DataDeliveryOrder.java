package com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models;


import java.util.Date;
import java.util.List;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 14:14 2018/9/12
 * @Modified By:
 */
public class DataDeliveryOrder {
    private boolean IsReplace;//是否覆盖
    private String BillNO;//单据编号
    private String Date;//日期
    private String CustNumber;//客户代码
    private String CustName;//客户名称
    private String FetchStyleNumber;//交货方式（FJH01 客户自提，FJH02 送货上门，FJH03 物流发货）
    private String DeptNumber;//部门代码
    private String DeptName;//部门名称
    private String EmpNumber;//业务代码
    private String EmpName;//业务员名称
    private String BillerName;//制单人
    private String ManagerNumber;//主管代码
    private String ManagerName;//主管名称
    private String CheckDate;//审核日期
    private String Explanation;//摘要
    private String OrderTypeNumber;//订单类型（L 长租，R 短短租(天)，X 销售，D 短租）
    private String BusinessTypeNumber;//经营类型（ZY 经营性租赁，RZ 融资性租赁）
    private String OrderFromNumber;//订单来源（XS 线上，XX 线下）
    private String DeliveryName;//提货人
    private String DeliveryAddress;//交货地址
    private String DeliverPhone;//收货人电话
    private String PayMethodNumber;//付款方式（01 先付后用，02 先付后用(货到付款)，03 先用后付）
    private String CompanyNumber;//分公司（00 总部，01 深圳，02 北京，03 上海，04 广州，05 武汉，06 南京，07 成都，08 厦门，10 电销，20 生产中心）
    private String ExecuteCompanyNumber;//执行分公司（00 总部，01 深圳，02 北京，03 上海，04 广州，05 武汉，06 南京，07 成都，08 厦门，10 电销，20 生产中心）
    private String CheckerName;//审核人
    private String AreaPS;//订单类型（销售/租赁）
    private String AcctDeptNumber;//对账部门编号
    private String AcctDeptName;//对账部门名称
    private String InvoiceType;//票据类型 01:专票/02:普票/03:收据
    private String WillSendDate;//交货日期
    private Integer StatementDate;//结算日
    private List<DataDeliveryOrderEntry> Entrys;//换货订单明细

    public boolean getIsReplace() {
        return IsReplace;
    }

    public void setIsReplace(boolean isReplace) {
        IsReplace = isReplace;
    }

    public String getBillNO() {
        return BillNO;
    }

    public void setBillNO(String billNO) {
        BillNO = billNO;
    }

    public boolean isReplace() {
        return IsReplace;
    }

    public void setReplace(boolean replace) {
        IsReplace = replace;
    }

    public String getCustNumber() {
        return CustNumber;
    }

    public void setCustNumber(String custNumber) {
        CustNumber = custNumber;
    }

    public String getCustName() {
        return CustName;
    }

    public void setCustName(String custName) {
        CustName = custName;
    }

    public String getFetchStyleNumber() {
        return FetchStyleNumber;
    }

    public void setFetchStyleNumber(String fetchStyleNumber) {
        FetchStyleNumber = fetchStyleNumber;
    }

    public String getDeptNumber() {
        return DeptNumber;
    }

    public void setDeptNumber(String deptNumber) {
        DeptNumber = deptNumber;
    }

    public String getDeptName() {
        return DeptName;
    }

    public void setDeptName(String deptName) {
        DeptName = deptName;
    }

    public String getEmpNumber() {
        return EmpNumber;
    }

    public void setEmpNumber(String empNumber) {
        EmpNumber = empNumber;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    public String getBillerName() {
        return BillerName;
    }

    public void setBillerName(String billerName) {
        BillerName = billerName;
    }

    public String getManagerNumber() {
        return ManagerNumber;
    }

    public void setManagerNumber(String managerNumber) {
        ManagerNumber = managerNumber;
    }

    public String getManagerName() {
        return ManagerName;
    }

    public void setManagerName(String managerName) {
        ManagerName = managerName;
    }

    public String getExplanation() {
        return Explanation;
    }

    public void setExplanation(String explanation) {
        Explanation = explanation;
    }

    public String getOrderTypeNumber() {
        return OrderTypeNumber;
    }

    public void setOrderTypeNumber(String orderTypeNumber) {
        OrderTypeNumber = orderTypeNumber;
    }

    public String getBusinessTypeNumber() {
        return BusinessTypeNumber;
    }

    public void setBusinessTypeNumber(String businessTypeNumber) {
        BusinessTypeNumber = businessTypeNumber;
    }

    public String getOrderFromNumber() {
        return OrderFromNumber;
    }

    public void setOrderFromNumber(String orderFromNumber) {
        OrderFromNumber = orderFromNumber;
    }

    public String getDeliveryName() {
        return DeliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        DeliveryName = deliveryName;
    }

    public String getDeliveryAddress() {
        return DeliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        DeliveryAddress = deliveryAddress;
    }

    public String getDeliverPhone() {
        return DeliverPhone;
    }

    public void setDeliverPhone(String deliverPhone) {
        DeliverPhone = deliverPhone;
    }

    public String getPayMethodNumber() {
        return PayMethodNumber;
    }

    public void setPayMethodNumber(String payMethodNumber) {
        PayMethodNumber = payMethodNumber;
    }

    public String getCompanyNumber() {
        return CompanyNumber;
    }

    public void setCompanyNumber(String companyNumber) {
        CompanyNumber = companyNumber;
    }

    public String getExecuteCompanyNumber() {
        return ExecuteCompanyNumber;
    }

    public void setExecuteCompanyNumber(String executeCompanyNumber) {
        ExecuteCompanyNumber = executeCompanyNumber;
    }

    public String getCheckerName() {
        return CheckerName;
    }

    public void setCheckerName(String checkerName) {
        CheckerName = checkerName;
    }

    public String getAreaPS() {
        return AreaPS;
    }

    public void setAreaPS(String areaPS) {
        AreaPS = areaPS;
    }

    public String getAcctDeptNumber() {
        return AcctDeptNumber;
    }

    public void setAcctDeptNumber(String acctDeptNumber) {
        AcctDeptNumber = acctDeptNumber;
    }

    public String getAcctDeptName() {
        return AcctDeptName;
    }

    public void setAcctDeptName(String acctDeptName) {
        AcctDeptName = acctDeptName;
    }

    public String getInvoiceType() {
        return InvoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        InvoiceType = invoiceType;
    }

    public Integer getStatementDate() {
        return StatementDate;
    }

    public void setStatementDate(Integer statementDate) {
        StatementDate = statementDate;
    }

    public List<DataDeliveryOrderEntry> getEntrys() {
        return Entrys;
    }

    public void setEntrys(List<DataDeliveryOrderEntry> entrys) {
        Entrys = entrys;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getCheckDate() {
        return CheckDate;
    }

    public void setCheckDate(String checkDate) {
        CheckDate = checkDate;
    }

    public String getWillSendDate() {
        return WillSendDate;
    }

    public void setWillSendDate(String willSendDate) {
        WillSendDate = willSendDate;
    }
}
