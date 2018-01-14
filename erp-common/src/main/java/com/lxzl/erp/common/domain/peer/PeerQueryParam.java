package com.lxzl.erp.common.domain.peer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/13
 * @Time : Created in 15:01
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PeerQueryParam extends BasePageParam implements Serializable {
    private String peerName;  //供应商名称
    private String peerCode;  //供应商自定义编码
    private String peerNo;  //供应商编码
    private Integer provinceId;  //省份ID
    private Integer cityId;  //城市ID
    private Integer districtId;  //区ID
    private Date createStartTime;  //起始时间
    private Date createEndTime;  //结束时间

    public Date getCreateStartTime() {
        return createStartTime;
    }

    public void setCreateStartTime(Date createStartTime) {
        this.createStartTime = createStartTime;
    }

    public Date getCreateEndTime() {
        return createEndTime;
    }

    public void setCreateEndTime(Date createEndTime) {
        this.createEndTime = createEndTime;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public String getPeerNo() {
        return peerNo;
    }

    public void setPeerNo(String peerNo) {
        this.peerNo = peerNo;
    }

    public String getPeerName() {
        return peerName;
    }

    public void setPeerName(String peerName) {
        this.peerName = peerName;
    }

    public String getPeerCode() {
        return peerCode;
    }

    public void setPeerCode(String peerCode) {
        this.peerCode = peerCode;
    }
}
