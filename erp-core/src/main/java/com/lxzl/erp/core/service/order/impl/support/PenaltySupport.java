package com.lxzl.erp.core.service.order.impl.support;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.StatementOrderStatus;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.k3.pojo.order.Order;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrder;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrderDetail;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.DateUtil;
import com.lxzl.erp.core.service.k3.K3Service;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.*;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.dao.mysql.returnOrder.ReturnOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.returnOrder.ReturnOrderMaterialBulkMapper;
import com.lxzl.erp.dataaccess.dao.mysql.returnOrder.ReturnOrderProductEquipmentMapper;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.order.*;
import com.lxzl.erp.dataaccess.domain.product.ProductDO;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import com.lxzl.erp.dataaccess.domain.returnOrder.ReturnOrderDO;
import com.lxzl.erp.dataaccess.domain.returnOrder.ReturnOrderMaterialBulkDO;
import com.lxzl.erp.dataaccess.domain.returnOrder.ReturnOrderProductEquipmentDO;
import com.lxzl.se.dataaccess.mysql.source.interceptor.SqlLogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;


/**
 * 描述: ${DESCRIPTION}
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
    private StatementService statementService;
    @Autowired
    private OrderMaterialMapper orderMaterialMapper;
    @Autowired
    private OrderProductMapper orderProductMapper;
    @Autowired
    private K3ReturnOrderMapper k3ReturnOrderMapper;
    @Autowired
    private K3Service k3Service;

    /**
     * erp违约金计算
     *
     * @param returnOrderNo
     * @return
     * @author kai
     *
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
            //todo 计算订单物料相同（全新次新出租价格不同问题）
            SqlLogInterceptor.setExecuteSql("skip print returnOrderMaterialBulkMapper.findByReturnOrderMaterialId  sql ......");
            List<ReturnOrderMaterialBulkDO> returnOrderMaterialBulkDO = returnOrderMaterialBulkMapper.findByReturnOrderMaterialId(returnOrderDO.getReturnOrderMaterialDOList().get(i).getId());
            if (returnOrderMaterialBulkDO == null) {
                result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
                return result;
            }
            Map<BigDecimal, Integer> map = new HashMap<>();
            BigDecimal amount = null;
            Integer count = 1;
            for (int y = 0; y < returnOrderMaterialBulkDO.size(); y++) {
                OrderMaterialBulkDO orderMaterialBulkDO = orderMaterialBulkMapper.findByOrderNoAndBulkMaterialNo(returnOrderMaterialBulkDO.get(y).getOrderNo(), returnOrderMaterialBulkDO.get(y).getBulkMaterialNo());
                if (amount == null) {
                    amount = orderMaterialBulkDO.getMaterialBulkUnitAmount();
                    map.put(amount, count);
                    count++;
                } else if (orderMaterialBulkDO.getMaterialBulkUnitAmount().equals(amount)) {
                    amount = orderMaterialBulkDO.getMaterialBulkUnitAmount();
                    map.put(amount, count);
                    count++;
                } else {
                    //todo 如果退货 新 旧 新 旧 退 有问题  （退货单物料退貨本身有問題）
                    amount = orderMaterialBulkDO.getMaterialBulkUnitAmount();
                    count = 1;
                    map.put(amount, count);
                    count++;
                }
            }

            OrderMaterialBulkDO orderMaterialBulkDO = orderMaterialBulkMapper.findByOrderNoAndBulkMaterialNo(returnOrderMaterialBulkDO.get(0).getOrderNo(), returnOrderMaterialBulkDO.get(0).getBulkMaterialNo());
            if (orderMaterialBulkDO == null) {
                result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
                return result;
            }
            OrderMaterialDO orderMaterialDO = orderMaterialMapper.findById(orderMaterialBulkDO.getOrderMaterialId());
            if (orderMaterialDO == null) {
                result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
                return result;
            }
            OrderDO orderDO = orderMapper.findByOrderNo(returnOrderMaterialBulkDO.get(i).getOrderNo());
            if (orderDO == null) {
                result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
                return result;
            }
            BigDecimal price = null;
            Integer total = 0;
            Iterator it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                price = (BigDecimal) entry.getKey();
                total = (Integer) entry.getValue();

                //设备租金单价与数量相乘
                BigDecimal materialPrice = BigDecimalUtil.mul(price, new BigDecimal(total));

                //(1)非即租即还设备，甲方提前退还租赁设备，乙方将收取合同期内未到期的剩余租金的50%作为违约金，且乙方无须退还其他费用及押金；
                if (materialDO.getIsReturnAnyTime() == CommonConstant.NO) {//是否允许随时归还，0否1是
                    if (orderMaterialDO.getRentType() == 2) {//判断月

                        //判断结算单租金付了多少
                        SqlLogInterceptor.setExecuteSql("skip print statementService.queryStatementOrderDetailByOrderId  sql ......");
                        ServiceResult<String, StatementOrder> statementOrderServiceResult = statementService.queryStatementOrderDetailByOrderId(orderDO.getOrderNo());
                        List<StatementOrderDetail> statementOrderDetailList = statementOrderServiceResult.getResult().getStatementOrderDetailList();
                        BigDecimal yesPay = new BigDecimal(0);
                        BigDecimal noPay = new BigDecimal(0);
                        for (int j = 0; j < statementOrderDetailList.size(); j++) {
                            if (statementOrderDetailList.get(j).getOrderItemType() == 2 && statementOrderDetailList.get(j).getStatementDetailType() == 1 && statementOrderDetailList.get(j).getItemRentType() == 2) {
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
            //todo 计算订单设备相同（全新次新出租价格不同问题）
            SqlLogInterceptor.setExecuteSql("skip print returnOrderProductEquipmentMapper.findByReturnOrderProductId  sql ......");
            List<ReturnOrderProductEquipmentDO> returnOrderProductEquipmentDO = returnOrderProductEquipmentMapper.findByReturnOrderProductId(returnOrderDO.getReturnOrderProductDOList().get(i).getId());
            if (returnOrderProductEquipmentDO == null) {
                result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
                return result;
            }
            Map<BigDecimal, Integer> map = new HashMap<>();
            BigDecimal amount = null;
            Integer count = 1;
            for (int y = 0; y < returnOrderProductEquipmentDO.size(); y++) {
                OrderProductEquipmentDO orderProductEquipmentDO = orderProductEquipmentMapper.findByOrderNoAndEquipmentNo(returnOrderProductEquipmentDO.get(y).getOrderNo(), returnOrderProductEquipmentDO.get(y).getEquipmentNo());
                if (amount == null) {
                    amount = orderProductEquipmentDO.getProductEquipmentUnitAmount();
                    map.put(amount, count);
                    count++;
                } else if (orderProductEquipmentDO.getProductEquipmentUnitAmount().equals(amount)) {
                    amount = orderProductEquipmentDO.getProductEquipmentUnitAmount();
                    map.put(amount, count);
                    count++;
                } else {
                    //todo 如果依据商品价格 退货 新 旧 新 旧 退 有问题
                    amount = orderProductEquipmentDO.getProductEquipmentUnitAmount();
                    count = 1;
                    map.put(amount, count);
                    count++;
                }
            }

            OrderProductEquipmentDO orderProductEquipmentDO = orderProductEquipmentMapper.findByOrderNoAndEquipmentNo(returnOrderProductEquipmentDO.get(0).getOrderNo(), returnOrderProductEquipmentDO.get(0).getEquipmentNo());
            if (orderProductEquipmentDO == null) {
                result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
                return result;
            }
            OrderProductDO orderProductDO = orderProductMapper.findById(orderProductEquipmentDO.getOrderProductId());
            if (orderProductDO == null) {
                result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
                return result;
            }
            OrderDO orderDO = orderMapper.findByOrderNo(returnOrderProductEquipmentDO.get(i).getOrderNo());
            if (orderDO == null) {
                result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
                return result;
            }

            BigDecimal price = null;
            Integer total = 0;
            Iterator it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                price = (BigDecimal) entry.getKey();
                total = (Integer) entry.getValue();

                //设备单价与数量相乘
                BigDecimal productPrice = BigDecimalUtil.mul(price, new BigDecimal(total));

                if (productDO.getIsReturnAnyTime() == CommonConstant.NO) {//是否允许随时归还，0否1是
                    if (orderProductDO.getRentType() == 2) {//判断月

                        //判断结算单租金付了多少
                        SqlLogInterceptor.setExecuteSql("skip print statementService.queryStatementOrderDetailByOrderId  sql ......");
                        ServiceResult<String, StatementOrder> statementOrderServiceResult = statementService.queryStatementOrderDetailByOrderId(orderDO.getOrderNo());
                        List<StatementOrderDetail> statementOrderDetailList = statementOrderServiceResult.getResult().getStatementOrderDetailList();
                        BigDecimal yesPay = new BigDecimal(0);
                        BigDecimal noPay = new BigDecimal(0);
                        for (int j = 0; j < statementOrderDetailList.size(); j++) {
                            if (statementOrderDetailList.get(j).getOrderItemType() == 1 && statementOrderDetailList.get(j).getStatementDetailType() == 1 && statementOrderDetailList.get(j).getItemRentType() == 2) {
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
     * @param returnOrderNo
     * @return
     * @author kai
     *
     */
    public ServiceResult<String, BigDecimal> k3OrderPenalty(String returnOrderNo) {
        ServiceResult<String, BigDecimal> result = new ServiceResult<>();

        K3ReturnOrderDO k3ReturnOrderDO = k3ReturnOrderMapper.findByNo(returnOrderNo);
        if (k3ReturnOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }

        BigDecimal productPenalty = new BigDecimal(0);
        BigDecimal productTotal = new BigDecimal(0);
        for (int i = 0; i < k3ReturnOrderDO.getK3ReturnOrderDetailDOList().size(); i++) {
            SqlLogInterceptor.setExecuteSql("skip print k3Service.queryOrder  sql ......");
            ServiceResult<String, Order> k3Order = k3Service.queryOrder(k3ReturnOrderDO.getK3ReturnOrderDetailDOList().get(i).getOrderNo());
            if (k3Order == null) {
                result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
                return result;
            }
            //todo 物料的违约金判别（目前只有商品）

            //todo K3商品编码与ERP商品编码不一致 先取ERP订单判别(看后续更改用什么查询)

            //todo 新旧机同时名称 金额价格不同

            OrderDO orderDO = orderMapper.findByOrderNo(k3Order.getResult().getOrderNo());
            if (orderDO == null) {
                result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
                return result;
            }
            List<OrderProductDO> orderProductDO = orderProductMapper.findByOrderId(orderDO.getId());
            if (orderDO == null) {
                result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
                return result;
            }

            for (int j = 0; j < orderProductDO.size(); j++) {
                if (orderProductDO.get(j).getProductName().equals(k3ReturnOrderDO.getK3ReturnOrderDetailDOList().get(i).getProductName())) {
                    ProductDO productDO = productMapper.findById(orderProductDO.get(j).getProductId());
                    //设备单价与数量相乘
                    BigDecimal productPrice = BigDecimalUtil.mul((orderProductDO.get(j).getProductUnitAmount()), new BigDecimal(k3ReturnOrderDO.getK3ReturnOrderDetailDOList().get(i).getProductCount()));

                    if (productDO.getIsReturnAnyTime() == CommonConstant.NO) {//是否允许随时归还，0否1是

                        //判断结算单租金付了多少
                        SqlLogInterceptor.setExecuteSql("skip print statementService.queryStatementOrderDetailByOrderId  sql ......");
                        ServiceResult<String, StatementOrder> statementOrderServiceResult = statementService.queryStatementOrderDetailByOrderId(orderDO.getOrderNo());
                        List<StatementOrderDetail> statementOrderDetailList = statementOrderServiceResult.getResult().getStatementOrderDetailList();
                        BigDecimal yesPay = new BigDecimal(0);
                        BigDecimal noPay = new BigDecimal(0);
                        for (int z = 0; z < statementOrderDetailList.size(); z++) {
                            if (statementOrderDetailList.get(z).getOrderItemType() == 1 && statementOrderDetailList.get(z).getStatementDetailType() == 1 && statementOrderDetailList.get(z).getItemRentType() == 2) {
                                if ((orderProductDO.get(j).getProductName() + orderProductDO.get(j).getProductSkuName()).equals(statementOrderDetailList.get(z).getItemName())) {
                                    if (orderProductDO.get(j).getProductCount().equals(statementOrderDetailList.get(z).getItemCount())) {
                                        if (orderProductDO.get(j).getProductUnitAmount().equals(statementOrderDetailList.get(z).getUnitAmount())) {
                                            if (statementOrderDetailList.get(z).getStatementDetailStatus() == StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED) {
                                                yesPay = BigDecimalUtil.add(yesPay, statementOrderDetailList.get(z).getStatementDetailAmount());
                                            } else {
                                                noPay = BigDecimalUtil.add(noPay, statementOrderDetailList.get(z).getStatementDetailAmount());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        //计算平均物料的价格
                        BigDecimal average = BigDecimalUtil.div(noPay, new BigDecimal(orderProductDO.get(j).getProductCount()), 2);
                        //计算退货数量的剩馀租金
                        BigDecimal returnPrice = BigDecimalUtil.mul(average, new BigDecimal(k3ReturnOrderDO.getK3ReturnOrderDetailDOList().get(i).getProductCount()));
                        productTotal = BigDecimalUtil.div(returnPrice, new BigDecimal(2), 2);

                    } else if (productDO.getIsReturnAnyTime() == CommonConstant.YES) {
                        if (orderProductDO.get(j).getRentType() == 2) {//判断月
                            productTotal = materialAndProductTotal(productPrice, k3Order.getResult().getRentStartTime(), k3Order.getResult().getRentTimeLength(), k3ReturnOrderDO.getReturnTime());
                        }
                    }
                }
            }
            productPenalty = BigDecimalUtil.add(productPenalty, productTotal);
        }
        BigDecimal totalPenalty = productPenalty;

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(totalPenalty);
        return result;
    }

}
