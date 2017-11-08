package com.lxzl.erp.core.service.purchase.impl;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.company.pojo.SubCompany;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.purchase.PurchaseDeliveryOrderQueryParam;
import com.lxzl.erp.common.domain.purchase.PurchaseOrderCommitParam;
import com.lxzl.erp.common.domain.purchase.PurchaseOrderQueryParam;
import com.lxzl.erp.common.domain.purchase.pojo.PurchaseDeliveryOrder;
import com.lxzl.erp.common.domain.purchase.pojo.PurchaseOrder;
import com.lxzl.erp.common.domain.purchase.pojo.PurchaseOrderProduct;
import com.lxzl.erp.common.domain.purchase.pojo.PurchaseReceiveOrder;
import com.lxzl.erp.common.domain.warehouse.pojo.Warehouse;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.GenerateNoUtil;
import com.lxzl.erp.core.service.company.CompanyService;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.purchase.PurchaseOrderService;
import com.lxzl.erp.core.service.purchase.impl.support.PurchaseOrderConverter;
import com.lxzl.erp.core.service.purchase.impl.support.PurchaseOrderSupport;
import com.lxzl.erp.core.service.user.UserService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.warehouse.WarehouseService;
import com.lxzl.erp.core.service.warehouse.impl.support.WarehouseConverter;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.dao.mysql.purchase.*;
import com.lxzl.erp.dataaccess.dao.mysql.supplier.SupplierMapper;
import com.lxzl.erp.dataaccess.dao.mysql.warehouse.WarehouseMapper;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import com.lxzl.erp.dataaccess.domain.purchase.*;
import com.lxzl.erp.dataaccess.domain.supplier.SupplierDO;
import com.lxzl.erp.dataaccess.domain.warehouse.WarehouseDO;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(PurchaseOrderServiceImpl.class);
    @Autowired
    private UserService userService;
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
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> add(PurchaseOrder purchaseOrder) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date now = new Date();
        List<PurchaseOrderProduct> purchaseOrderProductList = purchaseOrder.getPurchaseOrderProductList();
        WarehouseDO warehouseDO = warehouseMapper.finByNo(purchaseOrder.getWarehouseNo());
        if(warehouseDO==null){
            result.setErrorCode(ErrorCode.WAREHOUSE_NOT_EXISTS);
            return result;
        }
        SupplierDO supplierDO = supplierMapper.findById(purchaseOrder.getProductSupplierId());
        if(supplierDO==null){
            result.setErrorCode(ErrorCode.SUPPLIER_NOT_EXISTS);
            return result;
        }
        //判断操作人是否可以选择该仓库
        boolean warehouseOp = userSupport.checkCurrentUserWarehouse(warehouseDO.getId());
        if(!warehouseOp){
            result.setErrorCode(ErrorCode.USER_CAN_NOT_OP_WAREHOUSE);
            return result;
        }
        //判断该笔采购单是总公司发起还是分公司发起，分公司表要加一个字段代表是否是总公司,这里取的用户是session中的user
        boolean isHead = userSupport.isHeadUser();
        //校验采购订单商品项
        ServiceResult<String, PurchaseOrderDetail> checkResult = checkPurchaseOrderProductList(purchaseOrderProductList,userSupport.getCurrentUserId().toString(),now,purchaseOrder.getIsNew(),isHead);
        if(!ErrorCode.SUCCESS.equals(checkResult.getErrorCode())){
            result.setErrorCode(checkResult.getErrorCode());
            return result;
        }
        PurchaseOrderDetail purchaseOrderDetail = checkResult.getResult();
        PurchaseOrderDO purchaseOrderDO = buildPurchaseOrder(now,purchaseOrder,purchaseOrderDetail,warehouseDO);
        purchaseOrderDO.setPurchaseNo(GenerateNoUtil.generatePurchaseOrderNo(now, userSupport.getCurrentUserId()));
        purchaseOrderDO.setCreateUser(userSupport.getCurrentUserId().toString());
        purchaseOrderDO.setCreateTime(now);
        purchaseOrderDO.setWarehouseId(warehouseDO.getId());
        purchaseOrderMapper.save(purchaseOrderDO);
        //保存采购订单商品项
        for(PurchaseOrderProductDO purchaseOrderProductDO : purchaseOrderDetail.purchaseOrderProductDOList){
            purchaseOrderProductDO.setPurchaseOrderId(purchaseOrderDO.getId());
            purchaseOrderProductMapper.save(purchaseOrderProductDO);
        }
        result.setResult(purchaseOrderDO.getPurchaseNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }
    private PurchaseOrderDO buildPurchaseOrder(Date now , PurchaseOrder purchaseOrder,PurchaseOrderDetail purchaseOrderDetail ,WarehouseDO warehouseDO){
        //保存采购单
        PurchaseOrderDO purchaseOrderDO = PurchaseOrderConverter.convertPurchaseOrder(purchaseOrder);
        if(purchaseOrderDO.getInvoiceSupplierId()==null){
            purchaseOrderDO.setInvoiceSupplierId(purchaseOrderDO.getProductSupplierId());
        }
        //查询库房信息并保存库房快照
        purchaseOrderDO.setWarehouseSnapshot(JSON.toJSONString(WarehouseConverter.convertWarehouseDO(warehouseDO)));
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
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> update(PurchaseOrder purchaseOrder) {
        ServiceResult<String, String> result = new ServiceResult<>();
        //校验采购单是否存在
        PurchaseOrderDO purchaseOrderDO = purchaseOrderMapper.findByPurchaseNo(purchaseOrder.getPurchaseNo());
        if(purchaseOrderDO==null){
            result.setErrorCode(ErrorCode.PURCHASE_ORDER_NOT_EXISTS);
            return result;
        }
        WarehouseDO warehouseDO = warehouseMapper.finByNo(purchaseOrder.getWarehouseNo());
        if(warehouseDO==null){
            result.setErrorCode(ErrorCode.WAREHOUSE_NOT_EXISTS);
            return result;
        }
        SupplierDO supplierDO = supplierMapper.findById(purchaseOrder.getProductSupplierId());
        if(supplierDO==null){
            result.setErrorCode(ErrorCode.SUPPLIER_NOT_EXISTS);
            return result;
        }
        //判断操作人是否可以选择该仓库
        boolean warehouseOp = userSupport.checkCurrentUserWarehouse(warehouseDO.getId());
        if(!warehouseOp){
            result.setErrorCode(ErrorCode.USER_CAN_NOT_OP_WAREHOUSE);
            return result;
        }
        //只有待提交的采购单可以修改
        if(!PurchaseOrderStatus.PURCHASE_ORDER_STATUS_WAIT_COMMIT.equals(purchaseOrderDO.getPurchaseOrderStatus())){
            result.setErrorCode(ErrorCode.PURCHASE_ORDER_COMMITTED_CAN_NOT_UPDATE);
            return result;
        }

        //判断该笔采购单是总公司发起还是分公司发起，这里取的用户是createUser
        boolean isHead = userSupport.isHeadUser(Integer.valueOf(purchaseOrderDO.getCreateUser()));
        Date now = new Date();
        List<PurchaseOrderProduct> purchaseOrderProductList = purchaseOrder.getPurchaseOrderProductList();
        //校验采购订单商品项
        ServiceResult<String, PurchaseOrderDetail> checkResult = checkPurchaseOrderProductList(purchaseOrderProductList,userSupport.getCurrentUserId().toString(),now,purchaseOrder.getIsNew(),isHead);
        if(!ErrorCode.SUCCESS.equals(checkResult.getErrorCode())){
            result.setErrorCode(checkResult.getErrorCode());
            return result;
        }
        PurchaseOrderDetail purchaseOrderDetail = checkResult.getResult();

        //更新采购单
        Integer purchaseOrderDOId = purchaseOrderDO.getId();
        String purchaseNo = purchaseOrderDO.getPurchaseNo();
        purchaseOrderDO = buildPurchaseOrder(now,purchaseOrder,purchaseOrderDetail,warehouseDO);
        purchaseOrderDO.setPurchaseNo(purchaseNo);
        purchaseOrderDO.setId(purchaseOrderDOId);
        purchaseOrderDO.setWarehouseId(warehouseDO.getId());
        purchaseOrderMapper.update(purchaseOrderDO);

        //查询旧采购订单项列表
        List<PurchaseOrderProductDO> purchaseOrderProductDOList =  purchaseOrderSupport.getAllPurchaseOrderProductDO(purchaseOrderDO.getId());
        //为方便比对，将旧采购订单项列表存入map

        Map<Integer,PurchaseOrderProductDO> oldMap = new HashMap<>();
        for(PurchaseOrderProductDO purchaseOrderProductDO : purchaseOrderProductDOList){
            oldMap.put(purchaseOrderProductDO.getProductSkuId(),purchaseOrderProductDO);
        }
        //为方便比对，将新采购订单项列表存入map
        Map<Integer,PurchaseOrderProductDO> newMap = new HashMap<>();
        for(PurchaseOrderProductDO purchaseOrderProductDO : purchaseOrderDetail.purchaseOrderProductDOList){
            newMap.put(purchaseOrderProductDO.getProductSkuId(),purchaseOrderProductDO);
        }

        for(Integer skuId : newMap.keySet()){
            //如果原列表有此skuId，则更新
            if(oldMap.containsKey(skuId)){
                PurchaseOrderProductDO oldDO  = oldMap.get(skuId);
                Integer oldId = oldDO.getId();
                PurchaseOrderProductDO newDO = newMap.get(skuId);
                newDO.setId(oldId);
                newDO.setUpdateTime(now);
                newDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                purchaseOrderProductMapper.update(newDO);
            }else{ //如果原列表没有新列表有，则添加
                PurchaseOrderProductDO newDO = newMap.get(skuId);
                newDO.setPurchaseOrderId(purchaseOrderDOId);
                purchaseOrderProductMapper.save(newDO);
            }
        }
        for(Integer skuId : oldMap.keySet()){
            if(!newMap.containsKey(skuId)){//如果原列表有新列表没有，则删除
                purchaseOrderProductMapper.delete(oldMap.get(skuId).getId());
            }
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(purchaseOrderDO.getPurchaseNo());
        return result;
    }
    //生成待保存的采购单项辅助类
    class PurchaseOrderDetail{
        //声明待保存的采购单项列表
        private List<PurchaseOrderProductDO> purchaseOrderProductDOList = new ArrayList<>();
        //声明累加采购单总价
        private BigDecimal totalAmount = new BigDecimal(0);
        private Date now = null;
        private String userId = null;
        public PurchaseOrderDetail(String userId , Date now){
            this.userId = userId;
            this.now = now;
        }
    }
    private ServiceResult<String,PurchaseOrderDetail> checkPurchaseOrderProductList(List<PurchaseOrderProduct> purchaseOrderProductList , String userId , Date now , Integer isNew ,boolean isHead){
        ServiceResult<String,PurchaseOrderDetail> result = new ServiceResult<>();
        PurchaseOrderDetail purchaseOrderDetail = new PurchaseOrderDetail(userId,now);

        //声明skuSet，如果一个采购单中有相同sku的商品项，则返回错误
        Set<Integer> skuSet = new HashSet<>();
        // 验证采购单商品项是否合法
        for(PurchaseOrderProduct purchaseOrderProduct : purchaseOrderProductList){

            if(purchaseOrderProduct.getProductSkuId()==null){
                result.setErrorCode(ErrorCode.PRODUCT_SKU_NOT_NULL);
                return result;
            }else{
                skuSet.add(purchaseOrderProduct.getProductSkuId());
            }
            if(purchaseOrderProduct.getProductAmount()==null||  purchaseOrderProduct.getProductAmount().doubleValue()<=0){
                result.setErrorCode(ErrorCode.PRODUCT_SKU_PRICE_ERROR);
                return result;
            }
            if(purchaseOrderProduct.getProductCount()==null||purchaseOrderProduct.getProductCount()<=0){
                result.setErrorCode(ErrorCode.PRODUCT_SKU_COUNT_ERROR);
                return result;
            }
            ProductSkuDO productSkuDO = productSkuMapper.findById(purchaseOrderProduct.getProductSkuId());
            if(productSkuDO==null){
                result.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                return result;
            }

            //累加采购单总价
            purchaseOrderDetail.totalAmount = BigDecimalUtil.add(purchaseOrderDetail.totalAmount,BigDecimalUtil.mul(purchaseOrderProduct.getProductAmount(),new BigDecimal(purchaseOrderProduct.getProductCount())));

            PurchaseOrderProductDO purchaseOrderProductDO = new PurchaseOrderProductDO();
            //保存采购订单商品项快照
            ServiceResult<String,Product> productResult = productService.queryProductById(productSkuDO.getProductId());
            purchaseOrderProductDO.setProductSnapshot(JSON.toJSONString(getProductBySkuId(productResult.getResult(),productSkuDO.getId())));
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
        //skuId有重复
        if(purchaseOrderProductList.size()!=skuSet.size()){
            result.setErrorCode(ErrorCode.PURCHASE_ORDER_PRODUCT_CAN_NOT_REPEAT);
            return result;
        }
        //发起者不是总公司 ，并且采购20000元以上全新机，则返回错误
        if(!isHead&&CommonConstant.COMMON_CONSTANT_YES.equals(isNew)&&purchaseOrderDetail.totalAmount.doubleValue()>=20000D){
            result.setErrorCode(ErrorCode.PURCHASE_ORDER_CANNOT_CREATE_BY_NEW_AND_AMOUNT);
            return result;
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(purchaseOrderDetail);
        return result;
    }
    private Product getProductBySkuId(Product product , Integer productSkuId){
        List<ProductSku> productSkuList = product.getProductSkuList();
        List<ProductSku> list = new ArrayList<>();
        for(ProductSku productSku : productSkuList){
            if(productSkuId!=null&&productSkuId.equals(productSku.getSkuId())){
                list.add(productSku);
            }
        }
        product.setProductSkuList(list);
        return product;
    }
    @Override
    public ServiceResult<String, PurchaseOrder> queryPurchaseOrderByNo(String purchaseNo) {
        ServiceResult<String, PurchaseOrder> serviceResult = new ServiceResult<>();
        PurchaseOrderDO purchaseOrderDO = purchaseOrderMapper.findDetailByPurchaseNo(purchaseNo);
        if(purchaseOrderDO==null){
            serviceResult.setErrorCode(ErrorCode.PURCHASE_ORDER_NOT_EXISTS);
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        PurchaseOrder purchaseOrder = PurchaseOrderConverter.convertPurchaseOrderDO(purchaseOrderDO);
        List<Product> productList = new ArrayList<>();
        serviceResult.setResult(purchaseOrder);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Page<PurchaseOrder>> page(PurchaseOrderQueryParam purchaseOrderQueryParam) {
        ServiceResult<String, Page<PurchaseOrder>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(purchaseOrderQueryParam.getPageNo(), purchaseOrderQueryParam.getPageSize());
        if(StringUtil.isNotEmpty(purchaseOrderQueryParam.getWarehouseNo())){
            WarehouseDO warehouseDO = warehouseMapper.finByNo(purchaseOrderQueryParam.getWarehouseNo());
            purchaseOrderQueryParam.setWarehouseId(warehouseDO.getId());
        }
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("queryParam", purchaseOrderQueryParam);

        Integer totalCount = purchaseOrderMapper.findPurchaseOrderCountByParams(maps);
        List<PurchaseOrderDO> purchaseOrderDOList = purchaseOrderMapper.findPurchaseOrderByParams(maps);
        List<PurchaseOrder> purchaseOrderList = PurchaseOrderConverter.convertPurchaseOrderDOList(purchaseOrderDOList);
        Page<PurchaseOrder> page = new Page<>(purchaseOrderList, totalCount, purchaseOrderQueryParam.getPageNo(), purchaseOrderQueryParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, Integer> commit(PurchaseOrderCommitParam purchaseOrderCommitParam) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        //校验采购单是否存在
        PurchaseOrderDO purchaseOrderDO = purchaseOrderMapper.findByPurchaseNo(purchaseOrderCommitParam.getPurchaseNo());
        if(purchaseOrderDO==null){
            result.setErrorCode(ErrorCode.PURCHASE_ORDER_NOT_EXISTS);
            return result;
        }else if(!PurchaseOrderStatus.PURCHASE_ORDER_STATUS_WAIT_COMMIT.equals(purchaseOrderDO.getPurchaseOrderStatus())){
            //只有待提交状态的采购单可以提交
            result.setErrorCode(ErrorCode.PURCHASE_ORDER_COMMITTED_CAN_NOT_COMMIT_AGAIN);
            return result;
        }
        //调用审核服务
        ServiceResult<String ,Integer > verifyResult = workflowService.commitWorkFlow(WorkflowType.WORKFLOW_TYPE_PURCHASE,purchaseOrderDO.getId(),purchaseOrderCommitParam.getVerifyUserId());
        //修改提交审核状态
        if(ErrorCode.SUCCESS.equals(verifyResult.getErrorCode())){
            purchaseOrderDO.setPurchaseOrderStatus(PurchaseOrderStatus.PURCHASE_ORDER_STATUS_VERIFYING);
            purchaseOrderMapper.update(purchaseOrderDO);
        }
        return verifyResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String delete(PurchaseOrder purchaseOrder) {
        PurchaseOrderDO purchaseOrderDO = purchaseOrderMapper.findByPurchaseNo(purchaseOrder.getPurchaseNo());
        if(purchaseOrderDO==null){
            return ErrorCode.PURCHASE_ORDER_NOT_EXISTS;
        }
        if(!PurchaseOrderStatus.PURCHASE_ORDER_STATUS_WAIT_COMMIT.equals(purchaseOrderDO.getPurchaseOrderStatus())){
            //只有待提交状态的采购单可以删除
            return ErrorCode.PURCHASE_ORDER_COMMITTED_CAN_NOT_DELETE;
        }
        purchaseOrderMapper.deleteByPurchaseNo(purchaseOrderDO.getPurchaseNo());
        purchaseOrderProductMapper.deleteByPurchaseOrderId(purchaseOrderDO.getId());
        return ErrorCode.SUCCESS;
    }

    @Override
    public ServiceResult<String, Page<PurchaseDeliveryOrder>> pagePurchaseDelivery(PurchaseDeliveryOrderQueryParam purchaseDeliveryOrderQueryParam) {
        ServiceResult<String, Page<PurchaseDeliveryOrder>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(purchaseDeliveryOrderQueryParam.getPageNo(), purchaseDeliveryOrderQueryParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("queryParam", purchaseDeliveryOrderQueryParam);

        Integer totalCount = purchaseDeliveryOrderMapper.findPurchaseDeliveryOrderCountByParams(maps);
        List<PurchaseDeliveryOrderDO> purchaseDeliveryOrderDOList = purchaseDeliveryOrderMapper.findPurchaseDeliveryOrderByParams(maps);
        List<PurchaseDeliveryOrder> purchaseDeliveryOrderList = PurchaseOrderConverter.convertPurchaseDeliveryOrderDOList(purchaseDeliveryOrderDOList);
        Page<PurchaseDeliveryOrder> page = new Page<>(purchaseDeliveryOrderList, totalCount, purchaseDeliveryOrderQueryParam.getPageNo(), purchaseDeliveryOrderQueryParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, PurchaseDeliveryOrder> queryPurchaseDeliveryOrderByNo(PurchaseDeliveryOrder purchaseDeliveryOrder) {
        ServiceResult<String, PurchaseDeliveryOrder> serviceResult = new ServiceResult<>();
        PurchaseDeliveryOrderDO purchaseDeliveryOrderDO =  purchaseDeliveryOrderMapper.findByNo(purchaseDeliveryOrder.getPurchaseDeliveryNo());
        if(purchaseDeliveryOrderDO==null){
            serviceResult.setErrorCode(ErrorCode.PURCHASE_DELIVERY_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(PurchaseOrderConverter.convertPurchaseDeliveryOrderDO(purchaseDeliveryOrderDO));
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Integer> updatePurchaseReceiveOrder(PurchaseReceiveOrder purchaseReceiveOrder) {

        return null;
    }

    /**
     * 接收审核结果通知，审核通过生成发货单
     * @param verifyResult
     * @param businessId
     */
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    @Override
    public boolean receiveVerifyResult(boolean verifyResult, Integer businessId) {
        try{
            PurchaseOrderDO purchaseOrderDO = purchaseOrderMapper.findById(businessId);
            if(verifyResult){
                if(purchaseOrderDO==null){
                    throw new Exception();
                }
                createPurchaseDeliveryAndReceiveOrder(purchaseOrderDO);
                purchaseOrderDO.setPurchaseOrderStatus(PurchaseOrderStatus.PURCHASE_ORDER_STATUS_VERIFYING);
            }else{
                purchaseOrderDO.setPurchaseOrderStatus(PurchaseOrderStatus.PURCHASE_ORDER_STATUS_WAIT_COMMIT);
            }
            purchaseOrderMapper.update(purchaseOrderDO);
            return true;
        }catch (Exception e){
            log.error("【采购单审核后，业务处理异常，未生成发货单】",e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return false;
        }catch (Throwable t){
            log.error("【采购单审核后，业务处理异常，未生成发货单】",t);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return false;
        }
    }

    /**
     * 如果有发票：生成发货通知单及收货通知单
     * 如果没有发票；收货库房为总公司库：生成发货通知单，收料通知单
     * 如果没有发票；收货库房为分公司库：生成发货通知单，生成总公司收料通知单（real空着不存，待分公司收货通知单状态为已确认时回填）及分拨单号，生成分公司收料通知单
     * @param purchaseOrderDO
     */
    public void createPurchaseDeliveryAndReceiveOrder(PurchaseOrderDO purchaseOrderDO){
        //生成发货通知单
        PurchaseDeliveryOrderDO purchaseDeliveryOrderDO = createDeliveryDetail(purchaseOrderDO);
        if(CommonConstant.COMMON_CONSTANT_YES.equals(purchaseOrderDO.getIsInvoice())){//如果有发票
            //直接生成收料通知单
            createReceiveDetail(purchaseDeliveryOrderDO,AutoAllotStatus.AUTO_ALLOT_STATUS_NO,null);
        }else if(CommonConstant.COMMON_CONSTANT_NO.equals(purchaseOrderDO.getIsInvoice())){//如果没有发票
            //解析采购单库房快照，是否为总公司库
            Warehouse warehouse = JSON.parseObject(purchaseOrderDO.getWarehouseSnapshot(),Warehouse.class);
            boolean isHead  = SubCompanyType.SUB_COMPANY_TYPE_HEADER.equals(warehouse.getSubCompanyType())?true:false;
            if(isHead){//如果是总公司仓库
                //直接生成收料通知单
                createReceiveDetail(purchaseDeliveryOrderDO,AutoAllotStatus.AUTO_ALLOT_STATUS_NO,null);
            }else{//如果是分公司仓库
                //生成总公司收料通知单
                PurchaseReceiveOrderDO purchaseReceiveOrderDO = createReceiveDetail(purchaseDeliveryOrderDO,AutoAllotStatus.AUTO_ALLOT_STATUS_YES,null);
                //生成分公司收料通知单
                createReceiveDetail(purchaseDeliveryOrderDO,AutoAllotStatus.AUTO_ALLOT_STATUS_RECEIVE,purchaseReceiveOrderDO.getAutoAllotNo());
            }

        }


    }

    /**
     * 生成发货通知单
     * @param purchaseOrderDO 采购原单
     */
    private PurchaseDeliveryOrderDO createDeliveryDetail(PurchaseOrderDO purchaseOrderDO){
        Date now = new Date();
        PurchaseDeliveryOrderDO purchaseDeliveryOrderDO = new PurchaseDeliveryOrderDO();
        purchaseDeliveryOrderDO.setPurchaseOrderId(purchaseOrderDO.getId());
        purchaseDeliveryOrderDO.setPurchaseDeliveryNo(GenerateNoUtil.generatePurchaseDeliveryOrderNo(now, purchaseOrderDO.getId()));
        purchaseDeliveryOrderDO.setWarehouseId(purchaseOrderDO.getWarehouseId());
        purchaseDeliveryOrderDO.setWarehouseSnapshot(purchaseOrderDO.getWarehouseSnapshot());
        purchaseDeliveryOrderDO.setIsInvoice(purchaseOrderDO.getIsInvoice());
        purchaseDeliveryOrderDO.setIsNew(purchaseOrderDO.getIsNew());
        purchaseDeliveryOrderDO.setPurchaseDeliveryOrderStatus(PurchaseDeliveryOrderStatus.PURCHASE_DELIVERY_ORDER_STATUS_WAIT);
        purchaseDeliveryOrderDO.setDeliveryTime(null);
        purchaseDeliveryOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        purchaseDeliveryOrderDO.setOwnerSupplierId(purchaseOrderDO.getProductSupplierId());
        purchaseDeliveryOrderDO.setCreateUser(String.valueOf(CommonConstant.SUPER_USER_ID));
        purchaseDeliveryOrderDO.setUpdateUser(String.valueOf(CommonConstant.SUPER_USER_ID));
        purchaseDeliveryOrderDO.setCreateTime(now);
        purchaseDeliveryOrderDO.setUpdateTime(now);
        purchaseDeliveryOrderMapper.save(purchaseDeliveryOrderDO);
        //查询采购订单项列表
        List<PurchaseOrderProductDO> purchaseOrderProductDOList =  purchaseOrderSupport.getAllPurchaseOrderProductDO(purchaseOrderDO.getId());
        //保存采购发货单商品项
        if(purchaseOrderProductDOList!=null&&purchaseOrderProductDOList.size()>0){
            for(PurchaseOrderProductDO purchaseOrderProductDO : purchaseOrderProductDOList){
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
                purchaseDeliveryOrderProductDO.setCreateUser(String.valueOf(CommonConstant.SUPER_USER_ID));
                purchaseDeliveryOrderProductDO.setUpdateUser(String.valueOf(CommonConstant.SUPER_USER_ID));
                purchaseDeliveryOrderProductMapper.save(purchaseDeliveryOrderProductDO);
            }
        }
        return purchaseDeliveryOrderDO;
    }

    /**
     * 生成收料通知单
     * @param purchaseDeliveryOrderDO 收料通知单原单
     * @param autoAllotStatus 分拨情况：0直接生成收货单，1：生成总公司收货单（带分拨单号）,已分拨 ，2：生成分公司收货单（保存由总公司带来的分拨号）
     */
    private PurchaseReceiveOrderDO createReceiveDetail(PurchaseDeliveryOrderDO purchaseDeliveryOrderDO, Integer autoAllotStatus,String autoAllotNo){
        Date now = new Date();
        PurchaseReceiveOrderDO purchaseReceiveOrderDO = new PurchaseReceiveOrderDO();
        purchaseReceiveOrderDO.setPurchaseDeliveryOrderId(purchaseDeliveryOrderDO.getId());
        purchaseReceiveOrderDO.setPurchaseOrderId(purchaseDeliveryOrderDO.getPurchaseOrderId());
        purchaseReceiveOrderDO.setPurchaseReceiveNo(GenerateNoUtil.generatePurchaseReceiveOrderNo(now, purchaseDeliveryOrderDO.getPurchaseOrderId()));
        purchaseReceiveOrderDO.setWarehouseId(purchaseDeliveryOrderDO.getWarehouseId());
        purchaseReceiveOrderDO.setWarehouseSnapshot(purchaseDeliveryOrderDO.getWarehouseSnapshot());
        purchaseReceiveOrderDO.setIsInvoice(purchaseDeliveryOrderDO.getIsInvoice());
        purchaseReceiveOrderDO.setIsNew(purchaseDeliveryOrderDO.getIsNew());
        purchaseReceiveOrderDO.setPurchaseReceiveOrderStatus(PurchaseReceiveOrderStatus.PURCHASE_RECEIVE_ORDER_STATUS_WAIT);
        purchaseReceiveOrderDO.setConfirmTime(null);
        purchaseReceiveOrderDO.setProductSupplierId(purchaseDeliveryOrderDO.getOwnerSupplierId());
        purchaseReceiveOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        //收货单的owner暂时不存
//        purchaseReceiveOrderDO.setOwner(purchaseOrderDO.getProductSupplierId());
        purchaseReceiveOrderDO.setCreateUser(String.valueOf(CommonConstant.SUPER_USER_ID));
        purchaseReceiveOrderDO.setUpdateUser(String.valueOf(CommonConstant.SUPER_USER_ID));
        purchaseReceiveOrderDO.setCreateTime(now);
        purchaseReceiveOrderDO.setUpdateTime(now);
        purchaseReceiveOrderDO.setWarehouseId(purchaseDeliveryOrderDO.getWarehouseId());
        purchaseReceiveOrderDO.setWarehouseSnapshot(purchaseDeliveryOrderDO.getWarehouseSnapshot());
        if(AutoAllotStatus.AUTO_ALLOT_STATUS_YES.equals(autoAllotStatus)){
            //查询总公司仓库，保存快照，未查到总公司仓库可直接抛出异常
            SubCompany subCompany = companyService.getHeaderCompany().getResult();
            Warehouse warehouse = warehouseService.getWarehouseByCompany(subCompany.getSubCompanyId()).getResult().get(0);
            purchaseReceiveOrderDO.setWarehouseId(2);
            purchaseReceiveOrderDO.setWarehouseSnapshot(JSON.toJSONString(warehouse));
            purchaseReceiveOrderDO.setAutoAllotStatus(AutoAllotStatus.AUTO_ALLOT_STATUS_YES);
            purchaseReceiveOrderDO.setAutoAllotNo(GenerateNoUtil.generateAutoAllotStatusOrderNo(now,purchaseDeliveryOrderDO.getPurchaseOrderId()));
        }else if(AutoAllotStatus.AUTO_ALLOT_STATUS_RECEIVE.equals(autoAllotStatus)){
            purchaseReceiveOrderDO.setAutoAllotStatus(AutoAllotStatus.AUTO_ALLOT_STATUS_RECEIVE);
            purchaseReceiveOrderDO.setAutoAllotNo(autoAllotNo);
        }else{
            purchaseReceiveOrderDO.setAutoAllotStatus(AutoAllotStatus.AUTO_ALLOT_STATUS_NO);
        }
        purchaseReceiveOrderMapper.save(purchaseReceiveOrderDO);
        //查询发货订单项列表
        List<PurchaseDeliveryOrderProductDO> purchaseDeliveryOrderProductDOList =  purchaseOrderSupport.getAllPurchaseDeliveryOrderProductDO(purchaseDeliveryOrderDO.getId());
        //保存采购发货单商品项
        if(purchaseDeliveryOrderProductDOList!=null&&purchaseDeliveryOrderProductDOList.size()>0){
            for(PurchaseDeliveryOrderProductDO purchaseDeliveryOrderProductDO : purchaseDeliveryOrderProductDOList){
                PurchaseReceiveOrderProductDO purchaseReceiveOrderProductDO = new PurchaseReceiveOrderProductDO();
                purchaseReceiveOrderProductDO.setPurchaseDeliveryOrderProductId(purchaseDeliveryOrderProductDO.getId());
                purchaseReceiveOrderProductDO.setPurchaseReceiveOrderId(purchaseReceiveOrderDO.getId());
                purchaseReceiveOrderProductDO.setPurchaseOrderProductId(purchaseDeliveryOrderProductDO.getPurchaseOrderProductId());
                purchaseReceiveOrderProductDO.setProductId(purchaseDeliveryOrderProductDO.getProductId());
                purchaseReceiveOrderProductDO.setProductName(purchaseDeliveryOrderProductDO.getProductName());
                purchaseReceiveOrderProductDO.setProductSkuId(purchaseDeliveryOrderProductDO.getProductSkuId());
                purchaseReceiveOrderProductDO.setProductSnapshot(purchaseDeliveryOrderProductDO.getProductSnapshot());
                purchaseReceiveOrderProductDO.setProductCount(purchaseDeliveryOrderProductDO.getProductCount());
                purchaseReceiveOrderProductDO.setRealProductId(purchaseDeliveryOrderProductDO.getProductId());
                purchaseReceiveOrderProductDO.setRealProductName(purchaseDeliveryOrderProductDO.getProductName());
                purchaseReceiveOrderProductDO.setRealProductSkuId(purchaseDeliveryOrderProductDO.getProductSkuId());
                purchaseReceiveOrderProductDO.setRealProductSnapshot(purchaseDeliveryOrderProductDO.getProductSnapshot());
                purchaseReceiveOrderProductDO.setRealProductCount(purchaseDeliveryOrderProductDO.getProductCount());
                purchaseReceiveOrderProductDO.setIsSrc(CommonConstant.YES);
                purchaseReceiveOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                purchaseReceiveOrderProductDO.setCreateTime(now);
                purchaseReceiveOrderProductDO.setUpdateTime(now);
                purchaseReceiveOrderProductDO.setCreateUser(String.valueOf(CommonConstant.SUPER_USER_ID));
                purchaseReceiveOrderProductDO.setUpdateUser(String.valueOf(CommonConstant.SUPER_USER_ID));
                purchaseReceiveOrderProductMapper.save(purchaseReceiveOrderProductDO);
            }
        }
        return purchaseReceiveOrderDO;
    }
}
