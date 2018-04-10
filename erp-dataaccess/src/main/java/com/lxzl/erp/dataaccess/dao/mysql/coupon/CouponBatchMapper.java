package com.lxzl.erp.dataaccess.dao.mysql.coupon;

import com.lxzl.erp.dataaccess.domain.coupon.CouponBatchDetailDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.coupon.CouponBatchDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface CouponBatchMapper extends BaseMysqlDAO<CouponBatchDO> {

	List<CouponBatchDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

    Integer findCouponBatchCountByParams(@Param("maps") Map<String, Object> paramMap);

	List<CouponBatchDO> findCouponBatchByParams(@Param("maps") Map<String, Object> paramMap);

	CouponBatchDO findByIdIgnoreDataStatus(Integer couponBatchId);

    void updateUseList(@Param("couponBatchDOList") List<CouponBatchDO> couponBatchDOList);
}