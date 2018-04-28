package com.lxzl.erp.core.service.coupon.impl.support;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.CouponStatus;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.coupon.pojo.Coupon;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.order.pojo.OrderProduct;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrder;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrderDetail;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.coupon.CouponBatchDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.coupon.CouponBatchMapper;
import com.lxzl.erp.dataaccess.dao.mysql.coupon.CouponMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderMapper;
import com.lxzl.erp.dataaccess.domain.coupon.CouponBatchDO;
import com.lxzl.erp.dataaccess.domain.coupon.CouponBatchDetailDO;
import com.lxzl.erp.dataaccess.domain.coupon.CouponDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDetailDO;
import com.lxzl.se.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\4\10 0010 14:14
 */
@Component
public class CouponSupport {
    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private CouponBatchMapper couponBatchMapper;
    @Autowired
    private CouponBatchDetailMapper couponBatchDetailMapper;
    @Autowired
    private UserSupport userSupport;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private StatementOrderDetailMapper statementOrderDetailMapper;
    @Autowired
    private StatementOrderMapper statementOrderMapper;
    @Autowired
    private OrderMapper orderMapper;
    /**
     * 使用设备优惠券
     * @param order
     * @return
     */
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String useCoupon(Order order){
        Date date = new Date();
        if (order == null) {
            return ErrorCode.PARAM_IS_ERROR;
        }
        List<Coupon> couponList = order.getCouponList();
        if (CollectionUtil.isEmpty(couponList)) {
            return ErrorCode.SUCCESS;
        }
        List<OrderProduct> orderProductList = order.getOrderProductList();
        //订单中设备总数量
        Integer totalProductCount = 0;
        for (int i = 0; i < orderProductList.size(); i++) {
            totalProductCount+=orderProductList.get(i).getProductCount();
        }
        //所选优惠券数量大于设备总数量返回错误信息
        if (couponList.size()>totalProductCount) {
            return ErrorCode.COUPON_AMOUNT_TOO_MANY;
        }
        //按照订单商品项中的商品单价对集合进行由大到小的排序
        Collections.sort(orderProductList, new Comparator(){
            @Override
            public int compare(Object o1, Object o2) {
                OrderProduct orderProduct1 = (OrderProduct)o1;
                OrderProduct orderProduct2 = (OrderProduct)o2;
                if(orderProduct1.getProductUnitAmount().intValue()<orderProduct2.getProductUnitAmount().intValue()){
                    return 1;
                }else if(orderProduct1.getProductUnitAmount().intValue()==orderProduct2.getProductUnitAmount().intValue()){
                    return 0;
                }else{
                    return -1;
                }
            }
        });
        List<Integer> couponIdList = new ArrayList<>();
        for (Coupon coupon:couponList) {
            couponIdList.add(coupon.getCouponId());
        }
        List<CouponDO> couponDOList = couponMapper.findCouponDOList(couponIdList);
        //按照优惠卷集合中优惠券的优惠金额进行由大到小的排序
        Collections.sort(couponDOList, new Comparator(){
            @Override
            public int compare(Object o1, Object o2) {
                CouponDO couponDO1 = (CouponDO)o1;
                CouponDO couponDO2 = (CouponDO)o2;
                if(couponDO1.getFaceValue().intValue()<couponDO2.getFaceValue().intValue()){
                    return 1;
                }else if(couponDO1.getFaceValue().intValue()==couponDO2.getFaceValue().intValue()){
                    return 0;
                }else{
                    return -1;
                }
            }
        });
        Map<Integer,CouponBatchDetailDO> couponBatchDetailDOMap = new HashMap<>();
        Map<Integer,CouponBatchDO> couponBatchDOMap = new HashMap<>();
        int couponCount = 0;
        outterLoop:for (int i = 0; i < orderProductList.size(); i++) {
            for (int j = 0; j < orderProductList.get(i).getProductCount(); j++) {
                //更改被锁定的优惠券状态
                couponDOList.get(couponCount).setOrderId(orderProductList.get(i).getOrderId());
                couponDOList.get(couponCount).setOrderNo(order.getOrderNo());
                couponDOList.get(couponCount).setOrderProductId(orderProductList.get(i).getOrderProductId());
                BigDecimal deductionAmount ;
                if (couponDOList.get(couponCount).getFaceValue().compareTo(orderProductList.get(i).getProductUnitAmount())==1) {
                    deductionAmount=orderProductList.get(i).getProductUnitAmount();
                } else{
                    deductionAmount=couponDOList.get(couponCount).getFaceValue();
                }
                couponDOList.get(couponCount).setDeductionAmount(deductionAmount);
                couponDOList.get(couponCount).setCouponStatus(CouponStatus.COUPON_STATUS_LOCK);
                couponDOList.get(couponCount).setUpdateTime(date);
                couponDOList.get(couponCount).setUpdateUser(userSupport.getCurrentUserId().toString());
                //记录增券状态
                if (couponBatchDetailDOMap.containsKey(couponDOList.get(couponCount).getCouponBatchDetailId())) {
                    CouponBatchDetailDO couponBatchDetailDO=couponBatchDetailDOMap.get(couponDOList.get(couponCount).getCouponBatchDetailId());
                    couponBatchDetailDO.setCouponLockCount(couponBatchDetailDO.getCouponLockCount()+1);
//                    couponBatchDetailDO.setTotalUsedAmount(BigDecimalUtil.add(couponBatchDetailDO.getTotalUsedAmount(),couponDOList.get(couponCount).getFaceValue()));
//                    couponBatchDetailDO.setTotalDeductionAmount(BigDecimalUtil.add(couponBatchDetailDO.getTotalDeductionAmount(),couponDOList.get(couponCount).getDeductionAmount()));
                    couponBatchDetailDO.setUpdateTime(date);
                    couponBatchDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    couponBatchDetailDOMap.put(couponDOList.get(couponCount).getCouponBatchDetailId(),couponBatchDetailDO);
                }else{
                    CouponBatchDetailDO couponBatchDetailDO = couponBatchDetailMapper.findById(couponDOList.get(couponCount).getCouponBatchDetailId());
                    couponBatchDetailDO.setCouponLockCount(couponBatchDetailDO.getCouponLockCount()+1);
//                    couponBatchDetailDO.setTotalUsedAmount(BigDecimalUtil.add(couponBatchDetailDO.getTotalUsedAmount(),couponDOList.get(couponCount).getFaceValue()));
//                    couponBatchDetailDO.setTotalDeductionAmount(BigDecimalUtil.add(couponBatchDetailDO.getTotalDeductionAmount(),couponDOList.get(couponCount).getDeductionAmount()));
                    couponBatchDetailDO.setUpdateTime(date);
                    couponBatchDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    couponBatchDetailDOMap.put(couponDOList.get(couponCount).getCouponBatchDetailId(),couponBatchDetailDO);
                }
                //记录批次状态
                if (couponBatchDOMap.containsKey(couponDOList.get(couponCount).getCouponBatchId())) {
                    CouponBatchDO couponBatchDO = couponBatchDOMap.get(couponDOList.get(couponCount).getCouponBatchId());
                    couponBatchDO.setCouponBatchLockCount(couponBatchDO.getCouponBatchLockCount()+1);
//                    couponBatchDO.setTotalUsedAmount(BigDecimalUtil.add(couponBatchDO.getTotalUsedAmount(),couponDOList.get(couponCount).getFaceValue()));
//                    couponBatchDO.setTotalDeductionAmount(BigDecimalUtil.add(couponBatchDO.getTotalDeductionAmount(),couponDOList.get(couponCount).getDeductionAmount()));
                    couponBatchDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    couponBatchDO.setUpdateTime(date);
                    couponBatchDOMap.put(couponDOList.get(couponCount).getCouponBatchId(),couponBatchDO);
                }else{
                    CouponBatchDO couponBatchDO = couponBatchMapper.findById(couponDOList.get(couponCount).getCouponBatchId());
                    couponBatchDO.setCouponBatchLockCount(couponBatchDO.getCouponBatchLockCount()+1);
//                    couponBatchDO.setTotalUsedAmount(BigDecimalUtil.add(couponBatchDO.getTotalUsedAmount(),couponDOList.get(couponCount).getFaceValue()));
//                    couponBatchDO.setTotalDeductionAmount(BigDecimalUtil.add(couponBatchDO.getTotalDeductionAmount(),couponDOList.get(couponCount).getDeductionAmount()));
                    couponBatchDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    couponBatchDO.setUpdateTime(date);
                    couponBatchDOMap.put(couponDOList.get(couponCount).getCouponBatchId(),couponBatchDO);
                }
                couponCount++;
                if (couponCount>=couponDOList.size()) {
                    break outterLoop;
                }
            }
        }
        couponMapper.updateLockList(couponDOList);
        // 获取需要更改的优惠券批次详情对象的集合
        List<CouponBatchDetailDO> couponBatchDetailDOList = new ArrayList<>();
        Iterator it = couponBatchDetailDOMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            CouponBatchDetailDO couponBatchDetailDO = (CouponBatchDetailDO) entry.getValue();
            couponBatchDetailDOList.add(couponBatchDetailDO);
        }
        couponBatchDetailMapper.updateLockList(couponBatchDetailDOList);
        List<CouponBatchDO> couponBatchDOList = new ArrayList<>();
        Iterator iq = couponBatchDOMap.entrySet().iterator();
        while (iq.hasNext()) {
            Map.Entry entry = (Map.Entry) iq.next();
            CouponBatchDO couponBatchDO = (CouponBatchDO) entry.getValue();
            couponBatchDOList.add(couponBatchDO);
        }
        couponBatchMapper.updateLockList(couponBatchDOList);
        return ErrorCode.SUCCESS;
    }
    /**
     * 结算单计算优惠券
     * @param statementOrderDetailDO
     * @return
     */
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, BigDecimal> setDeductionAmount(StatementOrderDetailDO statementOrderDetailDO){
        ServiceResult<String, BigDecimal> serviceResult = new ServiceResult<>();
        Date date = new Date();
        //先对结算单明细对象进行非空判断
        if (statementOrderDetailDO == null){
            serviceResult.setErrorCode(ErrorCode.STATEMENT_ORDER_DETAIL_NOT_EXISTS);
            return serviceResult;
        }
        // 不是订单的给出提示 ：该类型单子无法使用优惠券
        if (statementOrderDetailDO.getOrderType()!=1) {
            serviceResult.setErrorCode(ErrorCode.COUPON_NOT_USED_THIS_ORDER_TYPE);//该类型结算单无法使用优惠券
            return serviceResult;
        }
        //通过客户ID查出客户编号
        CustomerDO customerDO = customerMapper.findById(statementOrderDetailDO.getCustomerId());
        if (customerDO==null) {
             serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
             return serviceResult;
        }
        //通过客户编号，订单id,订单商品项id，查出优惠券集合(在CouponDO中添加couponType，通过优惠券批次ID进行关联查询)
        List<CouponDO> couponDOList = couponMapper.findUsedCouponDoList(customerDO.getCustomerNo(),statementOrderDetailDO.getOrderId(),statementOrderDetailDO.getOrderItemReferId());
        if (CollectionUtil.isEmpty(couponDOList)) {
            serviceResult.setErrorCode(ErrorCode.COUPON_NOT_USED_THIS_STATEMENT);//这个结算单未使用优惠券
            return serviceResult;
        }
        //判断优惠券类型和结算单期数，将所有优惠券抵扣金额求和
        Map<Integer,BigDecimal> couponTypeAndDeductionAmountMap = new HashMap<>();
        Map<Integer,CouponBatchDetailDO> couponBatchDetailDOMap = new HashMap<>();
        Map<Integer,CouponBatchDO> couponBatchDOMap = new HashMap<>();
        for (CouponDO couponDO:couponDOList) {
            if (couponTypeAndDeductionAmountMap.containsKey(couponDO.getCouponType())) {
                couponTypeAndDeductionAmountMap.put(couponDO.getCouponType(), BigDecimalUtil.add(couponTypeAndDeductionAmountMap.get(couponDO.getCouponType()), couponDO.getDeductionAmount()));
            } else {
                couponTypeAndDeductionAmountMap.put(couponDO.getCouponType(),couponDO.getDeductionAmount());
            }
            //给设备优惠券添加结算单id
            if (statementOrderDetailDO.getOrderItemType()==1 && couponDO.getCouponType()==1) {
                couponDO.setStatementOrderId(statementOrderDetailDO.getStatementOrderId());
                couponDO.setStatementOrderDetailId(statementOrderDetailDO.getOrderItemReferId());
                couponDO.setUpdateTime(date);
                couponDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                couponDO.setCouponStatus(CouponStatus.COUPON_STATUS_USED);
                couponDO.setUseTime(date);
                couponDO.setStatementOrderNo(statementOrderMapper.findById(statementOrderDetailDO.getStatementOrderId()).getStatementOrderNo());
                couponMapper.update(couponDO);
            }
            //记录增券状态
            if (couponBatchDetailDOMap.containsKey(couponDO.getCouponBatchDetailId())) {
                CouponBatchDetailDO couponBatchDetailDO=couponBatchDetailDOMap.get(couponDO.getCouponBatchDetailId());
                couponBatchDetailDO.setCouponUsedCount(couponBatchDetailDO.getCouponUsedCount()+1);
                couponBatchDetailDO.setTotalUsedAmount(BigDecimalUtil.add(couponBatchDetailDO.getTotalUsedAmount(),couponDO.getFaceValue()));
                couponBatchDetailDO.setTotalDeductionAmount(BigDecimalUtil.add(couponBatchDetailDO.getTotalDeductionAmount(),couponDO.getDeductionAmount()));
                couponBatchDetailDO.setUpdateTime(date);
                couponBatchDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                couponBatchDetailDO.setCouponLockCount(couponBatchDetailDO.getCouponLockCount()-1);
                couponBatchDetailDOMap.put(couponDO.getCouponBatchDetailId(),couponBatchDetailDO);
            }else{
                CouponBatchDetailDO couponBatchDetailDO = couponBatchDetailMapper.findById(couponDO.getCouponBatchDetailId());
                couponBatchDetailDO.setCouponLockCount(couponBatchDetailDO.getCouponLockCount()-1);
                couponBatchDetailDO.setCouponUsedCount(couponBatchDetailDO.getCouponUsedCount()+1);
                couponBatchDetailDO.setTotalUsedAmount(BigDecimalUtil.add(couponBatchDetailDO.getTotalUsedAmount(),couponDO.getFaceValue()));
                couponBatchDetailDO.setTotalDeductionAmount(BigDecimalUtil.add(couponBatchDetailDO.getTotalDeductionAmount(),couponDO.getDeductionAmount()));
                couponBatchDetailDO.setUpdateTime(date);
                couponBatchDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                couponBatchDetailDOMap.put(couponDO.getCouponBatchDetailId(),couponBatchDetailDO);
            }
            //记录批次状态
            if (couponBatchDOMap.containsKey(couponDO.getCouponBatchId())) {
                CouponBatchDO couponBatchDO = couponBatchDOMap.get(couponDO.getCouponBatchId());
                couponBatchDO.setCouponBatchLockCount(couponBatchDO.getCouponBatchLockCount()-1);
                couponBatchDO.setCouponBatchUsedCount(couponBatchDO.getCouponBatchUsedCount()+1);
                couponBatchDO.setTotalUsedAmount(BigDecimalUtil.add(couponBatchDO.getTotalUsedAmount(),couponDO.getFaceValue()));
                couponBatchDO.setTotalDeductionAmount(BigDecimalUtil.add(couponBatchDO.getTotalDeductionAmount(),couponDO.getDeductionAmount()));
                couponBatchDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                couponBatchDO.setUpdateTime(date);
                couponBatchDOMap.put(couponDO.getCouponBatchId(),couponBatchDO);
            }else{
                CouponBatchDO couponBatchDO = couponBatchMapper.findById(couponDO.getCouponBatchId());
                couponBatchDO.setCouponBatchLockCount(couponBatchDO.getCouponBatchLockCount()-1);
                couponBatchDO.setCouponBatchUsedCount(couponBatchDO.getCouponBatchUsedCount()+1);
                couponBatchDO.setTotalUsedAmount(BigDecimalUtil.add(couponBatchDO.getTotalUsedAmount(),couponDO.getFaceValue()));
                couponBatchDO.setTotalDeductionAmount(BigDecimalUtil.add(couponBatchDO.getTotalDeductionAmount(),couponDO.getDeductionAmount()));
                couponBatchDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                couponBatchDO.setUpdateTime(date);
                couponBatchDOMap.put(couponDO.getCouponBatchId(),couponBatchDO);
            }
        }
        // 获取需要更改的优惠券批次详情对象的集合
        List<CouponBatchDetailDO> couponBatchDetailDOList = new ArrayList<>();
        Iterator it = couponBatchDetailDOMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            CouponBatchDetailDO couponBatchDetailDO = (CouponBatchDetailDO) entry.getValue();
            couponBatchDetailDOList.add(couponBatchDetailDO);
        }
        couponBatchDetailMapper.updateUseList(couponBatchDetailDOList);
        List<CouponBatchDO> couponBatchDOList = new ArrayList<>();
        Iterator iq = couponBatchDOMap.entrySet().iterator();
        while (iq.hasNext()) {
            Map.Entry entry = (Map.Entry) iq.next();
            CouponBatchDO couponBatchDO = (CouponBatchDO) entry.getValue();
            couponBatchDOList.add(couponBatchDO);
        }
        couponBatchMapper.updateUseList(couponBatchDOList);
        //设备类型计算抵扣金额
        //增加结算单类型判断，设备的跟map集合中key为1的进行匹配
        //商品使用商品结算单
        if (statementOrderDetailDO.getOrderItemType()==1) {//订单项类型为商品
            if (couponTypeAndDeductionAmountMap.containsKey(1)) {//该用户下有使用设备优惠券
                //如果结算单租金金额大于使用的优惠券的总抵扣金额，则存入总抵扣金额
                if (statementOrderDetailDO.getStatementDetailRentAmount().compareTo(couponTypeAndDeductionAmountMap.get(1)) == 1) {
                    serviceResult.setErrorCode(ErrorCode.SUCCESS);
                    serviceResult.setResult(couponTypeAndDeductionAmountMap.get(1));
                    return serviceResult;
                } else {//如果结算单租金金额小于或等于使用的优惠券的总抵扣金额，则抵扣金额为结算单租金金额
                    serviceResult.setErrorCode(ErrorCode.SUCCESS);
                    serviceResult.setResult(couponTypeAndDeductionAmountMap.get(1));
                    return serviceResult;
                }

            }
        }
        serviceResult.setErrorCode(ErrorCode.BUSINESS_EXCEPTION);
        return serviceResult;
    }
    /**
     * 还原优惠券
     * @param orderNo
     * @return
     */
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String revertCoupon(String orderNo) {
        Date date = new Date();
        if (StringUtil.isEmpty(orderNo)) {
            return ErrorCode.ORDER_NO_NOT_NULL;
        }
        List<CouponDO> couponDOList = couponMapper.findByOrderNo(orderNo);
        if (CollectionUtil.isEmpty(couponDOList)) {
            return ErrorCode.SUCCESS;
        }
        for (CouponDO couponDO:couponDOList) {
            couponDO.setDeductionAmount(BigDecimal.ZERO);
            couponDO.setCouponStatus(CouponStatus.COUPON_STATUS_USABLE);
            couponDO.setUpdateTime(date);
            couponDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            couponDO.setOrderId(null);
            couponDO.setOrderNo(null);
            couponDO.setOrderProductId(null);
            couponDO.setStatementOrderId(null);
            couponDO.setStatementOrderNo(null);
            couponDO.setStatementOrderDetailId(null);
            couponDO.setUseTime(null);
        }
        couponMapper.updateRevertList(couponDOList);
        return ErrorCode.SUCCESS;
    }
}
