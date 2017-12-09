package com.lxzl.erp.core.service.basic;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.basic.BrandQueryParam;
import com.lxzl.erp.common.domain.basic.pojo.Brand;
import com.lxzl.se.core.service.BaseService;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-09 12:57
 */
public interface BasicService extends BaseService {

    /**
     * 查询所有品牌
     *
     * @param param 查询参数
     * @return 品牌分页列表
     */
    ServiceResult<String, Page<Brand>> queryAllBrand(BrandQueryParam param);
}
