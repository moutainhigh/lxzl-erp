/**
 * FormSEOutStockEntry.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models;

public class FormSEOutStockEntry  implements java.io.Serializable {
    private String note;

    private Integer orderEntry;

    private String orderNO;

    private String productNumber;

    private java.math.BigDecimal qty;

    public FormSEOutStockEntry() {
    }

    public FormSEOutStockEntry(
           String note,
           Integer orderEntry,
           String orderNO,
           String productNumber,
           java.math.BigDecimal qty) {
           this.note = note;
           this.orderEntry = orderEntry;
           this.orderNO = orderNO;
           this.productNumber = productNumber;
           this.qty = qty;
    }


    /**
     * Gets the note value for this FormSEOutStockEntry.
     * 
     * @return note
     */
    public String getNote() {
        return note;
    }


    /**
     * Sets the note value for this FormSEOutStockEntry.
     * 
     * @param note
     */
    public void setNote(String note) {
        this.note = note;
    }


    /**
     * Gets the orderEntry value for this FormSEOutStockEntry.
     * 
     * @return orderEntry
     */
    public Integer getOrderEntry() {
        return orderEntry;
    }


    /**
     * Sets the orderEntry value for this FormSEOutStockEntry.
     * 
     * @param orderEntry
     */
    public void setOrderEntry(Integer orderEntry) {
        this.orderEntry = orderEntry;
    }


    /**
     * Gets the orderNO value for this FormSEOutStockEntry.
     * 
     * @return orderNO
     */
    public String getOrderNO() {
        return orderNO;
    }


    /**
     * Sets the orderNO value for this FormSEOutStockEntry.
     * 
     * @param orderNO
     */
    public void setOrderNO(String orderNO) {
        this.orderNO = orderNO;
    }


    /**
     * Gets the productNumber value for this FormSEOutStockEntry.
     * 
     * @return productNumber
     */
    public String getProductNumber() {
        return productNumber;
    }


    /**
     * Sets the productNumber value for this FormSEOutStockEntry.
     * 
     * @param productNumber
     */
    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }


    /**
     * Gets the qty value for this FormSEOutStockEntry.
     * 
     * @return qty
     */
    public java.math.BigDecimal getQty() {
        return qty;
    }


    /**
     * Sets the qty value for this FormSEOutStockEntry.
     * 
     * @param qty
     */
    public void setQty(java.math.BigDecimal qty) {
        this.qty = qty;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof FormSEOutStockEntry)) return false;
        FormSEOutStockEntry other = (FormSEOutStockEntry) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.note==null && other.getNote()==null) || 
             (this.note!=null &&
              this.note.equals(other.getNote()))) &&
            ((this.orderEntry==null && other.getOrderEntry()==null) || 
             (this.orderEntry!=null &&
              this.orderEntry.equals(other.getOrderEntry()))) &&
            ((this.orderNO==null && other.getOrderNO()==null) || 
             (this.orderNO!=null &&
              this.orderNO.equals(other.getOrderNO()))) &&
            ((this.productNumber==null && other.getProductNumber()==null) || 
             (this.productNumber!=null &&
              this.productNumber.equals(other.getProductNumber()))) &&
            ((this.qty==null && other.getQty()==null) || 
             (this.qty!=null &&
              this.qty.equals(other.getQty())));
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
        if (getNote() != null) {
            _hashCode += getNote().hashCode();
        }
        if (getOrderEntry() != null) {
            _hashCode += getOrderEntry().hashCode();
        }
        if (getOrderNO() != null) {
            _hashCode += getOrderNO().hashCode();
        }
        if (getProductNumber() != null) {
            _hashCode += getProductNumber().hashCode();
        }
        if (getQty() != null) {
            _hashCode += getQty().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FormSEOutStockEntry.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "FormSEOutStockEntry"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("note");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "Note"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderEntry");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "OrderEntry"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderNO");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "OrderNO"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("productNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "ProductNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("qty");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "Qty"));
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
