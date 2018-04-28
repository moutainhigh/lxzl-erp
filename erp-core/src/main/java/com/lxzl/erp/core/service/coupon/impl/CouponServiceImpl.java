package com.lxzl.erp.core.service.coupon.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.CouponStatus;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.coupon.*;
import com.lxzl.erp.common.domain.coupon.pojo.Coupon;
import com.lxzl.erp.common.domain.coupon.pojo.CouponBatch;
import com.lxzl.erp.common.domain.coupon.pojo.CouponBatchDetail;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrderDetail;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.coupon.CouponService;
import com.lxzl.erp.core.service.coupon.impl.support.CouponSupport;
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
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.apache.velocity.runtime.directive.Foreach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * User : sunzhipeng
 * Date : Created in ${Date}
 * Time : Created in ${Time}
 */
@Service
public class CouponServiceImpl implements CouponService{
    private static final Logger logger = LoggerFactory.getLogger(ConverterUtil.class);
    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private CouponBatchDetailMapper couponBatchDetailMapper;
    @Autowired
    private CouponBatchMapper couponBatchMapper;
    @Autowired
    private UserSupport userSupport;
    @Autowired
    private GenerateNoSupport generateNoSupport;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private StatementOrderDetailMapper statementOrderDetailMapper;
    @Autowired
    private StatementOrderMapper statementOrderMapper;
    @Autowired
    private OrderMapper orderMapper;


    /**
     * 增加优惠卷批次
     * @param couponBatch
     * @return
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> addCouponBatch(CouponBatch couponBatch) {
        ServiceResult<String,String> serviceResult = new ServiceResult<>();
        Date date = new Date();
        //PO类转成DO类，自动将PO类中的参数进行封装
        CouponBatchDO couponBatchDo = ConverterUtil.convert(couponBatch, CouponBatchDO.class);
        couponBatchDo.setCouponBatchTotalCount(CommonConstant.COMMON_ZERO);
        couponBatchDo.setCouponBatchUsedCount(CommonConstant.COMMON_ZERO);
        couponBatchDo.setTotalFaceAmount(BigDecimal.ZERO);
        couponBatchDo.setTotalUsedAmount(BigDecimal.ZERO);
        couponBatchDo.setTotalDeductionAmount(BigDecimal.ZERO);
        couponBatchDo.setCreateTime(date);
        couponBatchDo.setUpdateTime(date);
        couponBatchDo.setCreateUser(userSupport.getCurrentUserId().toString());
        couponBatchDo.setUpdateUser(userSupport.getCurrentUserId().toString());
        couponBatchDo.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        couponBatchDo.setCouponBatchLockCount(CommonConstant.COMMON_ZERO);
        couponBatchDo.setCouponBatchCancelCount(CommonConstant.COMMON_ZERO);
        couponBatchMapper.save(couponBatchDo);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    /**
     * 修改优惠卷批次
     * @param couponBatch
     * @return
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> updateCouponBatch(CouponBatch couponBatch) {
        ServiceResult<String,String> serviceResult = new ServiceResult<>();
        Date date = new Date();
        CouponBatchDO couponBatchDO = couponBatchMapper.findById(couponBatch.getCouponBatchId());
        if (couponBatchDO == null ) {
            serviceResult.setErrorCode(ErrorCode.COUPON_BATCH_NOT_EXISTS);
            return serviceResult;
        }
        //优惠券总数为0，允许修改批次名称，优惠券类型等字段
        if (couponBatchDO.getCouponBatchTotalCount() == 0) {
            couponBatchDO.setEffectiveStartTime(couponBatch.getEffectiveStartTime());
            couponBatchDO.setEffectiveEndTime(couponBatch.getEffectiveEndTime());
            couponBatchDO.setCouponBatchName(couponBatch.getCouponBatchName());
            couponBatchDO.setCouponType(couponBatch.getCouponType());
        }

        couponBatchDO.setCouponBatchDescribe(couponBatch.getCouponBatchDescribe());
        couponBatchDO.setRemark(couponBatch.getRemark());
        couponBatchDO.setUpdateTime(date);
        couponBatchDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        couponBatchMapper.update(couponBatchDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    /**
     * 删除优惠卷批次
     * @param couponBatch
     * @return
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> deleteCouponBatch(CouponBatch couponBatch) {
        ServiceResult<String,String> serviceResult = new ServiceResult<>();
        Date date = new Date();
        CouponBatchDO couponBatchDO = couponBatchMapper.findById(couponBatch.getCouponBatchId());
        if (couponBatchDO == null) {
            serviceResult.setErrorCode(ErrorCode.COUPON_BATCH_NOT_EXISTS);
            return serviceResult;
        }
        //如果优惠券总数不为0就不能进行删除优惠卷批次操作
        if (couponBatchDO.getCouponBatchTotalCount() != 0) {
            serviceResult.setErrorCode(ErrorCode.COUPON_BATCH_CAN_NOT_DELETE);
            return serviceResult;
        }

        couponBatchDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        couponBatchDO.setUpdateTime(date);
        couponBatchDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        couponBatchMapper.update(couponBatchDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;


    }
    /**
     * 优惠卷批次列表
     * @param couponBatchQueryParam
     * @return
     */
    @Override
    public ServiceResult<String, Page<CouponBatch>> pageCouponBatch(CouponBatchQueryParam couponBatchQueryParam) {
        ServiceResult<String, Page<CouponBatch>> serviceResult = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(couponBatchQueryParam.getPageNo(), couponBatchQueryParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("couponBatchQueryParam", couponBatchQueryParam);
//        maps.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_USER));
        Integer totalCount = couponBatchMapper.findCouponBatchCountByParams(maps);
        List<CouponBatchDO> couponBatchDOList = couponBatchMapper.findCouponBatchByParams(maps);
        List<CouponBatch> CouponBatchList = ConverterUtil.convertList(couponBatchDOList, CouponBatch.class);
        Page<CouponBatch> page = new Page<>(CouponBatchList, totalCount, couponBatchQueryParam.getPageNo(), couponBatchQueryParam.getPageSize());
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(page);
        return serviceResult;

    }
    /**
     * 批次增卷
     * @param couponBatchDetail
     * @return
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> addCouponBatchDetail(CouponBatchDetail couponBatchDetail) {
        ServiceResult<String,String> serviceResult = new ServiceResult<>();
        Date date = new Date();
        CouponBatchDetailDO couponBatchDetailDO = ConverterUtil.convert(couponBatchDetail, CouponBatchDetailDO.class);
        CouponBatchDO couponBatchDO = couponBatchMapper.findByIdIgnoreDataStatus(couponBatchDetailDO.getCouponBatchId());
        if (couponBatchDO.getDataStatus()!=1) {
            serviceResult.setErrorCode(ErrorCode.COUPON_BATCH_NOT_EXISTS);
            return serviceResult;
        }

        //获取优惠卷总数（大于0张的判断在PO类中已完成校验）
        Integer couponTotalCount = couponBatchDetailDO.getCouponTotalCount();

        //判断优惠面值金额不能小于或者等于零
        BigDecimal faceValue = couponBatchDetailDO.getFaceValue();
        if(BigDecimalUtil.compare(faceValue,BigDecimal.ZERO)<=0){
            serviceResult.setErrorCode(ErrorCode.COUPON_FACE_AMOUNT_ERROR);
            return serviceResult;
        }

        couponBatchDetailDO.setCouponUsedCount(CommonConstant.COMMON_ZERO);
        couponBatchDetailDO.setCouponReceivedCount(CommonConstant.COMMON_ZERO);

        //设置优惠券总面值（乘）
        couponBatchDetailDO.setTotalFaceAmount(BigDecimalUtil.mul(new BigDecimal(couponTotalCount),faceValue));
        couponBatchDetailDO.setEffectiveStartTime(couponBatchDO.getEffectiveStartTime());
        couponBatchDetailDO.setEffectiveEndTime(couponBatchDO.getEffectiveEndTime());
        couponBatchDetailDO.setTotalUsedAmount(BigDecimal.ZERO);
        couponBatchDetailDO.setTotalDeductionAmount(BigDecimal.ZERO);
        couponBatchDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        couponBatchDetailDO.setCouponCanReceivedCount(couponTotalCount);
        couponBatchDetailDO.setCreateTime(date);
        couponBatchDetailDO.setCreateUser(userSupport.getCurrentUserId().toString());
        couponBatchDetailDO.setUpdateTime(date);
        couponBatchDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        couponBatchDetailDO.setCouponLockCount(CommonConstant.COMMON_ZERO);
        couponBatchDetailMapper.save(couponBatchDetailDO);

        //更新优惠卷批次数据
        couponBatchDO.setTotalFaceAmount(BigDecimalUtil.add(couponBatchDO.getTotalFaceAmount(), couponBatchDetailDO.getTotalFaceAmount()));
        couponBatchDO.setCouponBatchTotalCount(couponBatchDO.getCouponBatchTotalCount()+couponBatchDetailDO.getCouponTotalCount());
        couponBatchDO.setUpdateUser(couponBatchDetailDO.getUpdateUser());
        couponBatchDO.setUpdateTime(couponBatchDetailDO.getUpdateTime());
        couponBatchMapper.update(couponBatchDO);
        //增加优惠卷方法
        addCoupon(couponBatchDetailDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }
    /**
     * 增券列表
     * @param couponBatchDetailQueryParam
     * @return
     */
    @Override
    public ServiceResult<String, Page<CouponBatchDetail>> pageCouponBatchDetail(CouponBatchDetailQueryParam couponBatchDetailQueryParam) {
        ServiceResult<String, Page<CouponBatchDetail>> serviceResult = new ServiceResult<>();

        PageQuery pageQuery = new PageQuery(couponBatchDetailQueryParam.getPageNo(), couponBatchDetailQueryParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("couponBatchDetailQueryParam", couponBatchDetailQueryParam);
//        maps.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_USER));
        Integer totalCount = couponBatchDetailMapper.findCouponBatchDetailCountByParams(maps);
        List<CouponBatchDetailDO> couponBatchDetailDOList = couponBatchDetailMapper.findCouponBatchDetailByParams(maps);
        List<CouponBatchDetail> couponBatchDetailList = ConverterUtil.convertList(couponBatchDetailDOList, CouponBatchDetail.class);
        Page<CouponBatchDetail> page = new Page<>(couponBatchDetailList, totalCount, couponBatchDetailQueryParam.getPageNo(), couponBatchDetailQueryParam.getPageSize());
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(page);
        return serviceResult;
    }
    /**
     * 优惠券分页查询
     * @param couponQueryParam
     * @return
     */
    @Override
    public ServiceResult<String, Page<Coupon>> pageCoupon(CouponQueryParam couponQueryParam) {
        ServiceResult<String, Page<Coupon>> serviceResult = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(couponQueryParam.getPageNo(), couponQueryParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("couponQueryParam", couponQueryParam);
//        maps.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_USER));
        Integer totalCount = couponMapper.findCouponCountByParams(maps);
        List<CouponDO> couponDOList = couponMapper.findCouponByParams(maps);
        List<Coupon> couponList = ConverterUtil.convertList(couponDOList, Coupon.class);
        Page<Coupon> page = new Page<>(couponList, totalCount, couponQueryParam.getPageNo(), couponQueryParam.getPageSize());
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(page);
        return serviceResult;
    }

    /**
     * 批量作废优惠券
     * @param list
     * @return
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> deleteCoupon(List<Coupon> list) {
        ServiceResult<String,String> serviceResult = new ServiceResult<>();
        Date date = new Date();
        // 根据优惠券ID查询出所有优惠券
        List<Integer> couponDOIdList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            couponDOIdList.add(list.get(i).getCouponId());
        }
        List<CouponDO> couponDOList = couponMapper.findCouponDOList(couponDOIdList);
        if (CollectionUtil.isEmpty(couponDOList)) {
            serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return  serviceResult;
        }
        Map<Integer,List<Integer>> couponBatchDetailMap = new HashMap();

        // 循环判断是否有已经使用的，如果有已经使用的则返回错误信息
        for (int i = 0; i < couponDOList.size(); i++) {
            List<Integer> cancelAndReceivedList = new ArrayList<>();
            if (couponDOList.get(i).getCouponStatus() == CouponStatus.COUPON_STATUS_USED && couponDOList.get(i).getCouponStatus() == CouponStatus.COUPON_STATUS_LOCK) {
                serviceResult.setErrorCode(ErrorCode.COUPON_USED);
                return  serviceResult;
            }
            if (couponDOList.get(i).getCouponStatus() == CouponStatus.COUPON_STATUS_CANCEL) {
                serviceResult.setErrorCode(ErrorCode.COUPON_CANCEL);
                return  serviceResult;
            }
            //  根据每个优惠卷所属批次的详细ID进行存储并计数
            if (couponBatchDetailMap.containsKey(couponDOList.get(i).getCouponBatchDetailId())) {
                if (couponDOList.get(i).getCouponStatus() == 0) {
                    cancelAndReceivedList.add(couponBatchDetailMap.get(couponDOList.get(i).getCouponBatchDetailId()).get(0) + 1);
                    cancelAndReceivedList.add(couponBatchDetailMap.get(couponDOList.get(i).getCouponBatchDetailId()).get(1) + 1);
                    couponBatchDetailMap.put(couponDOList.get(i).getCouponBatchDetailId(), cancelAndReceivedList);
                } else {
                    cancelAndReceivedList.add(couponBatchDetailMap.get(couponDOList.get(i).getCouponBatchDetailId()).get(0) + 1);
                    cancelAndReceivedList.add(couponBatchDetailMap.get(couponDOList.get(i).getCouponBatchDetailId()).get(1) + 0);
                    couponBatchDetailMap.put(couponDOList.get(i).getCouponBatchDetailId(), cancelAndReceivedList);
                }
            } else {
                if (couponDOList.get(i).getCouponStatus() == 0) {
                    cancelAndReceivedList.add(1);
                    cancelAndReceivedList.add(1);
                    couponBatchDetailMap.put(couponDOList.get(i).getCouponBatchDetailId(), cancelAndReceivedList);
                } else {
                    cancelAndReceivedList.add(1);
                    cancelAndReceivedList.add(0);
                    couponBatchDetailMap.put(couponDOList.get(i).getCouponBatchDetailId(), cancelAndReceivedList);
                }
            }
            couponDOList.get(i).setCouponStatus(CouponStatus.COUPON_STATUS_CANCEL);
            couponDOList.get(i).setUpdateTime(date);
            couponDOList.get(i).setUpdateUser(userSupport.getCurrentUserId().toString());
        }
        // 删除优惠券
        couponMapper.deleteCouponList(couponDOList);
        // 获取需要更改的优惠券批次详情对象的集合
        List<CouponBatchDetailDO> couponBatchDetailDOList = new ArrayList<>();
        Map<Integer,Integer> couponBatchMap = new HashMap<>();
        Iterator it = couponBatchDetailMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Integer key = (Integer) entry.getKey();
            List<Integer> cancelAndReceivedList = (List) entry.getValue();
            CouponBatchDetailDO couponBatchDetailDO = couponBatchDetailMapper.findById(key);
            couponBatchDetailDO.setCouponCancelCount(cancelAndReceivedList.get(0)+couponBatchDetailDO.getCouponCancelCount());
            couponBatchDetailDO.setUpdateTime(date);
            couponBatchDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            couponBatchDetailDO.setCouponCanReceivedCount(couponBatchDetailDO.getCouponCanReceivedCount()-cancelAndReceivedList.get(1));
            couponBatchDetailDOList.add(couponBatchDetailDO);

            if (couponBatchMap.containsKey(couponBatchDetailDO.getCouponBatchId())) {
                couponBatchMap.put(couponBatchDetailDO.getCouponBatchId(),couponBatchMap.get(couponBatchDetailDO.getCouponBatchId())+cancelAndReceivedList.get(0));
            } else {
                couponBatchMap.put(couponBatchDetailDO.getCouponBatchId(),cancelAndReceivedList.get(0));
            }
        }

        // TODO: 2018\4\16 0016 添加 优惠券可领取总数逻辑
        couponBatchDetailMapper.updateCouponBatchDetailDOList(couponBatchDetailDOList);
        for (Integer key : couponBatchMap.keySet()) {
            CouponBatchDO couponBatchDO = couponBatchMapper.findById(key);
            couponBatchDO.setCouponBatchCancelCount(couponBatchMap.get(key)+couponBatchDO.getCouponBatchCancelCount());
            couponBatchDO.setUpdateTime(date);
            couponBatchDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            couponBatchMapper.update(couponBatchDO);
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }
    /**
     * 发放优惠卷
     * @param couponProvideParam
     * @return
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> provideCoupon(CouponProvideParam couponProvideParam) {
        ServiceResult<String,String> serviceResult = new ServiceResult<>();
        Date date = new Date();
        //  按照优惠卷批次详情ID查询可以发放优惠券的数量，与计算出传递过来需要发放总优惠卷数量进行比较，如果超过可发放优惠卷数量，给出错误提示
        Integer couponStatusCountIsZero = couponMapper.findCouponStatusCountIsZeroByCouponBatchDetailId(couponProvideParam.getCouponBatchDetailId());
        Integer totalCouponProvideAmount = 0;
        for (CustomerProvide customerProvide : couponProvideParam.getCustomerProvideList()) {
            totalCouponProvideAmount+=customerProvide.getProvideCount();
        }

        if (couponStatusCountIsZero < totalCouponProvideAmount) {
            serviceResult.setErrorCode(ErrorCode.COUPON_PROVIDE_COUNT_ERROR);
            return serviceResult;
        }

        for (CustomerProvide customerProvide: couponProvideParam.getCustomerProvideList()) {
            CustomerDO customerDO = customerMapper.findByNo(customerProvide.getCustomerNo());
            if (customerDO == null) {
                serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
                return serviceResult;
            }
            //  查询指定数量的出可以发放的优惠卷集合(这里要按照优惠券批次ID进行查询)
            List<CouponDO> couponDOList = couponMapper.findByCouponStatus(couponProvideParam.getCouponBatchDetailId(),customerProvide.getProvideCount());
            for (int i = 0; i < couponDOList.size(); i++) {
                couponDOList.get(i).setCustomerNo(customerProvide.getCustomerNo());
                couponDOList.get(i).setCouponStatus(CouponStatus.COUPON_STATUS_USABLE);
                couponDOList.get(i).setReceiveTime(date);
                couponDOList.get(i).setUpdateTime(date);
                couponDOList.get(i).setUpdateUser(userSupport.getCurrentUserId().toString());
            }
            couponMapper.updateList(couponDOList);
        }
        CouponBatchDetailDO couponBatchDetailDO = couponBatchDetailMapper.findById(couponProvideParam.getCouponBatchDetailId());
        couponBatchDetailDO.setCouponReceivedCount(totalCouponProvideAmount+couponBatchDetailDO.getCouponReceivedCount());
        couponBatchDetailDO.setCouponCanReceivedCount(couponBatchDetailDO.getCouponCanReceivedCount()-totalCouponProvideAmount);
        couponBatchDetailDO.setUpdateTime(date);
        couponBatchDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        couponBatchDetailMapper.update(couponBatchDetailDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }
    /**
     * 订单添加，编辑页面优惠卷列表展示
     * @param customerOrderCouponParam
     * @return
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, List<Coupon>> findCouponByCustomerNo(CustomerOrderCouponParam customerOrderCouponParam) {
        ServiceResult<String,List<Coupon>> serviceResult = new ServiceResult<>();
        if (customerOrderCouponParam.getCustomer() == null && customerOrderCouponParam.getCustomer().getCustomerNo() == null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NO_NOT_NULL);
            return serviceResult;
        }
        String customerNo = customerOrderCouponParam.getCustomer().getCustomerNo();
        String orderNo = customerOrderCouponParam.getOrder().getOrderNo();
        List<CouponDO> couponDOList = couponMapper.findByCustomerNo(customerNo,orderNo);
        List<Coupon> couponList = ConverterUtil.convertList(couponDOList, Coupon.class);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(couponList);
        return serviceResult;
    }
    /**
     * 根据批次ID获取批次详情
     * @param couponBatch
     * @return
     */
    @Override
    public ServiceResult<String, CouponBatch> findCouponBatchByID(CouponBatch couponBatch) {
        ServiceResult<String,CouponBatch> serviceResult = new ServiceResult<>();
        CouponBatchDO couponBatchDO = couponBatchMapper.findById(couponBatch.getCouponBatchId());
        CouponBatch couponBatch1 = ConverterUtil.convert(couponBatchDO, CouponBatch.class);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(couponBatch1);
        return serviceResult;
    }

    /**
     * 从增券列表进行作废优惠券
     * @param couponBatchDetail
     * @return
     */
    @Override
    public ServiceResult<String, String> cancelCouponByCouponBatchDetail(CouponBatchDetail couponBatchDetail) {
        ServiceResult<String,String> serviceResult = new ServiceResult<>();
        Date date = new Date();
        CouponBatchDetailDO couponBatchDetailDO = couponBatchDetailMapper.findById(couponBatchDetail.getCouponBatchDetailId());
        if (couponBatchDetailDO == null) {
            serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return  serviceResult;
        }
        if (couponBatchDetailDO.getCouponUsedCount()>0) {
            serviceResult.setErrorCode(ErrorCode.COUPON_USED);
            return serviceResult;
        }
        Integer cancelCount = couponBatchDetailDO.getCouponCancelCount();
        couponBatchDetailDO.setUpdateTime(date);
        couponBatchDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        couponBatchDetailDO.setCouponCancelCount(couponBatchDetailDO.getCouponTotalCount());
        couponBatchDetailDO.setCouponCanReceivedCount(CommonConstant.COMMON_ZERO);
        couponBatchDetailMapper.update(couponBatchDetailDO);
        couponMapper.cancelCoupon(couponBatchDetailDO.getId(),date,userSupport.getCurrentUserId().toString());
        CouponBatchDO couponBatchDO = couponBatchMapper.findById(couponBatchDetailDO.getCouponBatchId());
        couponBatchDO.setCouponBatchCancelCount(couponBatchDO.getCouponBatchCancelCount()+couponBatchDetailDO.getCouponCancelCount()-cancelCount);
        couponBatchDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        couponBatchDO.setUpdateTime(date);
        couponBatchMapper.update(couponBatchDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }
    /**
     * 从批次列表进行作废优惠券
     * @param couponBatch
     * @return
     */
    @Override
    public ServiceResult<String, String> cancelCouponByCouponBatch(CouponBatch couponBatch) {
        ServiceResult<String,String> serviceResult = new ServiceResult<>();
        Date date = new Date();
        CouponBatchDO couponBatchDO = couponBatchMapper.findById(couponBatch.getCouponBatchId());
        if (couponBatchDO == null) {
            serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return  serviceResult;
        }
        if (couponBatchDO.getCouponBatchUsedCount()>0) {
            serviceResult.setErrorCode(ErrorCode.COUPON_USED);
            return serviceResult;
        }
        if (couponBatchDO.getCouponBatchTotalCount() == couponBatchDO.getCouponBatchCancelCount()) {
            serviceResult.setErrorCode(ErrorCode.COUPON_CANCEL);
            return serviceResult;
        }
        couponBatchDO.setUpdateTime(date);
        couponBatchDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        couponBatchDO.setCouponBatchCancelCount(couponBatchDO.getCouponBatchTotalCount());
        couponBatchMapper.update(couponBatchDO);
        List<CouponBatchDetailDO> couponBatchDetailDOList = couponBatchDetailMapper.findByCouponBatchID(couponBatchDO.getId());
        for (int i = 0; i < couponBatchDetailDOList.size(); i++) {
            couponBatchDetailDOList.get(i).setUpdateTime(date);
            couponBatchDetailDOList.get(i).setUpdateUser(userSupport.getCurrentUserId().toString());
            couponBatchDetailDOList.get(i).setCouponCancelCount(couponBatchDetailDOList.get(i).getCouponTotalCount());
            couponBatchDetailDOList.get(i).setCouponCanReceivedCount(CommonConstant.COMMON_ZERO);
            couponBatchDetailMapper.update(couponBatchDetailDOList.get(i));
            couponMapper.cancelCoupon(couponBatchDetailDOList.get(i).getId(),date,userSupport.getCurrentUserId().toString());
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Autowired
    private CouponSupport couponSupport;

    /**
     * 使用优惠券的方法
     * @param order
     * @return
     */
    @Override
    public ServiceResult<String, String> useCoupon( Order order) {
        ServiceResult<String,String> serviceResult = new ServiceResult<>();
//        serviceResult.setResult(couponSupport.useCoupon(order));
        System.out.println(order.getOrderNo());
        serviceResult.setResult(couponSupport.revertCoupon(order.getOrderNo()));
        return serviceResult;
    }
    /**
     * 查询客户所有优惠券
     * @param customerCouponQueryParam
     * @return
     */
    @Override
    public ServiceResult<String, Page<Coupon>> pageCouponByCustomerNo(CustomerCouponQueryParam customerCouponQueryParam) {
        ServiceResult<String,Page<Coupon>> serviceResult = new ServiceResult<>();

        PageQuery pageQuery = new PageQuery(customerCouponQueryParam.getPageNo(), customerCouponQueryParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        String customerNo = customerCouponQueryParam.getCustomer().getCustomerNo();
        maps.put("customerNo", customerNo);
//        maps.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_USER));
        Integer totalCount = couponMapper.findCustomerCouponCountByParams(maps);
        List<CouponDO> couponDOList = couponMapper.findCustomerCouponByParams(maps);
        List<Coupon> couponList = ConverterUtil.convertList(couponDOList, Coupon.class);
        Page<Coupon> page = new Page<>(couponList, totalCount, customerCouponQueryParam.getPageNo(), customerCouponQueryParam.getPageSize());
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(page);
        return serviceResult;
    }

    /**
     * 结算单使用结算优惠券
     * @param statementCouponParam
     * @return
     */
    @Override
    public ServiceResult<String, String> useStatementCoupon(StatementCouponParam statementCouponParam) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date date = new Date();
        StatementOrderDetail statementOrderDetail = statementCouponParam.getStatementOrderDetail();
        //判断传进来的结算单是否存在
        if (statementOrderDetail == null) {
            serviceResult.setErrorCode(ErrorCode.STATEMENT_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        //结算单详情ID不能为空
        if (statementOrderDetail.getStatementOrderDetailId() == null) {
            serviceResult.setErrorCode(ErrorCode.STATEMENT_ORDER_DETAIL_ID_NOT_NULL);
            return serviceResult;
        }
        //判断该结算单是否已经使用过结算单优惠券，如果已经使用过，不能重复使用
        CouponDO usedCoupon = couponMapper.findByStatementOrderDetailId(statementOrderDetail.getStatementOrderDetailId());
        if (usedCoupon != null) {
            serviceResult.setErrorCode(ErrorCode.COUPON_USED_THIS_STATEMENT);
            return serviceResult;
        }
        //获取结算单对象和结算单详情对象
        StatementOrderDetailDO statementOrderDetailDO = statementOrderDetailMapper.findById(statementOrderDetail.getStatementOrderDetailId());
        if (statementOrderDetailDO == null) {
            serviceResult.setErrorCode(ErrorCode.STATEMENT_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        StatementOrderDO statementOrderDO = statementOrderMapper.findById(statementOrderDetailDO.getStatementOrderId());
        if (statementOrderDetailDO.getCustomerId() == null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }
        //判断根据结算单的客户ID能否查到客户
        CustomerDO customerDO = customerMapper.findById(statementOrderDetailDO.getCustomerId());
        if (customerDO == null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }
        //根据ID找到对应的优惠券
        Coupon coupon = statementCouponParam.getCoupon();
        if (coupon.getCouponId()==null){
            serviceResult.setErrorCode(ErrorCode.COUPON_ID_NOT_NULL);
            return serviceResult;
        }
        CouponDO couponDO = couponMapper.findById(coupon.getCouponId());
        if (couponDO==null) {
            serviceResult.setErrorCode(ErrorCode.COUPON_NOT_EXISTS);
            return serviceResult;
        }
        //判断优惠券状态是否是可用状态
        if (couponDO.getCouponStatus() != CouponStatus.COUPON_STATUS_USABLE ) {//优惠券状态不是可用状态
            serviceResult.setErrorCode(ErrorCode.COUPON_CUSTOMER_STATUS_NOT_USED);
            return serviceResult;
        }
        //判断优惠券客户编号是否为空
        if (couponDO.getCustomerNo() == null && couponDO.getCustomerNo() == "") {
            serviceResult.setErrorCode(ErrorCode.COUPON_CUSTOMER_NO_IS_NULL_OR_NOT);
            return serviceResult;
        }
        //判断优惠券是否属于该客户
        if (!customerDO.getCustomerNo().equals(couponDO.getCustomerNo())) {
            serviceResult.setErrorCode(ErrorCode.COUPON_CUSTOMER_NO_IS_NULL_OR_NOT);
            return serviceResult;
        }
        //使用优惠券，存入相关字段
        couponDO.setCouponStatus(CouponStatus.COUPON_STATUS_USED);
        couponDO.setUseTime(date);
        couponDO.setUpdateTime(date);
        couponDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        couponDO.setOrderId(statementOrderDetailDO.getOrderId());
        couponDO.setOrderNo(orderMapper.findByOrderId(statementOrderDetailDO.getOrderId()).getOrderNo());
        couponDO.setOrderProductId(statementOrderDetailDO.getOrderItemReferId());
        couponDO.setStatementOrderId(statementOrderDO.getId());
        couponDO.setStatementOrderNo(statementOrderDO.getStatementOrderNo());
        couponDO.setStatementOrderDetailId(statementOrderDetailDO.getId());
        //保存抵扣金额,如果结算单租金金额大于优惠券面值，则抵扣金额为优惠卷面值（结算单优惠金额累加优惠券面值）
        if (statementOrderDetailDO.getStatementDetailRentAmount().compareTo(couponDO.getFaceValue()) == 1) {
            //判断结算单租金金额是否大于结算单抵扣金额加上优惠券面值，如果大于则抵扣金额存为结算单优惠金额累加优惠券面值
            if (statementOrderDetailDO.getStatementDetailRentAmount().compareTo(BigDecimalUtil.add(statementOrderDetailDO.getStatementCouponAmount(), couponDO.getFaceValue())) == 1) {
                couponDO.setDeductionAmount(couponDO.getFaceValue());
                statementOrderDetailDO.setStatementCouponAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementCouponAmount(),couponDO.getFaceValue()));
                statementOrderDO.setStatementCouponAmount(BigDecimalUtil.add(statementOrderDO.getStatementCouponAmount(),couponDO.getFaceValue()));
            } else {//判断结算单租金金额是否大于结算单抵扣金额加上优惠券面值，如果小于或等于则抵扣金额存为结算单租金金额
                //优惠券的抵扣金额就要存为结算单租金金额减去之前的优惠券优惠金额的差值
                couponDO.setDeductionAmount(BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailRentAmount(),statementOrderDetailDO.getStatementCouponAmount()));
                statementOrderDetailDO.setStatementCouponAmount(statementOrderDetailDO.getStatementDetailRentAmount());
                statementOrderDO.setStatementCouponAmount(statementOrderDetailDO.getStatementDetailRentAmount());
            }
        } else { //保存抵扣金额,如果结算单租金金额小于或等于优惠券面值，则抵扣金额为结算单租金金额
            couponDO.setDeductionAmount(statementOrderDetailDO.getStatementDetailRentAmount());
            statementOrderDetailDO.setStatementCouponAmount(statementOrderDetailDO.getStatementDetailRentAmount());
            statementOrderDO.setStatementCouponAmount(statementOrderDetailDO.getStatementDetailRentAmount());
        }
        //保存优惠券
        couponMapper.update(couponDO);
        //设置结算单详情的更新时间和更新人并保存
        statementOrderDetailDO.setUpdateTime(date);
        statementOrderDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        statementOrderDetailMapper.update(statementOrderDetailDO);
        //设置结算单的更新时间和更新人并保存
        statementOrderDO.setUpdateTime(date);
        statementOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        statementOrderMapper.update(statementOrderDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }
    /**
     * 按客户编号查询该客户可用的结算单优惠券
     * @param customer
     * @return
     */
    @Override
    public ServiceResult<String, List<Coupon>> findStatementCouponByCustomerNo(Customer customer) {
        ServiceResult<String,List<Coupon>> serviceResult = new ServiceResult<>();
        List<CouponDO> couponDOList = couponMapper.findStatementCouponByCustomerNo(customer.getCustomerNo());
        List<Coupon> couponList = ConverterUtil.convertList(couponDOList, Coupon.class);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(couponList);
        return serviceResult;
    }

    /**
     * 订单详情中查询该订单使用的优惠券
     * @param order
     * @return
     */
    @Override
    public ServiceResult<String, List<Coupon>> findOrderCouponByOrderNo(Order order) {
        ServiceResult<String,List<Coupon>> serviceResult = new ServiceResult<>();
        List<CouponDO> couponDOList = couponMapper.findByOrderNo(order.getOrderNo());
        List<Coupon> couponList = ConverterUtil.convertList(couponDOList, Coupon.class);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(couponList);
        return serviceResult;
    }


    /**
     * 增加优惠券方法
     * @param couponBatchDetailDO
     * @return
     */
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void addCoupon(CouponBatchDetailDO couponBatchDetailDO) {
        Date date = new Date();
        List<CouponDO> list = new ArrayList<>();
        for (int i = 0; i <couponBatchDetailDO.getCouponTotalCount() ; i++) {
            CouponDO couponDO = new CouponDO();
            couponDO.setCouponBatchId(couponBatchDetailDO.getCouponBatchId());
            couponDO.setCouponCode(generateNoSupport.generateCouponCode());
            couponDO.setCouponBatchDetailId(couponBatchDetailDO.getId());
            couponDO.setFaceValue(couponBatchDetailDO.getFaceValue());
            couponDO.setDeductionAmount(BigDecimal.ZERO);
            couponDO.setIsOnline(couponBatchDetailDO.getIsOnline());
            //线上（1），线下（0）
            if (CommonConstant.COMMON_CONSTANT_NO.equals(couponBatchDetailDO.getIsOnline())) {
                //线下设置为可用（4）
                couponDO.setCouponStatus(CouponStatus.COUPON_STATUS_USABLE);
            } else {
                //线上设置为未领取（0）
                couponDO.setCouponStatus(CouponStatus.COUPON_STATUS_UNCLAIMED);
            }
            couponDO.setEffectiveStartTime(couponBatchDetailDO.getEffectiveStartTime());
            couponDO.setEffectiveEndTime(couponBatchDetailDO.getEffectiveEndTime());
            couponDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            couponDO.setCreateTime(date);
            couponDO.setUpdateTime(date);
            couponDO.setCreateUser(userSupport.getCurrentUserId().toString());
            couponDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            list.add(couponDO);
        }
        couponMapper.addList(list);

    }

}
