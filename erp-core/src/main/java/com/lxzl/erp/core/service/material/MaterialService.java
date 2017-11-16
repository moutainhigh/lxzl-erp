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
     * 添加物料
     *
     * @param material 物料信息
     * @return 物料编码
     */
    ServiceResult<String, String> addMaterial(Material material);

    /**
     * 修改物料
     *
     * @param material 物料信息
     * @return 物料编码
     */
    ServiceResult<String, String> updateMaterial(Material material);

    /**
     * 查询所有物料
     *
     * @param materialQueryParam 查询物料参数
     * @return 物料集合
     */
    ServiceResult<String, Page<Material>> queryAllMaterial(MaterialQueryParam materialQueryParam);

    /**
     * 查询所有散料
     *
     * @param bulkMaterialQueryParam 查询散料参数
     * @return 散料集合
     */
    ServiceResult<String, Page<BulkMaterial>> queryAllBulkMaterial(BulkMaterialQueryParam bulkMaterialQueryParam);

    /**
     * 根据物料信息查询散料
     *
     * @param bulkMaterialQueryParam 查询散料参数
     * @return 散料集合
     */
    ServiceResult<String, Page<BulkMaterial>> queryBulkMaterialByMaterialId(BulkMaterialQueryParam bulkMaterialQueryParam);
}
