package com.lxzl.erp.common.domain.base;

import com.alibaba.fastjson.annotation.JSONField;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;

import java.io.Serializable;
import java.util.List;

/**
 * User : LiuKe
 * Date : 2017/1/13
 * Time : 12:35
 */
public class BasePageParam extends PageQuery implements Serializable {

    //控制可查看用户的数据权限
    private List<Integer> dataAccessUserIdList;
    //控制可查看仓库的数据权限
    private List<Integer> dataAccessWarehouseIdList;
    //控制可查看泵分公司的数据权限
    private Integer dataAccessSubCompanyId;

    public List<Integer> getDataAccessUserIdList() {
        return dataAccessUserIdList;
    }

    public void setDataAccessUserIdList(List<Integer> dataAccessUserIdList) {
        this.dataAccessUserIdList = dataAccessUserIdList;
    }

    public List<Integer> getDataAccessWarehouseIdList() {
        return dataAccessWarehouseIdList;
    }

    public void setDataAccessWarehouseIdList(List<Integer> dataAccessWarehouseIdList) {
        this.dataAccessWarehouseIdList = dataAccessWarehouseIdList;
    }

    public Integer getDataAccessSubCompanyId() {
        return dataAccessSubCompanyId;
    }

    public void setDataAccessSubCompanyId(Integer dataAccessSubCompanyId) {
        this.dataAccessSubCompanyId = dataAccessSubCompanyId;
    }

    @JSONField(serialize = false)
    private int count;
    public BasePageParam(){
        if(getPageSize()==0){
            setPageSize(10);
        }
        if(getPageNo()==0){
            setPageNo(1);
        }
    }
}
