package com.lxzl.erp.core.service.warehouse.impl.support;

import com.lxzl.erp.common.domain.warehouse.pojo.StockOrder;
import com.lxzl.erp.common.domain.warehouse.pojo.StockOrderBulkMaterial;
import com.lxzl.erp.common.domain.warehouse.pojo.StockOrderEquipment;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.dataaccess.domain.warehouse.StockOrderBulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.warehouse.StockOrderDO;
import com.lxzl.erp.dataaccess.domain.warehouse.StockOrderEquipmentDO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: 出入库单转换器
 *
 * @author gaochao
 * @date 2017-11-20 17:45
 */
public class StockOrderConverter {
    public static List<StockOrder> convertStockOrderDOList(List<StockOrderDO> stockOrderDOList) {
        List<StockOrder> stockOrderList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(stockOrderDOList)) {
            for (StockOrderDO stockOrderDO : stockOrderDOList) {
                stockOrderList.add(convertStockOrderDO(stockOrderDO));
            }
        }
        return stockOrderList;
    }

    public static StockOrder convertStockOrderDO(StockOrderDO stockOrderDO) {
        StockOrder stockOrder = new StockOrder();
        if (stockOrderDO.getId() != null) {
            stockOrder.setStockOrderId(stockOrderDO.getId());
        }
        BeanUtils.copyProperties(stockOrderDO, stockOrder);
        stockOrder.setStockOrderEquipmentList(convertStockOrderEquipmentDOList(stockOrderDO.getStockOrderEquipmentDOList()));
        stockOrder.setStockOrderBulkMaterialList(convertStockOrderBulkMaterialDOList(stockOrderDO.getStockOrderBulkMaterialDOList()));
        return stockOrder;
    }

    public static List<StockOrderEquipment> convertStockOrderEquipmentDOList(List<StockOrderEquipmentDO> stockOrderEquipmentDOList) {
        List<StockOrderEquipment> stockOrderEquipmentList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(stockOrderEquipmentDOList)) {
            for (StockOrderEquipmentDO stockOrderEquipmentDO : stockOrderEquipmentDOList) {
                stockOrderEquipmentList.add(convertStockOrderEquipmentDO(stockOrderEquipmentDO));
            }
        }
        return stockOrderEquipmentList;
    }

    public static StockOrderEquipment convertStockOrderEquipmentDO(StockOrderEquipmentDO stockOrderEquipmentDO) {
        StockOrderEquipment stockOrderEquipment = new StockOrderEquipment();
        if (stockOrderEquipmentDO.getId() != null) {
            stockOrderEquipment.setStockOrderEquipmentId(stockOrderEquipmentDO.getId());
        }
        BeanUtils.copyProperties(stockOrderEquipmentDO, stockOrderEquipment);
        return stockOrderEquipment;
    }

    public static List<StockOrderBulkMaterial> convertStockOrderBulkMaterialDOList(List<StockOrderBulkMaterialDO> stockOrderBulkMaterialDOList) {
        List<StockOrderBulkMaterial> stockOrderBulkMaterialList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(stockOrderBulkMaterialDOList)) {
            for (StockOrderBulkMaterialDO stockOrderBulkMaterialDO : stockOrderBulkMaterialDOList) {
                stockOrderBulkMaterialList.add(convertStockOrderBulkMaterialDO(stockOrderBulkMaterialDO));
            }
        }
        return stockOrderBulkMaterialList;
    }

    public static StockOrderBulkMaterial convertStockOrderBulkMaterialDO(StockOrderBulkMaterialDO stockOrderBulkMaterialDO) {
        StockOrderBulkMaterial stockOrderBulkMaterial = new StockOrderBulkMaterial();
        if (stockOrderBulkMaterialDO.getId() != null) {
            stockOrderBulkMaterial.setStockOrderBulkMaterialId(stockOrderBulkMaterialDO.getId());
        }
        BeanUtils.copyProperties(stockOrderBulkMaterialDO, stockOrderBulkMaterial);
        return stockOrderBulkMaterial;
    }
}
