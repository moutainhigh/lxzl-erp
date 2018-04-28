package com.lxzl.erp.dataaccess.dao.mysql.coupon;

import com.lxzl.erp.dataaccess.domain.coupon.CouponDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface CouponMapper extends BaseMysqlDAO<CouponDO> {

	List<CouponDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

    void addList(@Param("list") List<CouponDO> list);

    Integer findCouponCountByParams(@Param("maps") Map<String, Object> maps);

    List<CouponDO> findCouponByParams(@Param("maps") Map<String, Object> maps);

    void deleteCouponList(@Param("couponDOList") List<CouponDO> couponDOList);

    Integer findCouponStatusCountIsZeroByCouponBatchDetailId(Integer couponBatchDetailId);

    void updateList(@Param("couponDOList") List<CouponDO> couponDOList);

    List<CouponDO> findByCouponStatus(@Param("couponBatchDetailId") Integer couponBatchDetailId,@Param("totalCouponProvideAmount") Integer totalCouponProvideAmount);

    List<CouponDO> findByCustomerNo(@Param("customerNo") String customerNo,@Param("orderNo") String orderNo);

    List<CouponDO> findCouponDOList(@Param("couponDOIdList") List<Integer> couponDOIdList);

    List<CouponDO> findByCouponBatchDetailID(@Param("couponBatchDetailId") Integer couponBatchDetailId);

    void cancelCoupon(@Param("couponBatchDetailId") Integer couponBatchDetailId,@Param("updateTime") Date updateTime, @Param("updateUser") String updateUser);

    Integer findCustomerCouponCountByParams(@Param("maps") Map<String, Object> maps);

    List<CouponDO> findCustomerCouponByParams(@Param("maps") Map<String, Object> maps);

    List<CouponDO> findUsedCouponDoList(@Param("customerNo") String customerNo,@Param("orderId") Integer orderId,@Param("orderProductId") Integer orderProductId);

    List<CouponDO> findStatementCouponByCustomerNo(@Param("customerNo") String customerNo);

    CouponDO findByStatementOrderDetailId(@Param("statementOrderDetailId") Integer statementOrderDetailId);

    Integer findLockCouponCountByParams(@Param("maps") Map<String, Object> maps);

    List<CouponDO> findLockCouponByParams(@Param("maps") Map<String, Object> maps);

    List<CouponDO> findByOrderNo(@Param("orderNo") String orderNo);

    void updateLockList(@Param("couponDOList") List<CouponDO> couponDOList);

    void updateRevertList(@Param("couponDOList") List<CouponDO> couponDOList);
}