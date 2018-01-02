package com.lxzl.erp.core.service.purchase.impl;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.company.pojo.SubCompany;
import com.lxzl.erp.common.domain.material.pojo.BulkMaterial;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.material.pojo.MaterialInStorage;
import com.lxzl.erp.common.domain.product.pojo.*;
import com.lxzl.erp.common.domain.purchase.*;
import com.lxzl.erp.common.domain.purchase.pojo.*;
import com.lxzl.erp.common.domain.warehouse.ProductInStockParam;
import com.lxzl.erp.common.domain.warehouse.pojo.Warehouse;
import com.lxzl.erp.common.domain.workflow.pojo.WorkflowLink;
import com.lxzl.erp.common.util.*;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.company.CompanyService;
import com.lxzl.erp.core.service.material.MaterialService;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.purchase.PurchaseOrderService;
import com.lxzl.erp.core.service.purchase.impl.support.PurchaseOrderSupport;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.warehouse.WarehouseService;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.material.BulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.dao.mysql.purchase.*;
import com.lxzl.erp.dataaccess.dao.mysql.supplier.SupplierMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.RoleUserFinalMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.dao.mysql.warehouse.StockOrderBulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.warehouse.StockOrderEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.warehouse.StockOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.warehouse.WarehouseMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.WorkflowLinkMapper;
import com.lxzl.erp.dataaccess.domain.material.BulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentDO;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import com.lxzl.erp.dataaccess.domain.purchase.*;
import com.lxzl.erp.dataaccess.domain.supplier.SupplierDO;
import com.lxzl.erp.dataaccess.domain.user.RoleUserFinalDO;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
import com.lxzl.erp.dataaccess.domain.warehouse.StockOrderBulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.warehouse.StockOrderEquipmentDO;
import com.lxzl.erp.dataaccess.domain.warehouse.WarehouseDO;
import com.lxzl.erp.dataaccess.domain.workflow.WorkflowLinkDO;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.*;


@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderServiceImpl.class);
    @Autowired
    private UserSupport userSupport;
    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;
    @Autowired
    private ProductSkuMapper productSkuMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private PurchaseOrderProductMapper purchaseOrderProductMapper;
    @Autowired
    private PurchaseDeliveryOrderMapper purchaseDeliveryOrderMapper;
    @Autowired
    private PurchaseDeliveryOrderProductMapper purchaseDeliveryOrderProductMapper;
    @Autowired
    private PurchaseReceiveOrderMapper purchaseReceiveOrderMapper;
    @Autowired
    private PurchaseReceiveOrderProductMapper purchaseReceiveOrderProductMapper;
    @Autowired
    private PurchaseReceiveOrderMaterialMapper purchaseReceiveOrderMaterialMapper;
    @Autowired
    private PurchaseOrderSupport purchaseOrderSupport;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private WarehouseMapper warehouseMapper;
    @Autowired
    private SupplierMapper supplierMapper;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private PurchaseOrderMaterialMapper purchaseOrderMaterialMapper;
    @Autowired
    private PurchaseDeliveryOrderMaterialMapper purchaseDeliveryOrderMaterialMapper;
    @Autowired
    private WorkflowLinkMapper workflowLinkMapper;
    @Autowired
    private StockOrderMapper stockOrderMapper;
    @Autowired
    private StockOrderEquipmentMapper stockOrderEquipmentMapper;
    @Autowired
    private StockOrderBulkMaterialMapper stockOrderBulkMaterialMapper;
    @Autowired
    private ProductEquipmentMapper productEquipmentMapper;
    @Autowired
    private BulkMaterialMapper bulkMaterialMapper;
    @Autowired
    private GenerateNoSupport generateNoSupport;
    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> add(PurchaseOrder purchaseOrder) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date now = new Date();
        List<PurchaseOrderProduct> purchaseOrderProductList = purchaseOrder.getPurchaseOrderProductList();
        List<PurchaseOrderMaterial> purchaseOrderMaterialList = purchaseOrder.getPurchaseOrderMaterialList();
        WarehouseDO warehouseDO = warehouseMapper.finByNo(purchaseOrder.getWarehouseNo());

        if (warehouseDO == null) {
            result.setErrorCode(ErrorCode.WAREHOUSE_NOT_EXISTS);
            return result;
        }
        SupplierDO supplierDO = supplierMapper.findById(purchaseOrder.getProductSupplierId());
        if (supplierDO == null) {
            result.setErrorCode(ErrorCode.SUPPLIER_NOT_EXISTS);
            return result;
        }
        //判断操作人是否可以选择该仓库
        boolean warehouseOp = userSupport.checkCurrentUserWarehouse(warehouseDO.getId());
        if (!warehouseOp) {
            result.setErrorCode(ErrorCode.USER_CAN_NOT_OP_WAREHOUSE);
            return result;
        }
        //商品项列表和物料项列表至少有一个不为空
        if (CollectionUtil.isEmpty(purchaseOrderProductList) && CollectionUtil.isEmpty(purchaseOrder.getPurchaseOrderMaterialList())) {
            result.setErrorCode(ErrorCode.PURCHASE_PRODUCT_MATERIAL_CAN_NOT_ALL_NULL);
            return result;
        }
        //判断该笔采购单是总公司发起还是分公司发起，分公司表要加一个字段代表是否是总公司,这里取的用户是session中的user
        boolean isHead = userSupport.isHeadUser();

        PurchaseOrderDetail purchaseOrderDetail = new PurchaseOrderDetail(userSupport.getCurrentUserId().toString(), now);
        //校验并准备数据
        String checkErrorCode = checkDetail(purchaseOrderDetail, purchaseOrder, purchaseOrderProductList, purchaseOrderMaterialList, isHead, now);
        if (!ErrorCode.SUCCESS.equals(checkErrorCode)) {
            result.setErrorCode(checkErrorCode);
            return result;
        }

        PurchaseOrderDO purchaseOrderDO = buildPurchaseOrder(now, purchaseOrder, purchaseOrderDetail, warehouseDO);
        purchaseOrderDO.setPurchaseNo(generateNoSupport.generatePurchaseOrderNo(now, warehouseDO.getId()));
        purchaseOrderDO.setCreateUser(userSupport.getCurrentUserId().toString());
        purchaseOrderDO.setCreateTime(now);
        purchaseOrderDO.setWarehouseId(warehouseDO.getId());
        purchaseOrderMapper.save(purchaseOrderDO);
        //保存采购订单商品项
        for (PurchaseOrderProductDO purchaseOrderProductDO : purchaseOrderDetail.purchaseOrderProductDOList) {
            purchaseOrderProductDO.setPurchaseOrderId(purchaseOrderDO.getId());
            purchaseOrderProductMapper.save(purchaseOrderProductDO);
        }
        //保存采购订单物料项
        for (PurchaseOrderMaterialDO purchaseOrderMaterialDO : purchaseOrderDetail.purchaseOrderMaterialDOList) {
            purchaseOrderMaterialDO.setPurchaseOrderId(purchaseOrderDO.getId());
            purchaseOrderMaterialMapper.save(purchaseOrderMaterialDO);
        }
        result.setResult(purchaseOrderDO.getPurchaseNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    private PurchaseOrderDO buildPurchaseOrder(Date now, PurchaseOrder purchaseOrder, PurchaseOrderDetail purchaseOrderDetail, WarehouseDO warehouseDO) {
        //保存采购单
        PurchaseOrderDO purchaseOrderDO = ConverterUtil.convert(purchaseOrder, PurchaseOrderDO.class);
        //查询库房信息并保存库房快照
        purchaseOrderDO.setWarehouseSnapshot(JSON.toJSONString(ConverterUtil.convert(warehouseDO,Warehouse.class)));
        purchaseOrderDO.setPurchaseOrderAmountTotal(purchaseOrderDetail.totalAmount);
        //创建采购单时，采购单实收金额和采购单结算金额为空
        purchaseOrderDO.setPurchaseOrderAmountReal(null);
        purchaseOrderDO.setPurchaseOrderAmountStatement(null);
        purchaseOrderDO.setPurchaseOrderStatus(PurchaseOrderStatus.PURCHASE_ORDER_STATUS_WAIT_COMMIT);
        purchaseOrderDO.setDeliveryTime(null);
        purchaseOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        purchaseOrderDO.setOwner(userSupport.getCurrentUserId());
        purchaseOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        purchaseOrderDO.setUpdateTime(now);
        return purchaseOrderDO;
    }

    private String checkDetail(PurchaseOrderDetail purchaseOrderDetail, PurchaseOrder purchaseOrder, List<PurchaseOrderProduct> purchaseOrderProductList, List<PurchaseOrderMaterial> purchaseOrderMaterialList, boolean isHead, Date now) {

        //如果是整机四大件，物料列表只能有四大件物料
        if (PurchaseType.PURCHASE_TYPE_ALL_OR_MAIN == purchaseOrder.getPurchaseType()) {

            if (CollectionUtil.isNotEmpty(purchaseOrderProductList)) {
                //校验采购订单商品项
                String checkErrorCode = checkPurchaseOrderProductList(purchaseOrderDetail, purchaseOrderProductList, purchaseOrder.getIsNew(), isHead);
                if (!ErrorCode.SUCCESS.equals(checkErrorCode)) {
                    return checkErrorCode;
                }
            }
            if (CollectionUtil.isNotEmpty(purchaseOrderMaterialList)) {
                //校验采购订单物料项
                String checkErrorCode = checkPurchaseOrderMaterialList(purchaseOrderDetail, purchaseOrderMaterialList, purchaseOrder.getPurchaseType());
                if (!ErrorCode.SUCCESS.equals(checkErrorCode)) {
                    return checkErrorCode;
                }

            }
        } else {
            //如果是小配件，物料项列表一定不为空
            if (CollectionUtil.isEmpty(purchaseOrderMaterialList)) {
                return ErrorCode.PURCHASE_ORDER_MATERIAL_LIST_NOT_NULL;
            }
            //校验采购订单物料项
            String checkErrorCode = checkPurchaseOrderMaterialList(purchaseOrderDetail, purchaseOrderMaterialList, purchaseOrder.getPurchaseType());
            if (!ErrorCode.SUCCESS.equals(checkErrorCode)) {
                return checkErrorCode;
            }
            //小配件没发票的，100元及以上的，不允许创建采购单
            if (CommonConstant.COMMON_CONSTANT_NO.equals(purchaseOrder.getIsInvoice()) && purchaseOrderDetail.totalMaterialAmount.compareTo(new BigDecimal(100)) > 0) {
                return ErrorCode.PURCHASE_ORDER_MATERIAL_CAN_NOT_CREATE;
            }
        }
        return ErrorCode.SUCCESS;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> update(PurchaseOrder purchaseOrder) {
        ServiceResult<String, String> result = new ServiceResult<>();
        //校验采购单是否存在
        PurchaseOrderDO purchaseOrderDO = purchaseOrderMapper.findByPurchaseNo(purchaseOrder.getPurchaseNo());
        if (purchaseOrderDO == null) {
            result.setErrorCode(ErrorCode.PURCHASE_ORDER_NOT_EXISTS);
            return result;
        }
        WarehouseDO warehouseDO = warehouseMapper.finByNo(purchaseOrder.getWarehouseNo());
        if (warehouseDO == null) {
            result.setErrorCode(ErrorCode.WAREHOUSE_NOT_EXISTS);
            return result;
        }
        SupplierDO supplierDO = supplierMapper.findById(purchaseOrder.getProductSupplierId());
        if (supplierDO == null) {
            result.setErrorCode(ErrorCode.SUPPLIER_NOT_EXISTS);
            return result;
        }

        //判断操作人是否可以选择该仓库
        boolean warehouseOp = userSupport.checkCurrentUserWarehouse(warehouseDO.getId());
        if (!warehouseOp) {
            result.setErrorCode(ErrorCode.USER_CAN_NOT_OP_WAREHOUSE);
            return result;
        }
        //只有待提交的采购单可以修改
        if (!PurchaseOrderStatus.PURCHASE_ORDER_STATUS_WAIT_COMMIT.equals(purchaseOrderDO.getPurchaseOrderStatus())) {
            result.setErrorCode(ErrorCode.PURCHASE_ORDER_COMMITTED_CAN_NOT_UPDATE);
            return result;
        }

        //判断该笔采购单是总公司发起还是分公司发起，这里取的用户是createUser
        boolean isHead = userSupport.isHeadUser(Integer.valueOf(purchaseOrderDO.getCreateUser()));
        Date now = new Date();
        List<PurchaseOrderProduct> purchaseOrderProductList = purchaseOrder.getPurchaseOrderProductList();
        List<PurchaseOrderMaterial> purchaseOrderMaterialList = purchaseOrder.getPurchaseOrderMaterialList();
        PurchaseOrderDetail purchaseOrderDetail = new PurchaseOrderDetail(userSupport.getCurrentUserId().toString(), now);
        //校验采购订单商品项
        String checkErrorCode = checkDetail(purchaseOrderDetail, purchaseOrder, purchaseOrderProductList, purchaseOrderMaterialList, isHead, now);
        if (!ErrorCode.SUCCESS.equals(checkErrorCode)) {
            result.setErrorCode(checkErrorCode);
            return result;
        }

        //更新采购单
        Integer purchaseOrderDOId = purchaseOrderDO.getId();
        String purchaseNo = purchaseOrderDO.getPurchaseNo();
        purchaseOrderDO = buildPurchaseOrder(now, purchaseOrder, purchaseOrderDetail, warehouseDO);
        purchaseOrderDO.setPurchaseNo(purchaseNo);
        purchaseOrderDO.setId(purchaseOrderDOId);
        purchaseOrderDO.setWarehouseId(warehouseDO.getId());
        purchaseOrderMapper.update(purchaseOrderDO);

        //更新采购单商品项
        updatePurchaseOrderProductList(purchaseOrderDetail.purchaseOrderProductDOList, purchaseOrderDOId, now);
        //更新采购单物料项
        updatePurchaseOrderMaterialList(purchaseOrderDetail.purchaseOrderMaterialDOList, purchaseOrderDOId, now);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(purchaseOrderDO.getPurchaseNo());
        return result;
    }

    private void updatePurchaseOrderProductList(List<PurchaseOrderProductDO> newPurchaseOrderProductDOList, Integer purchaseOrderDOId, Date now) {
        //查询旧采购订单项列表
        List<PurchaseOrderProductDO> oldPurchaseOrderProductDOList = purchaseOrderSupport.getAllPurchaseOrderProductDO(purchaseOrderDOId);
        //为方便比对，将旧采购订单项列表存入map

        Map<Integer, PurchaseOrderProductDO> oldMap = new HashMap<>();
        for (PurchaseOrderProductDO purchaseOrderProductDO : oldPurchaseOrderProductDOList) {
            oldMap.put(purchaseOrderProductDO.getProductSkuId(), purchaseOrderProductDO);
        }
        //为方便比对，将新采购订单项列表存入map
        Map<Integer, PurchaseOrderProductDO> newMap = new HashMap<>();
        for (PurchaseOrderProductDO purchaseOrderProductDO : newPurchaseOrderProductDOList) {
            newMap.put(purchaseOrderProductDO.getProductSkuId(), purchaseOrderProductDO);
        }

        for (Integer skuId : newMap.keySet()) {
            //如果原列表有此skuId，则更新
            if (oldMap.containsKey(skuId)) {
                PurchaseOrderProductDO oldDO = oldMap.get(skuId);
                Integer oldId = oldDO.getId();
                PurchaseOrderProductDO newDO = newMap.get(skuId);
                newDO.setId(oldId);
                newDO.setUpdateTime(now);
                newDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                purchaseOrderProductMapper.update(newDO);
            } else { //如果原列表没有新列表有，则添加
                PurchaseOrderProductDO newDO = newMap.get(skuId);
                newDO.setPurchaseOrderId(purchaseOrderDOId);
                purchaseOrderProductMapper.save(newDO);
            }
        }
        for (Integer skuId : oldMap.keySet()) {
            if (!newMap.containsKey(skuId)) {//如果原列表有新列表没有，则删除
                PurchaseOrderProductDO oldPurchaseOrderProductDO = oldMap.get(skuId);
                oldPurchaseOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                oldPurchaseOrderProductDO.setUpdateTime(now);
                oldPurchaseOrderProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                purchaseOrderProductMapper.update(oldPurchaseOrderProductDO);
            }
        }
    }

    private void updatePurchaseOrderMaterialList(List<PurchaseOrderMaterialDO> newPurchaseOrderMaterialDOList, Integer purchaseOrderDOId, Date now) {
        //查询旧采购订单项列表
        List<PurchaseOrderMaterialDO> oldPurchaseOrderMaterialDOList = purchaseOrderSupport.getAllPurchaseOrderMaterialDO(purchaseOrderDOId);
        //为方便比对，将旧采购订单物料项列表存入map

        Map<Integer, PurchaseOrderMaterialDO> oldMap = new HashMap<>();
        for (PurchaseOrderMaterialDO purchaseOrderMaterialDO : oldPurchaseOrderMaterialDOList) {
            oldMap.put(purchaseOrderMaterialDO.getMaterialId(), purchaseOrderMaterialDO);
        }
        //为方便比对，将新采购订单物料项列表存入map
        Map<Integer, PurchaseOrderMaterialDO> newMap = new HashMap<>();
        for (PurchaseOrderMaterialDO purchaseOrderMaterialDO : newPurchaseOrderMaterialDOList) {
            newMap.put(purchaseOrderMaterialDO.getMaterialId(), purchaseOrderMaterialDO);
        }

        for (Integer materialId : newMap.keySet()) {
            //如果原列表有此skuId，则更新
            if (oldMap.containsKey(materialId)) {
                PurchaseOrderMaterialDO oldDO = oldMap.get(materialId);
                Integer oldId = oldDO.getId();
                PurchaseOrderMaterialDO newDO = newMap.get(materialId);
                newDO.setId(oldId);
                newDO.setUpdateTime(now);
                newDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                purchaseOrderMaterialMapper.update(newDO);
            } else { //如果原列表没有新列表有，则添加
                PurchaseOrderMaterialDO newDO = newMap.get(materialId);
                newDO.setPurchaseOrderId(purchaseOrderDOId);
                purchaseOrderMaterialMapper.save(newDO);
            }
        }
        for (Integer materialId : oldMap.keySet()) {
            if (!newMap.containsKey(materialId)) {//如果原列表有新列表没有，则删除
                PurchaseOrderMaterialDO oldPurchaseOrderMaterialDO = oldMap.get(materialId);
                oldPurchaseOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                oldPurchaseOrderMaterialDO.setUpdateTime(now);
                oldPurchaseOrderMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                purchaseOrderMaterialMapper.update(oldPurchaseOrderMaterialDO);
            }
        }
    }

    //生成待保存的采购单项辅助类
    class PurchaseOrderDetail {
        //声明待保存的采购单项列表
        private List<PurchaseOrderProductDO> purchaseOrderProductDOList = new ArrayList<>();
        //声明待保存的采购单物料项列表
        private List<PurchaseOrderMaterialDO> purchaseOrderMaterialDOList = new ArrayList<>();
        //声明累加采购单总价
        private BigDecimal totalAmount = new BigDecimal(0);
        //声明累加采购单商品项总价
        private BigDecimal totalProductAmount = new BigDecimal(0);
        //声明累加采购单物料项总价
        private BigDecimal totalMaterialAmount = new BigDecimal(0);
        private Date now = null;
        private String userId = null;

        public PurchaseOrderDetail(String userId, Date now) {
            this.userId = userId;
            this.now = now;
        }
    }

    private String checkPurchaseOrderMaterialList(PurchaseOrderDetail purchaseOrderDetail, List<PurchaseOrderMaterial> purchaseOrderMaterialList, Integer purchaseType) {
        ServiceResult<String, PurchaseOrderDetail> result = new ServiceResult<>();

        //声明materialIdSet，如果一个采购单中有相同物料项，则返回错误
        Set<Integer> materialIdSet = new HashSet<>();
        //声明用于校验整机四大件列表
        List<Material> materialList = new ArrayList<>();
        // 验证采购单物料项是否合法
        for (PurchaseOrderMaterial purchaseOrderMaterial : purchaseOrderMaterialList) {

            if (StringUtil.isEmpty(purchaseOrderMaterial.getMaterialNo())) {
                return ErrorCode.MATERIAL_NO_NOT_NULL;
            }
            if (purchaseOrderMaterial.getMaterialAmount() == null || purchaseOrderMaterial.getMaterialAmount().doubleValue() <= 0) {
                return ErrorCode.MATERIAL_PRICE_ERROR;
            }
            if (purchaseOrderMaterial.getMaterialCount() == null || purchaseOrderMaterial.getMaterialCount() <= 0) {
                return ErrorCode.MATERIAL_COUNT_ERROR;
            }
            //保存采购订单物料项快照
            MaterialDO materialDO = materialMapper.findByNo(purchaseOrderMaterial.getMaterialNo());
            if (materialDO == null) {
                return ErrorCode.MATERIAL_NOT_EXISTS;
            }
            materialIdSet.add(materialDO.getId());
            //累加采购单物料项总价
            BigDecimal materialAmount = BigDecimalUtil.mul(purchaseOrderMaterial.getMaterialAmount(), new BigDecimal(purchaseOrderMaterial.getMaterialCount()));
            purchaseOrderDetail.totalAmount = BigDecimalUtil.add(purchaseOrderDetail.totalAmount, materialAmount);
            purchaseOrderDetail.totalMaterialAmount = BigDecimalUtil.add(purchaseOrderDetail.totalMaterialAmount, materialAmount);

            PurchaseOrderMaterialDO purchaseOrderMaterialDO = new PurchaseOrderMaterialDO();


            Material material = ConverterUtil.convert(materialDO, Material.class);
            materialList.add(material);
            purchaseOrderMaterialDO.setMaterialSnapshot(JSON.toJSONString(material));
            purchaseOrderMaterialDO.setMaterialId(materialDO.getId());
            purchaseOrderMaterialDO.setMaterialName(materialDO.getMaterialName());
            purchaseOrderMaterialDO.setMaterialCount(purchaseOrderMaterial.getMaterialCount());
            purchaseOrderMaterialDO.setMaterialAmount(purchaseOrderMaterial.getMaterialAmount());
            purchaseOrderMaterialDO.setRemark(purchaseOrderMaterial.getRemark());
            purchaseOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            purchaseOrderMaterialDO.setCreateUser(purchaseOrderDetail.userId);
            purchaseOrderMaterialDO.setUpdateUser(purchaseOrderDetail.userId);
            purchaseOrderMaterialDO.setCreateTime(purchaseOrderDetail.now);
            purchaseOrderMaterialDO.setUpdateTime(purchaseOrderDetail.now);
            purchaseOrderDetail.purchaseOrderMaterialDOList.add(purchaseOrderMaterialDO);
        }
        //采购单物料项物料ID有重复
        if (purchaseOrderMaterialList.size() != materialIdSet.size()) {
            return ErrorCode.PURCHASE_ORDER_MATERIAL_CAN_NOT_REPEAT;
        }
        //校验整机四大件类型的是否全为整机四大件
        if (PurchaseType.PURCHASE_TYPE_ALL_OR_MAIN == purchaseType && !materialService.isAllMainMaterial(materialList)) {
            return ErrorCode.PURCHASE_ORDER_MATERIAL_NOT_MAIN;
        }
        //校验小配件类型的是否全为小配件
        if (PurchaseType.PURCHASE_TYPE_GADGET == purchaseType && !materialService.isAllGadget(materialList)) {
            return ErrorCode.PURCHASE_ORDER_MATERIAL_NOT_GADGET;
        }

        return ErrorCode.SUCCESS;
    }

    private String checkPurchaseOrderProductList(PurchaseOrderDetail purchaseOrderDetail, List<PurchaseOrderProduct> purchaseOrderProductList, Integer isNew, boolean isHead) {

        // 验证采购单商品项是否合法
        for (PurchaseOrderProduct purchaseOrderProduct : purchaseOrderProductList) {
            if (purchaseOrderProduct.getProductSkuId() == null) {
                return ErrorCode.PRODUCT_SKU_NOT_NULL;
            }
            if (purchaseOrderProduct.getProductAmount() == null || purchaseOrderProduct.getProductAmount().doubleValue() <= 0) {
                return ErrorCode.PRODUCT_SKU_PRICE_ERROR;
            }
            if (purchaseOrderProduct.getProductCount() == null || purchaseOrderProduct.getProductCount() <= 0) {
                return ErrorCode.PRODUCT_SKU_COUNT_ERROR;
            }
            ProductSkuDO productSkuDO = productSkuMapper.findById(purchaseOrderProduct.getProductSkuId());
            if (productSkuDO == null) {
                return ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS;
            }
            //校验此sku是否可以使用productMaterialList的物料配置
            ProductSku productSku = new ProductSku();
            productSku.setSkuId(productSkuDO.getId());
            productSku.setProductMaterialList(purchaseOrderProduct.getProductMaterialList());
            ServiceResult<String, Integer> verifyProductMaterialResult = productService.verifyProductMaterial(productSku);
            if (!ErrorCode.SUCCESS.equals(verifyProductMaterialResult.getErrorCode())) {
                return verifyProductMaterialResult.getErrorCode();
            }
            //累加采购单总价
            purchaseOrderDetail.totalAmount = BigDecimalUtil.add(purchaseOrderDetail.totalAmount, BigDecimalUtil.mul(purchaseOrderProduct.getProductAmount(), new BigDecimal(purchaseOrderProduct.getProductCount())));

            PurchaseOrderProductDO purchaseOrderProductDO = new PurchaseOrderProductDO();
            //保存采购订单商品项快照
//            ServiceResult<String,Product> productResult = productService.queryProductById(productSkuDO.getProductId());
            ServiceResult<String, Product> productResult = productService.queryProductBySkuId(productSkuDO.getId());
            purchaseOrderProductDO.setProductSnapshot(JSON.toJSONString(getProductBySkuId(productResult.getResult(), productSkuDO.getId(), purchaseOrderProduct.getProductMaterialList())));
            purchaseOrderProductDO.setProductId(productSkuDO.getProductId());
            purchaseOrderProductDO.setProductName(productSkuDO.getProductName());
            purchaseOrderProductDO.setProductSkuId(productSkuDO.getId());
            purchaseOrderProductDO.setProductCount(purchaseOrderProduct.getProductCount());
            purchaseOrderProductDO.setProductAmount(purchaseOrderProduct.getProductAmount());
            purchaseOrderProductDO.setRemark(purchaseOrderProduct.getRemark());
            purchaseOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            purchaseOrderProductDO.setCreateUser(purchaseOrderDetail.userId);
            purchaseOrderProductDO.setUpdateUser(purchaseOrderDetail.userId);
            purchaseOrderProductDO.setCreateTime(purchaseOrderDetail.now);
            purchaseOrderProductDO.setUpdateTime(purchaseOrderDetail.now);
            purchaseOrderDetail.purchaseOrderProductDOList.add(purchaseOrderProductDO);
        }
        //发起者不是总公司 ，并且采购20000元以上全新机，则返回错误
        if (!isHead && CommonConstant.COMMON_CONSTANT_YES.equals(isNew) && purchaseOrderDetail.totalAmount.compareTo(new BigDecimal(20000)) > 0) {
            return ErrorCode.PURCHASE_ORDER_CANNOT_CREATE_BY_NEW_AND_AMOUNT;
        }
        return ErrorCode.SUCCESS;
    }

    private ServiceResult<String, List<PurchaseReceiveOrderProductDO>> checkPurchaseReceiveOrderProductListForUpdate(List<PurchaseReceiveOrderProductDO> oldPurchaseReceiveOrderProductDOList,List<PurchaseReceiveOrderProduct> purchaseReceiveOrderProductList, String userId, Date now) {
        ServiceResult<String, List<PurchaseReceiveOrderProductDO>> result = new ServiceResult<>();
        List<PurchaseReceiveOrderProductDO> purchaseReceiveOrderProductDOList = new ArrayList<>();
        Set<Integer> productSet = new HashSet<>();
        for(PurchaseReceiveOrderProductDO purchaseReceiveOrderProductDO : oldPurchaseReceiveOrderProductDOList){
            productSet.add(purchaseReceiveOrderProductDO.getProductId());
        }
        if (CollectionUtil.isNotEmpty(purchaseReceiveOrderProductList)) {
            // 验证采购单商品项是否合法
            for (PurchaseReceiveOrderProduct purchaseReceiveOrderProduct : purchaseReceiveOrderProductList) {

                if (purchaseReceiveOrderProduct.getRealProductSkuId() == null) {
                    result.setErrorCode(ErrorCode.PURCHASE_RECEIVE_ORDER_PRODUCT_SKU_ID_NOT_NULL);
                    return result;
                }
                if (purchaseReceiveOrderProduct.getRealProductCount() == null || purchaseReceiveOrderProduct.getRealProductCount() <= 0) {
                    result.setErrorCode(ErrorCode.PURCHASE_RECEIVE_ORDER_PRODUCT_REAL_COUNT_NOT_NULL);
                    return result;
                }

                ProductSkuDO productSkuDO = productSkuMapper.findById(purchaseReceiveOrderProduct.getRealProductSkuId());
                if (productSkuDO == null) {
                    result.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                    return result;
                }
                //如果不是是采购单里面的商品
                if(!productSet.contains(productSkuDO.getProductId())){
                    result.setErrorCode(ErrorCode.PRODUCT_NOT_SAME);
                    return result;
                }
                //校验此sku是否可以使用productMaterialList的物料配置
                ProductSku productSku = new ProductSku();
                productSku.setSkuId(productSkuDO.getId());
                productSku.setProductMaterialList(purchaseReceiveOrderProduct.getProductMaterialList());
                ServiceResult<String, Integer> verifyProductMaterialResult = productService.verifyProductMaterial(productSku);
                if (!ErrorCode.SUCCESS.equals(verifyProductMaterialResult.getErrorCode())) {
                    result.setErrorCode(verifyProductMaterialResult.getErrorCode());
                    return result;
                }
                PurchaseReceiveOrderProductDO purchaseReceiveOrderProductDO = new PurchaseReceiveOrderProductDO();
                //保存采购收货单商品项快照
                ServiceResult<String, Product> productResult = productService.queryProductDetailById(productSkuDO.getProductId());
                purchaseReceiveOrderProductDO.setId(purchaseReceiveOrderProduct.getPurchaseReceiveOrderProductId());
                purchaseReceiveOrderProductDO.setRealProductSnapshot(JSON.toJSONString(getProductBySkuId(productResult.getResult(), productSkuDO.getId(), purchaseReceiveOrderProduct.getProductMaterialList())));
                purchaseReceiveOrderProductDO.setRealProductId(productSkuDO.getProductId());
                purchaseReceiveOrderProductDO.setRealProductName(productSkuDO.getProductName());
                purchaseReceiveOrderProductDO.setRealProductSkuId(productSkuDO.getId());
                purchaseReceiveOrderProductDO.setRealProductCount(purchaseReceiveOrderProduct.getRealProductCount());
                purchaseReceiveOrderProductDO.setRemark(purchaseReceiveOrderProduct.getRemark());
                purchaseReceiveOrderProductDO.setUpdateUser(userId);
                purchaseReceiveOrderProductDO.setUpdateTime(now);
                purchaseReceiveOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                purchaseReceiveOrderProductDOList.add(purchaseReceiveOrderProductDO);
            }
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(purchaseReceiveOrderProductDOList);
        return result;
    }

    private ServiceResult<String, List<PurchaseReceiveOrderMaterialDO>> checkPurchaseReceiveOrderMaterialListForUpdate(List<PurchaseReceiveOrderMaterialDO> oldPurchaseReceiveOrderMaterialDOList,List<PurchaseReceiveOrderMaterial> purchaseReceiveOrderMaterialList, String userId, Date now) {
        ServiceResult<String, List<PurchaseReceiveOrderMaterialDO>> result = new ServiceResult<>();
        List<PurchaseReceiveOrderMaterialDO> purchaseReceiveOrderMaterialDOList = new ArrayList<>();
        Set<Integer> materialIdSet = new HashSet<>();

        //用来判断是否是原单中应有的配件
        Set<Integer> materialSet = new HashSet<>();
        for(PurchaseReceiveOrderMaterialDO purchaseReceiveOrderMaterialDO : oldPurchaseReceiveOrderMaterialDOList){
            materialSet.add(purchaseReceiveOrderMaterialDO.getMaterialId());
        }
        if (CollectionUtil.isNotEmpty(purchaseReceiveOrderMaterialList)) {
            // 验证采购收料单物料项是否合法
            for (PurchaseReceiveOrderMaterial purchaseReceiveOrderMaterial : purchaseReceiveOrderMaterialList) {

                if (purchaseReceiveOrderMaterial.getRealMaterialNo() == null) {
                    result.setErrorCode(ErrorCode.PURCHASE_RECEIVE_ORDER_MATERIAL_NO_NOT_NULL);
                    return result;
                }
                if (purchaseReceiveOrderMaterial.getRealMaterialCount() == null || purchaseReceiveOrderMaterial.getRealMaterialCount() <= 0) {
                    result.setErrorCode(ErrorCode.PURCHASE_RECEIVE_ORDER_MATERIAL_REAL_COUNT_NOT_NULL);
                    return result;
                }
                MaterialDO materialDO = materialMapper.findByNo(purchaseReceiveOrderMaterial.getRealMaterialNo());
                if (materialDO == null) {
                    result.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
                    return result;
                }
                //如果不是是采购单里面的配件
                if(!materialSet.contains(materialDO.getId())){
                    result.setErrorCode(ErrorCode.MATERIAL_NOT_SAME);
                    return result;
                }
                PurchaseReceiveOrderMaterialDO purchaseReceiveOrderMaterialDO = new PurchaseReceiveOrderMaterialDO();
                //保存采购订单物料项快照
                purchaseReceiveOrderMaterialDO.setRealMaterialSnapshot(JSON.toJSONString(materialDO));
                purchaseReceiveOrderMaterialDO.setRealMaterialId(materialDO.getId());
                purchaseReceiveOrderMaterialDO.setRealMaterialName(materialDO.getMaterialName());
                purchaseReceiveOrderMaterialDO.setRealMaterialCount(purchaseReceiveOrderMaterial.getRealMaterialCount());
                purchaseReceiveOrderMaterialDO.setRemark(purchaseReceiveOrderMaterial.getRemark());
                purchaseReceiveOrderMaterialDO.setUpdateUser(userId);
                purchaseReceiveOrderMaterialDO.setUpdateTime(now);
                purchaseReceiveOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                purchaseReceiveOrderMaterialDOList.add(purchaseReceiveOrderMaterialDO);
                materialIdSet.add(materialDO.getId());
            }
        }
        //采购单物料项物料ID有重复
        if (purchaseReceiveOrderMaterialDOList.size() != materialIdSet.size()) {
            result.setErrorCode(ErrorCode.PURCHASE_ORDER_MATERIAL_CAN_NOT_REPEAT);
            return result;
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(purchaseReceiveOrderMaterialDOList);
        return result;
    }

    private Product getProductBySkuId(Product product, Integer productSkuId, List<ProductMaterial> materialList) {
        List<ProductSku> productSkuList = product.getProductSkuList();
        List<ProductSku> list = new ArrayList<>();
        for (ProductMaterial productMaterial : materialList) {
            MaterialDO materialDO = materialMapper.findByNo(productMaterial.getMaterialNo());
            productMaterial.setMaterialName(materialDO.getMaterialName());
            productMaterial.setMaterialModelId(materialDO.getMaterialModelId());
        }
        for (ProductSku productSku : productSkuList) {
            if (productSkuId != null && productSkuId.equals(productSku.getSkuId())) {
                productMaterial.setMaterialType(materialDO.getMaterialType());
                list.add(productSku);
                productSku.setProductMaterialList(materialList);
                break;
            }
        }
        product.setProductSkuList(list);
        return product;
    }

    @Override
        ServiceResult<String, PurchaseOrder> serviceResult = new ServiceResult<>();
        PurchaseOrderDO purchaseOrderDO = purchaseOrderMapper.findDetailByPurchaseNo(purchaseNo);
        if (purchaseOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.PURCHASE_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        PurchaseOrder purchaseOrder = ConverterUtil.convert(purchaseOrderDO, PurchaseOrder.class);
        public ServiceResult<String, PurchaseOrder> queryPurchaseOrderByNo(String purchaseNo) {
        List<PurchaseDeliveryOrderDO> purchaseDeliveryOrderDOList = purchaseDeliveryOrderMapper.findListByPurchaseId(purchaseOrderDO.getId());
        List<PurchaseReceiveOrderDO> purchaseReceiveOrderDOList = purchaseReceiveOrderMapper.findListByPurchaseId(purchaseOrderDO.getId());

        List<PurchaseDeliveryOrder> purchaseDeliveryOrderList = ConverterUtil.convertList(purchaseDeliveryOrderDOList, PurchaseDeliveryOrder.class);
        List<PurchaseReceiveOrder> purchaseReceiveOrderList = ConverterUtil.convertList(purchaseReceiveOrderDOList, PurchaseReceiveOrder.class);
        purchaseOrder.setPurchaseDeliveryOrderList(purchaseDeliveryOrderList);
        purchaseOrder.setPurchaseReceiveOrderList(purchaseReceiveOrderList);
        WorkflowLinkDO workflowLinkDO = workflowLinkMapper.findByWorkflowTypeAndReferNo(WorkflowType.WORKFLOW_TYPE_PURCHASE, purchaseNo);
        purchaseOrder.setWorkflowLink(ConverterUtil.convert(workflowLinkDO, WorkflowLink.class));
        serviceResult.setResult(purchaseOrder);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Page<PurchaseOrder>> page(PurchaseOrderQueryParam purchaseOrderQueryParam) {
        ServiceResult<String, Page<PurchaseOrder>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(purchaseOrderQueryParam.getPageNo(), purchaseOrderQueryParam.getPageSize());
        purchaseOrderQueryParam.setWarehouseId(null);
        if (StringUtil.isNotEmpty(purchaseOrderQueryParam.getWarehouseNo())) {
            WarehouseDO warehouseDO = warehouseMapper.finByNo(purchaseOrderQueryParam.getWarehouseNo());
            if (warehouseDO == null) {
                result.setErrorCode(ErrorCode.SUCCESS);
                Page<PurchaseOrder> page = new Page<>(new ArrayList<PurchaseOrder>(), 0, purchaseOrderQueryParam.getPageNo(), purchaseOrderQueryParam.getPageSize());
                result.setResult(page);
                return result;
            }
            purchaseOrderQueryParam.setWarehouseId(warehouseDO.getId());
        }
        //数据级权限控制-查找用户可查看用户列表
        Integer currentUserId = userSupport.getCurrentUserId();
        //获取用户最【新的】最终可观察用户列表
        List<UserDO> userDOList = userMapper.getPassiveUserByUser(currentUserId);
        List<Integer> passiveUserIdList = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(userDOList)){
            for(UserDO userDO : userDOList){
                passiveUserIdList.add(userDO.getId());
            }
        }else{
            result.setErrorCode(ErrorCode.SUCCESS);
            Page<PurchaseOrder> page = new Page<>(new ArrayList<PurchaseOrder>(), 0, purchaseOrderQueryParam.getPageNo(), purchaseOrderQueryParam.getPageSize());
            result.setResult(page);
            return result;
        }
        purchaseOrderQueryParam.setPassiveUserIdList(passiveUserIdList);

        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("queryParam", purchaseOrderQueryParam);

        Integer totalCount = purchaseOrderMapper.findPurchaseOrderCountByParams(maps);
        List<PurchaseOrderDO> purchaseOrderDOList = purchaseOrderMapper.findPurchaseOrderByParams(maps);
        List<PurchaseOrder> purchaseOrderList = ConverterUtil.convertList(purchaseOrderDOList, PurchaseOrder.class);
        Page<PurchaseOrder> page = new Page<>(purchaseOrderList, totalCount, purchaseOrderQueryParam.getPageNo(), purchaseOrderQueryParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, String> commit(PurchaseOrderCommitParam purchaseOrderCommitParam) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date now = new Date();
        //校验采购单是否存在
        PurchaseOrderDO purchaseOrderDO = purchaseOrderMapper.findByPurchaseNo(purchaseOrderCommitParam.getPurchaseNo());
        if (purchaseOrderDO == null) {
            result.setErrorCode(ErrorCode.PURCHASE_ORDER_NOT_EXISTS);
            return result;
        } else if (!PurchaseOrderStatus.PURCHASE_ORDER_STATUS_WAIT_COMMIT.equals(purchaseOrderDO.getPurchaseOrderStatus())) {
            //只有待提交状态的采购单可以提交
            result.setErrorCode(ErrorCode.PURCHASE_ORDER_COMMITTED_CAN_NOT_COMMIT_AGAIN);
            return result;
        }
        if (!purchaseOrderDO.getCreateUser().equals(userSupport.getCurrentUserId().toString())) {
            //只有创建采购单本人可以提交
            result.setErrorCode(ErrorCode.COMMIT_ONLY_SELF);
            return result;
        }
        ServiceResult<String, Boolean> needVerifyResult = workflowService.isNeedVerify(WorkflowType.WORKFLOW_TYPE_PURCHASE);
        if (!ErrorCode.SUCCESS.equals(needVerifyResult.getErrorCode())) {
            result.setErrorCode(needVerifyResult.getErrorCode());
            return result;
        } else if (needVerifyResult.getResult()) {
            if (purchaseOrderCommitParam.getVerifyUserId() == null) {
                result.setErrorCode(ErrorCode.VERIFY_USER_NOT_NULL);
                return result;
            }
            //调用提交审核服务
            ServiceResult<String, String> verifyResult = workflowService.commitWorkFlow(WorkflowType.WORKFLOW_TYPE_PURCHASE, purchaseOrderDO.getPurchaseNo(), purchaseOrderCommitParam.getVerifyUserId(), purchaseOrderCommitParam.getRemark());
            //修改提交审核状态
            if (ErrorCode.SUCCESS.equals(verifyResult.getErrorCode())) {
                purchaseOrderDO.setPurchaseOrderStatus(PurchaseOrderStatus.PURCHASE_ORDER_STATUS_VERIFYING);
                purchaseOrderDO.setUpdateTime(now);
                purchaseOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                purchaseOrderMapper.update(purchaseOrderDO);
                return verifyResult;
            } else {
                result.setErrorCode(verifyResult.getErrorCode());
                return result;
            }
        } else {
            purchaseOrderDO.setPurchaseOrderStatus(PurchaseOrderStatus.PURCHASE_ORDER_STATUS_PURCHASING);
            purchaseOrderDO.setUpdateTime(now);
            purchaseOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            purchaseOrderMapper.update(purchaseOrderDO);
            result.setErrorCode(ErrorCode.SUCCESS);
            return result;
        }
    }

    @Override
    public String cancel(PurchaseOrder purchaseOrder) {
        PurchaseOrderDO purchaseOrderDO = purchaseOrderMapper.findByPurchaseNo(purchaseOrder.getPurchaseNo());
        if (purchaseOrderDO == null) {
            return ErrorCode.PURCHASE_ORDER_NOT_EXISTS;
        }
        if (!PurchaseOrderStatus.PURCHASE_ORDER_STATUS_WAIT_COMMIT.equals(purchaseOrderDO.getPurchaseOrderStatus())) {
            //只有待提交状态的采购单可以取消
            return ErrorCode.PURCHASE_ORDER_COMMITTED_CAN_NOT_CANCEL;
        }
        Date now = new Date();

        purchaseOrderDO.setUpdateTime(now);
        purchaseOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        purchaseOrderDO.setPurchaseOrderStatus(PurchaseOrderStatus.PURCHASE_ORDER_STATUS_CANCEL);
        purchaseOrderMapper.update(purchaseOrderDO);
        return ErrorCode.SUCCESS;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public String strongCancel(PurchaseOrder purchaseOrder) {
        PurchaseOrderDO purchaseOrderDO = purchaseOrderMapper.findByPurchaseNo(purchaseOrder.getPurchaseNo());
        if (purchaseOrderDO == null) {
            return ErrorCode.PURCHASE_ORDER_NOT_EXISTS;
        }
        if (!PurchaseOrderStatus.PURCHASE_ORDER_STATUS_WAIT_COMMIT.equals(purchaseOrderDO.getPurchaseOrderStatus()) &&
                !PurchaseOrderStatus.PURCHASE_ORDER_STATUS_VERIFYING.equals(purchaseOrderDO.getPurchaseOrderStatus()) &&
                !PurchaseOrderStatus.PURCHASE_ORDER_STATUS_PURCHASING.equals(purchaseOrderDO.getPurchaseOrderStatus())) {
            //待提交和审核通过的采购单可以取消
            return ErrorCode.PURCHASE_ORDER_COMMITTED_CAN_NOT_CANCEL;
        }
        Date now = new Date();
        purchaseOrderDO.setUpdateTime(now);
        purchaseOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        purchaseOrderDO.setPurchaseOrderStatus(PurchaseOrderStatus.PURCHASE_ORDER_STATUS_CANCEL);
        purchaseOrderMapper.update(purchaseOrderDO);

        List<PurchaseDeliveryOrderDO> purchaseDeliveryOrderDOList = purchaseDeliveryOrderMapper.findListByPurchaseId(purchaseOrderDO.getId());
        List<PurchaseReceiveOrderDO> purchaseReceiveOrderDOList = purchaseReceiveOrderMapper.findListByPurchaseId(purchaseOrderDO.getId());

        if (CollectionUtil.isNotEmpty(purchaseDeliveryOrderDOList)) {
            for (PurchaseDeliveryOrderDO purchaseDeliveryOrderDO : purchaseDeliveryOrderDOList) {
                purchaseDeliveryOrderDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                purchaseDeliveryOrderDO.setUpdateTime(now);
                purchaseDeliveryOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                purchaseDeliveryOrderMapper.update(purchaseDeliveryOrderDO);
            }
        }
        if (CollectionUtil.isNotEmpty(purchaseReceiveOrderDOList)) {
            for (PurchaseReceiveOrderDO purchaseReceiveOrderDO : purchaseReceiveOrderDOList) {
                purchaseReceiveOrderDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                purchaseReceiveOrderDO.setUpdateTime(now);
                purchaseReceiveOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                purchaseReceiveOrderMapper.update(purchaseReceiveOrderDO);
            }
        }
        return ErrorCode.SUCCESS;
    }

    @Override
    public ServiceResult<String, Page<PurchaseDeliveryOrder>> pagePurchaseDelivery(PurchaseDeliveryOrderQueryParam purchaseDeliveryOrderQueryParam) {
        ServiceResult<String, Page<PurchaseDeliveryOrder>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(purchaseDeliveryOrderQueryParam.getPageNo(), purchaseDeliveryOrderQueryParam.getPageSize());
        purchaseDeliveryOrderQueryParam.setPurchaseOrderId(null);
        purchaseDeliveryOrderQueryParam.setWarehouseId(null);
        if (StringUtil.isNotEmpty(purchaseDeliveryOrderQueryParam.getPurchaseNo())) {
            PurchaseOrderDO purchaseOrderDO = purchaseOrderMapper.findByPurchaseNo(purchaseDeliveryOrderQueryParam.getPurchaseNo());
            if (purchaseOrderDO == null) {
                result.setErrorCode(ErrorCode.SUCCESS);
                Page<PurchaseDeliveryOrder> page = new Page<>(new ArrayList<PurchaseDeliveryOrder>(), 0, purchaseDeliveryOrderQueryParam.getPageNo(), purchaseDeliveryOrderQueryParam.getPageSize());
                result.setResult(page);
                return result;
            }
            purchaseDeliveryOrderQueryParam.setPurchaseOrderId(purchaseOrderDO.getId());
        }
        if (StringUtil.isNotEmpty(purchaseDeliveryOrderQueryParam.getWarehouseNo())) {
            WarehouseDO warehouseDO = warehouseMapper.finByNo(purchaseDeliveryOrderQueryParam.getWarehouseNo());
            if (warehouseDO == null) {
                result.setErrorCode(ErrorCode.SUCCESS);
                Page<PurchaseDeliveryOrder> page = new Page<>(new ArrayList<PurchaseDeliveryOrder>(), 0, purchaseDeliveryOrderQueryParam.getPageNo(), purchaseDeliveryOrderQueryParam.getPageSize());
                result.setResult(page);
                return result;
            }
            purchaseDeliveryOrderQueryParam.setWarehouseId(warehouseDO.getId());
        }

        //数据级权限控制-查找用户可查看用户列表
        Integer currentUserId = userSupport.getCurrentUserId();
        //获取用户最【新的】最终可观察用户列表
        List<UserDO> userDOList = userMapper.getPassiveUserByUser(currentUserId);
        List<Integer> passiveUserIdList = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(userDOList)){
            for(UserDO userDO : userDOList){
                passiveUserIdList.add(userDO.getId());
            }
        }else{
            result.setErrorCode(ErrorCode.SUCCESS);
            Page<PurchaseDeliveryOrder> page = new Page<>(new ArrayList<PurchaseDeliveryOrder>(), 0, purchaseDeliveryOrderQueryParam.getPageNo(), purchaseDeliveryOrderQueryParam.getPageSize());
            result.setResult(page);
            return result;
        }
        purchaseDeliveryOrderQueryParam.setPassiveUserIdList(passiveUserIdList);
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("queryParam", purchaseDeliveryOrderQueryParam);

        Integer totalCount = purchaseDeliveryOrderMapper.findPurchaseDeliveryOrderCountByParams(maps);
        List<PurchaseDeliveryOrderDO> purchaseDeliveryOrderDOList = purchaseDeliveryOrderMapper.findPurchaseDeliveryOrderByParams(maps);
        List<PurchaseDeliveryOrder> purchaseDeliveryOrderList = ConverterUtil.convertList(purchaseDeliveryOrderDOList, PurchaseDeliveryOrder.class);
        Page<PurchaseDeliveryOrder> page = new Page<>(purchaseDeliveryOrderList, totalCount, purchaseDeliveryOrderQueryParam.getPageNo(), purchaseDeliveryOrderQueryParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, PurchaseDeliveryOrder> queryPurchaseDeliveryOrderByNo(PurchaseDeliveryOrder purchaseDeliveryOrder) {
        ServiceResult<String, PurchaseDeliveryOrder> serviceResult = new ServiceResult<>();
        PurchaseDeliveryOrderDO purchaseDeliveryOrderDO = purchaseDeliveryOrderMapper.findByNo(purchaseDeliveryOrder.getPurchaseDeliveryNo());
        if (purchaseDeliveryOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.PURCHASE_DELIVERY_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(ConverterUtil.convert(purchaseDeliveryOrderDO, PurchaseDeliveryOrder.class));
        return serviceResult;
    }

    /**
     * 修改采购收货单
     * 分拨情况为已分拨的（自动流转到总仓的采购收货单）不可以修改
     * 采购收货单状态为已签单的不可以修改
     *
     * @param purchaseReceiveOrder
     * @return
     */
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    @Override
    public ServiceResult<String, String> updatePurchaseReceiveOrder(PurchaseReceiveOrder purchaseReceiveOrder) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        PurchaseReceiveOrderDO purchaseReceiveOrderDO = purchaseReceiveOrderMapper.findByNo(purchaseReceiveOrder.getPurchaseReceiveNo());
        if (purchaseReceiveOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.PURCHASE_RECEIVE_ORDER_NOT_EXISTS);
            return serviceResult;
        }

        //分拨情况为已分拨的（自动流转到总仓的采购收货单）不可以修改
        if (AutoAllotStatus.AUTO_ALLOT_STATUS_YES.equals(purchaseReceiveOrderDO.getAutoAllotStatus())) {
            serviceResult.setErrorCode(ErrorCode.PURCHASE_RECEIVE_ORDER_AUTO_ALLOT_YES_CAN_NOT_UPDATE);
            return serviceResult;
        }
        if (PurchaseReceiveOrderStatus.PURCHASE_RECEIVE_ORDER_STATUS_YET.equals(purchaseReceiveOrderDO.getPurchaseReceiveOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.PURCHASE_RECEIVE_ORDER_STATUS_CAN_NOT_UPDATE);
            return serviceResult;
        }
        if(CollectionUtil.isEmpty(purchaseReceiveOrder.getPurchaseReceiveOrderProductList())&&CollectionUtil.isEmpty(purchaseReceiveOrder.getPurchaseReceiveOrderMaterialList())){
            serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return serviceResult;
        }
        Date now = new Date();
        ServiceResult<String, List<PurchaseReceiveOrderProductDO>> newProductResult = checkPurchaseReceiveOrderProductListForUpdate(purchaseReceiveOrderDO.getPurchaseReceiveOrderProductDOList(),purchaseReceiveOrder.getPurchaseReceiveOrderProductList(), userSupport.getCurrentUserId().toString(), now);
        ServiceResult<String, List<PurchaseReceiveOrderMaterialDO>> newMaterialResult = checkPurchaseReceiveOrderMaterialListForUpdate(purchaseReceiveOrderDO.getPurchaseReceiveOrderMaterialDOList(),purchaseReceiveOrder.getPurchaseReceiveOrderMaterialList(), userSupport.getCurrentUserId().toString(), now);
        if (!ErrorCode.SUCCESS.equals(newProductResult.getErrorCode())) {
            serviceResult.setErrorCode(newProductResult.getErrorCode());
            return serviceResult;
        }

        purchaseReceiveOrderDO.setIsNew(purchaseReceiveOrder.getIsNew());
        purchaseReceiveOrderDO.setUpdateTime(now);
        purchaseReceiveOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        purchaseReceiveOrderDO.setRemark(purchaseReceiveOrder.getRemark());
        purchaseReceiveOrderMapper.update(purchaseReceiveOrderDO);

        //处理采购收货单商品项变化
        String productErrorCode = updatePurchaseReceiveOrderProductList(purchaseReceiveOrderDO, newProductResult.getResult(), now);
        if (!ErrorCode.SUCCESS.equals(productErrorCode)) {
            serviceResult.setErrorCode(productErrorCode);
            return serviceResult;
        }
        //处理采购收货单物料项变化
        String materialErrorCode = updatePurchaseReceiveOrderMaterialList(purchaseReceiveOrderDO, newMaterialResult.getResult(), now);
        if (!ErrorCode.SUCCESS.equals(materialErrorCode)) {
            serviceResult.setErrorCode(materialErrorCode);
            return serviceResult;
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(purchaseReceiveOrderDO.getPurchaseReceiveNo());
        return serviceResult;
    }

    private String updatePurchaseReceiveOrderProductList(PurchaseReceiveOrderDO purchaseReceiveOrderDO, List<PurchaseReceiveOrderProductDO> newPurchaseReceiveOrderProductDOList, Date now) {
        //商品项列表
        List<PurchaseReceiveOrderProductDO> oldProductList = purchaseReceiveOrderDO.getPurchaseReceiveOrderProductDOList();
        Map<Integer, PurchaseReceiveOrderProductDO> deleteMap = ListUtil.listToMap(oldProductList,"id");
        //如果新列表项有ID，则更新
        for (PurchaseReceiveOrderProductDO purchaseReceiveOrderProductDO : newPurchaseReceiveOrderProductDOList) {
            if (purchaseReceiveOrderProductDO.getId() != null) {
                purchaseReceiveOrderProductMapper.update(purchaseReceiveOrderProductDO);
                deleteMap.remove(purchaseReceiveOrderProductDO.getId());
            } else {
                //这种添加属于收货后添加的，用该字段标志
                purchaseReceiveOrderProductDO.setIsSrc(CommonConstant.COMMON_CONSTANT_NO);
                purchaseReceiveOrderProductDO.setPurchaseReceiveOrderId(purchaseReceiveOrderDO.getId());
                purchaseReceiveOrderProductDO.setCreateTime(now);
                purchaseReceiveOrderProductDO.setCreateUser(userSupport.getCurrentUserId().toString());
                purchaseReceiveOrderProductMapper.save(purchaseReceiveOrderProductDO);
            }
        }
        for (Integer id : deleteMap.keySet()) {
            PurchaseReceiveOrderProductDO purchaseReceiveOrderProductDO = deleteMap.get(id);
            purchaseReceiveOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
            purchaseReceiveOrderProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            purchaseReceiveOrderProductDO.setUpdateTime(now);
            purchaseReceiveOrderProductMapper.update(purchaseReceiveOrderProductDO);
        }
        return ErrorCode.SUCCESS;
    }

    private String updatePurchaseReceiveOrderMaterialList(PurchaseReceiveOrderDO purchaseReceiveOrderDO, List<PurchaseReceiveOrderMaterialDO> newPurchaseReceiveOrderMaterialDOList, Date now) {
        //这里的商品项列表
        List<PurchaseReceiveOrderMaterialDO> oldMaterialList = purchaseReceiveOrderDO.getPurchaseReceiveOrderMaterialDOList();
        //为方便比对，将旧采购订单项列表存入map
        Map<Integer, PurchaseReceiveOrderMaterialDO> deleteMap = ListUtil.listToMap(oldMaterialList,"id");

        for (PurchaseReceiveOrderMaterialDO purchaseReceiveOrderMaterialDO : newPurchaseReceiveOrderMaterialDOList) {
            if (purchaseReceiveOrderMaterialDO.getId() != null) {
                purchaseReceiveOrderMaterialMapper.update(purchaseReceiveOrderMaterialDO);
                deleteMap.remove(purchaseReceiveOrderMaterialDO.getId());
            } else {
                //这种添加属于收货后添加的，用该字段标志
                purchaseReceiveOrderMaterialDO.setIsSrc(CommonConstant.COMMON_CONSTANT_NO);
                purchaseReceiveOrderMaterialDO.setPurchaseReceiveOrderId(purchaseReceiveOrderDO.getId());
                purchaseReceiveOrderMaterialDO.setCreateTime(now);
                purchaseReceiveOrderMaterialDO.setCreateUser(userSupport.getCurrentUserId().toString());
                purchaseReceiveOrderMaterialMapper.save(purchaseReceiveOrderMaterialDO);
            }
        }
        for (Integer id : deleteMap.keySet()) {
            PurchaseReceiveOrderMaterialDO purchaseReceiveOrderMaterialDO = deleteMap.get(id);
            purchaseReceiveOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
            purchaseReceiveOrderMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            purchaseReceiveOrderMaterialDO.setUpdateTime(now);
            purchaseReceiveOrderMaterialMapper.update(purchaseReceiveOrderMaterialDO);
        }
        return ErrorCode.SUCCESS;
    }

    /**
     * 采购收货单确认签单
     * 调用入库接口，改变采购收货单状态，改变采购收货单状态
     * 如果有自动流转总仓，则总仓数据一并更新
     *
     * @param purchaseReceiveOrder
     * @return
     */

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> confirmPurchaseReceiveOrder(PurchaseReceiveOrder purchaseReceiveOrder) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        PurchaseReceiveOrderDO purchaseReceiveOrderDO = purchaseReceiveOrderMapper.findAllByNo(purchaseReceiveOrder.getPurchaseReceiveNo());
        if (purchaseReceiveOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.PURCHASE_RECEIVE_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        //已入库的采购单不能再次入库
        if (PurchaseReceiveOrderStatus.PURCHASE_RECEIVE_ORDER_STATUS_YET.equals(purchaseReceiveOrderDO.getPurchaseReceiveOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.PURCHASE_RECEIVE_ORDER_STATUS_YET_CAN_NOT_IN_AGAIN);
            return serviceResult;
        }
        if (AutoAllotStatus.AUTO_ALLOT_STATUS_YES.equals(purchaseReceiveOrderDO.getAutoAllotStatus())) {
            serviceResult.setErrorCode(ErrorCode.PURCHASE_RECEIVE_ORDER_AUTO_ALLOT_YES_CAN_NOT_UPDATE);
            return serviceResult;
        }
        //组装入库接口数据
        ProductInStockParam productInStockParam = new ProductInStockParam();
        productInStockParam.setTargetWarehouseId(purchaseReceiveOrderDO.getWarehouseId());
        productInStockParam.setCauseType(StockCauseType.STOCK_CAUSE_TYPE_IN_PURCHASE);
        productInStockParam.setReferNo(purchaseReceiveOrderDO.getPurchaseReceiveNo());
        List<ProductInStorage> productInStorageList = new ArrayList<>();
        List<MaterialInStorage> materialInStorageList = new ArrayList<>();
        List<PurchaseReceiveOrderProductDO> productList = purchaseReceiveOrderDO.getPurchaseReceiveOrderProductDOList();
        List<PurchaseReceiveOrderMaterialDO> materialList = purchaseReceiveOrderDO.getPurchaseReceiveOrderMaterialDOList();
        //用来保存实际采购的sku及数量
        Map<Integer, PurchaseReceiveOrderProductDO> realSkuMap = new HashMap<>();
        //构建入库参数的同时，将数据保存到map一份，方便后面匹配采购单商品项
        for (PurchaseReceiveOrderProductDO purchaseReceiveOrderProductDO : productList) {
            if (CommonConstant.DATA_STATUS_ENABLE.equals(purchaseReceiveOrderProductDO.getDataStatus())) {
                ProductInStorage productInStorage = new ProductInStorage();
                productInStorage.setProductId(purchaseReceiveOrderProductDO.getRealProductId());
                productInStorage.setProductSkuId(purchaseReceiveOrderProductDO.getRealProductSkuId());
                productInStorage.setProductCount(purchaseReceiveOrderProductDO.getRealProductCount());
                productInStorage.setItemReferId(purchaseReceiveOrderProductDO.getId());
                String realProductSnapshot = purchaseReceiveOrderProductDO.getRealProductSnapshot();
                Product product = JSON.parseObject(realProductSnapshot, Product.class);
                productInStorage.setProductMaterialList(product.getProductSkuList().get(0).getProductMaterialList());
                productInStorage.setIsNew(purchaseReceiveOrderDO.getIsNew());
                productInStorageList.add(productInStorage);
            }
            realSkuMap.put(purchaseReceiveOrderProductDO.getRealProductSkuId(), purchaseReceiveOrderProductDO);
        }
        productInStockParam.setProductInStorageList(productInStorageList);
        //用来保存实际采购的物料及数量
        Map<Integer, PurchaseReceiveOrderMaterialDO> realMaterialMap = new HashMap<>();
        //构建入库参数的同时，将数据保存到map一份，方便后面匹配采购单物料项
        for (PurchaseReceiveOrderMaterialDO purchaseReceiveOrderMaterialDO : materialList) {
            if (CommonConstant.DATA_STATUS_ENABLE.equals(purchaseReceiveOrderMaterialDO.getDataStatus())) {
                MaterialInStorage materialInStorage = new MaterialInStorage();
                materialInStorage.setMaterialId(purchaseReceiveOrderMaterialDO.getRealMaterialId());
                materialInStorage.setMaterialCount(purchaseReceiveOrderMaterialDO.getRealMaterialCount());
                materialInStorage.setItemReferId(purchaseReceiveOrderMaterialDO.getId());
                materialInStorage.setIsNew(purchaseReceiveOrderDO.getIsNew());
                materialInStorageList.add(materialInStorage);
            }
            realMaterialMap.put(purchaseReceiveOrderMaterialDO.getRealMaterialId(), purchaseReceiveOrderMaterialDO);
        }
        productInStockParam.setProductInStorageList(productInStorageList);
        productInStockParam.setMaterialInStorageList(materialInStorageList);

        Date now = new Date();
        //更新采购收货单状态
        purchaseReceiveOrderDO.setPurchaseReceiveOrderStatus(PurchaseReceiveOrderStatus.PURCHASE_RECEIVE_ORDER_STATUS_YET);
        purchaseReceiveOrderDO.setConfirmTime(now);
        purchaseReceiveOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        purchaseReceiveOrderDO.setUpdateTime(now);
        purchaseReceiveOrderMapper.update(purchaseReceiveOrderDO);

        //如果是被总仓分拨的采购收货单，则总仓分拨单，分拨单项一并更新
        if (AutoAllotStatus.AUTO_ALLOT_STATUS_RECEIVE.equals(purchaseReceiveOrderDO.getAutoAllotStatus())) {
            PurchaseReceiveOrderDO autoPurchaseReceiveOrderDO = purchaseReceiveOrderMapper.findAutoAllotReceiveOrder(purchaseReceiveOrderDO.getAutoAllotNo());
            autoPurchaseReceiveOrderDO.setPurchaseReceiveOrderStatus(PurchaseReceiveOrderStatus.PURCHASE_RECEIVE_ORDER_STATUS_YET);
            autoPurchaseReceiveOrderDO.setConfirmTime(now);
            autoPurchaseReceiveOrderDO.setUpdateUser(CommonConstant.SUPER_USER_ID.toString());
            autoPurchaseReceiveOrderDO.setUpdateTime(now);
            purchaseReceiveOrderMapper.update(autoPurchaseReceiveOrderDO);
            List<PurchaseReceiveOrderProductDO> purchaseReceiveOrderProductDOList = autoPurchaseReceiveOrderDO.getPurchaseReceiveOrderProductDOList();
            //保存新列表的map，方便后面比对
            Map<Integer, PurchaseReceiveOrderProductDO> newSkuMap = new HashMap<>();
            for (PurchaseReceiveOrderProductDO autoPurchaseReceiveOrderProductDO : purchaseReceiveOrderProductDOList) {
                newSkuMap.put(autoPurchaseReceiveOrderProductDO.getRealProductSkuId(), autoPurchaseReceiveOrderProductDO);
                PurchaseReceiveOrderProductDO purchaseReceiveOrderProductDO = realSkuMap.get(autoPurchaseReceiveOrderProductDO.getProductSkuId());
                autoPurchaseReceiveOrderProductDO.setRealProductCount(purchaseReceiveOrderProductDO.getRealProductCount());
                autoPurchaseReceiveOrderProductDO.setRealProductId(purchaseReceiveOrderProductDO.getRealProductId());
                autoPurchaseReceiveOrderProductDO.setRealProductName(purchaseReceiveOrderProductDO.getRealProductName());
                autoPurchaseReceiveOrderProductDO.setRealProductSkuId(purchaseReceiveOrderProductDO.getRealProductSkuId());
                autoPurchaseReceiveOrderProductDO.setRealProductSnapshot(purchaseReceiveOrderProductDO.getRealProductSnapshot());
                autoPurchaseReceiveOrderProductDO.setUpdateTime(now);
                autoPurchaseReceiveOrderProductDO.setUpdateUser(CommonConstant.SUPER_USER_ID.toString());
                autoPurchaseReceiveOrderProductDO.setDataStatus(purchaseReceiveOrderProductDO.getDataStatus());
                purchaseReceiveOrderProductMapper.update(autoPurchaseReceiveOrderProductDO);
            }
            for (Integer skuId : realSkuMap.keySet()) {
                if (!newSkuMap.containsKey(skuId)) {
                    PurchaseReceiveOrderProductDO purchaseReceiveOrderProductDO = new PurchaseReceiveOrderProductDO();
                    BeanUtils.copyProperties(realSkuMap.get(skuId), purchaseReceiveOrderProductDO);
                    purchaseReceiveOrderProductDO.setPurchaseReceiveOrderId(autoPurchaseReceiveOrderDO.getId());
                    purchaseReceiveOrderProductDO.setCreateUser(userSupport.getCurrentUserId().toString());
                    purchaseReceiveOrderProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    purchaseReceiveOrderProductDO.setCreateTime(now);
                    purchaseReceiveOrderProductDO.setUpdateTime(now);
                    purchaseReceiveOrderProductDO.setId(null);
                    purchaseReceiveOrderProductMapper.save(purchaseReceiveOrderProductDO);
                }
            }

            List<PurchaseReceiveOrderMaterialDO> purchaseReceiveOrderMaterialDOList = autoPurchaseReceiveOrderDO.getPurchaseReceiveOrderMaterialDOList();
            //保存新列表的map，方便后面比对
            Map<Integer, PurchaseReceiveOrderMaterialDO> newMaterialMap = new HashMap<>();
            for (PurchaseReceiveOrderMaterialDO autoPurchaseReceiveOrderMaterialDO : purchaseReceiveOrderMaterialDOList) {
                newMaterialMap.put(autoPurchaseReceiveOrderMaterialDO.getRealMaterialId(), autoPurchaseReceiveOrderMaterialDO);
                PurchaseReceiveOrderMaterialDO purchaseReceiveOrderMaterialDO = realMaterialMap.get(autoPurchaseReceiveOrderMaterialDO.getMaterialId());
                purchaseReceiveOrderMaterialDO.setRealMaterialCount(purchaseReceiveOrderMaterialDO.getRealMaterialCount());
                purchaseReceiveOrderMaterialDO.setRealMaterialId(purchaseReceiveOrderMaterialDO.getRealMaterialId());
                purchaseReceiveOrderMaterialDO.setRealMaterialName(purchaseReceiveOrderMaterialDO.getRealMaterialName());
                purchaseReceiveOrderMaterialDO.setRealMaterialSnapshot(purchaseReceiveOrderMaterialDO.getRealMaterialSnapshot());
                purchaseReceiveOrderMaterialDO.setUpdateTime(now);
                purchaseReceiveOrderMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                purchaseReceiveOrderMaterialDO.setDataStatus(purchaseReceiveOrderMaterialDO.getDataStatus());
                purchaseReceiveOrderMaterialMapper.update(purchaseReceiveOrderMaterialDO);
            }
            for (Integer materialId : newMaterialMap.keySet()) {
                if (!newMaterialMap.containsKey(materialId)) {
                    PurchaseReceiveOrderMaterialDO purchaseReceiveOrderMaterialDO = new PurchaseReceiveOrderMaterialDO();
                    BeanUtils.copyProperties(realSkuMap.get(materialId), purchaseReceiveOrderMaterialDO);
                    purchaseReceiveOrderMaterialDO.setPurchaseReceiveOrderId(autoPurchaseReceiveOrderDO.getId());
                    purchaseReceiveOrderMaterialDO.setCreateUser(userSupport.getCurrentUserId().toString());
                    purchaseReceiveOrderMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    purchaseReceiveOrderMaterialDO.setCreateTime(now);
                    purchaseReceiveOrderMaterialDO.setUpdateTime(now);
                    purchaseReceiveOrderMaterialDO.setId(null);
                    purchaseReceiveOrderMaterialMapper.save(purchaseReceiveOrderMaterialDO);
                }
            }
        }

        //判断采购单是否全部采购，更新采购单状态
        PurchaseOrderDO purchaseOrderDO = purchaseOrderMapper.findDetailById(purchaseReceiveOrderDO.getPurchaseOrderId());
        List<PurchaseOrderProductDO> purchaseOrderProductDOList = purchaseOrderDO.getPurchaseOrderProductDOList();
        //循环中临时记录循环的前面所有项的综合采购状态
//        Integer lastPurchaseOrderStatus = PurchaseOrderStatus.PURCHASE_ORDER_STATUS_PURCHASING;
        Integer stateInt = 1;
        Integer totalCount = 0;
        for (PurchaseOrderProductDO purchaseOrderProductDO : purchaseOrderProductDOList) {

            PurchaseReceiveOrderProductDO purchaseReceiveOrderProductDO = realSkuMap.get(purchaseOrderProductDO.getProductSkuId());
            //如果待采购商品项 在 已采购商品列表中
            if (purchaseReceiveOrderProductDO != null) {
                totalCount += purchaseReceiveOrderProductDO.getRealProductCount();
                //如果实际采购商品数小于采购单商品项的商品数
                if (purchaseReceiveOrderProductDO.getRealProductCount() < purchaseOrderProductDO.getProductCount()) {
                    stateInt = stateInt & 0;
//                    lastPurchaseOrderStatus = PurchaseOrderStatus.PURCHASE_ORDER_STATUS_PART;
                } else {
                    //如果实际采购商品数大于等于采购单商品项的商品数
                    //并且目前采购单状态为待采购状态，则更新采购状态为全部采购,如果是全部采购状态，不需要处理
//                    lastPurchaseOrderStatus = PurchaseOrderStatus.PURCHASE_ORDER_STATUS_ALL;
                    stateInt = stateInt & 1;
                }
            }
        }

        List<PurchaseOrderMaterialDO> purchaseOrderMaterialDOList = purchaseOrderDO.getPurchaseOrderMaterialDOList();
        //循环中临时记录循环的前面所有项的综合采购状态
        for (PurchaseOrderMaterialDO purchaseOrderMaterialDO : purchaseOrderMaterialDOList) {

            PurchaseReceiveOrderMaterialDO purchaseReceiveOrderMaterialDO = realMaterialMap.get(purchaseOrderMaterialDO.getMaterialId());
            //如果待采购物料项 在 已采购物料列表中
            if (purchaseReceiveOrderMaterialDO != null) {
                totalCount += purchaseReceiveOrderMaterialDO.getRealMaterialCount();
                //如果实际采购商品数小于采购单物料项的数量
                if (purchaseReceiveOrderMaterialDO.getRealMaterialCount() < purchaseOrderMaterialDO.getMaterialCount()) {
                    stateInt = stateInt & 0;
                } else {
                    //如果实际采购商品数大于等于采购单商品项的商品数
                    //并且目前采购单状态为待采购状态，则更新采购状态为全部采购,如果是全部采购状态，不需要处理
                    stateInt = stateInt & 1;
                }
            }
        }
        Integer finalStatus = null;
        if (stateInt == 1) {
            finalStatus = PurchaseOrderStatus.PURCHASE_ORDER_STATUS_ALL;
        } else if (totalCount == 0) {
            finalStatus = PurchaseOrderStatus.PURCHASE_ORDER_STATUS_PURCHASING;
        } else {
            finalStatus = PurchaseOrderStatus.PURCHASE_ORDER_STATUS_PART;
        }
        purchaseOrderDO.setPurchaseOrderStatus(finalStatus);
        purchaseOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        purchaseOrderDO.setUpdateTime(now);
        purchaseOrderMapper.update(purchaseOrderDO);
        //调用入库接口
        ServiceResult<String, Integer> inStockResult = warehouseService.productInStock(productInStockParam);
        if (!ErrorCode.SUCCESS.equals(inStockResult.getErrorCode())) {
            System.out.println("inStockResult.getErrorCode())"+inStockResult.getErrorCode());
            System.out.println("-----------------回滚-------------------");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            serviceResult.setErrorCode(inStockResult.getErrorCode());
            return serviceResult;
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(purchaseReceiveOrder.getPurchaseReceiveNo());
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Page<PurchaseReceiveOrder>> pagePurchaseReceive(PurchaseReceiveOrderQueryParam purchaseReceiveOrderQueryParam) {
        ServiceResult<String, Page<PurchaseReceiveOrder>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(purchaseReceiveOrderQueryParam.getPageNo(), purchaseReceiveOrderQueryParam.getPageSize());
        //将3个ID的参数转为No
        purchaseReceiveOrderQueryParam.setPurchaseOrderId(null);
        purchaseReceiveOrderQueryParam.setWarehouseId(null);
        purchaseReceiveOrderQueryParam.setPurchaseDeliveryOrderId(null);
        Page<PurchaseReceiveOrder> nullPage = new Page<>(new ArrayList<PurchaseReceiveOrder>(), 0, purchaseReceiveOrderQueryParam.getPageNo(), purchaseReceiveOrderQueryParam.getPageSize());
        if (StringUtil.isNotEmpty(purchaseReceiveOrderQueryParam.getPurchaseNo())) {
            PurchaseOrderDO purchaseOrderDO = purchaseOrderMapper.findByPurchaseNo(purchaseReceiveOrderQueryParam.getPurchaseNo());
            if (purchaseOrderDO == null) {
                result.setErrorCode(ErrorCode.SUCCESS);
                result.setResult(nullPage);
                return result;
            }
            purchaseReceiveOrderQueryParam.setPurchaseOrderId(purchaseOrderDO.getId());
        }
        if (StringUtil.isNotEmpty(purchaseReceiveOrderQueryParam.getWarehouseNo())) {
            WarehouseDO warehouseDO = warehouseMapper.finByNo(purchaseReceiveOrderQueryParam.getWarehouseNo());
            if (warehouseDO == null) {
                result.setErrorCode(ErrorCode.SUCCESS);
                result.setResult(nullPage);
                return result;
            }
            purchaseReceiveOrderQueryParam.setWarehouseId(warehouseDO.getId());
        }
        if (StringUtil.isNotEmpty(purchaseReceiveOrderQueryParam.getPurchaseDeliveryNo())) {
            PurchaseDeliveryOrderDO purchaseDeliveryOrderDO = purchaseDeliveryOrderMapper.findByNo(purchaseReceiveOrderQueryParam.getPurchaseDeliveryNo());
            if (purchaseDeliveryOrderDO == null) {
                result.setErrorCode(ErrorCode.SUCCESS);
                result.setResult(nullPage);
                return result;
            }
            purchaseReceiveOrderQueryParam.setPurchaseDeliveryOrderId(purchaseDeliveryOrderDO.getId());
        }

        //数据级权限控制-查找用户可查看用户列表
        Integer currentUserId = userSupport.getCurrentUserId();
        //获取用户最【新的】最终可观察用户列表
        List<UserDO> userDOList = userMapper.getPassiveUserByUser(currentUserId);
        //数据级权限控制-查找用户可查看仓库列表
        ServiceResult<String,List<Warehouse>> warehouseListResult = warehouseService.getAvailableWarehouse();
        if(!ErrorCode.SUCCESS.equals(warehouseListResult.getErrorCode())){
            result.setErrorCode(ErrorCode.SYSTEM_ERROR);
            return result;
        }
        List<Warehouse> warehouseList = warehouseListResult.getResult();
        List<Integer> passiveUserIdList = new ArrayList<>();
        List<Integer> warehouseIdList = new ArrayList<>();
        if(CollectionUtil.isEmpty(userDOList)&&CollectionUtil.isEmpty(warehouseList)){
            result.setErrorCode(ErrorCode.SUCCESS);
            Page<PurchaseReceiveOrder> page = new Page<>(new ArrayList<PurchaseReceiveOrder>(), 0, purchaseReceiveOrderQueryParam.getPageNo(), purchaseReceiveOrderQueryParam.getPageSize());
            result.setResult(page);
            return result;
        }
        if(CollectionUtil.isNotEmpty(userDOList)){
            for(UserDO userDO : userDOList){
                passiveUserIdList.add(userDO.getId());
            }
        }
        if(CollectionUtil.isNotEmpty(warehouseList)){
            for(Warehouse warehouse : warehouseList){
                warehouseIdList.add(warehouse.getWarehouseId());
            }
        }
        purchaseReceiveOrderQueryParam.setPassiveUserIdList(passiveUserIdList);
        purchaseReceiveOrderQueryParam.setWarehouseIdList(warehouseIdList);

        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("queryParam", purchaseReceiveOrderQueryParam);

        Integer totalCount = purchaseReceiveOrderMapper.findPurchaseReceiveOrderCountByParams(maps);
        List<PurchaseReceiveOrderDO> purchaseReceiveOrderDOList = purchaseReceiveOrderMapper.findPurchaseReceiveOrderByParams(maps);
        List<PurchaseReceiveOrder> purchaseReceiveOrderList = ConverterUtil.convertList(purchaseReceiveOrderDOList, PurchaseReceiveOrder.class);
        Page<PurchaseReceiveOrder> page = new Page<>(purchaseReceiveOrderList, totalCount, purchaseReceiveOrderQueryParam.getPageNo(), purchaseReceiveOrderQueryParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, PurchaseReceiveOrder> queryPurchaseReceiveOrderByNo(PurchaseReceiveOrder purchaseReceiveOrder) {
        ServiceResult<String, PurchaseReceiveOrder> serviceResult = new ServiceResult<>();
        PurchaseReceiveOrderDO purchaseReceiveOrderDO = purchaseReceiveOrderMapper.findByNo(purchaseReceiveOrder.getPurchaseReceiveNo());
        if (purchaseReceiveOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.PURCHASE_RECEIVE_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(ConverterUtil.convert(purchaseReceiveOrderDO, PurchaseReceiveOrder.class));
        return serviceResult;
    }

    /**
     * 采购单结束
     * 只有部分采购和全部采购的采购单 可以结束采购单
     *
     * @param purchaseOrder
     * @return
     */
    @Override
    public ServiceResult<String, String> endPurchaseOrder(PurchaseOrder purchaseOrder) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        PurchaseOrderDO purchaseOrderDO = purchaseOrderMapper.findByPurchaseNo(purchaseOrder.getPurchaseNo());
        if (purchaseOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.PURCHASE_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        //只有部分采购 和 全部采购的采购单 可以结束采购单
        if (!PurchaseOrderStatus.PURCHASE_ORDER_STATUS_PART.equals(purchaseOrderDO.getPurchaseOrderStatus())
                && !PurchaseOrderStatus.PURCHASE_ORDER_STATUS_ALL.equals(purchaseOrderDO.getPurchaseOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.PURCHASE_ORDER_STATUS_CAN_NOT_END);
            return serviceResult;
        }

        //获取采购单下所有采购设备
        List<ProductEquipmentDO> productEquipmentDOList = productEquipmentMapper.findByPurchaseOrderNo(purchaseOrderDO.getPurchaseNo());
        //获取采购单下所有采购物料
        List<BulkMaterialDO> bulkMaterialDOList = bulkMaterialMapper.findByPurchaseOrderNo(purchaseOrderDO.getPurchaseNo());

        //全部收料定价后，才可结束
        if (CollectionUtil.isNotEmpty(productEquipmentDOList)) {
            for (ProductEquipmentDO productEquipmentDO : productEquipmentDOList) {
                if (productEquipmentDO.getPurchasePrice() == null) {
                    serviceResult.setErrorCode(ErrorCode.EQUIPMENT_PURCHASE_PRICE_NOT_NULL, productEquipmentDO.getEquipmentNo());
                    return serviceResult;
                }
            }
        }
        if (CollectionUtil.isNotEmpty(bulkMaterialDOList)) {
            for (BulkMaterialDO bulkMaterialDO : bulkMaterialDOList) {
                if (bulkMaterialDO.getPurchasePrice() == null) {
                    serviceResult.setErrorCode(ErrorCode.BULK_PURCHASE_PRICE_NOT_NULL, bulkMaterialDO.getBulkMaterialNo());
                    return serviceResult;
                }
            }
        }
        purchaseOrderDO.setPurchaseOrderStatus(PurchaseOrderStatus.PURCHASE_ORDER_STATUS_END);
        purchaseOrderDO.setUpdateTime(new Date());
        purchaseOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        purchaseOrderMapper.update(purchaseOrderDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(purchaseOrderDO.getPurchaseNo());
        return serviceResult;
    }

    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    @Override
    public ServiceResult<String, String> continuePurchaseOrder(PurchaseOrder purchaseOrder) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        PurchaseOrderDO purchaseOrderDO = purchaseOrderMapper.findDetailByPurchaseNo(purchaseOrder.getPurchaseNo());
        if (purchaseOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.PURCHASE_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        //只有部分采购的采购单可以继续采购
        if (PurchaseOrderStatus.PURCHASE_ORDER_STATUS_PART.equals(purchaseOrderDO.getPurchaseOrderStatus())) {
            //查询采购单的所有收货单列表
            List<PurchaseReceiveOrderDO> purchaseReceiveOrderDOList = purchaseReceiveOrderMapper.findListByPurchaseId(purchaseOrderDO.getId());
            //用来保存所有已签单的采购收货单实际已收到的sku总数
            Map<Integer, Integer> skuCountMap = new HashMap<>();
            //用来保存所有已签单的采购收货单实际已收到的sku总数
            Map<Integer, Integer> materialCountMap = new HashMap<>();
            for (PurchaseReceiveOrderDO purchaseReceiveOrderDO : purchaseReceiveOrderDOList) {
                //只累加已签单的sku，并且非自动流转总仓的
                if (PurchaseReceiveOrderStatus.PURCHASE_RECEIVE_ORDER_STATUS_YET.equals(purchaseReceiveOrderDO.getPurchaseReceiveOrderStatus())
                        && !AutoAllotStatus.AUTO_ALLOT_STATUS_YES.equals(purchaseReceiveOrderDO.getAutoAllotStatus())) {
                    //找到的所有采购单项sku，累加数量
                    List<PurchaseReceiveOrderProductDO> purchaseReceiveOrderProductDOList = purchaseReceiveOrderDO.getPurchaseReceiveOrderProductDOList();
                    for (PurchaseReceiveOrderProductDO purchaseReceiveOrderProductDO : purchaseReceiveOrderProductDOList) {
                        Integer countNow = skuCountMap.get(purchaseReceiveOrderProductDO.getRealProductSkuId());
                        if (countNow == null) {
                            skuCountMap.put(purchaseReceiveOrderProductDO.getRealProductSkuId(), 0);
                        }
                        skuCountMap.put(purchaseReceiveOrderProductDO.getRealProductSkuId(), skuCountMap.get(purchaseReceiveOrderProductDO.getRealProductSkuId()) + purchaseReceiveOrderProductDO.getRealProductCount());
                    }
                    //找到所采购单物料项，累加数量
                    List<PurchaseReceiveOrderMaterialDO> purchaseReceiveOrderMaterialDOList = purchaseReceiveOrderDO.getPurchaseReceiveOrderMaterialDOList();
                    for (PurchaseReceiveOrderMaterialDO purchaseReceiveOrderMaterialDO : purchaseReceiveOrderMaterialDOList) {
                        Integer countNow = skuCountMap.get(purchaseReceiveOrderMaterialDO.getMaterialId());
                        if (countNow == null) {
                            skuCountMap.put(purchaseReceiveOrderMaterialDO.getRealMaterialId(), 0);
                        }
                        materialCountMap.put(purchaseReceiveOrderMaterialDO.getRealMaterialId(), countNow + purchaseReceiveOrderMaterialDO.getRealMaterialCount());
                    }
                }
            }
            List<PurchaseOrderProductDO> purchaseOrderProductDOList = purchaseOrderDO.getPurchaseOrderProductDOList();
            //找出没有完成的采购单项，并计算未采购完成的数量
            for (PurchaseOrderProductDO purchaseOrderProductDO : purchaseOrderProductDOList) {
                Integer skuCount = skuCountMap.get(purchaseOrderProductDO.getProductSkuId());
                skuCount = skuCount == null ? 0 : skuCount;
                purchaseOrderProductDO.setProductCount(purchaseOrderProductDO.getProductCount() - skuCount);
            }

            List<PurchaseOrderMaterialDO> purchaseOrderMaterialDOList = purchaseOrderDO.getPurchaseOrderMaterialDOList();
            //找出没有完成的采购单物料项，并计算未采购完成的数量
            for (PurchaseOrderMaterialDO purchaseOrderMaterialDO : purchaseOrderMaterialDOList) {
                Integer materialCount = materialCountMap.get(purchaseOrderMaterialDO.getMaterialId());
                materialCount = materialCount == null ? 0 : materialCount;
                purchaseOrderMaterialDO.setMaterialCount(purchaseOrderMaterialDO.getMaterialCount() - materialCount);
            }
            //类似采购单审核之后的流程，自动生成发货通知单、收货通知单

            createPurchaseDeliveryAndReceiveOrder(purchaseOrderDO);
            serviceResult.setErrorCode(ErrorCode.SUCCESS);
            serviceResult.setResult(purchaseOrderDO.getPurchaseNo());
        } else {
            serviceResult.setErrorCode(ErrorCode.PURCHASE_ORDER_STATUS_CAN_NOT_CONTINUE);
        }
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> updateReceiveEquipmentPrice(UpdatePurchaseReceiveEquipmentPriceParam updatePurchaseReceiveEquipmentPriceParam) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        PurchaseReceiveOrderDO purchaseReceiveOrderDO = purchaseReceiveOrderMapper.findByNo(updatePurchaseReceiveEquipmentPriceParam.getPurchaseReceiveOrderNo());
        if (purchaseReceiveOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.PURCHASE_RECEIVE_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        if (!PurchaseReceiveOrderStatus.PURCHASE_RECEIVE_ORDER_STATUS_COMMITTED.equals(purchaseReceiveOrderDO.getPurchaseReceiveOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.PURCHASE_RECEIVE_ORDER_STATUS_CAN_NOT_UPDATE);
            return serviceResult;
        }
        List<ProductEquipment> productEquipmentList = updatePurchaseReceiveEquipmentPriceParam.getEquipmentList();
        List<BulkMaterial> bulkMaterialList = updatePurchaseReceiveEquipmentPriceParam.getBulkMaterialList();

        if (CollectionUtil.isEmpty(productEquipmentList) &&
                CollectionUtil.isEmpty(bulkMaterialList)) {
            serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return serviceResult;
        }
        String stockOrderNo = stockOrderMapper.findNoByTypeAndRefer(StockCauseType.STOCK_CAUSE_TYPE_IN_PURCHASE, purchaseReceiveOrderDO.getPurchaseReceiveNo());
        Date now = new Date();
        //累加总价
        BigDecimal totalAmount = purchaseReceiveOrderDO.getTotalAmount() == null ? BigDecimal.ZERO : purchaseReceiveOrderDO.getTotalAmount();
        if (CollectionUtil.isNotEmpty(productEquipmentList)) {
            List<StockOrderEquipmentDO> stockOrderEquipmentDOList = stockOrderEquipmentMapper.findByStockOrderNo(stockOrderNo);
            Map<Integer, StockOrderEquipmentDO> purchaseOrderProductDOMap = ListUtil.listToMap(stockOrderEquipmentDOList, "equipmentNo");
            for (ProductEquipment productEquipment : productEquipmentList) {
                if (purchaseOrderProductDOMap.get(productEquipment.getEquipmentNo()) == null) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.EQUIPMENT_NOT_EXISTS);
                    return serviceResult;
                }
                ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(productEquipment.getEquipmentNo());
                //累加原采购价，减去原采购价
                totalAmount = BigDecimalUtil.add(totalAmount, productEquipment.getPurchasePrice());
                totalAmount = BigDecimalUtil.sub(totalAmount, productEquipmentDO.getPurchasePrice());
                productEquipmentDO.setPurchasePrice(productEquipment.getPurchasePrice());
                productEquipmentDO.setUpdateTime(now);
                productEquipmentDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                productEquipmentMapper.update(productEquipmentDO);
            }
        }
        if (CollectionUtil.isNotEmpty(bulkMaterialList)) {
            List<StockOrderBulkMaterialDO> stockOrderBulkMaterialDOList = stockOrderBulkMaterialMapper.findByStockOrderNo(stockOrderNo);
            Map<Integer, StockOrderBulkMaterialDO> purchaseOrderMaterialDOMap = ListUtil.listToMap(stockOrderBulkMaterialDOList, "bulkMaterialNo");
            for (BulkMaterial bulkMaterial : bulkMaterialList) {
                if (purchaseOrderMaterialDOMap.get(bulkMaterial.getBulkMaterialNo()) == null) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.EQUIPMENT_NOT_EXISTS);
                    return serviceResult;
                }
                BulkMaterialDO bulkMaterialDO = bulkMaterialMapper.findByNo(bulkMaterial.getBulkMaterialNo());
                //累加原采购价，减去原采购价
                totalAmount = BigDecimalUtil.add(totalAmount, bulkMaterial.getPurchasePrice());
                totalAmount = BigDecimalUtil.sub(totalAmount, bulkMaterialDO.getPurchasePrice());
                bulkMaterialDO.setPurchasePrice(bulkMaterial.getPurchasePrice());
                bulkMaterialDO.setUpdateTime(now);
                bulkMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                bulkMaterialMapper.update(bulkMaterialDO);
            }
        }
        purchaseReceiveOrderDO.setTotalAmount(totalAmount);
        purchaseReceiveOrderDO.setUpdateTime(now);
        purchaseReceiveOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(purchaseReceiveOrderDO.getPurchaseReceiveNo());
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Page<ProductEquipment>> pageReceiveOrderProductEquipment(PurchaseReceiveOrderProductEquipmentPageParam pageParam) {
        ServiceResult<String, Page<ProductEquipment>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(pageParam.getPageNo(), pageParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("queryParam", pageParam);

        Integer totalCount = productEquipmentMapper.listByPurchaseReceiveOrderProductIdCount(maps);
        List<ProductEquipmentDO> productEquipmentDOList = productEquipmentMapper.listByPurchaseReceiveOrderProductId(maps);
        List<ProductEquipment> productEquipmentList = ConverterUtil.convertList(productEquipmentDOList, ProductEquipment.class);
        Page<ProductEquipment> page = new Page<>(productEquipmentList, totalCount, pageParam.getPageNo(), pageParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, Page<BulkMaterial>> pageReceiveOrderMaterialBulk(PurchaseReceiveOrderMaterialBulkPageParam pageParam) {
        ServiceResult<String, Page<BulkMaterial>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(pageParam.getPageNo(), pageParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("queryParam", pageParam);

        Integer totalCount = bulkMaterialMapper.listByPurchaseReceiveOrderMaterialIdCount(maps);
        List<BulkMaterialDO> bulkMaterialDOList = bulkMaterialMapper.listByPurchaseReceiveOrderMaterialId(maps);
        List<BulkMaterial> bulkMaterialList = ConverterUtil.convertList(bulkMaterialDOList, BulkMaterial.class);
        Page<BulkMaterial> page = new Page<>(bulkMaterialList, totalCount, pageParam.getPageNo(), pageParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, String> updateReceiveEquipmentRemark(UpdateReceiveEquipmentRemarkParam updateReceiveEquipmentRemarkParam) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        PurchaseReceiveOrderDO purchaseReceiveOrderDO = purchaseReceiveOrderMapper.findByNo(updateReceiveEquipmentRemarkParam.getPurchaseReceiveOrderNo());
        if (purchaseReceiveOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.PURCHASE_RECEIVE_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        if (!PurchaseReceiveOrderStatus.PURCHASE_RECEIVE_ORDER_STATUS_YET.equals(purchaseReceiveOrderDO.getPurchaseReceiveOrderStatus()) &&
                !PurchaseReceiveOrderStatus.PURCHASE_RECEIVE_ORDER_STATUS_COMMITTED.equals(purchaseReceiveOrderDO.getPurchaseReceiveOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.PURCHASE_RECEIVE_ORDER_STATUS_CAN_NOT_UPDATE);
            return serviceResult;
        }
        ProductEquipment productEquipment = updateReceiveEquipmentRemarkParam.getProductEquipment();
        ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(productEquipment.getEquipmentNo());
        if (productEquipmentDO == null) {
            serviceResult.setErrorCode(ErrorCode.EQUIPMENT_NOT_EXISTS);
            return serviceResult;
        }
        productEquipmentDO.setPurchaseReceiveRemark(productEquipment.getPurchaseReceiveRemark());
        productEquipmentDO.setUpdateTime(new Date());
        productEquipmentDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        productEquipmentMapper.update(productEquipmentDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(productEquipmentDO.getEquipmentNo());
        return serviceResult;
    }

    @Override
    public ServiceResult<String, String> commitPurchaseReceiveOrder(PurchaseReceiveOrder purchaseReceiveOrder) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        PurchaseReceiveOrderDO purchaseReceiveOrderDO = purchaseReceiveOrderMapper.findAllByNo(purchaseReceiveOrder.getPurchaseReceiveNo());
        if (purchaseReceiveOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.PURCHASE_RECEIVE_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        if (!PurchaseReceiveOrderStatus.PURCHASE_RECEIVE_ORDER_STATUS_YET.equals(purchaseReceiveOrderDO.getPurchaseReceiveOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.PURCHASE_RECEIVE_ORDER_STATUS_YET_CAN_NOT_COMMIT);
            return serviceResult;
        }
        purchaseReceiveOrderDO.setPurchaseReceiveOrderStatus(PurchaseReceiveOrderStatus.PURCHASE_RECEIVE_ORDER_STATUS_COMMITTED);
        purchaseReceiveOrderDO.setUpdateTime(new Date());
        purchaseReceiveOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        purchaseReceiveOrderMapper.update(purchaseReceiveOrderDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(purchaseReceiveOrderDO.getPurchaseReceiveNo());
        return serviceResult;
    }

    @Override
    public ServiceResult<String, List<PurchaseReceiveOrderMaterialPrice>> getPurchaseReceiveMaterialPriceList(PurchaseReceiveOrderMaterial purchaseReceiveOrderMaterial) {
        ServiceResult<String, List<PurchaseReceiveOrderMaterialPrice>> serviceResult = new ServiceResult<>();
        PurchaseReceiveOrderMaterialDO purchaseReceiveOrderMaterialDO = purchaseReceiveOrderMaterialMapper.findById(purchaseReceiveOrderMaterial.getPurchaseReceiveOrderMaterialId());
        if (purchaseReceiveOrderMaterialDO == null) {
            serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return serviceResult;
        }
        List<BulkMaterialDO> bulkMaterialDOList = getBulkMaterialDOList(purchaseReceiveOrderMaterialDO.getId());
        List<PurchaseReceiveOrderMaterialPrice> purchaseReceiveOrderMaterialPriceList = new ArrayList<>();
        Map<BigDecimal, Integer> purchaseReceiveOrderMaterialPriceMap = new HashMap<>();
        for (BulkMaterialDO bulkMaterialDO : bulkMaterialDOList) {
            if (bulkMaterialDO.getPurchasePrice() != null) {
                if (purchaseReceiveOrderMaterialPriceMap.get(bulkMaterialDO.getPurchasePrice()) == null) {
                    purchaseReceiveOrderMaterialPriceMap.put(bulkMaterialDO.getPurchasePrice(), 0);
                }
                purchaseReceiveOrderMaterialPriceMap.put(bulkMaterialDO.getPurchasePrice(), purchaseReceiveOrderMaterialPriceMap.get(bulkMaterialDO.getPurchasePrice()) + 1);
            }
        }
        for (BigDecimal bigDecimal : purchaseReceiveOrderMaterialPriceMap.keySet()) {
            PurchaseReceiveOrderMaterialPrice purchaseReceiveOrderMaterialPrice = new PurchaseReceiveOrderMaterialPrice();
            purchaseReceiveOrderMaterialPrice.setPrice(bigDecimal);
            purchaseReceiveOrderMaterialPrice.setCount(purchaseReceiveOrderMaterialPriceMap.get(bigDecimal));
            purchaseReceiveOrderMaterialPriceList.add(purchaseReceiveOrderMaterialPrice);
        }
        serviceResult.setResult(purchaseReceiveOrderMaterialPriceList);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Integer> updatePurchaseReceiveMaterialPrice(UpdatePurchaseReceiveMaterialPriceParam updatePurchaseReceiveMaterialPriceParam) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        PurchaseReceiveOrderMaterialDO purchaseReceiveOrderMaterialDO = purchaseReceiveOrderMaterialMapper.findById(updatePurchaseReceiveMaterialPriceParam.getPurchaseReceiveOrderMaterialId());
        if (purchaseReceiveOrderMaterialDO == null) {
            serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return serviceResult;
        }
        List<BulkMaterialDO> bulkMaterialDOList = getBulkMaterialDOList(purchaseReceiveOrderMaterialDO.getId());
        if (CollectionUtil.isEmpty(bulkMaterialDOList)) {
            serviceResult.setErrorCode(ErrorCode.PURCHASE_RECEIVE_ORDER_MATERIAL_PRICE_NOT_NEED_UPDATE);
            return serviceResult;
        }
        PurchaseReceiveOrderDO purchaseReceiveOrderDO = purchaseReceiveOrderMapper.findById(purchaseReceiveOrderMaterialDO.getPurchaseReceiveOrderId());
        if (PurchaseReceiveOrderStatus.PURCHASE_RECEIVE_ORDER_STATUS_WAIT.equals(purchaseReceiveOrderDO.getPurchaseReceiveOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.PURCHASE_RECEIVE_ORDER_STATUS_CAN_NOT_UPDATE);
            return serviceResult;
        }
        List<PurchaseReceiveOrderMaterialPrice> purchaseReceiveOrderMaterialPriceList = updatePurchaseReceiveMaterialPriceParam.getPurchaseReceiveOrderMaterialPriceList();
        Integer total = 0;
        for (PurchaseReceiveOrderMaterialPrice purchaseReceiveOrderMaterialPrice : purchaseReceiveOrderMaterialPriceList) {
            total += purchaseReceiveOrderMaterialPrice.getCount();
        }
        if (!total.equals(bulkMaterialDOList.size())) {
            serviceResult.setErrorCode(ErrorCode.UPDATE_ITEM_COUNT_ERROR);
            return serviceResult;
        }
        Date now = new Date();

        BigDecimal totalAmount = purchaseReceiveOrderDO.getTotalAmount() == null ? BigDecimal.ZERO : purchaseReceiveOrderDO.getTotalAmount();

        int index = 0;
        for (PurchaseReceiveOrderMaterialPrice purchaseReceiveOrderMaterialPrice : purchaseReceiveOrderMaterialPriceList) {
            int count = purchaseReceiveOrderMaterialPrice.getCount();
            for (int i = 0; i < count; i++) {
                BulkMaterialDO bulkMaterialDO = bulkMaterialDOList.get(index++);
                //累加原采购价，减去原采购价
                totalAmount = BigDecimalUtil.add(totalAmount, purchaseReceiveOrderMaterialPrice.getPrice());
                totalAmount = BigDecimalUtil.sub(totalAmount, bulkMaterialDO.getPurchasePrice());
                bulkMaterialDO.setPurchasePrice(purchaseReceiveOrderMaterialPrice.getPrice());
                bulkMaterialDO.setUpdateTime(now);
                bulkMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            }
        }
        bulkMaterialMapper.updateList(bulkMaterialDOList);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(purchaseReceiveOrderMaterialDO.getId());
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> updatePurchaseReceiveMaterialRemark(UpdateReceiveMaterialRemarkParam updateReceiveMaterialRemarkParam) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        PurchaseReceiveOrderDO purchaseReceiveOrderDO = purchaseReceiveOrderMapper.findAllByNo(updateReceiveMaterialRemarkParam.getPurchaseReceiveOrderNo());
        if (purchaseReceiveOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.PURCHASE_RECEIVE_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        if (PurchaseReceiveOrderStatus.PURCHASE_RECEIVE_ORDER_STATUS_WAIT.equals(purchaseReceiveOrderDO.getPurchaseReceiveOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.PURCHASE_RECEIVE_ORDER_STATUS_CAN_NOT_UPDATE);
            return serviceResult;
        }
        List<PurchaseReceiveOrderMaterialDO> purchaseReceiveOrderMaterialDOList = purchaseReceiveOrderDO.getPurchaseReceiveOrderMaterialDOList();
        //获取数据库采购收货单物料项
        Map<Integer, PurchaseReceiveOrderMaterialDO> purchaseReceiveOrderMaterialDOMap = ListUtil.listToMap(purchaseReceiveOrderMaterialDOList, "id");
        //要修改的项
        List<PurchaseReceiveOrderMaterialDO> purchaseReceiveOrderMaterialDOListForUpdate = purchaseReceiveOrderDO.getPurchaseReceiveOrderMaterialDOList();
        //传入修改的采购收货单物料项
        List<PurchaseReceiveOrderMaterial> purchaseReceiveOrderMaterialList = updateReceiveMaterialRemarkParam.getPurchaseReceiveOrderMaterialList();
        Date now = new Date();
        for (PurchaseReceiveOrderMaterial purchaseReceiveOrderMaterial : purchaseReceiveOrderMaterialList) {
            PurchaseReceiveOrderMaterialDO purchaseReceiveOrderMaterialDO = purchaseReceiveOrderMaterialDOMap.get(purchaseReceiveOrderMaterial.getPurchaseReceiveOrderMaterialId());
            if (purchaseReceiveOrderMaterialDO == null) {
                serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
                return serviceResult;
            }
            if (StringUtil.isNotEmpty(purchaseReceiveOrderMaterial.getRemark())) {
                purchaseReceiveOrderMaterialDO.setRemark(purchaseReceiveOrderMaterial.getRemark());
                purchaseReceiveOrderMaterialDO.setUpdateTime(now);
                purchaseReceiveOrderMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                purchaseReceiveOrderMaterialDOListForUpdate.add(purchaseReceiveOrderMaterialDO);
            }
        }
        for (PurchaseReceiveOrderMaterialDO purchaseReceiveOrderMaterialDO : purchaseReceiveOrderMaterialDOListForUpdate) {
            purchaseReceiveOrderMaterialMapper.update(purchaseReceiveOrderMaterialDO);
        }
        serviceResult.setResult(purchaseReceiveOrderDO.getPurchaseReceiveNo());
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    private List<BulkMaterialDO> getBulkMaterialDOList(Integer purchaseReceiveOrderMaterialId) {
        PurchaseReceiveOrderMaterialBulkPageParam purchaseReceiveOrderMaterialBulkPageParam = new PurchaseReceiveOrderMaterialBulkPageParam();
        purchaseReceiveOrderMaterialBulkPageParam.setPurchaseReceiveOrderMaterialId(purchaseReceiveOrderMaterialId);
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", 0);
        maps.put("pageSize", Integer.MAX_VALUE);
        maps.put("queryParam", purchaseReceiveOrderMaterialBulkPageParam);
        return bulkMaterialMapper.listByPurchaseReceiveOrderMaterialId(maps);
    }

    /**
     * 接收审核结果通知，审核通过生成发货单
     *
     * @param verifyResult
     * @param businessNo
     */
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    @Override
    public boolean receiveVerifyResult(boolean verifyResult, String businessNo) {
        try {
            PurchaseOrderDO purchaseOrderDO = purchaseOrderMapper.findDetailByPurchaseNo(businessNo);
            if (purchaseOrderDO == null) {
                return false;
            }
            //不是审核中状态的采购单，拒绝处理
            if (!PurchaseOrderStatus.PURCHASE_ORDER_STATUS_VERIFYING.equals(purchaseOrderDO.getPurchaseOrderStatus())) {
                return false;
            }
            if (verifyResult) {
                createPurchaseDeliveryAndReceiveOrder(purchaseOrderDO);
                purchaseOrderDO.setPurchaseOrderStatus(PurchaseOrderStatus.PURCHASE_ORDER_STATUS_PURCHASING);
            } else {
                purchaseOrderDO.setPurchaseOrderStatus(PurchaseOrderStatus.PURCHASE_ORDER_STATUS_WAIT_COMMIT);
            }
            purchaseOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            purchaseOrderDO.setUpdateTime(new Date());
            purchaseOrderMapper.update(purchaseOrderDO);
            return true;
        } catch (Exception e) {
            logger.error("【采购单审核后，业务处理异常，未生成发货单】", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            logger.error("【数据已回滚】");
            return false;
        } catch (Throwable t) {
            logger.error("【采购单审核后，业务处理异常，未生成发货单】", t);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            logger.error("【数据已回滚】");
            return false;
        }
    }

    /**
     * 如果有发票：生成发货通知单，收料通知单
     * 如果没有发票；收货库房为总公司库：生成发货通知单，收料通知单
     * 如果没有发票；收货库房为分公司库：生成发货通知单，生成总公司收料通知单（real空着不存，待分公司收货通知单状态为已确认时回填）及分拨单号，生成分公司收料通知单
     * 如果是小配件；生成发货通知单，收料通知单
     *
     * @param purchaseOrderDO
     */
    public void createPurchaseDeliveryAndReceiveOrder(PurchaseOrderDO purchaseOrderDO) {
        //生成发货通知单
        PurchaseDeliveryOrderDO purchaseDeliveryOrderDO = createDeliveryDetail(purchaseOrderDO);
        if (CommonConstant.COMMON_CONSTANT_YES.equals(purchaseOrderDO.getIsInvoice()) || PurchaseType.PURCHASE_TYPE_GADGET == purchaseOrderDO.getPurchaseType()) {//如果有发票或者是小配件类型
            //直接生成收料通知单
            createReceiveDetail(purchaseDeliveryOrderDO, AutoAllotStatus.AUTO_ALLOT_STATUS_NO, null);
        } else if (CommonConstant.COMMON_CONSTANT_NO.equals(purchaseOrderDO.getIsInvoice())) {//如果没有发票
            //解析采购单库房快照，是否为总公司库
            Warehouse warehouse = JSON.parseObject(purchaseOrderDO.getWarehouseSnapshot(), Warehouse.class);
            boolean isHead = SubCompanyType.SUB_COMPANY_TYPE_HEADER.equals(warehouse.getSubCompanyType());
            if (isHead) {//如果是总公司仓库
                //直接生成收料通知单
                createReceiveDetail(purchaseDeliveryOrderDO, AutoAllotStatus.AUTO_ALLOT_STATUS_NO, null);
            } else {//如果是分公司仓库
                //生成总公司收料通知单
                PurchaseReceiveOrderDO purchaseReceiveOrderDO = createReceiveDetail(purchaseDeliveryOrderDO, AutoAllotStatus.AUTO_ALLOT_STATUS_YES, null);
                //生成分公司收料通知单
                createReceiveDetail(purchaseDeliveryOrderDO, AutoAllotStatus.AUTO_ALLOT_STATUS_RECEIVE, purchaseReceiveOrderDO.getAutoAllotNo());
            }
        }
    }

    /**
     * 生成发货通知单
     *
     * @param purchaseOrderDO 采购原单
     */
    private PurchaseDeliveryOrderDO createDeliveryDetail(PurchaseOrderDO purchaseOrderDO) {
        Date now = new Date();
        PurchaseDeliveryOrderDO purchaseDeliveryOrderDO = new PurchaseDeliveryOrderDO();
        purchaseDeliveryOrderDO.setPurchaseOrderId(purchaseOrderDO.getId());
        purchaseDeliveryOrderDO.setPurchaseDeliveryNo(generateNoSupport.generatePurchaseDeliveryOrderNo(now, purchaseOrderDO.getWarehouseId()));
        purchaseDeliveryOrderDO.setWarehouseId(purchaseOrderDO.getWarehouseId());
        purchaseDeliveryOrderDO.setWarehouseSnapshot(purchaseOrderDO.getWarehouseSnapshot());
        purchaseDeliveryOrderDO.setIsInvoice(purchaseOrderDO.getIsInvoice());
        purchaseDeliveryOrderDO.setIsNew(purchaseOrderDO.getIsNew());
        purchaseDeliveryOrderDO.setPurchaseDeliveryOrderStatus(PurchaseDeliveryOrderStatus.PURCHASE_DELIVERY_ORDER_STATUS_WAIT);
        purchaseDeliveryOrderDO.setDeliveryTime(null);
        purchaseDeliveryOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        purchaseDeliveryOrderDO.setOwnerSupplierId(purchaseOrderDO.getProductSupplierId());
        purchaseDeliveryOrderDO.setCreateUser(String.valueOf(purchaseOrderDO.getCreateUser()));
        purchaseDeliveryOrderDO.setUpdateUser(String.valueOf(purchaseOrderDO.getCreateUser()));
        purchaseDeliveryOrderDO.setCreateTime(now);
        purchaseDeliveryOrderDO.setUpdateTime(now);
        purchaseDeliveryOrderMapper.save(purchaseDeliveryOrderDO);
        //获取采购订单项列表
//        List<PurchaseOrderProductDO> purchaseOrderProductDOList =  purchaseOrderSupport.getAllPurchaseOrderProductDO(purchaseOrderDO.getId());
        List<PurchaseOrderProductDO> purchaseOrderProductDOList = purchaseOrderDO.getPurchaseOrderProductDOList();
        //声明发货单商品项列表，后面会将保存后的发货单商品项add到发货单中
        List<PurchaseDeliveryOrderProductDO> purchaseDeliveryOrderProductDOList = new ArrayList<>();

        //保存采购发货单商品项
        if (CollectionUtil.isNotEmpty(purchaseOrderProductDOList)) {
            for (PurchaseOrderProductDO purchaseOrderProductDO : purchaseOrderProductDOList) {
                PurchaseDeliveryOrderProductDO purchaseDeliveryOrderProductDO = new PurchaseDeliveryOrderProductDO();
                purchaseDeliveryOrderProductDO.setPurchaseDeliveryOrderId(purchaseDeliveryOrderDO.getId());
                purchaseDeliveryOrderProductDO.setPurchaseOrderProductId(purchaseOrderProductDO.getId());
                purchaseDeliveryOrderProductDO.setProductId(purchaseOrderProductDO.getProductId());
                purchaseDeliveryOrderProductDO.setProductName(purchaseOrderProductDO.getProductName());
                purchaseDeliveryOrderProductDO.setProductSkuId(purchaseOrderProductDO.getProductSkuId());
                purchaseDeliveryOrderProductDO.setProductSnapshot(purchaseOrderProductDO.getProductSnapshot());
                purchaseDeliveryOrderProductDO.setProductCount(purchaseOrderProductDO.getProductCount());
                purchaseDeliveryOrderProductDO.setProductAmount(purchaseOrderProductDO.getProductAmount());
                purchaseDeliveryOrderProductDO.setRealProductId(purchaseOrderProductDO.getProductId());
                purchaseDeliveryOrderProductDO.setRealProductName(purchaseOrderProductDO.getProductName());
                purchaseDeliveryOrderProductDO.setRealProductSkuId(purchaseOrderProductDO.getProductSkuId());
                purchaseDeliveryOrderProductDO.setRealProductSnapshot(purchaseOrderProductDO.getProductSnapshot());
                purchaseDeliveryOrderProductDO.setRealProductCount(purchaseOrderProductDO.getProductCount());
                purchaseDeliveryOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                purchaseDeliveryOrderProductDO.setCreateTime(now);
                purchaseDeliveryOrderProductDO.setUpdateTime(now);
                purchaseDeliveryOrderProductDO.setCreateUser(String.valueOf(purchaseOrderDO.getCreateUser()));
                purchaseDeliveryOrderProductDO.setUpdateUser(String.valueOf(purchaseOrderDO.getCreateUser()));
                purchaseDeliveryOrderProductMapper.save(purchaseDeliveryOrderProductDO);
                purchaseDeliveryOrderProductDOList.add(purchaseDeliveryOrderProductDO);
            }
        }
        purchaseDeliveryOrderDO.setPurchaseDeliveryOrderProductDOList(purchaseDeliveryOrderProductDOList);

        //获取采购订单物料项列表
//        List<PurchaseOrderProductDO> purchaseOrderProductDOList =  purchaseOrderSupport.getAllPurchaseOrderProductDO(purchaseOrderDO.getId());
        List<PurchaseOrderMaterialDO> purchaseOrderMaterialDOList = purchaseOrderDO.getPurchaseOrderMaterialDOList();
        //声明发货单物料项列表，后面会将保存后的发货单物料项add到发货单中
        List<PurchaseDeliveryOrderMaterialDO> purchaseDeliveryOrderMaterialDOList = new ArrayList<>();

        //保存采购发货单商品项
        if (CollectionUtil.isNotEmpty(purchaseOrderMaterialDOList)) {
            for (PurchaseOrderMaterialDO purchaseOrderMaterialDO : purchaseOrderMaterialDOList) {
                PurchaseDeliveryOrderMaterialDO purchaseDeliveryOrderMaterialDO = new PurchaseDeliveryOrderMaterialDO();
                purchaseDeliveryOrderMaterialDO.setPurchaseDeliveryOrderId(purchaseDeliveryOrderDO.getId());
                purchaseDeliveryOrderMaterialDO.setPurchaseOrderMaterialId(purchaseOrderMaterialDO.getId());
                purchaseDeliveryOrderMaterialDO.setMaterialId(purchaseOrderMaterialDO.getMaterialId());
                purchaseDeliveryOrderMaterialDO.setMaterialName(purchaseOrderMaterialDO.getMaterialName());
                purchaseDeliveryOrderMaterialDO.setMaterialSnapshot(purchaseOrderMaterialDO.getMaterialSnapshot());
                purchaseDeliveryOrderMaterialDO.setMaterialCount(purchaseOrderMaterialDO.getMaterialCount());
                purchaseDeliveryOrderMaterialDO.setMaterialAmount(purchaseOrderMaterialDO.getMaterialAmount());
                purchaseDeliveryOrderMaterialDO.setRealMaterialId(purchaseOrderMaterialDO.getMaterialId());
                purchaseDeliveryOrderMaterialDO.setRealMaterialName(purchaseOrderMaterialDO.getMaterialName());
                purchaseDeliveryOrderMaterialDO.setRealMaterialSnapshot(purchaseOrderMaterialDO.getMaterialSnapshot());
                purchaseDeliveryOrderMaterialDO.setRealMaterialCount(purchaseOrderMaterialDO.getMaterialCount());
                purchaseDeliveryOrderMaterialDO.setRealMaterialAmount(purchaseOrderMaterialDO.getMaterialAmount());
                purchaseDeliveryOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                purchaseDeliveryOrderMaterialDO.setCreateTime(now);
                purchaseDeliveryOrderMaterialDO.setUpdateTime(now);
                purchaseDeliveryOrderMaterialDO.setCreateUser(String.valueOf(purchaseOrderDO.getCreateUser()));
                purchaseDeliveryOrderMaterialDO.setUpdateUser(String.valueOf(purchaseOrderDO.getCreateUser()));
                purchaseDeliveryOrderMaterialMapper.save(purchaseDeliveryOrderMaterialDO);
                purchaseDeliveryOrderMaterialDOList.add(purchaseDeliveryOrderMaterialDO);
            }
        }
        purchaseDeliveryOrderDO.setPurchaseDeliveryOrderMaterialDOList(purchaseDeliveryOrderMaterialDOList);
        return purchaseDeliveryOrderDO;
    }

    /**
     * 生成收料通知单
     *
     * @param purchaseDeliveryOrderDO 收料通知单原单
     * @param autoAllotStatus         分拨情况：0直接生成收货单，1：生成总公司收货单（带分拨单号）,已分拨 ，2：生成分公司收货单（保存由总公司带来的分拨号）
     */
    private PurchaseReceiveOrderDO createReceiveDetail(PurchaseDeliveryOrderDO purchaseDeliveryOrderDO, Integer autoAllotStatus, String autoAllotNo) {
        Date now = new Date();
        PurchaseReceiveOrderDO purchaseReceiveOrderDO = new PurchaseReceiveOrderDO();
        purchaseReceiveOrderDO.setPurchaseDeliveryOrderId(purchaseDeliveryOrderDO.getId());
        purchaseReceiveOrderDO.setPurchaseOrderId(purchaseDeliveryOrderDO.getPurchaseOrderId());
        purchaseReceiveOrderDO.setPurchaseReceiveNo(generateNoSupport.generatePurchaseReceiveOrderNo(now, purchaseDeliveryOrderDO.getWarehouseId()));
        purchaseReceiveOrderDO.setWarehouseId(purchaseDeliveryOrderDO.getWarehouseId());
        purchaseReceiveOrderDO.setWarehouseSnapshot(purchaseDeliveryOrderDO.getWarehouseSnapshot());
        purchaseReceiveOrderDO.setIsInvoice(purchaseDeliveryOrderDO.getIsInvoice());
        purchaseReceiveOrderDO.setIsNew(purchaseDeliveryOrderDO.getIsNew());
        purchaseReceiveOrderDO.setPurchaseReceiveOrderStatus(PurchaseReceiveOrderStatus.PURCHASE_RECEIVE_ORDER_STATUS_WAIT);
        purchaseReceiveOrderDO.setConfirmTime(null);
        purchaseReceiveOrderDO.setProductSupplierId(purchaseDeliveryOrderDO.getOwnerSupplierId());
        purchaseReceiveOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        //收货单的owner不存，签单后保存
        purchaseReceiveOrderDO.setCreateUser(String.valueOf(purchaseDeliveryOrderDO.getCreateUser()));
        purchaseReceiveOrderDO.setUpdateUser(String.valueOf(purchaseDeliveryOrderDO.getCreateUser()));
        purchaseReceiveOrderDO.setCreateTime(now);
        purchaseReceiveOrderDO.setUpdateTime(now);
        purchaseReceiveOrderDO.setWarehouseId(purchaseDeliveryOrderDO.getWarehouseId());
        purchaseReceiveOrderDO.setWarehouseSnapshot(purchaseDeliveryOrderDO.getWarehouseSnapshot());
        if (AutoAllotStatus.AUTO_ALLOT_STATUS_YES.equals(autoAllotStatus)) {
            //查询总公司仓库，保存快照，未查到总公司仓库可直接抛出异常
            SubCompany subCompany = companyService.getHeaderCompany().getResult();
            Warehouse warehouse = warehouseService.getWarehouseByCompany(subCompany.getSubCompanyId()).getResult().get(0);
            purchaseReceiveOrderDO.setWarehouseId(warehouse.getWarehouseId());
            purchaseReceiveOrderDO.setWarehouseSnapshot(JSON.toJSONString(warehouse));
            purchaseReceiveOrderDO.setAutoAllotStatus(AutoAllotStatus.AUTO_ALLOT_STATUS_YES);
            purchaseReceiveOrderDO.setAutoAllotNo(GenerateNoUtil.generateAutoAllotStatusOrderNo(now, purchaseDeliveryOrderDO.getPurchaseOrderId()));
        } else if (AutoAllotStatus.AUTO_ALLOT_STATUS_RECEIVE.equals(autoAllotStatus)) {
            purchaseReceiveOrderDO.setAutoAllotStatus(AutoAllotStatus.AUTO_ALLOT_STATUS_RECEIVE);
            purchaseReceiveOrderDO.setAutoAllotNo(autoAllotNo);
        } else {
            purchaseReceiveOrderDO.setAutoAllotStatus(AutoAllotStatus.AUTO_ALLOT_STATUS_NO);
        }
        purchaseReceiveOrderMapper.save(purchaseReceiveOrderDO);
        //获取发货订单项列表
//        List<PurchaseDeliveryOrderProductDO> purchaseDeliveryOrderProductDOList =  purchaseOrderSupport.getAllPurchaseDeliveryOrderProductDO(purchaseDeliveryOrderDO.getId());
        List<PurchaseDeliveryOrderProductDO> purchaseDeliveryOrderProductDOList = purchaseDeliveryOrderDO.getPurchaseDeliveryOrderProductDOList();
        //保存采购发货单商品项
        if (CollectionUtil.isNotEmpty(purchaseDeliveryOrderProductDOList)) {
            for (PurchaseDeliveryOrderProductDO purchaseDeliveryOrderProductDO : purchaseDeliveryOrderProductDOList) {
                PurchaseReceiveOrderProductDO purchaseReceiveOrderProductDO = new PurchaseReceiveOrderProductDO();
                purchaseReceiveOrderProductDO.setPurchaseDeliveryOrderProductId(purchaseDeliveryOrderProductDO.getId());
                purchaseReceiveOrderProductDO.setPurchaseReceiveOrderId(purchaseReceiveOrderDO.getId());
                purchaseReceiveOrderProductDO.setPurchaseOrderProductId(purchaseDeliveryOrderProductDO.getPurchaseOrderProductId());
                purchaseReceiveOrderProductDO.setProductId(purchaseDeliveryOrderProductDO.getProductId());
                purchaseReceiveOrderProductDO.setProductName(purchaseDeliveryOrderProductDO.getProductName());
                purchaseReceiveOrderProductDO.setProductSkuId(purchaseDeliveryOrderProductDO.getProductSkuId());
                purchaseReceiveOrderProductDO.setProductSnapshot(purchaseDeliveryOrderProductDO.getProductSnapshot());
                purchaseReceiveOrderProductDO.setProductCount(purchaseDeliveryOrderProductDO.getProductCount());
                purchaseReceiveOrderProductDO.setProductAmount(purchaseDeliveryOrderProductDO.getProductAmount());
                purchaseReceiveOrderProductDO.setRealProductId(purchaseDeliveryOrderProductDO.getProductId());
                purchaseReceiveOrderProductDO.setRealProductName(purchaseDeliveryOrderProductDO.getProductName());
                purchaseReceiveOrderProductDO.setRealProductSkuId(purchaseDeliveryOrderProductDO.getProductSkuId());
                purchaseReceiveOrderProductDO.setRealProductSnapshot(purchaseDeliveryOrderProductDO.getProductSnapshot());
                purchaseReceiveOrderProductDO.setRealProductCount(purchaseDeliveryOrderProductDO.getProductCount());
                purchaseReceiveOrderProductDO.setIsSrc(CommonConstant.YES);
                purchaseReceiveOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                purchaseReceiveOrderProductDO.setCreateTime(now);
                purchaseReceiveOrderProductDO.setUpdateTime(now);
                purchaseReceiveOrderProductDO.setCreateUser(String.valueOf(purchaseDeliveryOrderDO.getCreateUser()));
                purchaseReceiveOrderProductDO.setUpdateUser(String.valueOf(purchaseDeliveryOrderDO.getCreateUser()));
                purchaseReceiveOrderProductMapper.save(purchaseReceiveOrderProductDO);
            }
        }

        //获取发货订单物料项列表
//        List<PurchaseDeliveryOrderProductDO> purchaseDeliveryOrderProductDOList =  purchaseOrderSupport.getAllPurchaseDeliveryOrderProductDO(purchaseDeliveryOrderDO.getId());
        List<PurchaseDeliveryOrderMaterialDO> purchaseDeliveryOrderMaterialDOList = purchaseDeliveryOrderDO.getPurchaseDeliveryOrderMaterialDOList();
        //保存采购发货单商品项
        if (CollectionUtil.isNotEmpty(purchaseDeliveryOrderMaterialDOList)) {
            for (PurchaseDeliveryOrderMaterialDO purchaseDeliveryOrderMaterialDO : purchaseDeliveryOrderMaterialDOList) {
                PurchaseReceiveOrderMaterialDO purchaseReceiveOrderMaterialDO = new PurchaseReceiveOrderMaterialDO();
                purchaseReceiveOrderMaterialDO.setPurchaseDeliveryOrderMaterialId(purchaseDeliveryOrderMaterialDO.getId());
                purchaseReceiveOrderMaterialDO.setPurchaseReceiveOrderId(purchaseReceiveOrderDO.getId());
                purchaseReceiveOrderMaterialDO.setPurchaseDeliveryOrderMaterialId(purchaseDeliveryOrderMaterialDO.getPurchaseOrderMaterialId());
                purchaseReceiveOrderMaterialDO.setMaterialId(purchaseDeliveryOrderMaterialDO.getMaterialId());
                purchaseReceiveOrderMaterialDO.setMaterialName(purchaseDeliveryOrderMaterialDO.getMaterialName());
                purchaseReceiveOrderMaterialDO.setMaterialSnapshot(purchaseDeliveryOrderMaterialDO.getMaterialSnapshot());
                purchaseReceiveOrderMaterialDO.setMaterialCount(purchaseDeliveryOrderMaterialDO.getMaterialCount());
                purchaseReceiveOrderMaterialDO.setMaterialAmount(purchaseDeliveryOrderMaterialDO.getMaterialAmount());
                purchaseReceiveOrderMaterialDO.setRealMaterialId(purchaseDeliveryOrderMaterialDO.getMaterialId());
                purchaseReceiveOrderMaterialDO.setRealMaterialName(purchaseDeliveryOrderMaterialDO.getMaterialName());
                purchaseReceiveOrderMaterialDO.setRealMaterialSnapshot(purchaseDeliveryOrderMaterialDO.getMaterialSnapshot());
                purchaseReceiveOrderMaterialDO.setRealMaterialCount(purchaseDeliveryOrderMaterialDO.getMaterialCount());
                purchaseReceiveOrderMaterialDO.setIsSrc(CommonConstant.YES);
                purchaseReceiveOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                purchaseReceiveOrderMaterialDO.setCreateTime(now);
                purchaseReceiveOrderMaterialDO.setUpdateTime(now);
                purchaseReceiveOrderMaterialDO.setCreateUser(String.valueOf(purchaseDeliveryOrderDO.getCreateUser()));
                purchaseReceiveOrderMaterialDO.setUpdateUser(String.valueOf(purchaseDeliveryOrderDO.getCreateUser()));
                purchaseReceiveOrderMaterialMapper.save(purchaseReceiveOrderMaterialDO);
            }
        }
        return purchaseReceiveOrderDO;
    }
}
