package com.lxzl.erp.core.service.area;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.area.AreaProvince;

import java.util.List;

public interface AreaService {
    ServiceResult<String,  List<AreaProvince>> getAreaList();

    /**
     * 获取邮政编码
     * */
    ServiceResult<String, List<String>> selectPostCodeAndSavaData2postCode();
    /**
     * 中英文简写
     * */
    ServiceResult<String,Object> conversionType();
    /**
     * 查询并添加数据到erp_area_district
     * */
    ServiceResult<String, Object> saveCode2erpAreaDistrict();
}
