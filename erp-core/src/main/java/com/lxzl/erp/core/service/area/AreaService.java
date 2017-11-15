package com.lxzl.erp.core.service.area;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.area.AreaProvince;

import java.util.List;

public interface AreaService {
    ServiceResult<String,  List<AreaProvince>> getAreaList();
}
