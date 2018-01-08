package com.lxzl.erp.core.service.assembleOrder.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.assembleOder.AssembleOrderQueryParam;
import com.lxzl.erp.common.domain.assembleOder.pojo.AssembleOrderMaterial;
import com.lxzl.erp.common.domain.assembleOder.pojo.AssembleOrder;
import com.lxzl.erp.common.domain.material.BulkMaterialQueryParam;
import com.lxzl.erp.common.domain.product.pojo.ProductMaterial;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.assembleOrder.AssembleOrderService;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.material.BulkMaterialService;
import com.lxzl.erp.core.service.material.impl.support.BulkMaterialSupport;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.warehouse.impl.support.WarehouseSupport;
import com.lxzl.erp.dataaccess.dao.mysql.assembleOder.AssembleOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.assembleOderMaterial.AssembleOrderMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.assembleOderMaterialEquipment.AssembleOrderProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.*;
import com.lxzl.erp.dataaccess.domain.assembleOder.AssembleOrderDO;
import com.lxzl.erp.dataaccess.domain.assembleOder.AssembleOrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.assembleOder.AssembleOrderProductEquipmentDO;
import com.lxzl.erp.dataaccess.domain.material.BulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.product.*;
import com.lxzl.erp.dataaccess.domain.warehouse.WarehouseDO;
import com.lxzl.se.dataaccess.mongo.config.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

/**
 * User : XiaoLuYu
 * Date : Created in ${Date}
 * Time : Created in ${Time}
 */
@Service
public class AssembleOrderServiceImpl implements AssembleOrderService {
    /**
     * 添加组装单
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> addAssembleOrder(AssembleOrder assembleOrder) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();

        ProductSkuDO productSkuDO = productSkuMapper.findById(assembleOrder.getAssembleProductSkuId());
        //校验skuId
        if (productSkuDO == null) {
            serviceResult.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
            return serviceResult;
        }
        //是否是用户可用仓库
        WarehouseDO warehouseDO = warehouseSupport.getAvailableWarehouse(assembleOrder.getWarehouseId());
        if (warehouseDO == null) {
            serviceResult.setErrorCode(ErrorCode.WAREHOUSE_NOT_EXISTS);
            return serviceResult;
        }

        List<AssembleOrderMaterial> assembleOrderMaterialList = assembleOrder.getAssembleOrderMaterialList();
        Date now = new Date();
        Map<Integer, AssembleOrderMaterial> assembleOrderMaterialMap = new HashMap<>();
        Map<Integer, LinkedList<BulkMaterialDO>> bulkMaterialDOMap = new HashMap<>();
        List<ProductMaterial> productMaterialList = new ArrayList<>();
        for (AssembleOrderMaterial assembleOrderMaterial : assembleOrderMaterialList) {
            //校验是否有足够数量的物料库存
            MaterialDO materialDO = materialMapper.findById(assembleOrderMaterial.getMaterialId());
            //校验物料是否存在
            if(materialDO == null){
                serviceResult.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
                return serviceResult;
            }
            assembleOrderMaterial.setMaterialNo(materialDO.getMaterialNo());
            //每一个物料可用数量
            int amount = assembleOrderMaterial.getMaterialCount() * assembleOrder.getAssembleProductCount();
            assembleOrderMaterialMap.put(assembleOrderMaterial.getMaterialId(), assembleOrderMaterial);
            if (amount > materialDO.getStock()) {
                serviceResult.setErrorCode(ErrorCode.BULK_MATERIAL_HAVE_NOT_ENOUGH_BY_PARAM, materialDO.getMaterialName());
                return serviceResult;
            }

            //准备校验所需数据（sku配件是否满足条件）
            ProductMaterial productMaterial = new ProductMaterial();
            productMaterial.setMaterialNo(materialDO.getMaterialNo());
            productMaterial.setMaterialCount(assembleOrderMaterial.getMaterialCount());
            productMaterialList.add(productMaterial);
            List<BulkMaterialDO> bulkMaterialDOList = bulkMaterialSupport.queryFitBulkMaterialDOList(assembleOrder.getWarehouseId(), assembleOrderMaterial.getMaterialId(), amount);
            //判断是否找到足够的物料
            if(bulkMaterialDOList.size()<assembleOrderMaterial.getMaterialCount()){
                serviceResult.setErrorCode(ErrorCode.BULK_MATERIAL_HAVE_NOT_ENOUGH_BY_PARAM, materialDO.getMaterialName());
                return serviceResult;
            }
            LinkedList<BulkMaterialDO> bulkMaterialDOLinkedList = new LinkedList<>(bulkMaterialDOList);
            bulkMaterialDOMap.put(assembleOrderMaterial.getMaterialId(), bulkMaterialDOLinkedList);
        }

        ProductSku productSku = new ProductSku();
        productSku.setSkuId(productSkuDO.getId());
        productSku.setProductMaterialList(productMaterialList);
        ServiceResult<String, Integer> verifyProductMaterialResult = productService.verifyProductMaterial(productSku);
        if (!ErrorCode.SUCCESS.equals(verifyProductMaterialResult.getErrorCode())) {
            serviceResult.setErrorCode(verifyProductMaterialResult.getErrorCode(), verifyProductMaterialResult.getFormatArgs());
            return serviceResult;
        }

        Integer assembleOrderId = null;
        //保存组装单
        AssembleOrderDO assembleOrderDO = ConverterUtil.convert(assembleOrder, AssembleOrderDO.class);
        //生成组装单编号
        assembleOrderDO.setAssembleOrderNo(generateNoSupport.generateAssemblerOderNo(now, assembleOrder.getWarehouseId()));
        assembleOrderDO.setAssembleProductId(productSkuDO.getProductId());
        assembleOrderDO.setAssembleOrderStatus(AssembleOrderStatus.INIT);
        assembleOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        assembleOrderDO.setCreateTime(now);
        assembleOrderDO.setCreateUser(userSupport.getCurrentUserId().toString());
        assembleOrderDO.setUpdateTime(now);
        assembleOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        assembleOrderMapper.save(assembleOrderDO);

        for (Integer key : assembleOrderMaterialMap.keySet()) {
            AssembleOrderMaterial assembleOrderMaterial = assembleOrderMaterialMap.get(key);
            //保存组装单配件
            AssembleOrderMaterialDO assembleOrderMaterialDO = new AssembleOrderMaterialDO();
            assembleOrderMaterialDO.setAssembleOrderId(assembleOrderDO.getId());
            assembleOrderMaterialDO.setMaterialId(assembleOrderMaterial.getMaterialId());
            assembleOrderMaterialDO.setMaterialNo(assembleOrderMaterial.getMaterialNo());
            assembleOrderMaterialDO.setMaterialCount(assembleOrderMaterial.getMaterialCount());
            assembleOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            assembleOrderMaterialDO.setCreateTime(now);
            assembleOrderMaterialDO.setCreateUser(userSupport.getCurrentUserId().toString());
            assembleOrderMaterialDO.setUpdateTime(now);
            assembleOrderMaterialDO.setCreateUser(userSupport.getCurrentUserId().toString());
            assembleOrderMaterialMapper.save(assembleOrderMaterialDO);
        }

        //生成设备
        for (int i = 0; i < assembleOrder.getAssembleProductCount(); i++) {
            //保存商品设备
            ProductEquipmentDO productEquipmentDO = new ProductEquipmentDO();
            String productEquipmentNo = generateNoSupport.generateProductEquipmentNo(now, assembleOrder.getWarehouseId());
            productEquipmentDO.setEquipmentNo(productEquipmentNo);
            productEquipmentDO.setProductId(productSkuDO.getProductId());
            productEquipmentDO.setSkuId(assembleOrder.getAssembleProductSkuId());
            productEquipmentDO.setCurrentWarehouseId(assembleOrder.getWarehouseId());
            productEquipmentDO.setOwnerWarehouseId(assembleOrder.getWarehouseId());
            productEquipmentDO.setEquipmentPrice(productSkuDO.getSkuPrice());
            productEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_IDLE);
            productEquipmentDO.setIsNew(CommonConstant.COMMON_CONSTANT_NO);
            productEquipmentDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            productEquipmentDO.setCreateTime(now);
            productEquipmentDO.setCreateUser(userSupport.getCurrentUserId().toString());
            productEquipmentDO.setUpdateTime(now);
            productEquipmentDO.setCreateUser(userSupport.getCurrentUserId().toString());
            productEquipmentMapper.save(productEquipmentDO);

            //遍历物料
            for (AssembleOrderMaterial assembleOrderMaterial : assembleOrderMaterialList) {
                ProductEquipmentMaterialDO productEquipmentMaterialDO = new ProductEquipmentMaterialDO();
                productEquipmentMaterialDO.setEquipmentId(productEquipmentDO.getId());
                productEquipmentMaterialDO.setEquipmentNo(productEquipmentDO.getEquipmentNo());
                productEquipmentMaterialDO.setMaterialCount(0);
                productEquipmentMaterialDO.setMaterialId(assembleOrderMaterial.getMaterialId());
                productEquipmentMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                productEquipmentMaterialDO.setCreateTime(now);
                productEquipmentMaterialDO.setCreateUser(userSupport.getCurrentUserId().toString());
                productEquipmentMaterialDO.setUpdateTime(now);
                productEquipmentMaterialDO.setCreateUser(userSupport.getCurrentUserId().toString());
                productEquipmentMaterialMapper.save(productEquipmentMaterialDO);
                LinkedList<BulkMaterialDO> bulkMaterialDOLinkedList = bulkMaterialDOMap.get(assembleOrderMaterial.getMaterialId());
                for (int j = 0; j < assembleOrderMaterial.getMaterialCount(); j++) {
                    BulkMaterialDO bulkMaterialDO = bulkMaterialDOLinkedList.getLast();
                    //安装
                    BulkMaterialQueryParam bulkMaterialQueryParam = new BulkMaterialQueryParam();
                    bulkMaterialQueryParam.setBulkMaterialId(bulkMaterialDO.getId());
                    bulkMaterialQueryParam.setCurrentEquipmentNo(productEquipmentDO.getEquipmentNo());
                    ServiceResult<String, Integer> returnServiceResult = bulkMaterialService.installBulkMaterial(bulkMaterialQueryParam);
                    if (!returnServiceResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        // 返回错误信息
                        return returnServiceResult;
                    }
                    bulkMaterialDOLinkedList.removeLast();
                }

            }

            //保存组装单组装成功设备
            AssembleOrderProductEquipmentDO assembleOrderProductEquipmentDO = new AssembleOrderProductEquipmentDO();
            assembleOrderProductEquipmentDO.setAssembleOrderId(assembleOrderDO.getId());
            assembleOrderProductEquipmentDO.setProductEquipmentNo(productEquipmentNo);
            assembleOrderProductEquipmentDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            assembleOrderProductEquipmentDO.setCreateTime(now);
            assembleOrderProductEquipmentDO.setCreateUser(userSupport.getCurrentUserId().toString());
            assembleOrderProductEquipmentDO.setUpdateTime(now);
            assembleOrderProductEquipmentDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            assembleOrderProductEquipmentMapper.save(assembleOrderProductEquipmentDO);
            assembleOrderId = assembleOrderDO.getId();
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(assembleOrderId);
        return serviceResult;
    }


    /**
     * 查询组装单详情
     */
    @Override
    public ServiceResult<String, AssembleOrder> queryAssembleOrderByAssembleOrderId(Integer assembleOrderId) {
        ServiceResult<String, AssembleOrder> serviceResult = new ServiceResult<>();
        AssembleOrderDO assembleOrderDO = assembleOrderMapper.findDetailByAssembleOrderId(assembleOrderId);
        if (assembleOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.ASSEMBLE_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        List<AssembleOrderMaterialDO> assembleOrderMaterialDOList = assembleOrderDO.getAssembleOrderMaterialDOList();
        if (CollectionUtil.isNotEmpty(assembleOrderMaterialDOList)) {
            for (AssembleOrderMaterialDO assembleOrderMaterialDO : assembleOrderMaterialDOList) {
                MaterialDO materialDO = materialMapper.findById(assembleOrderMaterialDO.getMaterialId());
                assembleOrderMaterialDO.setMaterialDO(materialDO);
            }
        }
        AssembleOrder assembleOrder = ConverterUtil.convert(assembleOrderDO, AssembleOrder.class);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(assembleOrder);
        return serviceResult;
    }

    /**
     * 分页查询组装单
     */
    @Override
    public ServiceResult<String, Page<AssembleOrder>> pageAssembleOrder(AssembleOrderQueryParam assembleOrderQueryParam) {
        ServiceResult<String, Page<AssembleOrder>> serviceResult = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(assembleOrderQueryParam.getPageNo(), assembleOrderQueryParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("queryParam", assembleOrderQueryParam);
        Integer assembleOrderCount = assembleOrderMapper.listCount(maps);
        List<AssembleOrderDO> assembleOrderDOList = assembleOrderMapper.findAssembleOrderByParams(maps);
        List<AssembleOrder> assembleOrderList = ConverterUtil.convertList(assembleOrderDOList, AssembleOrder.class);
        Page<AssembleOrder> page = new Page<>(assembleOrderList, assembleOrderCount, assembleOrderQueryParam.getPageNo(), assembleOrderQueryParam.getPageSize());
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(page);
        return serviceResult;
    }


    @Autowired
    private ProductSkuMapper productSkuMapper;
    @Autowired
    private AssembleOrderMapper assembleOrderMapper;
    @Autowired
    private AssembleOrderMaterialMapper assembleOrderMaterialMapper;
    @Autowired
    private UserSupport userSupport;
    @Autowired
    private ProductEquipmentMapper productEquipmentMapper;
    @Autowired
    private GenerateNoSupport generateNoSupport;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private WarehouseSupport warehouseSupport;
    @Autowired
    private ProductService productService;
    @Autowired
    private BulkMaterialService bulkMaterialService;
    @Autowired
    private AssembleOrderProductEquipmentMapper assembleOrderProductEquipmentMapper;
    @Autowired
    private BulkMaterialSupport bulkMaterialSupport;
    @Autowired
    private ProductEquipmentMaterialMapper productEquipmentMaterialMapper;
}
