package com.lxzl.erp.core.service.material.impl.support;

import com.lxzl.erp.common.constant.BulkMaterialStatus;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.domain.material.BulkMaterialQueryParam;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.core.service.warehouse.impl.support.WarehouseSupport;
import com.lxzl.erp.dataaccess.dao.mysql.material.BulkMaterialMapper;
import com.lxzl.erp.dataaccess.domain.material.BulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.warehouse.WarehouseDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-14 17:49
 */
@Component
public class BulkMaterialSupport {

    @Autowired
    private BulkMaterialMapper bulkMaterialMapper;

    public List<BulkMaterialDO> queryFitBulkMaterialDOList(Integer warehouseId, Integer materialId, Integer materialCount) {
        BulkMaterialQueryParam bulkMaterialQueryParam = new BulkMaterialQueryParam();
        bulkMaterialQueryParam.setMaterialId(materialId);
        bulkMaterialQueryParam.setCurrentWarehouseId(warehouseId);
        bulkMaterialQueryParam.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE);
        bulkMaterialQueryParam.setIsOnEquipment(CommonConstant.COMMON_CONSTANT_NO);

        Map<String, Object> bulkQueryParam = new HashMap<>();
        bulkQueryParam.put("start", 0);
        bulkQueryParam.put("pageSize", materialCount);
        bulkQueryParam.put("bulkMaterialQueryParam", bulkMaterialQueryParam);
        return bulkMaterialMapper.listPage(bulkQueryParam);
    }

    public BulkMaterialDO queryFitBulkMaterialDO(Integer warehouseId, Integer materialId) {
        BulkMaterialQueryParam bulkMaterialQueryParam = new BulkMaterialQueryParam();
        bulkMaterialQueryParam.setMaterialId(materialId);
        bulkMaterialQueryParam.setCurrentWarehouseId(warehouseId);
        bulkMaterialQueryParam.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE);
        bulkMaterialQueryParam.setIsOnEquipment(CommonConstant.COMMON_CONSTANT_NO);

        Map<String, Object> bulkQueryParam = new HashMap<>();
        bulkQueryParam.put("start", 0);
        bulkQueryParam.put("pageSize", 1);
        bulkQueryParam.put("bulkMaterialQueryParam", bulkMaterialQueryParam);
        List<BulkMaterialDO> bulkMaterialDOList = bulkMaterialMapper.listPage(bulkQueryParam);
        return CollectionUtil.isNotEmpty(bulkMaterialDOList) ? bulkMaterialDOList.get(0) : null;
    }

    public List<BulkMaterialDO> queryBusyBulkMaterialDOList(String orderNo, Integer materialId, Integer materialCount) {
        BulkMaterialQueryParam bulkMaterialQueryParam = new BulkMaterialQueryParam();
        bulkMaterialQueryParam.setMaterialId(materialId);
        bulkMaterialQueryParam.setOrderNo(orderNo);
        bulkMaterialQueryParam.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_BUSY);
        bulkMaterialQueryParam.setIsOnEquipment(CommonConstant.COMMON_CONSTANT_NO);

        Map<String, Object> bulkQueryParam = new HashMap<>();
        bulkQueryParam.put("start", 0);
        bulkQueryParam.put("pageSize", materialCount);
        bulkQueryParam.put("bulkMaterialQueryParam", bulkMaterialQueryParam);
        return bulkMaterialMapper.listPage(bulkQueryParam);
    }

}
