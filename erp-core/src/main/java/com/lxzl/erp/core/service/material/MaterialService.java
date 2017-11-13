package com.lxzl.erp.core.service.material;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.BulkMaterialQueryParam;
import com.lxzl.erp.common.domain.material.MaterialQueryParam;
import com.lxzl.erp.common.domain.material.pojo.BulkMaterial;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.se.core.service.BaseService;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-13 13:43
 */
public interface MaterialService extends BaseService {

    /**
     * @param materialQueryParam 查询物料参数
     * @return  物料集合
     */
    ServiceResult<String, Page<Material>> queryAllMaterial(MaterialQueryParam materialQueryParam);

    /**
     * @param bulkMaterialQueryParam 查询散料参数
     * @return 散料集合
     */
    ServiceResult<String, Page<BulkMaterial>> queryAllBulkMaterial(BulkMaterialQueryParam bulkMaterialQueryParam);

    /**
     * @param bulkMaterialQueryParam 查询散料参数
     * @return 散料集合
     */
    ServiceResult<String, Page<BulkMaterial>> queryBulkMaterialByMaterialId(BulkMaterialQueryParam bulkMaterialQueryParam);
}
