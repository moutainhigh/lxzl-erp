package com.lxzl.erp.core.service.warehouse.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.ProductEquipmentStatus;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.product.ProductEquipmentQueryParam;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.domain.warehouse.WarehouseQueryParam;
import com.lxzl.erp.common.domain.warehouse.pojo.Warehouse;
import com.lxzl.erp.common.util.GenerateNoUtil;
import com.lxzl.erp.core.service.warehouse.WarehouseService;
import com.lxzl.erp.core.service.warehouse.impl.support.WarehouseConverter;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.warehouse.WarehouseMapper;
import com.lxzl.erp.dataaccess.dao.mysql.warehouse.WarehousePositionMapper;
import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentDO;
import com.lxzl.erp.dataaccess.domain.warehouse.WarehouseDO;
import com.lxzl.se.common.util.date.DateUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-06 17:48
 */
@Service("warehouseService")
public class WarehouseServiceImpl implements WarehouseService {
    @Autowired
    private WarehouseMapper warehouseMapper;

    @Autowired
    private WarehousePositionMapper warehousePositionMapper;

    @Autowired
    private HttpSession session;

    @Autowired
    private ProductEquipmentMapper productEquipmentMapper;

    @Override
    public ServiceResult<String, Page<Warehouse>> getWarehousePage(WarehouseQueryParam param) {
        ServiceResult<String, Page<Warehouse>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(param.getPageNo(), param.getPageSize());
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", pageQuery.getStart());
        paramMap.put("pageSize", pageQuery.getPageSize());
        paramMap.put("warehouseQueryParam", param);
        Integer dataCount = warehouseMapper.listCount(paramMap);
        List<WarehouseDO> dataList = warehouseMapper.listPage(paramMap);
        Page<Warehouse> page = new Page<>(WarehouseConverter.convertWarehouseDOList(dataList), dataCount, param.getPageNo(), param.getPageSize());

        result.setResult(page);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, List<Warehouse>> getWarehouseByCompany(Integer subCompanyId) {
        ServiceResult<String, List<Warehouse>> result = new ServiceResult<>();
        WarehouseQueryParam param = new WarehouseQueryParam();
        param.setSubCompanyId(subCompanyId);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", 0);
        paramMap.put("pageSize", Integer.MAX_VALUE);
        paramMap.put("warehouseQueryParam", param);
        List<WarehouseDO> dataList = warehouseMapper.listPage(paramMap);
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(WarehouseConverter.convertWarehouseDOList(dataList));
        return result;
    }

    @Override
    public ServiceResult<String, Warehouse> getWarehouseById(Integer warehouseId) {
        ServiceResult<String, Warehouse> result = new ServiceResult<>();
        WarehouseDO warehouseDO = warehouseMapper.findById(warehouseId);
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(WarehouseConverter.convertWarehouseDO(warehouseDO));
        return result;
    }

    @Override
    public ServiceResult<String, Integer> productOutStock(List<ProductSku> productSkuList, Integer warehouseId, Integer productCount) {
        ServiceResult<String, Integer> result = new ServiceResult<>();

        return result;
    }

    @Override
    public ServiceResult<String, Integer> productInStock(List<ProductSku> productSkuList, Integer warehouseId, Integer productCount) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        String errorCode = verifyProductInfo(productSkuList);
        if (!ErrorCode.SUCCESS.equals(errorCode)) {
            result.setErrorCode(errorCode);
            return result;
        }
        WarehouseDO warehouseDO = warehouseMapper.findById(warehouseId);
        if (warehouseDO == null) {
            result.setErrorCode(ErrorCode.WAREHOUSE_NOT_EXISTS);
        }


        return result;
    }

    public String verifyProductInfo(List<ProductSku> productSkuList) {

        return ErrorCode.SUCCESS;
    }


    void saveProductEquipment(Integer warehouseId, Integer productId, Integer productSkuId, Integer productCount, Date currentTime) {
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        ProductEquipmentQueryParam param = new ProductEquipmentQueryParam();
        param.setCreateStartTime(DateUtil.getBeginOfDay(currentTime));
        param.setCreateEndTime(DateUtil.getEndOfDay(currentTime));
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", 1);
        maps.put("pageSize", Integer.MAX_VALUE);
        maps.put("productEquipmentQueryParam", param);
        Integer oldCount = productEquipmentMapper.findProductEquipmentCountByParams(maps);
        oldCount = oldCount == null ? 0 : oldCount;

        for (int i = 0; i < productCount; i++) {
            ProductEquipmentDO productEquipmentDO = new ProductEquipmentDO();
            productEquipmentDO.setEquipmentNo(GenerateNoUtil.generateEquipmentNo(currentTime, warehouseId, (oldCount + i + 1)));
            productEquipmentDO.setProductId(productId);
            productEquipmentDO.setSkuId(productSkuId);
            productEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_IDLE);
            productEquipmentDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            productEquipmentDO.setUpdateUser(loginUser.getUserId().toString());
            productEquipmentDO.setCreateUser(loginUser.getUserId().toString());
            productEquipmentDO.setUpdateTime(currentTime);
            productEquipmentDO.setCreateTime(currentTime);
            productEquipmentMapper.save(productEquipmentDO);
        }
    }
}
