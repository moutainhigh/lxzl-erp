/**
 * FormSEOutStock.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models;

public class FormSEOutStock  implements java.io.Serializable {
    private String address;

    private String backCompanyNO;

    private java.util.Calendar backDate;

    private String backMode;

    private String backType;

    private String billNO;

    private String biller;

    private String contacts;

    private String custName;

    private String custNumber;

    private java.util.Calendar date;

    private FormSEOutStockEntry[] entryList;

    private String note;

    private String phone;

    public FormSEOutStock() {
    }

    public FormSEOutStock(
           String address,
           String backCompanyNO,
           java.util.Calendar backDate,
           String backMode,
           String backType,
           String billNO,
           String biller,
           String contacts,
           String custName,
           String custNumber,
           java.util.Calendar date,
           FormSEOutStockEntry[] entryList,
           String note,
           String phone) {
           this.address = address;
           this.backCompanyNO = backCompanyNO;
           this.backDate = backDate;
           this.backMode = backMode;
           this.backType = backType;
           this.billNO = billNO;
           this.biller = biller;
           this.contacts = contacts;
           this.custName = custName;
           this.custNumber = custNumber;
           this.date = date;
           this.entryList = entryList;
           this.note = note;
           this.phone = phone;
    }


    /**
     * Gets the address value for this FormSEOutStock.
     * 
     * @return address
     */
    public String getAddress() {
        return address;
    }


    /**
     * Sets the address value for this FormSEOutStock.
     * 
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }


    /**
     * Gets the backCompanyNO value for this FormSEOutStock.
     * 
     * @return backCompanyNO
     */
    public String getBackCompanyNO() {
        return backCompanyNO;
    }


    /**
     * Sets the backCompanyNO value for this FormSEOutStock.
     * 
     * @param backCompanyNO
     */
    public void setBackCompanyNO(String backCompanyNO) {
        this.backCompanyNO = backCompanyNO;
    }


    /**
     * Gets the backDate value for this FormSEOutStock.
     * 
     * @return backDate
     */
    public java.util.Calendar getBackDate() {
        return backDate;
    }


    /**
     * Sets the backDate value for this FormSEOutStock.
     * 
     * @param backDate
     */
    public void setBackDate(java.util.Calendar backDate) {
        this.backDate = backDate;
    }


    /**
     * Gets the backMode value for this FormSEOutStock.
     * 
     * @return backMode
     */
    public String getBackMode() {
        return backMode;
    }


    /**
     * Sets the backMode value for this FormSEOutStock.
     * 
     * @param backMode
     */
    public void setBackMode(String backMode) {
        this.backMode = backMode;
    }


    /**
     * Gets the backType value for this FormSEOutStock.
     * 
     * @return backType
     */
    public String getBackType() {
        return backType;
    }


    /**
     * Sets the backType value for this FormSEOutStock.
     * 
     * @param backType
     */
    public void setBackType(String backType) {
        this.backType = backType;
    }


    /**
     * Gets the billNO value for this FormSEOutStock.
     * 
     * @return billNO
     */
    public String getBillNO() {
        return billNO;
    }


    /**
     * Sets the billNO value for this FormSEOutStock.
     * 
     * @param billNO
     */
    public void setBillNO(String billNO) {
        this.billNO = billNO;
    }


    /**
     * Gets the biller value for this FormSEOutStock.
     * 
     * @return biller
     */
    public String getBiller() {
        return biller;
    }


    /**
     * Sets the biller value for this FormSEOutStock.
     * 
     * @param biller
     */
    public void setBiller(String biller) {
        this.biller = biller;
    }


    /**
     * Gets the contacts value for this FormSEOutStock.
     * 
     * @return contacts
     */
    public String getContacts() {
        return contacts;
    }


    /**
     * Sets the contacts value for this FormSEOutStock.
     * 
     * @param contacts
     */
    public void setContacts(String contacts) {
        this.contacts = contacts;
    }


    /**
     * Gets the custName value for this FormSEOutStock.
     * 
     * @return custName
     */
    public String getCustName() {
        return custName;
    }


    /**
     * Sets the custName value for this FormSEOutStock.
     * 
     * @param custName
     */
    public void setCustName(String custName) {
        this.custName = custName;
    }


    /**
     * Gets the custNumber value for this FormSEOutStock.
     * 
     * @return custNumber
     */
    public String getCustNumber() {
        return custNumber;
    }


    /**
     * Sets the custNumber value for this FormSEOutStock.
     * 
     * @param custNumber
     */
    public void setCustNumber(String custNumber) {
        this.custNumber = custNumber;
    }


    /**
     * Gets the date value for this FormSEOutStock.
     * 
     * @return date
     */
    public java.util.Calendar getDate() {
        return date;
    }


    /**
     * Sets the date value for this FormSEOutStock.
     * 
     * @param date
     */
    public void setDate(java.util.Calendar date) {
        this.date = date;
    }


    /**
     * Gets the entryList value for this FormSEOutStock.
     * 
     * @return entryList
     */
    public FormSEOutStockEntry[] getEntryList() {
        return entryList;
    }


    /**
     * Sets the entryList value for this FormSEOutStock.
     * 
     * @param entryList
     */
    public void setEntryList(FormSEOutStockEntry[] entryList) {
        this.entryList = entryList;
    }


    /**
     * Gets the note value for this FormSEOutStock.
     * 
     * @return note
     */
    public String getNote() {
        return note;
    }


    /**
     * Sets the note value for this FormSEOutStock.
     * 
     * @param note
     */
    public void setNote(String note) {
        this.note = note;
    }


    /**
     * Gets the phone value for this FormSEOutStock.
     * 
     * @return phone
     */
    public String getPhone() {
        return phone;
    }


    /**
     * Sets the phone value for this FormSEOutStock.
     * 
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof FormSEOutStock)) return false;
        FormSEOutStock other = (FormSEOutStock) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.address==null && other.getAddress()==null) || 
             (this.address!=null &&
              this.address.equals(other.getAddress()))) &&
            ((this.backCompanyNO==null && other.getBackCompanyNO()==null) || 
             (this.backCompanyNO!=null &&
              this.backCompanyNO.equals(other.getBackCompanyNO()))) &&
            ((this.backDate==null && other.getBackDate()==null) || 
             (this.backDate!=null &&
              this.backDate.equals(other.getBackDate()))) &&
            ((this.backMode==null && other.getBackMode()==null) || 
             (this.backMode!=null &&
              this.backMode.equals(other.getBackMode()))) &&
            ((this.backType==null && other.getBackType()==null) || 
             (this.backType!=null &&
              this.backType.equals(other.getBackType()))) &&
            ((this.billNO==null && other.getBillNO()==null) || 
             (this.billNO!=null &&
              this.billNO.equals(other.getBillNO()))) &&
            ((this.biller==null && other.getBiller()==null) || 
             (this.biller!=null &&
              this.biller.equals(other.getBiller()))) &&
            ((this.contacts==null && other.getContacts()==null) || 
             (this.contacts!=null &&
              this.contacts.equals(other.getContacts()))) &&
            ((this.custName==null && other.getCustName()==null) || 
             (this.custName!=null &&
              this.custName.equals(other.getCustName()))) &&
            ((this.custNumber==null && other.getCustNumber()==null) || 
             (this.custNumber!=null &&
              this.custNumber.equals(other.getCustNumber()))) &&
            ((this.date==null && other.getDate()==null) || 
             (this.date!=null &&
              this.date.equals(other.getDate()))) &&
            ((this.entryList==null && other.getEntryList()==null) || 
             (this.entryList!=null &&
              java.util.Arrays.equals(this.entryList, other.getEntryList()))) &&
            ((this.note==null && other.getNote()==null) || 
             (this.note!=null &&
              this.note.equals(other.getNote()))) &&
            ((this.phone==null && other.getPhone()==null) || 
             (this.phone!=null &&
              this.phone.equals(other.getPhone())));
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
        if (getAddress() != null) {
            _hashCode += getAddress().hashCode();
        }
        if (getBackCompanyNO() != null) {
            _hashCode += getBackCompanyNO().hashCode();
        }
        if (getBackDate() != null) {
            _hashCode += getBackDate().hashCode();
        }
        if (getBackMode() != null) {
            _hashCode += getBackMode().hashCode();
        }
        if (getBackType() != null) {
            _hashCode += getBackType().hashCode();
        }
        if (getBillNO() != null) {
            _hashCode += getBillNO().hashCode();
        }
        if (getBiller() != null) {
            _hashCode += getBiller().hashCode();
        }
        if (getContacts() != null) {
            _hashCode += getContacts().hashCode();
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
        if (getEntryList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getEntryList());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getEntryList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getNote() != null) {
            _hashCode += getNote().hashCode();
        }
        if (getPhone() != null) {
            _hashCode += getPhone().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FormSEOutStock.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "FormSEOutStock"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("address");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "Address"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("backCompanyNO");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "BackCompanyNO"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("backDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "BackDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("backMode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "BackMode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("backType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "BackType"));
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
        elemField.setFieldName("biller");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "Biller"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contacts");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "Contacts"));
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
        elemField.setFieldName("entryList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "EntryList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "FormSEOutStockEntry"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "FormSEOutStockEntry"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("note");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "Note"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("phone");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "Phone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
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
