package com.lxzl.erp.core.service.order.impl.support;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrderDetail;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.common.util.DateUtil;
import com.lxzl.erp.core.service.amount.support.AmountSupport;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.k3.K3Service;
import com.lxzl.erp.core.service.product.impl.support.ProductSupport;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.*;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.dao.mysql.returnOrder.ReturnOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.returnOrder.ReturnOrderMaterialBulkMapper;
import com.lxzl.erp.dataaccess.dao.mysql.returnOrder.ReturnOrderProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderMapper;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDO;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDetailDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.order.*;
import com.lxzl.erp.dataaccess.domain.product.ProductDO;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import com.lxzl.erp.dataaccess.domain.returnOrder.ReturnOrderDO;
import com.lxzl.erp.dataaccess.domain.returnOrder.ReturnOrderMaterialBulkDO;
import com.lxzl.erp.dataaccess.domain.returnOrder.ReturnOrderProductEquipmentDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDetailDO;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.dataaccess.mysql.source.interceptor.SqlLogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;


/**
 * 违约金计算
 *
 * @author kai
 * @date 2018-02-24 17:08
 */
@Component
public class PenaltySupport {

    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ReturnOrderMapper returnOrderMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ReturnOrderMaterialBulkMapper returnOrderMaterialBulkMapper;
    @Autowired
    private OrderMaterialBulkMapper orderMaterialBulkMapper;
    @Autowired
    private ProductSkuMapper productSkuMapper;
    @Autowired
    private ReturnOrderProductEquipmentMapper returnOrderProductEquipmentMapper;
    @Autowired
    private OrderProductEquipmentMapper orderProductEquipmentMapper;
    @Autowired
    private OrderMaterialMapper orderMaterialMapper;
    @Autowired
    private OrderProductMapper orderProductMapper;
    @Autowired
    private K3ReturnOrderMapper k3ReturnOrderMapper;
    @Autowired
    private K3Service k3Service;
    @Autowired
    private StatementOrderDetailMapper statementOrderDetailMapper;
    @Autowired
    private ProductSupport productSupport;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private UserSupport userSupport;
    @Autowired
    private AmountSupport amountSupport;
    @Autowired
    private StatementOrderMapper statementOrderMapper;
    @Autowired
    private GenerateNoSupport generateNoSupport;

    /**
     * erp违约金计算
     *
     * @param returnOrderNo
     * @return
     * @author kai
     * <p>
     * 目前根据退货单形式 看对方法 是否新增加字段
     */
    public ServiceResult<String, BigDecimal> orderPenalty(String returnOrderNo) {
        ServiceResult<String, BigDecimal> result = new ServiceResult<>();

        ReturnOrderDO returnOrderDO = returnOrderMapper.findByNo(returnOrderNo);
        if (returnOrderDO == null) {
            result.setErrorCode(ErrorCode.RETURN_ORDER_NOT_EXISTS);
            return result;
        }
        BigDecimal materialPenalty = new BigDecimal(0);
        BigDecimal materialTotal = new BigDecimal(0);
        for (int i = 0; i < returnOrderDO.getReturnOrderMaterialDOList().size(); i++) {
            MaterialDO materialDO = materialMapper.findById(returnOrderDO.getReturnOrderMaterialDOList().get(i).getReturnMaterialId());
            if (materialDO == null) {
                result.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
                return result;
            }
            //计算订单物料相同（全新次新出租价格不同问题）
            SqlLogInterceptor.setExecuteSql("skip print returnOrderMaterialBulkMapper.findByReturnOrderMaterialId  sql ......");
            List<ReturnOrderMaterialBulkDO> returnOrderMaterialBulkDO = returnOrderMaterialBulkMapper.findByReturnOrderMaterialId(returnOrderDO.getReturnOrderMaterialDOList().get(i).getId());
            if (returnOrderMaterialBulkDO == null) {
                result.setErrorCode(ErrorCode.RETURN_ORDER_MATERIAL_BULK_NOT_NULL);
                return result;
            }

            List<OrderMaterialBulkDO> orderMaterialBulkDOList = new ArrayList<>();
            for (int a = 0; a < returnOrderMaterialBulkDO.size(); a++) {
                SqlLogInterceptor.setExecuteSql("skip print orderMaterialBulkMapper.findByOrderNoAndBulkMaterialNo  sql ......");
                OrderMaterialBulkDO orderMaterialBulkDO = orderMaterialBulkMapper.findByOrderNoAndBulkMaterialNo(returnOrderMaterialBulkDO.get(a).getOrderNo(), returnOrderMaterialBulkDO.get(a).getBulkMaterialNo());
                if (orderMaterialBulkDO == null) {
                    result.setErrorCode(ErrorCode.ORDER_MATERIAL_BULK_NOT_EXISTS);
                    return result;
                }
                orderMaterialBulkDOList.add(orderMaterialBulkDO);
            }

            Map countMaterialMap = new HashMap();
            for (OrderMaterialBulkDO temp : orderMaterialBulkDOList) {
                Integer count = (Integer) countMaterialMap.get(temp.getOrderMaterialId() + "," + temp.getMaterialBulkUnitAmount());
                countMaterialMap.put(temp.getOrderMaterialId() + "," + temp.getMaterialBulkUnitAmount(), (count == null) ? 1 : count + 1);
            }

            OrderDO orderDO = orderMapper.findByOrderNo(returnOrderMaterialBulkDO.get(i).getOrderNo());
            if (orderDO == null) {
                result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
                return result;
            }

            BigDecimal price;
            Integer total;
            Integer orderMaterialId;
            Iterator it = countMaterialMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String key = (String) entry.getKey();
                String[] str = key.split(",");
                orderMaterialId = Integer.valueOf(str[0]);
                price = new BigDecimal(str[1]);
                total = (Integer) entry.getValue();

                OrderMaterialDO orderMaterialDO = orderMaterialMapper.findById(orderMaterialId);
                if (orderMaterialDO == null) {
                    result.setErrorCode(ErrorCode.ORDER_MATERIAL_NOT_EXISTS);
                    return result;
                }

                //设备租金单价与数量相乘
                BigDecimal materialPrice = BigDecimalUtil.mul(price, new BigDecimal(total));

                //(1)非即租即还设备，甲方提前退还租赁设备，乙方将收取合同期内未到期的剩余租金的50%作为违约金，且乙方无须退还其他费用及押金；
                if (materialDO.getIsReturnAnyTime() == CommonConstant.NO) {//是否允许随时归还，0否1是
                    if (orderMaterialDO.getRentType() == 2) {//判断月

                        //判断结算单租金付了多少
                        ServiceResult<String, List<StatementOrderDetail>> statementOrderDetailListResult = queryStatementOrderDetailByOrderId(orderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialId);
                        if (!statementOrderDetailListResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
                            result.setErrorCode(statementOrderDetailListResult.getErrorCode());
                            return result;
                        }

                        List<StatementOrderDetail> statementOrderDetailList = statementOrderDetailListResult.getResult();

                        BigDecimal yesPay = new BigDecimal(0);
                        BigDecimal noPay = new BigDecimal(0);
                        for (int j = 0; j < statementOrderDetailList.size(); j++) {
                            if (statementOrderDetailList.get(j).getOrderItemType() == 2 && statementOrderDetailList.get(j).getItemRentType() == 2) {
                                if (orderMaterialDO.getMaterialName().equals(statementOrderDetailList.get(j).getItemName())) {
                                    if (total.equals(statementOrderDetailList.get(j).getItemCount())) {
                                        if (price.equals(statementOrderDetailList.get(j).getUnitAmount())) {
                                            if (statementOrderDetailList.get(j).getStatementDetailStatus() == StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED) {
                                                yesPay = BigDecimalUtil.add(yesPay, statementOrderDetailList.get(j).getStatementDetailAmount());
                                            } else {
                                                noPay = BigDecimalUtil.add(noPay, statementOrderDetailList.get(j).getStatementDetailAmount());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        //计算平均物料的价格
                        BigDecimal average = BigDecimalUtil.div(noPay, new BigDecimal(orderMaterialDO.getMaterialCount()), 2);
                        //计算退货数量的剩馀租金
                        BigDecimal returnPrice = BigDecimalUtil.mul(average, new BigDecimal(total));
                        materialTotal = BigDecimalUtil.div(returnPrice, new BigDecimal(2), 2);
                    }
                } else if (materialDO.getIsReturnAnyTime() == CommonConstant.YES) {
                    if (orderMaterialDO.getRentType() == 2) {//判断月
                        materialTotal = materialAndProductTotal(materialPrice, orderDO.getRentStartTime(), orderDO.getRentTimeLength(), returnOrderDO.getReturnTime());
                    }
                }
                materialPenalty = BigDecimalUtil.add(materialPenalty, materialTotal);
            }
        }

        BigDecimal productPenalty = new BigDecimal(0);
        BigDecimal productTotal = new BigDecimal(0);
        for (int i = 0; i < returnOrderDO.getReturnOrderProductDOList().size(); i++) {
            ProductSkuDO productSkuDO = productSkuMapper.findById(returnOrderDO.getReturnOrderProductDOList().get(i).getReturnProductSkuId());
            if (productSkuDO == null) {
                result.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                return result;
            }
            ProductDO productDO = productMapper.findById(productSkuDO.getProductId());
            if (productDO == null) {
                result.setErrorCode(ErrorCode.PRODUCT_IS_NULL_OR_NOT_EXISTS);
                return result;
            }
            //计算订单设备相同（全新次新出租价格不同问题）
            SqlLogInterceptor.setExecuteSql("skip print returnOrderProductEquipmentMapper.findByReturnOrderProductId  sql ......");
            List<ReturnOrderProductEquipmentDO> returnOrderProductEquipmentDO = returnOrderProductEquipmentMapper.findByReturnOrderProductId(returnOrderDO.getReturnOrderProductDOList().get(i).getId());
            if (returnOrderProductEquipmentDO == null) {
                result.setErrorCode(ErrorCode.RETURN_ORDER_PRODUCT_EQUIPMENT_NOT_NULL);
                return result;
            }
            List<OrderProductEquipmentDO> orderProductEquipmentDOList = new ArrayList<>();
            for (int a = 0; a < returnOrderProductEquipmentDO.size(); a++) {
                SqlLogInterceptor.setExecuteSql("skip print orderProductEquipmentMapper.findByOrderNoAndEquipmentNo  sql ......");
                OrderProductEquipmentDO orderProductEquipmentDO = orderProductEquipmentMapper.findByOrderNoAndEquipmentNo(returnOrderProductEquipmentDO.get(a).getOrderNo(), returnOrderProductEquipmentDO.get(a).getEquipmentNo());
                if (orderProductEquipmentDO == null) {
                    result.setErrorCode(ErrorCode.ORDER_PRODUCT_EQUIPMENT_NOT_EXISTS);
                    return result;
                }
                orderProductEquipmentDOList.add(orderProductEquipmentDO);
            }
            Map countProductMap = new HashMap();
            for (OrderProductEquipmentDO temp : orderProductEquipmentDOList) {
                Integer count = (Integer) countProductMap.get(temp.getOrderProductId() + "," + temp.getProductEquipmentUnitAmount());
                countProductMap.put(temp.getOrderProductId() + "," + temp.getProductEquipmentUnitAmount(), (count == null) ? 1 : count + 1);
            }
            OrderDO orderDO = orderMapper.findByOrderNo(returnOrderProductEquipmentDO.get(i).getOrderNo());
            if (orderDO == null) {
                result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
                return result;
            }

            BigDecimal price;
            Integer total;
            Integer orderProductId;
            Iterator it = countProductMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String key = (String) entry.getKey();
                String[] str = key.split(",");
                orderProductId = Integer.valueOf(str[0]);
                price = new BigDecimal(str[1]);
                total = (Integer) entry.getValue();

                OrderProductDO orderProductDO = orderProductMapper.findById(orderProductId);
                if (orderProductDO == null) {
                    result.setErrorCode(ErrorCode.ORDER_PRODUCT_NOT_EXISTS);
                    return result;
                }

                //设备单价与数量相乘
                BigDecimal productPrice = BigDecimalUtil.mul(price, new BigDecimal(total));

                if (productDO.getIsReturnAnyTime() == CommonConstant.NO) {//是否允许随时归还，0否1是
                    if (orderProductDO.getRentType() == 2) {//判断月

                        //判断结算单租金付了多少
                        ServiceResult<String, List<StatementOrderDetail>> statementOrderDetailListResult = queryStatementOrderDetailByOrderId(orderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductId);
                        if (!statementOrderDetailListResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
                            result.setErrorCode(statementOrderDetailListResult.getErrorCode());
                            return result;
                        }

                        List<StatementOrderDetail> statementOrderDetailList = statementOrderDetailListResult.getResult();

                        BigDecimal yesPay = new BigDecimal(0);
                        BigDecimal noPay = new BigDecimal(0);
                        for (int j = 0; j < statementOrderDetailList.size(); j++) {
                            if (statementOrderDetailList.get(j).getOrderItemType() == 1 && statementOrderDetailList.get(j).getItemRentType() == 2) {
                                if ((orderProductDO.getProductName() + orderProductDO.getProductSkuName()).equals(statementOrderDetailList.get(j).getItemName())) {
                                    if (total.equals(statementOrderDetailList.get(j).getItemCount())) {
                                        if (price.equals(statementOrderDetailList.get(j).getUnitAmount())) {
                                            if (statementOrderDetailList.get(j).getStatementDetailStatus() == StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED) {
                                                yesPay = BigDecimalUtil.add(yesPay, statementOrderDetailList.get(j).getStatementDetailAmount());
                                            } else {
                                                noPay = BigDecimalUtil.add(noPay, statementOrderDetailList.get(j).getStatementDetailAmount());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        //计算平均物料的价格
                        BigDecimal average = BigDecimalUtil.div(noPay, new BigDecimal(orderProductDO.getProductCount()), 2);
                        //计算退货数量的剩馀租金
                        BigDecimal returnPrice = BigDecimalUtil.mul(average, new BigDecimal(total));
                        productTotal = BigDecimalUtil.div(returnPrice, new BigDecimal(2), 2);
                    }
                } else if (productDO.getIsReturnAnyTime() == CommonConstant.YES) {
                    if (orderProductDO.getRentType() == 2) {//判断月
                        productTotal = materialAndProductTotal(productPrice, orderDO.getRentStartTime(), orderDO.getRentTimeLength(), returnOrderDO.getReturnTime());
                    }
                }
                productPenalty = BigDecimalUtil.add(productPenalty, productTotal);
            }
        }
        BigDecimal totalPenalty = BigDecimalUtil.add(materialPenalty, productPenalty);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(totalPenalty);
        return result;
    }

    public BigDecimal materialAndProductTotal(BigDecimal price, Date rentStartTime, Integer rentTimeLength, Date returnTime) {
        /*
         * ①若约定租期为6个月以上，租满6个月之后退还，凌雄租赁不收取额外违约金，只需由客户承担退货运费；
         * 若6个月内退还，实际租期≤3个月，将收取3个月租金作为违约金，并由客户自己承担退货运费；
         * 实际租期＞3个月，需补齐6个月租金作为违约金（如客户已租期4个月，需支付2个月租金作为违约金），并由客户自己承担退货运费。
         *
         * ②若约定租期为6个月或以内，实际租期≤3个月，将收取3个月租金作为违约金，并由客户自己承担退货运费；实际租期＞3个月，
         * 需补齐6个月租金作为违约金（如客户已租期4个月，需另支付2个月租金作为违约金）。
         *
         * ③若约定租期为≤3个月，需补齐约定租期所有租金作为违约金（如客户已租期2个月，需另支付1个月租金作为违约金），
         * 并由客户自己承担退货运费；设备正常归还、款项结清后合同终止。（ERP未设置）
         */
        Integer month = DateUtil.getMonthSpace(rentStartTime, returnTime) + 1;
        BigDecimal Total = new BigDecimal(0);
        if (rentTimeLength > 6) {
            if (month >= 6) {
                Total = new BigDecimal(0);
            } else if (month <= 3) {
                Total = BigDecimalUtil.mul(price, new BigDecimal(3));
            } else if (month > 3 && month <= 6) {
                Total = BigDecimalUtil.mul(price, new BigDecimal(6 - month));
            }
        } else if (rentTimeLength <= 6 && rentTimeLength > 3) {
            if (month <= 3) {
                Total = BigDecimalUtil.mul(price, new BigDecimal(3));
            } else {
                Total = BigDecimalUtil.mul(price, new BigDecimal(6 - month));
            }
        } else if (rentTimeLength <= 3) {
            Total = BigDecimalUtil.mul(price, new BigDecimal(3 - month));
        }
        return Total;
    }

    /**
     * k3违约金
     *
     * @param k3ReturnOrderNo
     * @return
     * @author kai
     */
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, BigDecimal> k3OrderPenalty(String k3ReturnOrderNo) {
        ServiceResult<String, BigDecimal> result = new ServiceResult<>();
        K3ReturnOrderDO k3ReturnOrderDO = k3ReturnOrderMapper.findByNo(k3ReturnOrderNo);
        if (k3ReturnOrderDO == null) {
            result.setErrorCode(ErrorCode.K3_RETURN_ORDER_IS_NOT_NULL);
            return result;
        }
        String buyerCustomerNo = k3ReturnOrderDO.getK3CustomerNo();
        CustomerDO customerDO = customerMapper.findByNo(buyerCustomerNo);
        if (customerDO == null) {
            result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return result;
        }
        Integer buyerCustomerId = customerDO.getId();
//        SqlLogInterceptor.setExecuteSql("skip print k3Service.queryOrder  sql ......");
        Date currentTime = new Date();
        Date returnTime = k3ReturnOrderDO.getReturnTime();
        List<StatementOrderDetailDO> statementOrderDetailDOList = new ArrayList<StatementOrderDetailDO>();
        BigDecimal totalPenalty = BigDecimal.ZERO;
        List<K3ReturnOrderDetailDO> k3ReturnOrderDetailDOList = k3ReturnOrderDO.getK3ReturnOrderDetailDOList();
        for (K3ReturnOrderDetailDO k3ReturnOrderDetailDO : k3ReturnOrderDetailDOList) {
            BigDecimal productPenaltyAmount = BigDecimal.ZERO;
            String productNo = k3ReturnOrderDetailDO.getProductNo();
            if (StringUtil.isEmpty(productNo)) {
                result.setErrorCode(ErrorCode.PRODUCT_NO_IS_NULL);
                return result;
            }
            boolean isMaterial = productSupport.isMaterial(productNo);
            if (!isMaterial) {//商品
                OrderProductDO orderProductDO = orderProductMapper.findById(Integer.valueOf(k3ReturnOrderDetailDO.getOrderItemId()));
                if (orderProductDO == null) continue;  //k3订单商品跳过
                OrderDO orderDO = orderMapper.findById(orderProductDO.getOrderId());
                if (orderDO == null) {
                    result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
                    return result;
                }
                Date rentStartTime = orderDO.getRentStartTime();
                Date expectReturnTime = orderDO.getExpectReturnTime();
                //判断是否到期(到期的跳过)
                if (returnTime.compareTo(expectReturnTime) >= 0) continue;
                ProductDO productDO = productMapper.findById(orderProductDO.getProductId());
                if (productDO == null) {
                    result.setErrorCode(ErrorCode.PRODUCT_IS_NULL_OR_NOT_EXISTS);
                    return result;
                }
                //短短租的若提前退还，需补齐所有合同金额。若有支付部分的，需补交全部剩余租金。
                if (orderProductDO.getRentType() == OrderRentType.RENT_TYPE_DAY) {
                    //短短租订单未结清则无法退货
                    if (BigDecimalUtil.compare(orderDO.getTotalOrderAmount(), orderDO.getTotalPaidOrderAmount()) > 0) {
                        result.setErrorCode(ErrorCode.DAY_RENT_ORDER_NOT_PAY);
                        return result;
                    }
                    continue;
                    // productPenaltyAmount = amountSupport.calculateRentAmount(returnTime, expectReturnTime, orderProductDO.getProductUnitAmount(), k3ReturnOrderDetailDO.getProductCount());
                } else {
                    int monthGap = DateUtil.getMonthSpace(rentStartTime, returnTime);
                    if (productDO.getIsReturnAnyTime() == CommonConstant.NO) {//是否允许随时归还，0否1是//todo 后续增加续租商品 默认为即租即还
                        //未到期租金
                        BigDecimal remainRentAmount = amountSupport.calculateRentAmount(returnTime, expectReturnTime, orderProductDO.getProductUnitAmount(), k3ReturnOrderDetailDO.getProductCount());
                        //取未到期金额50%作为违约金
                        productPenaltyAmount = BigDecimalUtil.mul(remainRentAmount, new BigDecimal(0.5), BigDecimalUtil.STANDARD_SCALE);

                    } else if (productDO.getIsReturnAnyTime() == CommonConstant.YES) {
                        //针对即租即还设备，超过6个月但不满约定租期提前退还的，不收取额外违约金，只需由甲方承担退货运费；
                        if (monthGap >= 6) continue;//运费结算单是在退货中结算

                        // 若6个月内退还且租期≤3个月，将收取3个月租金作为违约金，并由甲方承担退货运费；//todo 等于3的判断
                        Calendar startRentCalendar = Calendar.getInstance();
                        Calendar returnTimeCalendar = Calendar.getInstance();
                        startRentCalendar.setTime(rentStartTime);
                        returnTimeCalendar.setTime(returnTime);
                        startRentCalendar.add(Calendar.MONTH, monthGap);
                        if (monthGap < 3 || startRentCalendar.getTimeInMillis() == returnTimeCalendar.getTimeInMillis()) {//小于等于3月
                            productPenaltyAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(orderProductDO.getProductUnitAmount(), new BigDecimal(3)), new BigDecimal(k3ReturnOrderDetailDO.getProductCount()), BigDecimalUtil.STANDARD_SCALE);
                        }
                        // 租期＞3个月，需补齐6个月租金作为违约金，并由甲方承担退货运费
                        else {
                            BigDecimal paidProductPenaltyAmount = amountSupport.calculateRentAmount(rentStartTime, returnTime, orderProductDO.getProductUnitAmount(), k3ReturnOrderDetailDO.getProductCount());
                            productPenaltyAmount = BigDecimalUtil.sub(BigDecimalUtil.mul(BigDecimalUtil.mul(orderProductDO.getProductUnitAmount(), new BigDecimal(6)), new BigDecimal(k3ReturnOrderDetailDO.getProductCount()), BigDecimalUtil.STANDARD_SCALE), paidProductPenaltyAmount, BigDecimalUtil.STANDARD_SCALE);
                        }
                    }
                }
                if (BigDecimalUtil.compare(productPenaltyAmount, BigDecimal.ZERO) < 0) {
                    result.setErrorCode(ErrorCode.ORDER_PRODUCT_AMOUNT_ERROR);
                    return result;
                }
                totalPenalty = BigDecimalUtil.add(totalPenalty, productPenaltyAmount);
                //创建违约金结算详情
                StatementOrderDetailDO statementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_RETURN, k3ReturnOrderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT, k3ReturnOrderDetailDO.getId(), returnTime, returnTime, returnTime, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, productPenaltyAmount, currentTime, userSupport.getCurrentUser().getUserId());
                if (statementOrderDetailDO == null) continue;
                statementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_PENALTY);
                //statementOrderDetailDO.setReturnReferId(k3ReturnOrderDetailDO.getId());
                statementOrderDetailDOList.add(statementOrderDetailDO);
            } else if (isMaterial) {//物料
                OrderMaterialDO orderMaterialDO = orderMaterialMapper.findById(Integer.valueOf(k3ReturnOrderDetailDO.getOrderItemId()));
                if (orderMaterialDO == null) continue; //k3订单物料跳过
                OrderDO orderDO = orderMapper.findById(orderMaterialDO.getOrderId());
                if (orderDO == null) {
                    result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
                    return result;
                }
                Date rentStartTime = orderDO.getRentStartTime();
                Date expectReturnTime = orderDO.getExpectReturnTime();
                //判断是否到期(到期的跳过)
                if (returnTime.compareTo(expectReturnTime) >= 0) continue;

                MaterialDO materialDO = materialMapper.findById(orderMaterialDO.getMaterialId());
                if (materialDO == null) {
                    result.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
                    return result;
                }
                //短短租的若提前退还，需补齐所有合同金额。若有支付部分的，需补交全部剩余租金。
                if (orderMaterialDO.getRentType() == OrderRentType.RENT_TYPE_DAY) {
                    //短短租未结清，则不允许退货
                    if (BigDecimalUtil.compare(orderDO.getTotalOrderAmount(), orderDO.getTotalPaidOrderAmount()) > 0) {
                        result.setErrorCode(ErrorCode.DAY_RENT_ORDER_NOT_PAY);
                        return result;
                    }
                    continue;
                    //productPenaltyAmount = amountSupport.calculateRentAmount(returnTime, expectReturnTime, orderMaterialDO.getMaterialUnitAmount(), k3ReturnOrderDetailDO.getProductCount());
                } else {
                    int monthGap = DateUtil.getMonthSpace(rentStartTime, returnTime);
                    if (materialDO.getIsReturnAnyTime() == CommonConstant.NO) {//是否允许随时归还，0否1是//todo 后续增加续租商品 默认为即租即还

                        //未到期租金
                        BigDecimal remainRentAmount = amountSupport.calculateRentAmount(returnTime, expectReturnTime, orderMaterialDO.getMaterialUnitAmount(), k3ReturnOrderDetailDO.getProductCount());
                        //取未到期金额50%作为违约金
                        productPenaltyAmount = BigDecimalUtil.mul(remainRentAmount, new BigDecimal(0.5), BigDecimalUtil.STANDARD_SCALE);

                    } else if (materialDO.getIsReturnAnyTime() == CommonConstant.YES) {
                        //针对即租即还设备，超过6个月但不满约定租期提前退还的，不收取额外违约金，只需由甲方承担退货运费；
                        if (monthGap >= 6) continue;//todo 运费结算单是在退货中结算

                        // 若6个月内退还且租期≤3个月，将收取3个月租金作为违约金，并由甲方承担退货运费；
                        Calendar startRentCalendar = Calendar.getInstance();
                        Calendar returnTimeCalendar = Calendar.getInstance();
                        startRentCalendar.setTime(rentStartTime);
                        returnTimeCalendar.setTime(returnTime);
                        startRentCalendar.add(Calendar.MONTH, monthGap);
                        if (monthGap < 3 || startRentCalendar.getTimeInMillis() == returnTimeCalendar.getTimeInMillis()) {//小于等于3月
                            productPenaltyAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(orderMaterialDO.getMaterialUnitAmount(), new BigDecimal(3)), new BigDecimal(k3ReturnOrderDetailDO.getProductCount()), BigDecimalUtil.STANDARD_SCALE);
                        }
                        // 租期＞3个月，需补齐6个月租金作为违约金，并由甲方承担退货运费
                        else {
                            BigDecimal paidProductPenaltyAmount = amountSupport.calculateRentAmount(rentStartTime, returnTime, orderMaterialDO.getMaterialUnitAmount(), k3ReturnOrderDetailDO.getProductCount());
                            productPenaltyAmount = BigDecimalUtil.sub(BigDecimalUtil.mul(BigDecimalUtil.mul(orderMaterialDO.getMaterialUnitAmount(), new BigDecimal(6)), new BigDecimal(k3ReturnOrderDetailDO.getProductCount()), BigDecimalUtil.STANDARD_SCALE), paidProductPenaltyAmount, BigDecimalUtil.STANDARD_SCALE);
                        }
                    }
                }
                if (BigDecimalUtil.compare(productPenaltyAmount, BigDecimal.ZERO) < 0) {
                    result.setErrorCode(ErrorCode.ORDER_MATERIAL_AMOUNT_ERROR);
                    return result;
                }
                totalPenalty = BigDecimalUtil.add(totalPenalty, productPenaltyAmount);
                //创建违约金结算详情
                StatementOrderDetailDO statementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_RETURN, k3ReturnOrderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT, k3ReturnOrderDetailDO.getId(), returnTime, returnTime, returnTime, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, productPenaltyAmount, currentTime, userSupport.getCurrentUser().getUserId());
                if (statementOrderDetailDO == null) continue;
                statementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_PENALTY);
                // statementOrderDetailDO.setReturnReferId(statementOrderDetailDO.getId());
                statementOrderDetailDOList.add(statementOrderDetailDO);
            }

        }
        //修改结算单
        saveStatementOrder(statementOrderDetailDOList, currentTime, userSupport.getCurrentUser().getUserId());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(totalPenalty);
        return result;
    }


    public ServiceResult<String, List<StatementOrderDetail>> queryStatementOrderDetailByOrderId(Integer
                                                                                                        orderId, Integer orderItemType, Integer orderItemReferId) {
        ServiceResult<String, List<StatementOrderDetail>> result = new ServiceResult<>();

        List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByOrderIdAndOrderItemType(orderId, orderItemType, orderItemReferId);
        if (statementOrderDetailDOList == null) {
            result.setErrorCode(ErrorCode.STATEMENT_ORDER_DETAIL_NOT_EXISTS);
            return result;
        }
        List<StatementOrderDetail> statementOrderDetailList = ConverterUtil.convertList(statementOrderDetailDOList, StatementOrderDetail.class);

        if (CollectionUtil.isNotEmpty(statementOrderDetailList)) {
            for (StatementOrderDetail statementOrderDetail : statementOrderDetailList) {
                if (OrderType.ORDER_TYPE_ORDER.equals(statementOrderDetail.getOrderType())) {
                    SqlLogInterceptor.setExecuteSql("skip print orderMapper.findByOrderId  sql ......");
                    OrderDO orderDO = orderMapper.findByOrderId(statementOrderDetail.getOrderId());
                    if (orderDO != null) {
                        if (CollectionUtil.isNotEmpty(orderDO.getOrderProductDOList())) {
                            for (OrderProductDO orderProductDO : orderDO.getOrderProductDOList()) {
                                if (OrderItemType.ORDER_ITEM_TYPE_PRODUCT.equals(statementOrderDetail.getOrderItemType()) && statementOrderDetail.getOrderItemReferId().equals(orderProductDO.getId())) {
                                    statementOrderDetail.setItemCount(orderProductDO.getProductCount());
                                    statementOrderDetail.setItemName(orderProductDO.getProductName() + orderProductDO.getProductSkuName());
                                    statementOrderDetail.setUnitAmount(orderProductDO.getProductUnitAmount());
                                    statementOrderDetail.setItemRentType(orderProductDO.getRentType());
                                    break;
                                }
                            }
                        }
                        if (CollectionUtil.isNotEmpty(orderDO.getOrderMaterialDOList())) {
                            for (OrderMaterialDO orderMaterialDO : orderDO.getOrderMaterialDOList()) {
                                if (OrderItemType.ORDER_ITEM_TYPE_MATERIAL.equals(statementOrderDetail.getOrderItemType()) && statementOrderDetail.getOrderItemReferId().equals(orderMaterialDO.getId())) {
                                    statementOrderDetail.setItemCount(orderMaterialDO.getMaterialCount());
                                    statementOrderDetail.setItemName(orderMaterialDO.getMaterialName());
                                    statementOrderDetail.setUnitAmount(orderMaterialDO.getMaterialUnitAmount());
                                    statementOrderDetail.setItemRentType(orderMaterialDO.getRentType());
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(statementOrderDetailList);
        return result;
    }

    StatementOrderDetailDO buildStatementOrderDetailDO(Integer customerId, Integer orderType, Integer
            orderId, Integer orderItemType, Integer orderItemReferId, Date statementExpectPayTime, Date startTime, Date
                                                               endTime, BigDecimal statementDetailRentAmount, BigDecimal statementDetailRentDepositAmount, BigDecimal
                                                               statementDetailDepositAmount, BigDecimal otherAmount, BigDecimal penaltyAmount, Date currentTime, Integer
                                                               loginUserId) {
        StatementOrderDetailDO statementOrderDetailDO = new StatementOrderDetailDO();
        statementOrderDetailDO.setCustomerId(customerId);
        statementOrderDetailDO.setOrderType(orderType);
        statementOrderDetailDO.setOrderId(orderId);
        statementOrderDetailDO.setOrderItemType(orderItemType);
        statementOrderDetailDO.setOrderItemReferId(orderItemReferId);
        statementOrderDetailDO.setStatementExpectPayTime(com.lxzl.se.common.util.date.DateUtil.getBeginOfDay(statementExpectPayTime));
        statementOrderDetailDO.setStatementStartTime(startTime);
        statementOrderDetailDO.setStatementEndTime(endTime);
        statementOrderDetailDO.setStatementDetailAmount(BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(statementDetailRentDepositAmount, statementDetailDepositAmount), statementDetailRentAmount), otherAmount), penaltyAmount));
        statementOrderDetailDO.setStatementDetailRentDepositAmount(statementDetailRentDepositAmount);
        statementOrderDetailDO.setStatementDetailDepositAmount(statementDetailDepositAmount);
        statementOrderDetailDO.setStatementDetailRentAmount(statementDetailRentAmount);
        statementOrderDetailDO.setStatementDetailOtherAmount(otherAmount);
        statementOrderDetailDO.setStatementDetailPenaltyAmount(penaltyAmount);
        if (BigDecimalUtil.compare(statementDetailRentDepositAmount, BigDecimal.ZERO) > 0
                || BigDecimalUtil.compare(statementDetailDepositAmount, BigDecimal.ZERO) > 0
                || BigDecimalUtil.compare(statementDetailRentAmount, BigDecimal.ZERO) > 0
                || BigDecimalUtil.compare(otherAmount, BigDecimal.ZERO) > 0 || BigDecimalUtil.compare(penaltyAmount, BigDecimal.ZERO) > 0) {
            statementOrderDetailDO.setStatementDetailStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_INIT);
        } else if (BigDecimalUtil.compare(statementDetailRentDepositAmount, BigDecimal.ZERO) < 0
                || BigDecimalUtil.compare(statementDetailDepositAmount, BigDecimal.ZERO) < 0
                || BigDecimalUtil.compare(statementDetailRentAmount, BigDecimal.ZERO) < 0
                || BigDecimalUtil.compare(otherAmount, BigDecimal.ZERO) < 0 || BigDecimalUtil.compare(penaltyAmount, BigDecimal.ZERO) < 0) {
            statementOrderDetailDO.setStatementDetailStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_NO);
        } else {
            return null;
        }
        statementOrderDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        statementOrderDetailDO.setCreateTime(currentTime);
        statementOrderDetailDO.setUpdateTime(currentTime);
        statementOrderDetailDO.setCreateUser(loginUserId.toString());
        statementOrderDetailDO.setUpdateUser(loginUserId.toString());
        return statementOrderDetailDO;
    }

    void saveStatementOrder(List<StatementOrderDetailDO> addStatementOrderDetailDOList, Date currentTime, Integer loginUserId) {

        if (CollectionUtil.isNotEmpty(addStatementOrderDetailDOList)) {
            // 同一个时间的做归集
            Map<Date, StatementOrderDO> statementOrderDOMap = new HashMap<>();
            for (StatementOrderDetailDO statementOrderDetailDO : addStatementOrderDetailDOList) {
                if (statementOrderDetailDO == null) {
                    continue;
                }
                Date dateKey = com.lxzl.se.common.util.date.DateUtil.getBeginOfDay(statementOrderDetailDO.getStatementExpectPayTime());
                StatementOrderDO statementOrderDO = statementOrderMapper.findByCustomerAndPayTime(statementOrderDetailDO.getCustomerId(), dateKey);
                if (statementOrderDO != null) {
                    statementOrderDOMap.put(dateKey, statementOrderDO);
                }

                if (!statementOrderDOMap.containsKey(dateKey)) {
                    statementOrderDO = new StatementOrderDO();
                    statementOrderDO.setStatementOrderNo(generateNoSupport.generateStatementOrderNo(dateKey, statementOrderDetailDO.getCustomerId()));
                    statementOrderDO.setCustomerId(statementOrderDetailDO.getCustomerId());
                    if (dateKey != null) {
                        statementOrderDO.setStatementExpectPayTime(com.lxzl.se.common.util.date.DateUtil.getBeginOfDay(dateKey));
                    }
                    statementOrderDO.setStatementAmount(statementOrderDetailDO.getStatementDetailAmount());
                    statementOrderDO.setStatementRentDepositAmount(statementOrderDetailDO.getStatementDetailRentDepositAmount());
                    statementOrderDO.setStatementDepositAmount(statementOrderDetailDO.getStatementDetailDepositAmount());
                    statementOrderDO.setStatementRentAmount(statementOrderDetailDO.getStatementDetailRentAmount());
                    statementOrderDO.setStatementOtherAmount(statementOrderDetailDO.getStatementDetailOtherAmount());
                    statementOrderDO.setStatementPenaltyAmount(statementOrderDetailDO.getStatementDetailOtherAmount());
                    statementOrderDO.setStatementStartTime(statementOrderDetailDO.getStatementStartTime());
                    statementOrderDO.setStatementEndTime(statementOrderDetailDO.getStatementEndTime());
                    statementOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    statementOrderDO.setCreateUser(loginUserId.toString());
                    statementOrderDO.setUpdateUser(loginUserId.toString());
                    statementOrderDO.setCreateTime(currentTime);
                    statementOrderDO.setUpdateTime(currentTime);
                    statementOrderDO.setStatementCouponAmount(statementOrderDetailDO.getStatementCouponAmount());
                    statementOrderMapper.save(statementOrderDO);
                } else {
                    statementOrderDO = statementOrderDOMap.get(dateKey);
                    statementOrderDO.setStatementAmount(BigDecimalUtil.add(BigDecimalUtil.round(statementOrderDO.getStatementAmount(), BigDecimalUtil.STANDARD_SCALE), BigDecimalUtil.round(statementOrderDetailDO.getStatementDetailAmount(), BigDecimalUtil.STANDARD_SCALE)));
                    // 结算单不能为负数
                    if (BigDecimalUtil.compare(statementOrderDO.getStatementAmount(), BigDecimal.ZERO) < 0) {
                        BigDecimal diffAmount = BigDecimalUtil.sub(BigDecimal.ZERO, statementOrderDO.getStatementAmount());
                        statementOrderDO.setStatementAmount(BigDecimal.ZERO);
                        statementOrderDetailDO.setStatementDetailAmount(BigDecimalUtil.sub(BigDecimalUtil.round(statementOrderDetailDO.getStatementDetailAmount(), BigDecimalUtil.STANDARD_SCALE), BigDecimalUtil.round(diffAmount, BigDecimalUtil.STANDARD_SCALE)));
                    }
                    statementOrderDO.setStatementRentAmount(BigDecimalUtil.add(BigDecimalUtil.round(statementOrderDO.getStatementRentAmount(), BigDecimalUtil.STANDARD_SCALE), BigDecimalUtil.round(statementOrderDetailDO.getStatementDetailRentAmount(), BigDecimalUtil.STANDARD_SCALE)));
                    // 结算单不能为负数
                    if (BigDecimalUtil.compare(statementOrderDO.getStatementRentAmount(), BigDecimal.ZERO) < 0) {
                        BigDecimal diffAmount = BigDecimalUtil.sub(BigDecimal.ZERO, statementOrderDO.getStatementRentAmount());
                        statementOrderDO.setStatementRentAmount(BigDecimal.ZERO);
                        statementOrderDetailDO.setStatementDetailRentAmount(BigDecimalUtil.sub(BigDecimalUtil.round(statementOrderDetailDO.getStatementDetailRentAmount(), BigDecimalUtil.STANDARD_SCALE), BigDecimalUtil.round(diffAmount, BigDecimalUtil.STANDARD_SCALE)));
                    }
                    statementOrderDO.setStatementDepositAmount(BigDecimalUtil.add(BigDecimalUtil.round(statementOrderDO.getStatementDepositAmount(), BigDecimalUtil.STANDARD_SCALE), BigDecimalUtil.round(statementOrderDetailDO.getStatementDetailDepositAmount(), BigDecimalUtil.STANDARD_SCALE)));
                    statementOrderDO.setStatementRentDepositAmount(BigDecimalUtil.add(BigDecimalUtil.round(statementOrderDO.getStatementRentDepositAmount(), BigDecimalUtil.STANDARD_SCALE), BigDecimalUtil.round(statementOrderDetailDO.getStatementDetailRentDepositAmount(), BigDecimalUtil.STANDARD_SCALE)));
                    statementOrderDO.setStatementOtherAmount(BigDecimalUtil.add(BigDecimalUtil.round(statementOrderDO.getStatementOtherAmount(), BigDecimalUtil.STANDARD_SCALE), BigDecimalUtil.round(statementOrderDetailDO.getStatementDetailOtherAmount(), BigDecimalUtil.STANDARD_SCALE)));
                    statementOrderDO.setStatementPenaltyAmount(BigDecimalUtil.add(BigDecimalUtil.round(statementOrderDO.getStatementPenaltyAmount(), BigDecimalUtil.STANDARD_SCALE), BigDecimalUtil.round(statementOrderDetailDO.getStatementDetailPenaltyAmount(), BigDecimalUtil.STANDARD_SCALE)));
                    if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDO.getStatementStatus())) {
                        statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED_PART);
                    }
                    if (BigDecimalUtil.compare(statementOrderDO.getStatementAmount(), new BigDecimal(0.1)) < 0) {
                        statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_NO);
                    }
                    if (statementOrderDetailDO.getStatementStartTime().getTime() < statementOrderDO.getStatementStartTime().getTime()) {
                        statementOrderDO.setStatementStartTime(statementOrderDetailDO.getStatementStartTime());
                    }
                    if (statementOrderDetailDO.getStatementEndTime().getTime() > statementOrderDO.getStatementEndTime().getTime()) {
                        statementOrderDO.setStatementEndTime(statementOrderDetailDO.getStatementEndTime());
                    }
                    statementOrderDO.setStatementCouponAmount(statementOrderDetailDO.getStatementCouponAmount());
                    statementOrderMapper.update(statementOrderDO);
                }
                statementOrderDOMap.put(dateKey, statementOrderDO);
                statementOrderDetailDO.setStatementOrderId(statementOrderDO.getId());
            }
            if (CollectionUtil.isNotEmpty(addStatementOrderDetailDOList)) {
                statementOrderDetailMapper.saveList(addStatementOrderDetailDOList);
            }
        }
    }
}
