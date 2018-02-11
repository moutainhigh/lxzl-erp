/**
 * FormOrganization.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models;

public class FormOrganization  implements java.io.Serializable {
    private String address;

    private String contact;

    private String empName;

    private String empNumber;

    private String name;

    private String number;

    private ItemNumber[] numbers;

    private String phone;

    private java.math.BigDecimal valueAddRate;

    public FormOrganization() {
    }

    public FormOrganization(
           String address,
           String contact,
           String empName,
           String empNumber,
           String name,
           String number,
           ItemNumber[] numbers,
           String phone,
           java.math.BigDecimal valueAddRate) {
           this.address = address;
           this.contact = contact;
           this.empName = empName;
           this.empNumber = empNumber;
           this.name = name;
           this.number = number;
           this.numbers = numbers;
           this.phone = phone;
           this.valueAddRate = valueAddRate;
    }


    /**
     * Gets the address value for this FormOrganization.
     * 
     * @return address
     */
    public String getAddress() {
        return address;
    }


    /**
     * Sets the address value for this FormOrganization.
     * 
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }


    /**
     * Gets the contact value for this FormOrganization.
     * 
     * @return contact
     */
    public String getContact() {
        return contact;
    }


    /**
     * Sets the contact value for this FormOrganization.
     * 
     * @param contact
     */
    public void setContact(String contact) {
        this.contact = contact;
    }


    /**
     * Gets the empName value for this FormOrganization.
     * 
     * @return empName
     */
    public String getEmpName() {
        return empName;
    }


    /**
     * Sets the empName value for this FormOrganization.
     * 
     * @param empName
     */
    public void setEmpName(String empName) {
        this.empName = empName;
    }


    /**
     * Gets the empNumber value for this FormOrganization.
     * 
     * @return empNumber
     */
    public String getEmpNumber() {
        return empNumber;
    }


    /**
     * Sets the empNumber value for this FormOrganization.
     * 
     * @param empNumber
     */
    public void setEmpNumber(String empNumber) {
        this.empNumber = empNumber;
    }


    /**
     * Gets the name value for this FormOrganization.
     * 
     * @return name
     */
    public String getName() {
        return name;
    }


    /**
     * Sets the name value for this FormOrganization.
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Gets the number value for this FormOrganization.
     * 
     * @return number
     */
    public String getNumber() {
        return number;
    }


    /**
     * Sets the number value for this FormOrganization.
     * 
     * @param number
     */
    public void setNumber(String number) {
        this.number = number;
    }


    /**
     * Gets the numbers value for this FormOrganization.
     * 
     * @return numbers
     */
    public ItemNumber[] getNumbers() {
        return numbers;
    }


    /**
     * Sets the numbers value for this FormOrganization.
     * 
     * @param numbers
     */
    public void setNumbers(ItemNumber[] numbers) {
        this.numbers = numbers;
    }


    /**
     * Gets the phone value for this FormOrganization.
     * 
     * @return phone
     */
    public String getPhone() {
        return phone;
    }


    /**
     * Sets the phone value for this FormOrganization.
     * 
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }


    /**
     * Gets the valueAddRate value for this FormOrganization.
     * 
     * @return valueAddRate
     */
    public java.math.BigDecimal getValueAddRate() {
        return valueAddRate;
    }


    /**
     * Sets the valueAddRate value for this FormOrganization.
     * 
     * @param valueAddRate
     */
    public void setValueAddRate(java.math.BigDecimal valueAddRate) {
        this.valueAddRate = valueAddRate;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof FormOrganization)) return false;
        FormOrganization other = (FormOrganization) obj;
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
            ((this.contact==null && other.getContact()==null) || 
             (this.contact!=null &&
              this.contact.equals(other.getContact()))) &&
            ((this.empName==null && other.getEmpName()==null) || 
             (this.empName!=null &&
              this.empName.equals(other.getEmpName()))) &&
            ((this.empNumber==null && other.getEmpNumber()==null) || 
             (this.empNumber!=null &&
              this.empNumber.equals(other.getEmpNumber()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.number==null && other.getNumber()==null) || 
             (this.number!=null &&
              this.number.equals(other.getNumber()))) &&
            ((this.numbers==null && other.getNumbers()==null) || 
             (this.numbers!=null &&
              java.util.Arrays.equals(this.numbers, other.getNumbers()))) &&
            ((this.phone==null && other.getPhone()==null) || 
             (this.phone!=null &&
              this.phone.equals(other.getPhone()))) &&
            ((this.valueAddRate==null && other.getValueAddRate()==null) || 
             (this.valueAddRate!=null &&
              this.valueAddRate.equals(other.getValueAddRate())));
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
        if (getContact() != null) {
            _hashCode += getContact().hashCode();
        }
        if (getEmpName() != null) {
            _hashCode += getEmpName().hashCode();
        }
        if (getEmpNumber() != null) {
            _hashCode += getEmpNumber().hashCode();
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
        if (getPhone() != null) {
            _hashCode += getPhone().hashCode();
        }
        if (getValueAddRate() != null) {
            _hashCode += getValueAddRate().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FormOrganization.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "FormOrganization"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("address");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "Address"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contact");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "Contact"));
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("phone");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "Phone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("valueAddRate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ERPServer.Models", "ValueAddRate"));
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
