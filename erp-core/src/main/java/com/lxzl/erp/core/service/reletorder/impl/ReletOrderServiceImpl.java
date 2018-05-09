package com.lxzl.erp.core.service.reletorder.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.order.pojo.OrderMaterial;
import com.lxzl.erp.common.domain.order.pojo.OrderProduct;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.reletorder.ReletOrderQueryParam;
import com.lxzl.erp.common.domain.reletorder.pojo.ReletOrder;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.*;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.material.MaterialService;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.core.service.order.impl.support.OrderTimeAxisSupport;
import com.lxzl.erp.core.service.permission.PermissionSupport;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.reletorder.ReletOrderService;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.core.service.statement.impl.support.StatementOrderSupport;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerRiskManagementMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialTypeMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.dao.mysql.reletorder.ReletOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.reletorder.ReletOrderMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.reletorder.ReletOrderProductMapper;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerRiskManagementDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialTypeDO;
import com.lxzl.erp.dataaccess.domain.reletorder.ReletOrderDO;
import com.lxzl.erp.dataaccess.domain.reletorder.ReletOrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.reletorder.ReletOrderProductDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDO;
import com.lxzl.se.common.exception.BusinessException;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.common.util.date.DateUtil;
import com.lxzl.se.dataaccess.mongo.config.PageQuery;
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


@Service("reletOrderService")
public class ReletOrderServiceImpl implements ReletOrderService {

    private static Logger logger = LoggerFactory.getLogger(ReletOrderServiceImpl.class);

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, Integer> createReletOrder(Order order){
        ServiceResult<String, Integer> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        Date reletStartTime;

        if (order == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        if (StringUtil.isEmpty(order.getOrderNo())){
            result.setErrorCode(ErrorCode.RELET_ORDER_NO_NOT_NULL);
            return result;
        }
        //查询订单信息
        ServiceResult<String, Order> orderServiceResult = orderService.queryOrderByNo(order.getOrderNo());
        if (!ErrorCode.SUCCESS.equals(orderServiceResult.getErrorCode())) {
            result.setErrorCode(orderServiceResult.getErrorCode());
            return result;
        }
        //查询最近一次续租信息
        ReletOrderDO recentlyReletOrderInDB = reletOrderMapper.findRecentlyReletOrderByOrderNo(order.getOrderNo());
        if (recentlyReletOrderInDB == null){
            //第一次续租
            Integer dayCount = com.lxzl.erp.common.util.DateUtil.daysBetween(order.getExpectReturnTime(), currentTime);
            if (dayCount < -10 || dayCount > 3){  //订单到期 前10天 至 后3天 可续租
                result.setErrorCode(ErrorCode.RELET_ORDER_NOT_IN_RELET_TIME_SCOPE);
                return result;
            }
            //续租起始时间 = 订单的归还时间
            reletStartTime = order.getExpectReturnTime();
        }
        else {
            Integer dayCount = com.lxzl.erp.common.util.DateUtil.daysBetween(recentlyReletOrderInDB.getExpectReturnTime(), currentTime);
            if (dayCount < -10 || dayCount > 3){
                result.setErrorCode(ErrorCode.RELET_ORDER_NOT_IN_RELET_TIME_SCOPE);
                return result;
            }
            //续租起始时间 = 最近一次续租归还时间
            reletStartTime = recentlyReletOrderInDB.getExpectReturnTime();
        }
        //合法性
        String verifyCreateOrderCode = verifyOperateOrder(orderServiceResult.getResult());
        if (!ErrorCode.SUCCESS.equals(verifyCreateOrderCode)) {
            result.setErrorCode(verifyCreateOrderCode);
            return result;
        }

        ReletOrder reletOrder = new ReletOrder(orderServiceResult.getResult());

        reletOrder.setRentStartTime(reletStartTime);//更新起租时间

        //CustomerDO customerDO = customerMapper.findByNo(reletOrder.getBuyerCustomerNo());
        ReletOrderDO reletOrderDO = ConverterUtil.convert(reletOrder, ReletOrderDO.class);

        // 校验客户风控信息
//        verifyCustomerRiskInfo(reletOrderDO);
        calculateReletOrderProductInfo(reletOrderDO.getReletOrderProductDOList(), reletOrderDO);
        calculateReletOrderMaterialInfo(reletOrderDO.getReletOrderMaterialDOList(), reletOrderDO);

        SubCompanyDO subCompanyDO = subCompanyMapper.findById(reletOrder.getDeliverySubCompanyId());
        if (reletOrder.getDeliverySubCompanyId() == null || subCompanyDO == null) {
            result.setErrorCode(ErrorCode.SUB_COMPANY_NOT_EXISTS);
            return result;
        }

        SubCompanyDO orderSubCompanyDO = subCompanyMapper.findById(reletOrderDO.getOrderSubCompanyId());
        reletOrderDO.setTotalOrderAmount(BigDecimalUtil.sub(BigDecimalUtil.add(reletOrderDO.getTotalProductAmount(), reletOrderDO.getTotalMaterialAmount()), reletOrderDO.getTotalDiscountAmount()));
        reletOrderDO.setReletOrderNo(generateNoSupport.generateReletOrderNo(currentTime, orderSubCompanyDO != null ? orderSubCompanyDO.getSubCompanyCode() : null));

        //reletOrderDO.setOrderSellerId(customerDO.getOwner());
        //reletOrderDO.setBuyerCustomerName(customerDO.getCustomerName());

        //添加客户的结算时间（天）
        Date rentStartTime = reletOrder.getRentStartTime();
        Integer statementDate = reletOrder.getStatementDate();//customerDO.getStatementDate();

        //计算结算时间
        Integer statementDays = statementOrderSupport.getCustomerStatementDate(statementDate, rentStartTime);


        //获取
        reletOrderDO.setStatementDate(statementDays);
        reletOrderDO.setReletOrderStatus(OrderStatus.ORDER_STATUS_RELET);
        reletOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        reletOrderDO.setCreateUser(loginUser.getUserId().toString());
        reletOrderDO.setUpdateUser(loginUser.getUserId().toString());
        reletOrderDO.setCreateTime(currentTime);
        reletOrderDO.setUpdateTime(currentTime);

        Date expectReturnTime = generateExpectReturnTime(reletOrderDO);
        reletOrderDO.setExpectReturnTime(expectReturnTime);
        //保存续租单 商品项 配件项
        reletOrderMapper.save(reletOrderDO);
        saveReletOrderProductInfo(reletOrderDO, loginUser, currentTime);
        saveReletOrderMaterialInfo(reletOrderDO, loginUser, currentTime);

        // 续租单生成结算单 ，使用订单Id
        ServiceResult<String, BigDecimal> createStatementOrderResult = statementService.createReletOrderStatement(reletOrderDO);
        if (!ErrorCode.SUCCESS.equals(createStatementOrderResult.getErrorCode())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.setErrorCode(createStatementOrderResult.getErrorCode());
            return result;
        }


        //TODO 续租时间轴

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(reletOrderDO.getId());
        return result;
    }


    @Override
    public ServiceResult<String, Page<ReletOrder>> queryAllReletOrder(ReletOrderQueryParam reletOrderQueryParam) {
        ServiceResult<String, Page<ReletOrder>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(reletOrderQueryParam.getPageNo(), reletOrderQueryParam.getPageSize());

        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("reletOrderQueryParam", reletOrderQueryParam);
        maps.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_SERVICE, PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_BUSINESS, PermissionType.PERMISSION_TYPE_USER));

        Integer totalCount = reletOrderMapper.findReletOrderCountByParams(maps);
        List<ReletOrderDO> reletOrderDOList = reletOrderMapper.findReletOrderByParams(maps);
        List<ReletOrder> reletOrderList = ConverterUtil.convertList(reletOrderDOList, ReletOrder.class);
        Page<ReletOrder> page = new Page<>(reletOrderList, totalCount, reletOrderQueryParam.getPageNo(), reletOrderQueryParam.getPageSize());
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }


    @Override
    public ServiceResult<String, ReletOrder> queryReletOrderDetailById(Integer reletOrderId){
        ServiceResult<String, ReletOrder> result = new ServiceResult<>();

        if (null == reletOrderId){
            result.setErrorCode(ErrorCode.RELET_ORDER_QUERY_ID_NOT_NULL);
            return result;
        }
        ReletOrderDO reletOrderDO = reletOrderMapper.findDetailByReletOrderId(reletOrderId);
        ReletOrder reletOrder = ConverterUtil.convert(reletOrderDO, ReletOrder.class);
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(reletOrder);
        return result;
    }



    private void saveReletOrderProductInfo(ReletOrderDO reletOrderDO, User loginUser, Date currentTime) {

        List<ReletOrderProductDO> saveOrderProductDOList = new ArrayList<>();
        Map<Integer, ReletOrderProductDO> updateOrderProductDOMap = new HashMap<>();
        List<ReletOrderProductDO> dbOrderProductDOList = reletOrderProductMapper.findByReletOrderId(reletOrderDO.getId());
        Map<Integer, ReletOrderProductDO> dbOrderProductDOMap = ListUtil.listToMap(dbOrderProductDOList, "id");
        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderProductDOList())) {
            for (ReletOrderProductDO orderProductDO : reletOrderDO.getReletOrderProductDOList()) {

                ReletOrderProductDO dbOrderProductDO = dbOrderProductDOMap.get(orderProductDO.getId());
                if (dbOrderProductDO != null) {
                    orderProductDO.setId(dbOrderProductDO.getId());
                    updateOrderProductDOMap.put(orderProductDO.getId(), orderProductDO);
                    dbOrderProductDOMap.remove(orderProductDO.getId());
                } else {
                    saveOrderProductDOList.add(orderProductDO);
                }
            }
        }

        if (saveOrderProductDOList.size() > 0) {
            for (ReletOrderProductDO orderProductDO : saveOrderProductDOList) {
                orderProductDO.setReletOrderId(reletOrderDO.getId());
                orderProductDO.setReletOrderNo(reletOrderDO.getReletOrderNo());
                orderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                orderProductDO.setCreateUser(loginUser.getUserId().toString());
                orderProductDO.setUpdateUser(loginUser.getUserId().toString());
                orderProductDO.setCreateTime(currentTime);
                orderProductDO.setUpdateTime(currentTime);
                reletOrderProductMapper.save(orderProductDO);
            }
        }

        if (updateOrderProductDOMap.size() > 0) {
            for (Map.Entry<Integer, ReletOrderProductDO> entry : updateOrderProductDOMap.entrySet()) {
                ReletOrderProductDO orderProductDO = entry.getValue();
                orderProductDO.setReletOrderId(reletOrderDO.getId());
                orderProductDO.setReletOrderNo(reletOrderDO.getReletOrderNo());
                orderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                orderProductDO.setUpdateUser(loginUser.getUserId().toString());
                orderProductDO.setUpdateTime(currentTime);
                reletOrderProductMapper.update(orderProductDO);
            }
        }

        if (dbOrderProductDOMap.size() > 0) {
            for (Map.Entry<Integer, ReletOrderProductDO> entry : dbOrderProductDOMap.entrySet()) {
                ReletOrderProductDO orderProductDO = entry.getValue();
                orderProductDO.setReletOrderId(reletOrderDO.getId());
                orderProductDO.setReletOrderNo(reletOrderDO.getReletOrderNo());
                orderProductDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                orderProductDO.setUpdateUser(loginUser.getUserId().toString());
                orderProductDO.setUpdateTime(currentTime);
                reletOrderProductMapper.update(orderProductDO);
            }
        }
    }

    private void saveReletOrderMaterialInfo(ReletOrderDO reletOrderDO, User loginUser, Date currentTime) {

        List<ReletOrderMaterialDO> saveOrderMaterialDOList = new ArrayList<>();
        Map<Integer, ReletOrderMaterialDO> updateOrderMaterialDOMap = new HashMap<>();
        List<ReletOrderMaterialDO> dbOrderMaterialDOList = reletOrderMaterialMapper.findByReletOrderId(reletOrderDO.getId());
        Map<Integer, ReletOrderMaterialDO> dbOrderMaterialDOMap = ListUtil.listToMap(dbOrderMaterialDOList, "id");
        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderMaterialDOList())) {
            for (ReletOrderMaterialDO orderMaterialDO : reletOrderDO.getReletOrderMaterialDOList()) {
                ReletOrderMaterialDO dbOrderMaterialDO = dbOrderMaterialDOMap.get(orderMaterialDO.getId());
                if (dbOrderMaterialDO != null) {
                    orderMaterialDO.setId(dbOrderMaterialDO.getId());
                    updateOrderMaterialDOMap.put(orderMaterialDO.getId(), orderMaterialDO);
                    dbOrderMaterialDOMap.remove(orderMaterialDO.getId());
                } else {
                    saveOrderMaterialDOList.add(orderMaterialDO);
                }
            }
        }

        if (saveOrderMaterialDOList.size() > 0) {
            for (ReletOrderMaterialDO orderMaterialDO : saveOrderMaterialDOList) {
                orderMaterialDO.setReletOrderId(reletOrderDO.getId());
                orderMaterialDO.setReletOrderNo(reletOrderDO.getReletOrderNo());
                orderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                orderMaterialDO.setCreateUser(loginUser.getUserId().toString());
                orderMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                orderMaterialDO.setCreateTime(currentTime);
                orderMaterialDO.setUpdateTime(currentTime);
                reletOrderMaterialMapper.save(orderMaterialDO);
            }
        }

        if (updateOrderMaterialDOMap.size() > 0) {
            for (Map.Entry<Integer, ReletOrderMaterialDO> entry : updateOrderMaterialDOMap.entrySet()) {
                ReletOrderMaterialDO orderMaterialDO = entry.getValue();
                orderMaterialDO.setReletOrderId(reletOrderDO.getId());
                orderMaterialDO.setReletOrderNo(reletOrderDO.getReletOrderNo());
                orderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                orderMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                orderMaterialDO.setUpdateTime(currentTime);
                reletOrderMaterialMapper.update(orderMaterialDO);
            }
        }

        if (dbOrderMaterialDOMap.size() > 0) {
            for (Map.Entry<Integer, ReletOrderMaterialDO> entry : dbOrderMaterialDOMap.entrySet()) {
                ReletOrderMaterialDO orderMaterialDO = entry.getValue();
                orderMaterialDO.setReletOrderId(reletOrderDO.getId());
                orderMaterialDO.setReletOrderNo(reletOrderDO.getReletOrderNo());
                orderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                orderMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                orderMaterialDO.setUpdateTime(currentTime);
                reletOrderMaterialMapper.update(orderMaterialDO);
            }
        }
    }

    private Date generateExpectReturnTime(ReletOrderDO reletOrderDO) {
        Date expectReturnTime = null;
        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderProductDOList())) {
            for (ReletOrderProductDO orderProductDO : reletOrderDO.getReletOrderProductDOList()) {
                Date thisExpectReturnTime = calculationOrderExpectReturnTime(reletOrderDO.getRentStartTime(), reletOrderDO.getRentType(), reletOrderDO.getRentTimeLength());
                if (thisExpectReturnTime != null) {
                    expectReturnTime = expectReturnTime == null || expectReturnTime.getTime() < thisExpectReturnTime.getTime() ? thisExpectReturnTime : expectReturnTime;
                }
            }
        }
        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderMaterialDOList())) {
            for (ReletOrderMaterialDO orderMaterialDO : reletOrderDO.getReletOrderMaterialDOList()) {
                Date thisExpectReturnTime = calculationOrderExpectReturnTime(reletOrderDO.getRentStartTime(), reletOrderDO.getRentType(), reletOrderDO.getRentTimeLength());
                if (thisExpectReturnTime != null) {
                    expectReturnTime = expectReturnTime == null || expectReturnTime.getTime() < thisExpectReturnTime.getTime() ? thisExpectReturnTime : expectReturnTime;
                }
            }
        }
        return expectReturnTime;
    }

    /**
     * 计算订单预计归还时间
     */
    private Date calculationOrderExpectReturnTime(Date rentStartTime, Integer rentType, Integer rentTimeLength) {
        if (OrderRentType.RENT_TYPE_DAY.equals(rentType)) {
            return DateUtil.dateInterval(rentStartTime, rentTimeLength - 1);
        } else if (OrderRentType.RENT_TYPE_MONTH.equals(rentType)) {
            return DateUtil.dateInterval(DateUtil.monthInterval(rentStartTime, rentTimeLength), -1);
        }
        return null;
    }


    private void calculateReletOrderProductInfo(List<ReletOrderProductDO> reletOrderProductDOList, ReletOrderDO reletOrderDO) {
        //CustomerRiskManagementDO customerRiskManagementDO = customerRiskManagementMapper.findByCustomerId(reletOrderDO.getBuyerCustomerId());
        if (reletOrderProductDOList != null && !reletOrderProductDOList.isEmpty()) {
            int productCount = 0;
            // 商品租赁总额
            BigDecimal productAmountTotal = new BigDecimal(0.0);


            for (ReletOrderProductDO reletOrderProductDO : reletOrderProductDOList) {

                String skuName = "";
                BigDecimal skuPrice = BigDecimal.ZERO;
                Product product = new Product();

                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(reletOrderProductDO.getProductSkuId());
                if (!ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode())) {
                    throw new BusinessException(productServiceResult.getErrorCode());
                }
                product = productServiceResult.getResult();
                reletOrderProductDO.setProductName(product.getProductName());
                ProductSku productSku = CollectionUtil.isNotEmpty(product.getProductSkuList()) ? product.getProductSkuList().get(0) : null;
                if (productSku == null) {
                    throw new BusinessException(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                }
                skuPrice = CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderProductDO.getIsNewProduct()) ? productSku.getNewSkuPrice() : productSku.getSkuPrice();
                skuName = productSku.getSkuName();


                reletOrderProductDO.setProductSkuName(skuName);
                reletOrderProductDO.setProductAmount(BigDecimalUtil.mul(BigDecimalUtil.mul(reletOrderProductDO.getProductUnitAmount(), new BigDecimal(reletOrderDO.getRentTimeLength()), 2), new BigDecimal(reletOrderProductDO.getProductCount())));
                reletOrderProductDO.setProductSkuSnapshot(FastJsonUtil.toJSONString(product));

                productCount += reletOrderProductDO.getProductCount();
                productAmountTotal = BigDecimalUtil.add(productAmountTotal, reletOrderProductDO.getProductAmount());
            }

            reletOrderDO.setTotalProductCount(productCount);
            reletOrderDO.setTotalProductAmount(productAmountTotal);
        }
    }


    private void calculateReletOrderMaterialInfo(List<ReletOrderMaterialDO> reletOrderMaterialDOList, ReletOrderDO reletOrderDO) {
        //CustomerRiskManagementDO customerRiskManagementDO = customerRiskManagementMapper.findByCustomerId(orderDO.getBuyerCustomerId());
        if (reletOrderMaterialDOList != null && !reletOrderMaterialDOList.isEmpty()) {
            int materialCount = 0;
            // 商品租赁总额
            BigDecimal materialAmountTotal = BigDecimal.ZERO;
            String materialName = "";
            Material material = new Material();

            for (ReletOrderMaterialDO reletOrderMaterialDO : reletOrderMaterialDOList) {

                ServiceResult<String, Material> materialServiceResult = materialService.queryMaterialById(reletOrderMaterialDO.getMaterialId());
                if (!ErrorCode.SUCCESS.equals(materialServiceResult.getErrorCode())) {
                    throw new BusinessException(materialServiceResult.getErrorCode());
                }
                material = materialServiceResult.getResult();
                if (material == null) {
                    throw new BusinessException(ErrorCode.MATERIAL_NOT_EXISTS);
                }
                MaterialTypeDO materialTypeDO = materialTypeMapper.findById(material.getMaterialType());
                reletOrderMaterialDO.setMaterialName(material.getMaterialName());
                materialName = material.getMaterialName();

                reletOrderMaterialDO.setMaterialName(StringUtil.isBlank(materialName) ? reletOrderMaterialDO.getMaterialName() : materialName);
                reletOrderMaterialDO.setMaterialAmount(BigDecimalUtil.mul(BigDecimalUtil.mul(reletOrderMaterialDO.getMaterialUnitAmount(), new BigDecimal(reletOrderDO.getRentTimeLength()), 2), new BigDecimal(reletOrderMaterialDO.getMaterialCount())));
                reletOrderMaterialDO.setMaterialSnapshot(FastJsonUtil.toJSONString(material));

                materialCount += reletOrderMaterialDO.getMaterialCount();
                materialAmountTotal = BigDecimalUtil.add(materialAmountTotal, reletOrderMaterialDO.getMaterialAmount());

            }
            reletOrderDO.setTotalMaterialCount(materialCount);
            reletOrderDO.setTotalMaterialAmount(materialAmountTotal);
        }
    }


    private void verifyCustomerRiskInfo(ReletOrderDO reletOrderDO) {
        CustomerRiskManagementDO customerRiskManagementDO = customerRiskManagementMapper.findByCustomerId(reletOrderDO.getBuyerCustomerId());
        boolean isCheckRiskManagement = isCheckRiskManagement(reletOrderDO);
        if (isCheckRiskManagement) {
            if (customerRiskManagementDO == null) {
                throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_NOT_EXISTS);
            }
        }

        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderProductDOList())) {
            for (ReletOrderProductDO reletOrderProductDO : reletOrderDO.getReletOrderProductDOList()) {
                if (OrderRentType.RENT_TYPE_DAY.equals(reletOrderDO.getRentType())
                        && reletOrderDO.getRentTimeLength() >= CommonConstant.ORDER_NEED_VERIFY_DAYS) {
                    throw new BusinessException(ErrorCode.ORDER_RENT_LENGTH_MORE_THAN_90);
                }

                // 如果走风控
                if (isCheckRiskManagement) {
                    ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(reletOrderProductDO.getProductSkuId());
                    if (!productServiceResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
                        throw new BusinessException(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                    }
                    Product product = productServiceResult.getResult();
                    ProductSku productSku = productServiceResult.getResult().getProductSkuList().get(0);

                    if (BrandId.BRAND_ID_APPLE.equals(product.getBrandId()) && CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsLimitApple())) {
                        throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_APPLE_LIMIT);
                    }
                    if (CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderProductDO.getIsNewProduct()) && CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsLimitNew())) {
                        throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_NEW_LIMIT);
                    }
                    BigDecimal skuPrice = CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderProductDO.getIsNewProduct()) ? productSku.getNewSkuPrice() : productSku.getSkuPrice();
                    if (customerRiskManagementDO.getSingleLimitPrice() != null && BigDecimalUtil.compare(skuPrice, customerRiskManagementDO.getSingleLimitPrice()) > 0) {
                        throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_PRICE_LIMIT);
                    }

                    Integer payCycle, payMode;  //续租时无押金depositCycle
                    boolean productIsCheckRiskManagement = isCheckRiskManagement(reletOrderDO, reletOrderProductDO, null);
                    if (!productIsCheckRiskManagement) {
                        if (reletOrderProductDO.getPayMode() == null) {
                            throw new BusinessException(ErrorCode.ORDER_PAY_MODE_NOT_NULL);
                        }
                        // 如果不走风控，就押0付0
                        //reletOrderProductDO.setDepositCycle(0);  无押金
                        reletOrderProductDO.setPaymentCycle(0);
                        continue;
                    }
                    // 判断用户是否为全额押金用户，如果为全额押金用户，即全额押金，并且押0付1
                    if (CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsFullDeposit())) {
                        //depositCycle = 0;
                        payCycle = 1;
                        payMode = OrderPayMode.PAY_MODE_PAY_BEFORE;
                    } else {
                        // 查看客户风控信息是否齐全
                        /*if (!customerSupport.isFullRiskManagement(orderDO.getBuyerCustomerId())) {
                            throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_NOT_FULL);
                        }*/
                        if (BrandId.BRAND_ID_APPLE.equals(product.getBrandId())) {
                            // 商品品牌为苹果品牌
                            //depositCycle = customerRiskManagementDO.getAppleDepositCycle();
                            payCycle = customerRiskManagementDO.getApplePaymentCycle();
                            payMode = customerRiskManagementDO.getApplePayMode();
                        } else if (CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderProductDO.getIsNewProduct())) {
                            //depositCycle = customerRiskManagementDO.getNewDepositCycle();
                            payCycle = customerRiskManagementDO.getNewPaymentCycle();
                            payMode = customerRiskManagementDO.getNewPayMode();
                        } else {
                            //depositCycle = customerRiskManagementDO.getDepositCycle();
                            payCycle = customerRiskManagementDO.getPaymentCycle();
                            payMode = customerRiskManagementDO.getPayMode();
                        }
                    }
                    if (OrderRentType.RENT_TYPE_MONTH.equals(reletOrderDO.getRentType())) {
                        payCycle = payCycle > reletOrderDO.getRentTimeLength() ? reletOrderDO.getRentTimeLength() : payCycle;
                    }
                    //reletOrderProductDO.setDepositCycle(depositCycle);
                    reletOrderProductDO.setPaymentCycle(payCycle);
                    reletOrderProductDO.setPayMode(payMode);
                } else {
                    if (reletOrderProductDO.getPayMode() == null) {
                        throw new BusinessException(ErrorCode.ORDER_PAY_MODE_NOT_NULL);
                    } else if (OrderPayMode.PAY_MODE_PAY_BEFORE_PERCENT.equals(reletOrderProductDO.getPayMode())) {
                        //reletOrderProductDO.setDepositCycle(0);
                        reletOrderProductDO.setPaymentCycle(0);
                    }
                }
            }
        }

        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderMaterialDOList())) {
            for (ReletOrderMaterialDO reletOrderMaterialDO : reletOrderDO.getReletOrderMaterialDOList()) {
                if (OrderRentType.RENT_TYPE_DAY.equals(reletOrderDO.getRentType())
                        && reletOrderDO.getRentTimeLength() >= CommonConstant.ORDER_NEED_VERIFY_DAYS) {
                    throw new BusinessException(ErrorCode.ORDER_RENT_LENGTH_MORE_THAN_90);
                }

                // 如果走风控
                if (isCheckRiskManagement) {
                    ServiceResult<String, Material> materialResult = materialService.queryMaterialById(reletOrderMaterialDO.getMaterialId());
                    Material material = materialResult.getResult();
                    if (BrandId.BRAND_ID_APPLE.equals(material.getBrandId()) && CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsLimitApple())) {
                        throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_APPLE_LIMIT);
                    }
                    if (CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderMaterialDO.getIsNewMaterial()) && CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsLimitNew())) {
                        throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_NEW_LIMIT);
                    }
                    BigDecimal materialPrice = CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderMaterialDO.getIsNewMaterial()) ? material.getNewMaterialPrice() : material.getMaterialPrice();
                    if (customerRiskManagementDO.getSingleLimitPrice() != null && BigDecimalUtil.compare(materialPrice, customerRiskManagementDO.getSingleLimitPrice()) >= 0) {
                        throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_PRICE_LIMIT);
                    }
                    boolean materialIsCheckRiskManagement = isCheckRiskManagement(reletOrderDO, null, reletOrderMaterialDO);
                    if (!materialIsCheckRiskManagement) {
                        if (reletOrderMaterialDO.getPayMode() == null) {
                            throw new BusinessException(ErrorCode.ORDER_PAY_MODE_NOT_NULL);
                        }
                        // 如果不走风控，就押0付0
                        //reletOrderMaterialDO.setDepositCycle(0);
                        reletOrderMaterialDO.setPaymentCycle(0);
                        continue;
                    }
                    Integer payCycle, payMode;//depositCycle
                    // 判断用户是否为全额押金用户，如果为全额押金用户，即全额押金，并且押0付1
                    if (CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsFullDeposit())) {
                        //depositCycle = 0;
                        payCycle = 1;
                        payMode = OrderPayMode.PAY_MODE_PAY_BEFORE;
                    } else if (BrandId.BRAND_ID_APPLE.equals(material.getBrandId())) {
                        // 商品品牌为苹果品牌
                        //depositCycle = customerRiskManagementDO.getAppleDepositCycle();
                        payCycle = customerRiskManagementDO.getApplePaymentCycle();
                        payMode = customerRiskManagementDO.getApplePayMode();
                    } else if (CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderMaterialDO.getIsNewMaterial())) {
                        //depositCycle = customerRiskManagementDO.getNewDepositCycle();
                        payCycle = customerRiskManagementDO.getNewPaymentCycle();
                        payMode = customerRiskManagementDO.getNewPayMode();
                    } else {
                        //depositCycle = customerRiskManagementDO.getDepositCycle();
                        payCycle = customerRiskManagementDO.getPaymentCycle();
                        payMode = customerRiskManagementDO.getPayMode();
                    }
                    if (OrderRentType.RENT_TYPE_MONTH.equals(reletOrderDO.getRentType())) {
                        //depositCycle = depositCycle > orderMaterialDO.getRentTimeLength() ? orderMaterialDO.getRentTimeLength() : depositCycle;
                        payCycle = payCycle > reletOrderDO.getRentTimeLength() ? reletOrderDO.getRentTimeLength() : payCycle;
                    }
                    //orderMaterialDO.setDepositCycle(depositCycle);
                    reletOrderMaterialDO.setPaymentCycle(payCycle);
                    reletOrderMaterialDO.setPayMode(payMode);
                } else {
                    if (reletOrderMaterialDO.getPayMode() == null) {
                        throw new BusinessException(ErrorCode.ORDER_PAY_MODE_NOT_NULL);
                    } else if (OrderPayMode.PAY_MODE_PAY_BEFORE_PERCENT.equals(reletOrderMaterialDO.getPayMode())) {
                        //reletOrderMaterialDO.setDepositCycle(0);
                        reletOrderMaterialDO.setPaymentCycle(0);
                    }

                }
            }
        }
    }


    private boolean isCheckRiskManagement(ReletOrderDO reletOrderDO) {

        BigDecimal totalProductAmount = BigDecimal.ZERO;
        BigDecimal totalMaterialAmount = BigDecimal.ZERO;
        Integer totalProductCount = 0;
        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderProductDOList())) {
            for (ReletOrderProductDO reletOrderProductDO : reletOrderDO.getReletOrderProductDOList()) {
                boolean isCheckRiskManagement = isCheckRiskManagement(reletOrderDO, reletOrderProductDO, null);
                if (isCheckRiskManagement) {
                    return true;
                }
                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(reletOrderProductDO.getProductSkuId());
                if (ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode())) {
                    ProductSku productSku = productServiceResult.getResult().getProductSkuList().get(0);
                    BigDecimal skuPrice = CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderProductDO.getIsNewProduct()) ? productSku.getNewSkuPrice() : productSku.getSkuPrice();
                    totalProductAmount = BigDecimalUtil.add(totalProductAmount, BigDecimalUtil.mul(new BigDecimal(reletOrderProductDO.getProductCount()), skuPrice));
                    totalProductCount += reletOrderProductDO.getProductCount();
                }
            }
        }

        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderMaterialDOList())) {
            for (ReletOrderMaterialDO reletOrderMaterialDO : reletOrderDO.getReletOrderMaterialDOList()) {

                boolean isCheckRiskManagement = isCheckRiskManagement(reletOrderDO, null, reletOrderMaterialDO);
                if (isCheckRiskManagement) {
                    return true;
                }
                ServiceResult<String, Material> materialResult = materialService.queryMaterialById(reletOrderMaterialDO.getMaterialId());
                if (ErrorCode.SUCCESS.equals(materialResult.getErrorCode())) {
                    Material material = materialResult.getResult();
                    BigDecimal materialPrice = CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderMaterialDO.getIsNewMaterial()) ? material.getNewMaterialPrice() : material.getMaterialPrice();
                    totalMaterialAmount = BigDecimalUtil.add(totalMaterialAmount, BigDecimalUtil.mul(new BigDecimal(reletOrderMaterialDO.getMaterialCount()), materialPrice));
                }
            }
        }
        BigDecimal totalAmount = BigDecimalUtil.add(totalProductAmount, totalMaterialAmount);
        if (totalProductCount >= CommonConstant.ORDER_NEED_VERIFY_PRODUCT_COUNT || BigDecimalUtil.compare(totalAmount, CommonConstant.ORDER_NEED_VERIFY_PRODUCT_AMOUNT) >= 0) {
            return true;
        }
        return false;
    }


    private boolean isCheckRiskManagement(ReletOrderDO reletOrderDO, ReletOrderProductDO orderProductDO, ReletOrderMaterialDO orderMaterialDO) {
        if (orderProductDO != null || orderMaterialDO != null) {
            if (OrderRentType.RENT_TYPE_DAY.equals(reletOrderDO.getRentType())
                    && reletOrderDO.getRentTimeLength() >= CommonConstant.ORDER_NEED_VERIFY_DAYS) {
                return true;
            }
            if (OrderRentType.RENT_TYPE_MONTH.equals(reletOrderDO.getRentType())) {
                return true;
            }
        }
        return false;
    }


    private String verifyOperateOrder(Order order) {
        Integer intRentIngCount = 0;
        if (order == null) {
            return ErrorCode.SYSTEM_ERROR;
        }

        //确认收货，部分归还，状态时 可续租
        if (order.getOrderStatus() != OrderStatus.ORDER_STATUS_CONFIRM && order.getOrderStatus() != OrderStatus.ORDER_STATUS_PART_RETURN){
            return ErrorCode.RELET_ORDER_NOT_IN_RELET_STATUS_SCOPE;
        }

        if ((order.getOrderProductList() == null || order.getOrderProductList().isEmpty())
                && (order.getOrderMaterialList() == null || order.getOrderMaterialList().isEmpty())) {
            return ErrorCode.RELET_ORDER_LIST_NOT_NULL;
        }
        if (StringUtil.isEmpty(order.getBuyerCustomerNo())){
            return ErrorCode.SYSTEM_ERROR;
        }

        CustomerDO customerDO = customerMapper.findByNo(order.getBuyerCustomerNo());
        if (customerDO == null) {
            return ErrorCode.CUSTOMER_NOT_EXISTS;
        }

        // 判断逾期情况，如果客户存在未支付的逾期的结算单，不能产生新订单
        List<StatementOrderDO> overdueStatementOrderList = statementOrderSupport.getOverdueStatementOrderList(customerDO.getId());


        if (CollectionUtil.isNotEmpty(order.getOrderProductList())) {

            for (OrderProduct reletOrderProduct : order.getOrderProductList()) {

                intRentIngCount += reletOrderProduct.getRentingProductCount() == null ? 0:reletOrderProduct.getRentingProductCount();


                // 如果为长租，但凡有逾期，就不可以下单，如果为短租，在可用额度范围内，就可以下单
                Integer rentLengthType = OrderRentType.RENT_TYPE_MONTH.equals(order.getRentType()) && order.getRentTimeLength() >= CommonConstant.ORDER_RENT_TYPE_LONG_MIN ? OrderRentLengthType.RENT_LENGTH_TYPE_LONG : OrderRentLengthType.RENT_LENGTH_TYPE_SHORT;
                if (OrderRentLengthType.RENT_LENGTH_TYPE_LONG.equals(rentLengthType)
                        && CollectionUtil.isNotEmpty(overdueStatementOrderList)) {
                    return ErrorCode.CUSTOMER_HAVE_OVERDUE_STATEMENT_ORDER;
                }
            }
        }

        if (CollectionUtil.isNotEmpty(order.getOrderMaterialList())) {

            for (OrderMaterial reletOrderMaterial : order.getOrderMaterialList()) {

                intRentIngCount += reletOrderMaterial.getRentingMaterialCount() == null ? 0:reletOrderMaterial.getRentingMaterialCount();

                // 如果为长租，但凡有逾期，就不可以下单，如果为短租，在可用额度范围内，就可以下单
                if (OrderRentLengthType.RENT_LENGTH_TYPE_LONG.equals(order.getRentLengthType())
                        && CollectionUtil.isNotEmpty(overdueStatementOrderList)) {
                    return ErrorCode.CUSTOMER_HAVE_OVERDUE_STATEMENT_ORDER;
                }
            }
        }

        if (intRentIngCount <= 0){
            return ErrorCode.RELET_ORDER_RENT_COUNT_ERROR;
        }
        return verifyOrderShortRentReceivable(customerDO, ConverterUtil.convert(order, ReletOrderDO.class));
    }


    private String verifyOrderShortRentReceivable(CustomerDO customerDO, ReletOrderDO reletOrderDO) {

        Integer subCompanyId = reletOrderDO.getOrderSubCompanyId();
        subCompanyId = subCompanyId == null ? userSupport.getCurrentUserCompanyId() : subCompanyId;

        BigDecimal customerTotalShortRentReceivable = statementOrderSupport.getShortRentReceivable(customerDO.getId());
        //分公司的应收短期上线
        BigDecimal subCompanyTotalShortRentReceivable = statementOrderSupport.getSubCompanyShortRentReceivable(subCompanyId);
        if (BigDecimalUtil.compare(subCompanyTotalShortRentReceivable, BigDecimal.ZERO) < 0) {
            return ErrorCode.SHORT_RECEIVABLE_CALCULATE_FAIL;
        }
        //得到分公司设置的短期上线
        SubCompanyDO subCompanyDO = subCompanyMapper.findById(subCompanyId);
        BigDecimal shortLimitReceivableAmount = customerDO.getShortLimitReceivableAmount() == null ? new BigDecimal(Integer.MAX_VALUE) : customerDO.getShortLimitReceivableAmount();
        BigDecimal subCompanyShortLimitReceivableAmount = subCompanyDO.getShortLimitReceivableAmount() == null ? new BigDecimal(Integer.MAX_VALUE) : subCompanyDO.getShortLimitReceivableAmount();

        //续租无运费和押金
        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderProductDOList())) {
            for (ReletOrderProductDO reletOrderProductDO : reletOrderDO.getReletOrderProductDOList()) {
                if (OrderRentLengthType.RENT_LENGTH_TYPE_SHORT.equals(reletOrderDO.getRentLengthType())) {
                    BigDecimal thisTotalAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(new BigDecimal(reletOrderProductDO.getProductCount()), reletOrderProductDO.getProductUnitAmount()), new BigDecimal(reletOrderDO.getRentTimeLength()));

                    customerTotalShortRentReceivable = BigDecimalUtil.add(customerTotalShortRentReceivable, thisTotalAmount);
                    if (BigDecimalUtil.compare(shortLimitReceivableAmount, customerTotalShortRentReceivable) < 0) {
                        return ErrorCode.CUSTOMER_SHORT_LIMIT_RECEIVABLE_OVERFLOW;
                    }
                    //检验分公司是否超出
                    subCompanyTotalShortRentReceivable = BigDecimalUtil.add(subCompanyTotalShortRentReceivable, thisTotalAmount);
                    if (BigDecimalUtil.compare(subCompanyShortLimitReceivableAmount, subCompanyTotalShortRentReceivable) < 0) {
                        return ErrorCode.SUB_COMPANY_SHORT_LIMIT_RECEIVABLE_OVERFLOW;
                    }
                }
            }
        }

        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderMaterialDOList())) {
            for (ReletOrderMaterialDO reletOrderMaterialDO : reletOrderDO.getReletOrderMaterialDOList()) {
                if (OrderRentLengthType.RENT_LENGTH_TYPE_SHORT.equals(reletOrderDO.getRentLengthType())) {
                    BigDecimal thisTotalAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(new BigDecimal(reletOrderMaterialDO.getMaterialCount()), reletOrderMaterialDO.getMaterialUnitAmount()), new BigDecimal(reletOrderDO.getRentTimeLength()));

                    customerTotalShortRentReceivable = BigDecimalUtil.add(customerTotalShortRentReceivable, thisTotalAmount);
                    if (BigDecimalUtil.compare(shortLimitReceivableAmount, customerTotalShortRentReceivable) < 0) {
                        return ErrorCode.CUSTOMER_SHORT_LIMIT_RECEIVABLE_OVERFLOW;
                    }
                    //检验分公司是否超出
                    subCompanyTotalShortRentReceivable = BigDecimalUtil.add(subCompanyTotalShortRentReceivable, thisTotalAmount);
                    if (BigDecimalUtil.compare(subCompanyShortLimitReceivableAmount, subCompanyTotalShortRentReceivable) < 0) {
                        return ErrorCode.SUB_COMPANY_SHORT_LIMIT_RECEIVABLE_OVERFLOW;
                    }
                }
            }
        }

        return ErrorCode.SUCCESS;
    }


    @Autowired
    private UserSupport userSupport;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ReletOrderMapper reletOrderMapper;

    @Autowired
    private ReletOrderProductMapper reletOrderProductMapper;

    @Autowired
    private ReletOrderMaterialMapper reletOrderMaterialMapper;

    @Autowired
    private StatementOrderSupport statementOrderSupport;

    @Autowired
    private ProductService productService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SubCompanyMapper subCompanyMapper;

    @Autowired
    private CustomerRiskManagementMapper customerRiskManagementMapper;

    @Autowired
    private MaterialTypeMapper materialTypeMapper;

    @Autowired
    private GenerateNoSupport generateNoSupport;


    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private OrderTimeAxisSupport orderTimeAxisSupport;

    @Autowired
    private StatementService statementService;

    @Autowired
    private PermissionSupport permissionSupport;
}
