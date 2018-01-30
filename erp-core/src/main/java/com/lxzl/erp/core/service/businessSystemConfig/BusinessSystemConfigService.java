package com.lxzl.erp.core.service.businessSystemConfig;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.businessSystemConfig.BusinessSystemConfigQueryParam;
import com.lxzl.erp.common.domain.businessSystemConfig.pojo.BusinessSystemConfig;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/29
 * @Time : Created in 15:03
 */
public interface BusinessSystemConfigService {
    /**
    * 查询业务系统配置信息
    * @Author : XiaoLuYu
    * @Date : Created in 2018/1/29 15:16
    * @param : businessAppId
    * @param : businessAppSecret
    * @param : businessSystemName
    * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,com.lxzl.erp.common.domain.businessSystemConfig.pojo.BusinessSystemConfig>
    */
    ServiceResult<String,Page<BusinessSystemConfig>> queryBusinessSystemConfig(BusinessSystemConfigQueryParam businessSystemConfigQueryParam);
    /**
    * 校验erp身份信息
    * @Author : XiaoLuYu
    * @Date : Created in 2018/1/29 17:09
    * @param : erpAppId
    * @param : erpAppSecret
    * @Return : boolean
    */
    boolean verifyErpIdentity(String erpAppId,String erpAppSecret);
}
