package com.lxzl.erp.core.service.material.impl.support;

import com.lxzl.erp.common.domain.material.pojo.BulkMaterial;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.material.pojo.MaterialModel;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.dataaccess.domain.material.BulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialModelDO;
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

    public static MaterialModel convertMaterialModelDO(MaterialModelDO materialModelDO) {
        MaterialModel materialModel = new MaterialModel();
        if (materialModelDO.getId() != null) {
            materialModel.setMaterialModelId(materialModelDO.getId());
        }
        BeanUtils.copyProperties(materialModelDO, materialModel);
        return materialModel;
    }

    public static MaterialModelDO convertMaterialModel(MaterialModel materialModel) {
        MaterialModelDO materialModelDO = new MaterialModelDO();
        if (materialModel.getMaterialModelId() != null) {
            materialModelDO.setId(materialModel.getMaterialModelId());
        }
        BeanUtils.copyProperties(materialModel, materialModelDO);
        return materialModelDO;
    }
}
