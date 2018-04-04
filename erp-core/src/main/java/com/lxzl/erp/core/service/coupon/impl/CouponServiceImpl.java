package com.lxzl.erp.core.service.coupon.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.CouponStatus;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.coupon.CouponBatchDetailQueryParam;
import com.lxzl.erp.common.domain.coupon.CouponBatchQueryParam;
import com.lxzl.erp.common.domain.coupon.CouponQueryParam;
import com.lxzl.erp.common.domain.coupon.pojo.Coupon;
import com.lxzl.erp.common.domain.coupon.pojo.CouponBatch;
import com.lxzl.erp.common.domain.coupon.pojo.CouponBatchDetail;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.coupon.CouponService;
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
        Integer couponToyalCountSum =  couponBatchDetailMapper.findCouponTotalCountSumByCouponBatchId(couponBatchDetailDO.getCouponBatchId());
        BigDecimal totalFaceAmountSum =  couponBatchDetailMapper.findTotalFaceAmountSumByCouponBatchId(couponBatchDetailDO.getCouponBatchId());
        couponBatchDO.setTotalFaceAmount(BigDecimalUtil.add(totalFaceAmountSum, couponBatchDetailDO.getTotalFaceAmount()));
        couponBatchDO.setCouponBatchTotalCount(couponToyalCountSum);
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
    public ServiceResult<String, String> deleteCoupon(List<Coupon> list) {
        ServiceResult<String,String> serviceResult = new ServiceResult<>();
        Date date = new Date();
        ArrayList<CouponDO> couponDOList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            CouponDO couponDO = couponMapper.findByIdIgnoreDataStatus(list.get(i).getCouponId());
            if (couponDO.getDataStatus()==2) {
                serviceResult.setErrorCode(ErrorCode.COUPON_USED);
                return  serviceResult;
            }
            couponDOList.add(couponDO);
        }
        couponMapper.deleteCouponList(couponDOList);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
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
