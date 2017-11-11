package com.lxzl.erp.core.service.product.impl.support;

import com.lxzl.erp.common.domain.material.pojo.BulkMaterial;
import com.lxzl.erp.dataaccess.domain.material.BulkMaterialDO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-11 11:12
 */
public class MaterialConverter {

    public static List<BulkMaterial> convertProductBulkMaterialDOList(List<BulkMaterialDO> bulkMaterialDOList) {
        List<BulkMaterial> bulkMaterialList = new ArrayList<>();
        if (bulkMaterialDOList != null && !bulkMaterialDOList.isEmpty()) {
            for (BulkMaterialDO bulkMaterialDO : bulkMaterialDOList) {
                bulkMaterialList.add(convertProductBulkMaterialDO(bulkMaterialDO));
            }
        }
        return bulkMaterialList;
    }

    public static BulkMaterial convertProductBulkMaterialDO(BulkMaterialDO bulkMaterialDO) {
        BulkMaterial bulkMaterial = new BulkMaterial();
        if (bulkMaterialDO.getId() != null) {
            bulkMaterial.setBulkMaterialId(bulkMaterialDO.getId());
        }
        BeanUtils.copyProperties(bulkMaterialDO, bulkMaterial);
        return bulkMaterial;
    }
}
