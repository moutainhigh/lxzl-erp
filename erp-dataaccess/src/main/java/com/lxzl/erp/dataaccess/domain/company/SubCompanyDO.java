package com.lxzl.erp.dataaccess.domain.company;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;

import java.util.List;

public class SubCompanyDO extends BaseDO{

    private Integer id;
    private String subCompanyName;
    private Integer subCompanyType;
    private Integer province;
    private Integer city;
    private Integer district;
    private Integer dataOrder;
    private Integer dataStatus;
    private String remark;

    private String provinceName;
    private String cityName;
    private String districtName;

    private List<DepartmentDO> departmentDOList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubCompanyName() {
        return subCompanyName;
    }

    public void setSubCompanyName(String subCompanyName) {
        this.subCompanyName = subCompanyName;
    }

    public Integer getProvince() {
        return province;
    }

    public void setProvince(Integer province) {
        this.province = province;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public Integer getDistrict() {
        return district;
    }

    public void setDistrict(Integer district) {
        this.district = district;
    }

    public Integer getDataOrder() {
        return dataOrder;
    }

    public void setDataOrder(Integer dataOrder) {
        this.dataOrder = dataOrder;
    }

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<DepartmentDO> getDepartmentDOList() {
        return departmentDOList;
    }

    public void setDepartmentDOList(List<DepartmentDO> departmentDOList) {
        this.departmentDOList = departmentDOList;
    }

    public Integer getSubCompanyType() {
        return subCompanyType;
    }

    public void setSubCompanyType(Integer subCompanyType) {
        this.subCompanyType = subCompanyType;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }
}