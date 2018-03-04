/**
 * FormICItem.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models;

public class FormICItem  implements java.io.Serializable {
    private String model;

    private String name;

    private String number;

    private ItemNumber[] numbers;

    public FormICItem() {
    }

    public FormICItem(
           String model,
           String name,
           String number,
           ItemNumber[] numbers) {
           this.model = model;
           this.name = name;
           this.number = number;
           this.numbers = numbers;
    }


    /**
     * Gets the model value for this FormICItem.
     * 
     * @return model
     */
    public String getModel() {
        return model;
    }


    /**
     * Sets the model value for this FormICItem.
     * 
     * @param model
     */
    public void setModel(String model) {
        this.model = model;
    }


    /**
     * Gets the name value for this FormICItem.
     * 
     * @return name
     */
    public String getName() {
        return name;
    }


    /**
     * Sets the name value for this FormICItem.
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Gets the number value for this FormICItem.
     * 
     * @return number
     */
    public String getNumber() {
        return number;
    }


    /**
     * Sets the number value for this FormICItem.
     * 
     * @param number
     */
    public void setNumber(String number) {
        this.number = number;
    }


    /**
     * Gets the numbers value for this FormICItem.
     * 
     * @return numbers
     */
    public ItemNumber[] getNumbers() {
        return numbers;
    }


    /**
     * Sets the numbers value for this FormICItem.
     * 
     * @param numbers
     */
    public void setNumbers(ItemNumber[] numbers) {
        this.numbers = numbers;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof FormICItem)) return false;
        FormICItem other = (FormICItem) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.model==null && other.getModel()==null) || 
             (this.model!=null &&
              this.model.equals(other.getModel()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.number==null && other.getNumber()==null) || 
             (this.number!=null &&
              this.number.equals(other.getNumber()))) &&
            ((this.numbers==null && other.getNumbers()==null) || 
             (this.numbers!=null &&
              java.util.Arrays.equals(this.numbers, other.getNumbers())));
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
        if (getModel() != null) {
            _hashCode += getModel().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getNumber() != null) {
            _hashCode += getNumber().hashCode();
        }
        if (getNumbers() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getNumbers());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getNumbers(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FormICItem.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "FormICItem"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("model");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "Model"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "Name"));
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
        elemField.setFieldName("numbers");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "Numbers"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "ItemNumber"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "ItemNumber"));
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
