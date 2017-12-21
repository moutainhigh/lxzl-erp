package com.lxzl.erp.core.service.jointProduct;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.jointProduct.JointProduct;
import com.lxzl.erp.common.domain.jointProduct.JointProductQueryParam;

public interface JointProductService {

    /**
     * 添加组合商品
     * */
    ServiceResult<String,Integer> addJointProduct(JointProduct jointProduct);
    /**
     * 跟新组合商品
     * */
    ServiceResult<String,Integer> updateJointProduct(JointProduct jointProduct);
    /**
     * 删除组合商品
     * */
    ServiceResult<String,Integer> deleteJointProduct(JointProduct jointProduct);
    /**
     * 查详情
     * */
    ServiceResult<String,JointProduct> queryJointProductByJointProductId(Integer jointProductId);
    /**
     * 分页查询
     * */
    ServiceResult<String,Page<JointProduct>> pageJointProduct(JointProductQueryParam jointProductQueryParam);
}
