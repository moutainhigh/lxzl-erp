package com.lxzl.erp.core.service.material.impl.support;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-23 17:36
 */
@Component
public class MaterialSupport {
    @Autowired
    private MaterialMapper materialMapper;

    public String operateMaterialStock(Integer materialId, Integer stock) {
        Date currentTime = new Date();
        MaterialDO materialDO = materialMapper.findById(materialId);
        if (materialDO == null) {
            return ErrorCode.MATERIAL_NOT_EXISTS;
        }
        if (materialDO.getStock() < stock) {
            return ErrorCode.STOCK_NOT_ENOUGH;
        }
        materialDO.setStock(materialDO.getStock() - stock);
        materialDO.setUpdateTime(currentTime);
        materialMapper.update(materialDO);
        return ErrorCode.SUCCESS;
    }
}
