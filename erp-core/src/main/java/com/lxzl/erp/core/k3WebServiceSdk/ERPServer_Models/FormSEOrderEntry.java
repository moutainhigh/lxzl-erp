/**
 * FormSEOrderEntry.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models;

public class FormSEOrderEntry  implements java.io.Serializable {
    private java.math.BigDecimal addRate;

    private java.math.BigDecimal amount;

    private java.util.Calendar date;

    private java.math.BigDecimal EQAmount;

    private String EQConfigName;

    private String EQConfigNumber;

    private java.math.BigDecimal EQPrice;

    private String EQType;

    private java.math.BigDecimal EQYJAmount;

    private java.util.Calendar endDate;

    private java.math.BigDecimal leaseMonthCount;

    private String name;

    private String note;

    private String number;

    private Integer orderItemId;

    private java.math.BigDecimal payAmountTotal;

    private String payMethodNumber;

    private java.math.BigDecimal payMonthCount;

    private java.math.BigDecimal price;

    private java.math.BigDecimal qty;

    private java.math.BigDecimal SFAmount;

    private java.math.BigDecimal SFMonthCount;

    private java.util.Calendar startDate;

    private java.math.BigDecimal stdPrice;

    private String supplyNumber;

    private java.math.BigDecimal YJAmount;

    private java.math.BigDecimal YJMonthCount;

    public FormSEOrderEntry() {
    }

    public FormSEOrderEntry(
           java.math.BigDecimal addRate,
           java.math.BigDecimal amount,
           java.util.Calendar date,
           java.math.BigDecimal EQAmount,
           String EQConfigName,
           String EQConfigNumber,
           java.math.BigDecimal EQPrice,
           String EQType,
           java.math.BigDecimal EQYJAmount,
           java.util.Calendar endDate,
           java.math.BigDecimal leaseMonthCount,
           String name,
           String note,
           String number,
           Integer orderItemId,
           java.math.BigDecimal payAmountTotal,
           String payMethodNumber,
           java.math.BigDecimal payMonthCount,
           java.math.BigDecimal price,
           java.math.BigDecimal qty,
           java.math.BigDecimal SFAmount,
           java.math.BigDecimal SFMonthCount,
           java.util.Calendar startDate,
           java.math.BigDecimal stdPrice,
           String supplyNumber,
           java.math.BigDecimal YJAmount,
           java.math.BigDecimal YJMonthCount) {
           this.addRate = addRate;
           this.amount = amount;
           this.date = date;
           this.EQAmount = EQAmount;
           this.EQConfigName = EQConfigName;
           this.EQConfigNumber = EQConfigNumber;
           this.EQPrice = EQPrice;
           this.EQType = EQType;
           this.EQYJAmount = EQYJAmount;
           this.endDate = endDate;
           this.leaseMonthCount = leaseMonthCount;
           this.name = name;
           this.note = note;
           this.number = number;
           this.orderItemId = orderItemId;
           this.payAmountTotal = payAmountTotal;
           this.payMethodNumber = payMethodNumber;
           this.payMonthCount = payMonthCount;
           this.price = price;
           this.qty = qty;
           this.SFAmount = SFAmount;
           this.SFMonthCount = SFMonthCount;
           this.startDate = startDate;
           this.stdPrice = stdPrice;
           this.supplyNumber = supplyNumber;
           this.YJAmount = YJAmount;
           this.YJMonthCount = YJMonthCount;
    }


    /**
     * Gets the addRate value for this FormSEOrderEntry.
     * 
     * @return addRate
     */
    public java.math.BigDecimal getAddRate() {
        return addRate;
    }


    /**
     * Sets the addRate value for this FormSEOrderEntry.
     * 
     * @param addRate
     */
    public void setAddRate(java.math.BigDecimal addRate) {
        this.addRate = addRate;
    }


    /**
     * Gets the amount value for this FormSEOrderEntry.
     * 
     * @return amount
     */
    public java.math.BigDecimal getAmount() {
        return amount;
    }


    /**
     * Sets the amount value for this FormSEOrderEntry.
     * 
     * @param amount
     */
    public void setAmount(java.math.BigDecimal amount) {
        this.amount = amount;
    }


    /**
     * Gets the date value for this FormSEOrderEntry.
     * 
     * @return date
     */
    public java.util.Calendar getDate() {
        return date;
    }


    /**
     * Sets the date value for this FormSEOrderEntry.
     * 
     * @param date
     */
    public void setDate(java.util.Calendar date) {
        this.date = date;
    }


    /**
     * Gets the EQAmount value for this FormSEOrderEntry.
     * 
     * @return EQAmount
     */
    public java.math.BigDecimal getEQAmount() {
        return EQAmount;
    }


    /**
     * Sets the EQAmount value for this FormSEOrderEntry.
     * 
     * @param EQAmount
     */
    public void setEQAmount(java.math.BigDecimal EQAmount) {
        this.EQAmount = EQAmount;
    }


    /**
     * Gets the EQConfigName value for this FormSEOrderEntry.
     * 
     * @return EQConfigName
     */
    public String getEQConfigName() {
        return EQConfigName;
    }


    /**
     * Sets the EQConfigName value for this FormSEOrderEntry.
     * 
     * @param EQConfigName
     */
    public void setEQConfigName(String EQConfigName) {
        this.EQConfigName = EQConfigName;
    }


    /**
     * Gets the EQConfigNumber value for this FormSEOrderEntry.
     * 
     * @return EQConfigNumber
     */
    public String getEQConfigNumber() {
        return EQConfigNumber;
    }


    /**
     * Sets the EQConfigNumber value for this FormSEOrderEntry.
     * 
     * @param EQConfigNumber
     */
    public void setEQConfigNumber(String EQConfigNumber) {
        this.EQConfigNumber = EQConfigNumber;
    }


    /**
     * Gets the EQPrice value for this FormSEOrderEntry.
     * 
     * @return EQPrice
     */
    public java.math.BigDecimal getEQPrice() {
        return EQPrice;
    }


    /**
     * Sets the EQPrice value for this FormSEOrderEntry.
     * 
     * @param EQPrice
     */
    public void setEQPrice(java.math.BigDecimal EQPrice) {
        this.EQPrice = EQPrice;
    }


    /**
     * Gets the EQType value for this FormSEOrderEntry.
     * 
     * @return EQType
     */
    public String getEQType() {
        return EQType;
    }


    /**
     * Sets the EQType value for this FormSEOrderEntry.
     * 
     * @param EQType
     */
    public void setEQType(String EQType) {
        this.EQType = EQType;
    }


    /**
     * Gets the EQYJAmount value for this FormSEOrderEntry.
     * 
     * @return EQYJAmount
     */
    public java.math.BigDecimal getEQYJAmount() {
        return EQYJAmount;
    }


    /**
     * Sets the EQYJAmount value for this FormSEOrderEntry.
     * 
     * @param EQYJAmount
     */
    public void setEQYJAmount(java.math.BigDecimal EQYJAmount) {
        this.EQYJAmount = EQYJAmount;
    }


    /**
     * Gets the endDate value for this FormSEOrderEntry.
     * 
     * @return endDate
     */
    public java.util.Calendar getEndDate() {
        return endDate;
    }


    /**
     * Sets the endDate value for this FormSEOrderEntry.
     * 
     * @param endDate
     */
    public void setEndDate(java.util.Calendar endDate) {
        this.endDate = endDate;
    }


    /**
     * Gets the leaseMonthCount value for this FormSEOrderEntry.
     * 
     * @return leaseMonthCount
     */
    public java.math.BigDecimal getLeaseMonthCount() {
        return leaseMonthCount;
    }


    /**
     * Sets the leaseMonthCount value for this FormSEOrderEntry.
     * 
     * @param leaseMonthCount
     */
    public void setLeaseMonthCount(java.math.BigDecimal leaseMonthCount) {
        this.leaseMonthCount = leaseMonthCount;
    }


    /**
     * Gets the name value for this FormSEOrderEntry.
     * 
     * @return name
     */
    public String getName() {
        return name;
    }


    /**
     * Sets the name value for this FormSEOrderEntry.
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Gets the note value for this FormSEOrderEntry.
     * 
     * @return note
     */
    public String getNote() {
        return note;
    }


    /**
     * Sets the note value for this FormSEOrderEntry.
     * 
     * @param note
     */
    public void setNote(String note) {
        this.note = note;
    }


    /**
     * Gets the number value for this FormSEOrderEntry.
     * 
     * @return number
     */
    public String getNumber() {
        return number;
    }


    /**
     * Sets the number value for this FormSEOrderEntry.
     * 
     * @param number
     */
    public void setNumber(String number) {
        this.number = number;
    }


    /**
     * Gets the orderItemId value for this FormSEOrderEntry.
     * 
     * @return orderItemId
     */
    public Integer getOrderItemId() {
        return orderItemId;
    }


    /**
     * Sets the orderItemId value for this FormSEOrderEntry.
     * 
     * @param orderItemId
     */
    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }


    /**
     * Gets the payAmountTotal value for this FormSEOrderEntry.
     * 
     * @return payAmountTotal
     */
    public java.math.BigDecimal getPayAmountTotal() {
        return payAmountTotal;
    }


    /**
     * Sets the payAmountTotal value for this FormSEOrderEntry.
     * 
     * @param payAmountTotal
     */
    public void setPayAmountTotal(java.math.BigDecimal payAmountTotal) {
        this.payAmountTotal = payAmountTotal;
    }


    /**
     * Gets the payMethodNumber value for this FormSEOrderEntry.
     * 
     * @return payMethodNumber
     */
    public String getPayMethodNumber() {
        return payMethodNumber;
    }


    /**
     * Sets the payMethodNumber value for this FormSEOrderEntry.
     * 
     * @param payMethodNumber
     */
    public void setPayMethodNumber(String payMethodNumber) {
        this.payMethodNumber = payMethodNumber;
    }


    /**
     * Gets the payMonthCount value for this FormSEOrderEntry.
     * 
     * @return payMonthCount
     */
    public java.math.BigDecimal getPayMonthCount() {
        return payMonthCount;
    }


    /**
     * Sets the payMonthCount value for this FormSEOrderEntry.
     * 
     * @param payMonthCount
     */
    public void setPayMonthCount(java.math.BigDecimal payMonthCount) {
        this.payMonthCount = payMonthCount;
    }


    /**
     * Gets the price value for this FormSEOrderEntry.
     * 
     * @return price
     */
    public java.math.BigDecimal getPrice() {
        return price;
    }


    /**
     * Sets the price value for this FormSEOrderEntry.
     * 
     * @param price
     */
    public void setPrice(java.math.BigDecimal price) {
        this.price = price;
    }


    /**
     * Gets the qty value for this FormSEOrderEntry.
     * 
     * @return qty
     */
    public java.math.BigDecimal getQty() {
        return qty;
    }


    /**
     * Sets the qty value for this FormSEOrderEntry.
     * 
     * @param qty
     */
    public void setQty(java.math.BigDecimal qty) {
        this.qty = qty;
    }


    /**
     * Gets the SFAmount value for this FormSEOrderEntry.
     * 
     * @return SFAmount
     */
    public java.math.BigDecimal getSFAmount() {
        return SFAmount;
    }


    /**
     * Sets the SFAmount value for this FormSEOrderEntry.
     * 
     * @param SFAmount
     */
    public void setSFAmount(java.math.BigDecimal SFAmount) {
        this.SFAmount = SFAmount;
    }


    /**
     * Gets the SFMonthCount value for this FormSEOrderEntry.
     * 
     * @return SFMonthCount
     */
    public java.math.BigDecimal getSFMonthCount() {
        return SFMonthCount;
    }


    /**
     * Sets the SFMonthCount value for this FormSEOrderEntry.
     * 
     * @param SFMonthCount
     */
    public void setSFMonthCount(java.math.BigDecimal SFMonthCount) {
        this.SFMonthCount = SFMonthCount;
    }


    /**
     * Gets the startDate value for this FormSEOrderEntry.
     * 
     * @return startDate
     */
    public java.util.Calendar getStartDate() {
        return startDate;
    }


    /**
     * Sets the startDate value for this FormSEOrderEntry.
     * 
     * @param startDate
     */
    public void setStartDate(java.util.Calendar startDate) {
        this.startDate = startDate;
    }


    /**
     * Gets the stdPrice value for this FormSEOrderEntry.
     * 
     * @return stdPrice
     */
    public java.math.BigDecimal getStdPrice() {
        return stdPrice;
    }


    /**
     * Sets the stdPrice value for this FormSEOrderEntry.
     * 
     * @param stdPrice
     */
    public void setStdPrice(java.math.BigDecimal stdPrice) {
        this.stdPrice = stdPrice;
    }


    /**
     * Gets the supplyNumber value for this FormSEOrderEntry.
     * 
     * @return supplyNumber
     */
    public String getSupplyNumber() {
        return supplyNumber;
    }


    /**
     * Sets the supplyNumber value for this FormSEOrderEntry.
     * 
     * @param supplyNumber
     */
    public void setSupplyNumber(String supplyNumber) {
        this.supplyNumber = supplyNumber;
    }


    /**
     * Gets the YJAmount value for this FormSEOrderEntry.
     * 
     * @return YJAmount
     */
    public java.math.BigDecimal getYJAmount() {
        return YJAmount;
    }


    /**
     * Sets the YJAmount value for this FormSEOrderEntry.
     * 
     * @param YJAmount
     */
    public void setYJAmount(java.math.BigDecimal YJAmount) {
        this.YJAmount = YJAmount;
    }


    /**
     * Gets the YJMonthCount value for this FormSEOrderEntry.
     * 
     * @return YJMonthCount
     */
    public java.math.BigDecimal getYJMonthCount() {
        return YJMonthCount;
    }


    /**
     * Sets the YJMonthCount value for this FormSEOrderEntry.
     * 
     * @param YJMonthCount
     */
    public void setYJMonthCount(java.math.BigDecimal YJMonthCount) {
        this.YJMonthCount = YJMonthCount;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof FormSEOrderEntry)) return false;
        FormSEOrderEntry other = (FormSEOrderEntry) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.addRate==null && other.getAddRate()==null) || 
             (this.addRate!=null &&
              this.addRate.equals(other.getAddRate()))) &&
            ((this.amount==null && other.getAmount()==null) || 
             (this.amount!=null &&
              this.amount.equals(other.getAmount()))) &&
            ((this.date==null && other.getDate()==null) || 
             (this.date!=null &&
              this.date.equals(other.getDate()))) &&
            ((this.EQAmount==null && other.getEQAmount()==null) || 
             (this.EQAmount!=null &&
              this.EQAmount.equals(other.getEQAmount()))) &&
            ((this.EQConfigName==null && other.getEQConfigName()==null) || 
             (this.EQConfigName!=null &&
              this.EQConfigName.equals(other.getEQConfigName()))) &&
            ((this.EQConfigNumber==null && other.getEQConfigNumber()==null) || 
             (this.EQConfigNumber!=null &&
              this.EQConfigNumber.equals(other.getEQConfigNumber()))) &&
            ((this.EQPrice==null && other.getEQPrice()==null) || 
             (this.EQPrice!=null &&
              this.EQPrice.equals(other.getEQPrice()))) &&
            ((this.EQType==null && other.getEQType()==null) || 
             (this.EQType!=null &&
              this.EQType.equals(other.getEQType()))) &&
            ((this.EQYJAmount==null && other.getEQYJAmount()==null) || 
             (this.EQYJAmount!=null &&
              this.EQYJAmount.equals(other.getEQYJAmount()))) &&
            ((this.endDate==null && other.getEndDate()==null) || 
             (this.endDate!=null &&
              this.endDate.equals(other.getEndDate()))) &&
            ((this.leaseMonthCount==null && other.getLeaseMonthCount()==null) || 
             (this.leaseMonthCount!=null &&
              this.leaseMonthCount.equals(other.getLeaseMonthCount()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.note==null && other.getNote()==null) || 
             (this.note!=null &&
              this.note.equals(other.getNote()))) &&
            ((this.number==null && other.getNumber()==null) || 
             (this.number!=null &&
              this.number.equals(other.getNumber()))) &&
            ((this.orderItemId==null && other.getOrderItemId()==null) || 
             (this.orderItemId!=null &&
              this.orderItemId.equals(other.getOrderItemId()))) &&
            ((this.payAmountTotal==null && other.getPayAmountTotal()==null) || 
             (this.payAmountTotal!=null &&
              this.payAmountTotal.equals(other.getPayAmountTotal()))) &&
            ((this.payMethodNumber==null && other.getPayMethodNumber()==null) || 
             (this.payMethodNumber!=null &&
              this.payMethodNumber.equals(other.getPayMethodNumber()))) &&
            ((this.payMonthCount==null && other.getPayMonthCount()==null) || 
             (this.payMonthCount!=null &&
              this.payMonthCount.equals(other.getPayMonthCount()))) &&
            ((this.price==null && other.getPrice()==null) || 
             (this.price!=null &&
              this.price.equals(other.getPrice()))) &&
            ((this.qty==null && other.getQty()==null) || 
             (this.qty!=null &&
              this.qty.equals(other.getQty()))) &&
            ((this.SFAmount==null && other.getSFAmount()==null) || 
             (this.SFAmount!=null &&
              this.SFAmount.equals(other.getSFAmount()))) &&
            ((this.SFMonthCount==null && other.getSFMonthCount()==null) || 
             (this.SFMonthCount!=null &&
              this.SFMonthCount.equals(other.getSFMonthCount()))) &&
            ((this.startDate==null && other.getStartDate()==null) || 
             (this.startDate!=null &&
              this.startDate.equals(other.getStartDate()))) &&
            ((this.stdPrice==null && other.getStdPrice()==null) || 
             (this.stdPrice!=null &&
              this.stdPrice.equals(other.getStdPrice()))) &&
            ((this.supplyNumber==null && other.getSupplyNumber()==null) || 
             (this.supplyNumber!=null &&
              this.supplyNumber.equals(other.getSupplyNumber()))) &&
            ((this.YJAmount==null && other.getYJAmount()==null) || 
             (this.YJAmount!=null &&
              this.YJAmount.equals(other.getYJAmount()))) &&
            ((this.YJMonthCount==null && other.getYJMonthCount()==null) || 
             (this.YJMonthCount!=null &&
              this.YJMonthCount.equals(other.getYJMonthCount())));
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
        if (getAddRate() != null) {
            _hashCode += getAddRate().hashCode();
        }
        if (getAmount() != null) {
            _hashCode += getAmount().hashCode();
        }
        if (getDate() != null) {
            _hashCode += getDate().hashCode();
        }
        if (getEQAmount() != null) {
            _hashCode += getEQAmount().hashCode();
        }
        if (getEQConfigName() != null) {
            _hashCode += getEQConfigName().hashCode();
        }
        if (getEQConfigNumber() != null) {
            _hashCode += getEQConfigNumber().hashCode();
        }
        if (getEQPrice() != null) {
            _hashCode += getEQPrice().hashCode();
        }
        if (getEQType() != null) {
            _hashCode += getEQType().hashCode();
        }
        if (getEQYJAmount() != null) {
            _hashCode += getEQYJAmount().hashCode();
        }
        if (getEndDate() != null) {
            _hashCode += getEndDate().hashCode();
        }
        if (getLeaseMonthCount() != null) {
            _hashCode += getLeaseMonthCount().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getNote() != null) {
            _hashCode += getNote().hashCode();
        }
        if (getNumber() != null) {
            _hashCode += getNumber().hashCode();
        }
        if (getOrderItemId() != null) {
            _hashCode += getOrderItemId().hashCode();
        }
        if (getPayAmountTotal() != null) {
            _hashCode += getPayAmountTotal().hashCode();
        }
        if (getPayMethodNumber() != null) {
            _hashCode += getPayMethodNumber().hashCode();
        }
        if (getPayMonthCount() != null) {
            _hashCode += getPayMonthCount().hashCode();
        }
        if (getPrice() != null) {
            _hashCode += getPrice().hashCode();
        }
        if (getQty() != null) {
            _hashCode += getQty().hashCode();
        }
        if (getSFAmount() != null) {
            _hashCode += getSFAmount().hashCode();
        }
        if (getSFMonthCount() != null) {
            _hashCode += getSFMonthCount().hashCode();
        }
        if (getStartDate() != null) {
            _hashCode += getStartDate().hashCode();
        }
        if (getStdPrice() != null) {
            _hashCode += getStdPrice().hashCode();
        }
        if (getSupplyNumber() != null) {
            _hashCode += getSupplyNumber().hashCode();
        }
        if (getYJAmount() != null) {
            _hashCode += getYJAmount().hashCode();
        }
        if (getYJMonthCount() != null) {
            _hashCode += getYJMonthCount().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FormSEOrderEntry.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "FormSEOrderEntry"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("addRate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "AddRate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("amount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "Amount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("date");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "Date"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("EQAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "EQAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("EQConfigName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "EQConfigName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("EQConfigNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "EQConfigNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("EQPrice");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "EQPrice"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("EQType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "EQType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("EQYJAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "EQYJAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("endDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "EndDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("leaseMonthCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "LeaseMonthCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "Name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("note");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "Note"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("number");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "Number"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderItemId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "OrderItemId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("payAmountTotal");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "PayAmountTotal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("payMethodNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "PayMethodNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("payMonthCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "PayMonthCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("price");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "Price"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("qty");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "Qty"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SFAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "SFAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SFMonthCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "SFMonthCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "StartDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stdPrice");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "StdPrice"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("supplyNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "SupplyNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("YJAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "YJAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("YJMonthCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "YJMonthCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
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
