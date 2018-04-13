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
import com.lxzl.erp.dataaccess.domain.coupon.CouponBatchDO;
import com.lxzl.erp.dataaccess.domain.coupon.CouponBatchDetailDO;
import com.lxzl.erp.dataaccess.domain.coupon.CouponDO;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
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
        couponBatchDetailDO.setCreateTime(date);
        couponBatchDetailDO.setCreateUser(userSupport.getCurrentUserId().toString());
        couponBatchDetailDO.setUpdateTime(date);
        couponBatchDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
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
        Map<Integer,Integer> couponBatchDetailMap = new HashMap();
        // 循环判断是否有已经使用的，如果有已经使用的则返回错误信息
        for (int i = 0; i < couponDOList.size(); i++) {
            if (couponDOList.get(i).getCouponStatus() == CouponStatus.COUPON_STATUS_USED) {
                serviceResult.setErrorCode(ErrorCode.COUPON_USED);
                return  serviceResult;
            }
            if (couponDOList.get(i).getCouponStatus() == CouponStatus.COUPON_STATUS_CANCEL) {
                serviceResult.setErrorCode(ErrorCode.COUPON_CANCEL);
                return  serviceResult;
            }
            //  根据每个优惠卷所属批次的详细ID进行存储并计数
            if (couponBatchDetailMap.containsKey(couponDOList.get(i).getCouponBatchDetailId())) {
                couponBatchDetailMap.put(couponDOList.get(i).getCouponBatchDetailId(),couponBatchDetailMap.get(couponDOList.get(i).getCouponBatchDetailId())+1);
            } else {
                couponBatchDetailMap.put(couponDOList.get(i).getCouponBatchDetailId(),1);
            }
            couponDOList.get(i).setCouponStatus(CouponStatus.COUPON_STATUS_CANCEL);
            couponDOList.get(i).setUpdateTime(date);
            couponDOList.get(i).setUpdateUser(userSupport.getCurrentUserId().toString());
        }
        // 删除优惠券
        couponMapper.deleteCouponList(couponDOList);
        // 获取需要更改的优惠券批次详情对象的集合
        List<CouponBatchDetailDO> couponBatchDetailDOList = new ArrayList<>();
        Map<Integer,Integer> coucouponBatchMap = new HashMap<>();
        Iterator it = couponBatchDetailMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Integer key = (Integer) entry.getKey();
            Integer value = (Integer) entry.getValue();
            CouponBatchDetailDO couponBatchDetailDO = couponBatchDetailMapper.findById(key);
            couponBatchDetailDO.setCouponCancelCount(value+couponBatchDetailDO.getCouponCancelCount());
            couponBatchDetailDO.setUpdateTime(date);
            couponBatchDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            couponBatchDetailDOList.add(couponBatchDetailDO);

            if (coucouponBatchMap.containsKey(couponBatchDetailDO.getCouponBatchId())) {
                coucouponBatchMap.put(couponBatchDetailDO.getCouponBatchId(),coucouponBatchMap.get(couponBatchDetailDO.getCouponBatchId())+couponBatchDetailDO.getCouponCancelCount());
            } else {
                coucouponBatchMap.put(couponBatchDetailDO.getCouponBatchId(),couponBatchDetailDO.getCouponCancelCount());
            }
        }
        couponBatchDetailMapper.updateCouponBatchDetailDOList(couponBatchDetailDOList);
        for (Integer key : coucouponBatchMap.keySet()) {
            CouponBatchDO couponBatchDO = couponBatchMapper.findById(key);
            couponBatchDO.setCouponBatchCancelCount(coucouponBatchMap.get(key));
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
        couponBatchDetailDO.setUpdateTime(date);
        couponBatchDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        couponBatchDetailMapper.update(couponBatchDetailDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }
    /**
     * 按客户编号查询该客户可用优惠券
     * @param customer
     * @return
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, List<Coupon>> findCouponByCustomerNo(Customer customer) {
        ServiceResult<String,List<Coupon>> serviceResult = new ServiceResult<>();
        List<CouponDO> couponDOList = couponMapper.findByCustomerNo(customer.getCustomerNo());
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
        couponBatchDetailDO.setUpdateTime(date);
        couponBatchDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        couponBatchDetailDO.setCouponCancelCount(couponBatchDetailDO.getCouponTotalCount());
        couponBatchDetailMapper.update(couponBatchDetailDO);
        couponMapper.cancelCoupon(couponBatchDetailDO.getId(),date,userSupport.getCurrentUserId().toString());
        CouponBatchDO couponBatchDO = couponBatchMapper.findById(couponBatchDetailDO.getCouponBatchId());
        couponBatchDO.setCouponBatchCancelCount(couponBatchDO.getCouponBatchCancelCount()+couponBatchDetailDO.getCouponCancelCount());
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
            couponBatchDetailMapper.update(couponBatchDetailDOList.get(i));
            couponMapper.cancelCoupon(couponBatchDetailDOList.get(i).getId(),date,userSupport.getCurrentUserId().toString());
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Autowired
    private CouponSupport couponSupport;

    /**
     * 生成优惠券的方法
     * @param useCoupon
     * @return
     */
    @Override
    public ServiceResult<String, String> useCoupon( UseCoupon useCoupon) {
        ServiceResult<String,String> serviceResult = new ServiceResult<>();
        Order order = useCoupon.getOrder();
        List<Coupon> couponList = useCoupon.getCouponList();
        serviceResult.setResult(couponSupport.useCoupon(order,couponList));
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
