package com.lxzl.erp.core.service.material;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.BulkMaterialQueryParam;
import com.lxzl.erp.common.domain.material.pojo.BulkMaterial;

public interface BulkMaterialService {


    /**
     * 拆卸散料
     *
     * @param bulkMaterial 散料ID
     * @return
     */
    ServiceResult<String,Integer> dismantleBulkMaterial(BulkMaterial bulkMaterial);

    /**
     *安装散料
     *
     * @param bulkMaterialQueryParam 散料ID 和需要安装散料的设备编号
     * @return
     */
    ServiceResult<String,Integer> installBulkMaterial(BulkMaterialQueryParam bulkMaterialQueryParam);

    /**
     * 换货拆卸和安装
     *
     * @param dismantleBulkMaterialId 拆卸的散料ID
     * @param installBulkMaterialId 安装的散料ID
     * @return
     */
    ServiceResult<String,Integer> changeProductDismantleAndInstall(Integer dismantleBulkMaterialId,Integer installBulkMaterialId);

}
