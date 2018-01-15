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
