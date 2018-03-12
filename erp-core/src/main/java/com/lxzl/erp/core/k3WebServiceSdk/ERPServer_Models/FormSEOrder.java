/**
 * FormSEOrder.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models;

public class FormSEOrder  implements java.io.Serializable {
    private String acctDeptName;

    private String acctDeptNumber;

    private String areaPS;

    private String billNO;

    private String billerName;

    private String businessTypeNumber;

    private java.util.Calendar checkDate;

    private String checkerName;

    private String companyNumber;

    private String custName;

    private String custNumber;

    private java.util.Calendar date;

    private String deliverPhone;

    private String deliveryAddress;

    private String deliveryName;

    private String deptName;

    private String deptNumber;

    private String empName;

    private String empNumber;

    private FormSEOrderEntry[] entrys;

    private String explanation;

    private String fetchStyleNumber;

    private String invoiceType;

    private Boolean isReplace;

    private String managerName;

    private String managerNumber;

    private String orderFromNumber;

    private String orderTypeNumber;

    private String payMethodNumber;

    private java.util.Calendar willSendDate;

    public FormSEOrder() {
    }

    public FormSEOrder(
           String acctDeptName,
           String acctDeptNumber,
           String areaPS,
           String billNO,
           String billerName,
           String businessTypeNumber,
           java.util.Calendar checkDate,
           String checkerName,
           String companyNumber,
           String custName,
           String custNumber,
           java.util.Calendar date,
           String deliverPhone,
           String deliveryAddress,
           String deliveryName,
           String deptName,
           String deptNumber,
           String empName,
           String empNumber,
           FormSEOrderEntry[] entrys,
           String explanation,
           String fetchStyleNumber,
           String invoiceType,
           Boolean isReplace,
           String managerName,
           String managerNumber,
           String orderFromNumber,
           String orderTypeNumber,
           String payMethodNumber,
           java.util.Calendar willSendDate) {
           this.acctDeptName = acctDeptName;
           this.acctDeptNumber = acctDeptNumber;
           this.areaPS = areaPS;
           this.billNO = billNO;
           this.billerName = billerName;
           this.businessTypeNumber = businessTypeNumber;
           this.checkDate = checkDate;
           this.checkerName = checkerName;
           this.companyNumber = companyNumber;
           this.custName = custName;
           this.custNumber = custNumber;
           this.date = date;
           this.deliverPhone = deliverPhone;
           this.deliveryAddress = deliveryAddress;
           this.deliveryName = deliveryName;
           this.deptName = deptName;
           this.deptNumber = deptNumber;
           this.empName = empName;
           this.empNumber = empNumber;
           this.entrys = entrys;
           this.explanation = explanation;
           this.fetchStyleNumber = fetchStyleNumber;
           this.invoiceType = invoiceType;
           this.isReplace = isReplace;
           this.managerName = managerName;
           this.managerNumber = managerNumber;
           this.orderFromNumber = orderFromNumber;
           this.orderTypeNumber = orderTypeNumber;
           this.payMethodNumber = payMethodNumber;
           this.willSendDate = willSendDate;
    }


    /**
     * Gets the acctDeptName value for this FormSEOrder.
     * 
     * @return acctDeptName
     */
    public String getAcctDeptName() {
        return acctDeptName;
    }


    /**
     * Sets the acctDeptName value for this FormSEOrder.
     * 
     * @param acctDeptName
     */
    public void setAcctDeptName(String acctDeptName) {
        this.acctDeptName = acctDeptName;
    }


    /**
     * Gets the acctDeptNumber value for this FormSEOrder.
     * 
     * @return acctDeptNumber
     */
    public String getAcctDeptNumber() {
        return acctDeptNumber;
    }


    /**
     * Sets the acctDeptNumber value for this FormSEOrder.
     * 
     * @param acctDeptNumber
     */
    public void setAcctDeptNumber(String acctDeptNumber) {
        this.acctDeptNumber = acctDeptNumber;
    }


    /**
     * Gets the areaPS value for this FormSEOrder.
     * 
     * @return areaPS
     */
    public String getAreaPS() {
        return areaPS;
    }


    /**
     * Sets the areaPS value for this FormSEOrder.
     * 
     * @param areaPS
     */
    public void setAreaPS(String areaPS) {
        this.areaPS = areaPS;
    }


    /**
     * Gets the billNO value for this FormSEOrder.
     * 
     * @return billNO
     */
    public String getBillNO() {
        return billNO;
    }


    /**
     * Sets the billNO value for this FormSEOrder.
     * 
     * @param billNO
     */
    public void setBillNO(String billNO) {
        this.billNO = billNO;
    }


    /**
     * Gets the billerName value for this FormSEOrder.
     * 
     * @return billerName
     */
    public String getBillerName() {
        return billerName;
    }


    /**
     * Sets the billerName value for this FormSEOrder.
     * 
     * @param billerName
     */
    public void setBillerName(String billerName) {
        this.billerName = billerName;
    }


    /**
     * Gets the businessTypeNumber value for this FormSEOrder.
     * 
     * @return businessTypeNumber
     */
    public String getBusinessTypeNumber() {
        return businessTypeNumber;
    }


    /**
     * Sets the businessTypeNumber value for this FormSEOrder.
     * 
     * @param businessTypeNumber
     */
    public void setBusinessTypeNumber(String businessTypeNumber) {
        this.businessTypeNumber = businessTypeNumber;
    }


    /**
     * Gets the checkDate value for this FormSEOrder.
     * 
     * @return checkDate
     */
    public java.util.Calendar getCheckDate() {
        return checkDate;
    }


    /**
     * Sets the checkDate value for this FormSEOrder.
     * 
     * @param checkDate
     */
    public void setCheckDate(java.util.Calendar checkDate) {
        this.checkDate = checkDate;
    }


    /**
     * Gets the checkerName value for this FormSEOrder.
     * 
     * @return checkerName
     */
    public String getCheckerName() {
        return checkerName;
    }


    /**
     * Sets the checkerName value for this FormSEOrder.
     * 
     * @param checkerName
     */
    public void setCheckerName(String checkerName) {
        this.checkerName = checkerName;
    }


    /**
     * Gets the companyNumber value for this FormSEOrder.
     * 
     * @return companyNumber
     */
    public String getCompanyNumber() {
        return companyNumber;
    }


    /**
     * Sets the companyNumber value for this FormSEOrder.
     * 
     * @param companyNumber
     */
    public void setCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
    }


    /**
     * Gets the custName value for this FormSEOrder.
     * 
     * @return custName
     */
    public String getCustName() {
        return custName;
    }


    /**
     * Sets the custName value for this FormSEOrder.
     * 
     * @param custName
     */
    public void setCustName(String custName) {
        this.custName = custName;
    }


    /**
     * Gets the custNumber value for this FormSEOrder.
     * 
     * @return custNumber
     */
    public String getCustNumber() {
        return custNumber;
    }


    /**
     * Sets the custNumber value for this FormSEOrder.
     * 
     * @param custNumber
     */
    public void setCustNumber(String custNumber) {
        this.custNumber = custNumber;
    }


    /**
     * Gets the date value for this FormSEOrder.
     * 
     * @return date
     */
    public java.util.Calendar getDate() {
        return date;
    }


    /**
     * Sets the date value for this FormSEOrder.
     * 
     * @param date
     */
    public void setDate(java.util.Calendar date) {
        this.date = date;
    }


    /**
     * Gets the deliverPhone value for this FormSEOrder.
     * 
     * @return deliverPhone
     */
    public String getDeliverPhone() {
        return deliverPhone;
    }


    /**
     * Sets the deliverPhone value for this FormSEOrder.
     * 
     * @param deliverPhone
     */
    public void setDeliverPhone(String deliverPhone) {
        this.deliverPhone = deliverPhone;
    }


    /**
     * Gets the deliveryAddress value for this FormSEOrder.
     * 
     * @return deliveryAddress
     */
    public String getDeliveryAddress() {
        return deliveryAddress;
    }


    /**
     * Sets the deliveryAddress value for this FormSEOrder.
     * 
     * @param deliveryAddress
     */
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }


    /**
     * Gets the deliveryName value for this FormSEOrder.
     * 
     * @return deliveryName
     */
    public String getDeliveryName() {
        return deliveryName;
    }


    /**
     * Sets the deliveryName value for this FormSEOrder.
     * 
     * @param deliveryName
     */
    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }


    /**
     * Gets the deptName value for this FormSEOrder.
     * 
     * @return deptName
     */
    public String getDeptName() {
        return deptName;
    }


    /**
     * Sets the deptName value for this FormSEOrder.
     * 
     * @param deptName
     */
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }


    /**
     * Gets the deptNumber value for this FormSEOrder.
     * 
     * @return deptNumber
     */
    public String getDeptNumber() {
        return deptNumber;
    }


    /**
     * Sets the deptNumber value for this FormSEOrder.
     * 
     * @param deptNumber
     */
    public void setDeptNumber(String deptNumber) {
        this.deptNumber = deptNumber;
    }


    /**
     * Gets the empName value for this FormSEOrder.
     * 
     * @return empName
     */
    public String getEmpName() {
        return empName;
    }


    /**
     * Sets the empName value for this FormSEOrder.
     * 
     * @param empName
     */
    public void setEmpName(String empName) {
        this.empName = empName;
    }


    /**
     * Gets the empNumber value for this FormSEOrder.
     * 
     * @return empNumber
     */
    public String getEmpNumber() {
        return empNumber;
    }


    /**
     * Sets the empNumber value for this FormSEOrder.
     * 
     * @param empNumber
     */
    public void setEmpNumber(String empNumber) {
        this.empNumber = empNumber;
    }


    /**
     * Gets the entrys value for this FormSEOrder.
     * 
     * @return entrys
     */
    public FormSEOrderEntry[] getEntrys() {
        return entrys;
    }


    /**
     * Sets the entrys value for this FormSEOrder.
     * 
     * @param entrys
     */
    public void setEntrys(FormSEOrderEntry[] entrys) {
        this.entrys = entrys;
    }


    /**
     * Gets the explanation value for this FormSEOrder.
     * 
     * @return explanation
     */
    public String getExplanation() {
        return explanation;
    }


    /**
     * Sets the explanation value for this FormSEOrder.
     * 
     * @param explanation
     */
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }


    /**
     * Gets the fetchStyleNumber value for this FormSEOrder.
     * 
     * @return fetchStyleNumber
     */
    public String getFetchStyleNumber() {
        return fetchStyleNumber;
    }


    /**
     * Sets the fetchStyleNumber value for this FormSEOrder.
     * 
     * @param fetchStyleNumber
     */
    public void setFetchStyleNumber(String fetchStyleNumber) {
        this.fetchStyleNumber = fetchStyleNumber;
    }


    /**
     * Gets the invoiceType value for this FormSEOrder.
     * 
     * @return invoiceType
     */
    public String getInvoiceType() {
        return invoiceType;
    }


    /**
     * Sets the invoiceType value for this FormSEOrder.
     * 
     * @param invoiceType
     */
    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }


    /**
     * Gets the isReplace value for this FormSEOrder.
     * 
     * @return isReplace
     */
    public Boolean getIsReplace() {
        return isReplace;
    }


    /**
     * Sets the isReplace value for this FormSEOrder.
     * 
     * @param isReplace
     */
    public void setIsReplace(Boolean isReplace) {
        this.isReplace = isReplace;
    }


    /**
     * Gets the managerName value for this FormSEOrder.
     * 
     * @return managerName
     */
    public String getManagerName() {
        return managerName;
    }


    /**
     * Sets the managerName value for this FormSEOrder.
     * 
     * @param managerName
     */
    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }


    /**
     * Gets the managerNumber value for this FormSEOrder.
     * 
     * @return managerNumber
     */
    public String getManagerNumber() {
        return managerNumber;
    }


    /**
     * Sets the managerNumber value for this FormSEOrder.
     * 
     * @param managerNumber
     */
    public void setManagerNumber(String managerNumber) {
        this.managerNumber = managerNumber;
    }


    /**
     * Gets the orderFromNumber value for this FormSEOrder.
     * 
     * @return orderFromNumber
     */
    public String getOrderFromNumber() {
        return orderFromNumber;
    }


    /**
     * Sets the orderFromNumber value for this FormSEOrder.
     * 
     * @param orderFromNumber
     */
    public void setOrderFromNumber(String orderFromNumber) {
        this.orderFromNumber = orderFromNumber;
    }


    /**
     * Gets the orderTypeNumber value for this FormSEOrder.
     * 
     * @return orderTypeNumber
     */
    public String getOrderTypeNumber() {
        return orderTypeNumber;
    }


    /**
     * Sets the orderTypeNumber value for this FormSEOrder.
     * 
     * @param orderTypeNumber
     */
    public void setOrderTypeNumber(String orderTypeNumber) {
        this.orderTypeNumber = orderTypeNumber;
    }


    /**
     * Gets the payMethodNumber value for this FormSEOrder.
     * 
     * @return payMethodNumber
     */
    public String getPayMethodNumber() {
        return payMethodNumber;
    }


    /**
     * Sets the payMethodNumber value for this FormSEOrder.
     * 
     * @param payMethodNumber
     */
    public void setPayMethodNumber(String payMethodNumber) {
        this.payMethodNumber = payMethodNumber;
    }


    /**
     * Gets the willSendDate value for this FormSEOrder.
     * 
     * @return willSendDate
     */
    public java.util.Calendar getWillSendDate() {
        return willSendDate;
    }


    /**
     * Sets the willSendDate value for this FormSEOrder.
     * 
     * @param willSendDate
     */
    public void setWillSendDate(java.util.Calendar willSendDate) {
        this.willSendDate = willSendDate;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof FormSEOrder)) return false;
        FormSEOrder other = (FormSEOrder) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.acctDeptName==null && other.getAcctDeptName()==null) || 
             (this.acctDeptName!=null &&
              this.acctDeptName.equals(other.getAcctDeptName()))) &&
            ((this.acctDeptNumber==null && other.getAcctDeptNumber()==null) || 
             (this.acctDeptNumber!=null &&
              this.acctDeptNumber.equals(other.getAcctDeptNumber()))) &&
            ((this.areaPS==null && other.getAreaPS()==null) || 
             (this.areaPS!=null &&
              this.areaPS.equals(other.getAreaPS()))) &&
            ((this.billNO==null && other.getBillNO()==null) || 
             (this.billNO!=null &&
              this.billNO.equals(other.getBillNO()))) &&
            ((this.billerName==null && other.getBillerName()==null) || 
             (this.billerName!=null &&
              this.billerName.equals(other.getBillerName()))) &&
            ((this.businessTypeNumber==null && other.getBusinessTypeNumber()==null) || 
             (this.businessTypeNumber!=null &&
              this.businessTypeNumber.equals(other.getBusinessTypeNumber()))) &&
            ((this.checkDate==null && other.getCheckDate()==null) || 
             (this.checkDate!=null &&
              this.checkDate.equals(other.getCheckDate()))) &&
            ((this.checkerName==null && other.getCheckerName()==null) || 
             (this.checkerName!=null &&
              this.checkerName.equals(other.getCheckerName()))) &&
            ((this.companyNumber==null && other.getCompanyNumber()==null) || 
             (this.companyNumber!=null &&
              this.companyNumber.equals(other.getCompanyNumber()))) &&
            ((this.custName==null && other.getCustName()==null) || 
             (this.custName!=null &&
              this.custName.equals(other.getCustName()))) &&
            ((this.custNumber==null && other.getCustNumber()==null) || 
             (this.custNumber!=null &&
              this.custNumber.equals(other.getCustNumber()))) &&
            ((this.date==null && other.getDate()==null) || 
             (this.date!=null &&
              this.date.equals(other.getDate()))) &&
            ((this.deliverPhone==null && other.getDeliverPhone()==null) || 
             (this.deliverPhone!=null &&
              this.deliverPhone.equals(other.getDeliverPhone()))) &&
            ((this.deliveryAddress==null && other.getDeliveryAddress()==null) || 
             (this.deliveryAddress!=null &&
              this.deliveryAddress.equals(other.getDeliveryAddress()))) &&
            ((this.deliveryName==null && other.getDeliveryName()==null) || 
             (this.deliveryName!=null &&
              this.deliveryName.equals(other.getDeliveryName()))) &&
            ((this.deptName==null && other.getDeptName()==null) || 
             (this.deptName!=null &&
              this.deptName.equals(other.getDeptName()))) &&
            ((this.deptNumber==null && other.getDeptNumber()==null) || 
             (this.deptNumber!=null &&
              this.deptNumber.equals(other.getDeptNumber()))) &&
            ((this.empName==null && other.getEmpName()==null) || 
             (this.empName!=null &&
              this.empName.equals(other.getEmpName()))) &&
            ((this.empNumber==null && other.getEmpNumber()==null) || 
             (this.empNumber!=null &&
              this.empNumber.equals(other.getEmpNumber()))) &&
            ((this.entrys==null && other.getEntrys()==null) || 
             (this.entrys!=null &&
              java.util.Arrays.equals(this.entrys, other.getEntrys()))) &&
            ((this.explanation==null && other.getExplanation()==null) || 
             (this.explanation!=null &&
              this.explanation.equals(other.getExplanation()))) &&
            ((this.fetchStyleNumber==null && other.getFetchStyleNumber()==null) || 
             (this.fetchStyleNumber!=null &&
              this.fetchStyleNumber.equals(other.getFetchStyleNumber()))) &&
            ((this.invoiceType==null && other.getInvoiceType()==null) || 
             (this.invoiceType!=null &&
              this.invoiceType.equals(other.getInvoiceType()))) &&
            ((this.isReplace==null && other.getIsReplace()==null) || 
             (this.isReplace!=null &&
              this.isReplace.equals(other.getIsReplace()))) &&
            ((this.managerName==null && other.getManagerName()==null) || 
             (this.managerName!=null &&
              this.managerName.equals(other.getManagerName()))) &&
            ((this.managerNumber==null && other.getManagerNumber()==null) || 
             (this.managerNumber!=null &&
              this.managerNumber.equals(other.getManagerNumber()))) &&
            ((this.orderFromNumber==null && other.getOrderFromNumber()==null) || 
             (this.orderFromNumber!=null &&
              this.orderFromNumber.equals(other.getOrderFromNumber()))) &&
            ((this.orderTypeNumber==null && other.getOrderTypeNumber()==null) || 
             (this.orderTypeNumber!=null &&
              this.orderTypeNumber.equals(other.getOrderTypeNumber()))) &&
            ((this.payMethodNumber==null && other.getPayMethodNumber()==null) || 
             (this.payMethodNumber!=null &&
              this.payMethodNumber.equals(other.getPayMethodNumber()))) &&
            ((this.willSendDate==null && other.getWillSendDate()==null) || 
             (this.willSendDate!=null &&
              this.willSendDate.equals(other.getWillSendDate())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getAcctDeptName() != null) {
            _hashCode += getAcctDeptName().hashCode();
        }
        if (getAcctDeptNumber() != null) {
            _hashCode += getAcctDeptNumber().hashCode();
        }
        if (getAreaPS() != null) {
            _hashCode += getAreaPS().hashCode();
        }
        if (getBillNO() != null) {
            _hashCode += getBillNO().hashCode();
        }
        if (getBillerName() != null) {
            _hashCode += getBillerName().hashCode();
        }
        if (getBusinessTypeNumber() != null) {
            _hashCode += getBusinessTypeNumber().hashCode();
        }
        if (getCheckDate() != null) {
            _hashCode += getCheckDate().hashCode();
        }
        if (getCheckerName() != null) {
            _hashCode += getCheckerName().hashCode();
        }
        if (getCompanyNumber() != null) {
            _hashCode += getCompanyNumber().hashCode();
        }
        if (getCustName() != null) {
            _hashCode += getCustName().hashCode();
        }
        if (getCustNumber() != null) {
            _hashCode += getCustNumber().hashCode();
        }
        if (getDate() != null) {
            _hashCode += getDate().hashCode();
        }
        if (getDeliverPhone() != null) {
            _hashCode += getDeliverPhone().hashCode();
        }
        if (getDeliveryAddress() != null) {
            _hashCode += getDeliveryAddress().hashCode();
        }
        if (getDeliveryName() != null) {
            _hashCode += getDeliveryName().hashCode();
        }
        if (getDeptName() != null) {
            _hashCode += getDeptName().hashCode();
        }
        if (getDeptNumber() != null) {
            _hashCode += getDeptNumber().hashCode();
        }
        if (getEmpName() != null) {
            _hashCode += getEmpName().hashCode();
        }
        if (getEmpNumber() != null) {
            _hashCode += getEmpNumber().hashCode();
        }
        if (getEntrys() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getEntrys());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getEntrys(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getExplanation() != null) {
            _hashCode += getExplanation().hashCode();
        }
        if (getFetchStyleNumber() != null) {
            _hashCode += getFetchStyleNumber().hashCode();
        }
        if (getInvoiceType() != null) {
            _hashCode += getInvoiceType().hashCode();
        }
        if (getIsReplace() != null) {
            _hashCode += getIsReplace().hashCode();
        }
        if (getManagerName() != null) {
            _hashCode += getManagerName().hashCode();
        }
        if (getManagerNumber() != null) {
            _hashCode += getManagerNumber().hashCode();
        }
        if (getOrderFromNumber() != null) {
            _hashCode += getOrderFromNumber().hashCode();
        }
        if (getOrderTypeNumber() != null) {
            _hashCode += getOrderTypeNumber().hashCode();
        }
        if (getPayMethodNumber() != null) {
            _hashCode += getPayMethodNumber().hashCode();
        }
        if (getWillSendDate() != null) {
            _hashCode += getWillSendDate().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FormSEOrder.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "FormSEOrder"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("acctDeptName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "AcctDeptName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("acctDeptNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "AcctDeptNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("areaPS");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "AreaPS"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("billNO");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "BillNO"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("billerName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "BillerName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("businessTypeNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "BusinessTypeNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("checkDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "CheckDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("checkerName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "CheckerName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("companyNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "CompanyNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("custName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "CustName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("custNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "CustNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("date");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "Date"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deliverPhone");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "DeliverPhone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deliveryAddress");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "DeliveryAddress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deliveryName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "DeliveryName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deptName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "DeptName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deptNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "DeptNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("empName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "EmpName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("empNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "EmpNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entrys");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "Entrys"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "FormSEOrderEntry"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "FormSEOrderEntry"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("explanation");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "Explanation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fetchStyleNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "FetchStyleNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("invoiceType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "InvoiceType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isReplace");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "IsReplace"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("managerName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "ManagerName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("managerNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "ManagerNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderFromNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "OrderFromNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderTypeNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "OrderTypeNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("payMethodNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "PayMethodNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("willSendDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "WillSendDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           String mechType,
           Class _javaType,
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           String mechType,
           Class _javaType,
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
