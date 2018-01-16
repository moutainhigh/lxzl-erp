package com.lxzl.erp.common.domain.k3;

import java.util.List;

public class K3Customer {

    public String number;
    public String name;
    public String address;
    public String empNumber;
    public String empName;
    public String phone;
    public Integer valueAddRate;
    public String contact;
    public List<K3Number> numbers;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmpNumber() {
        return empNumber;
    }

    public void setEmpNumber(String empNumber) {
        this.empNumber = empNumber;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getValueAddRate() {
        return valueAddRate;
    }

    public void setValueAddRate(Integer valueAddRate) {
        this.valueAddRate = valueAddRate;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public List<K3Number> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<K3Number> numbers) {
        this.numbers = numbers;
    }
}
