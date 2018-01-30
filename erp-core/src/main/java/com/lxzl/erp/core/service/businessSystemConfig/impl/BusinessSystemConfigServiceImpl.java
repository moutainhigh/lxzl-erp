package com.lxzl.erp.core.service.businessSystemConfig.impl;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.businessSystemConfig.BusinessSystemConfigQueryParam;
import com.lxzl.erp.common.domain.businessSystemConfig.pojo.BusinessSystemConfig;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.businessSystemConfig.BusinessSystemConfigService;
import com.lxzl.erp.dataaccess.dao.mysql.businessSystemConfig.BusinessSystemConfigMapper;
import com.lxzl.erp.dataaccess.domain.businessSystemConfig.BusinessSystemConfigDO;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.dataaccess.mongo.config.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/29
 * @Time : Created in 15:03
 */
@Service
public class BusinessSystemConfigServiceImpl implements BusinessSystemConfigService {

    /**
     * 查询业务系统配置信息
     * @Author : XiaoLuYu
     * @Date : Created in 2018/1/29 15:16
     * @param : businessAppId
    * @param : businessAppSecret
    * @param : businessSystemName
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,com.lxzl.erp.common.domain.businessSystemConfig.pojo.BusinessSystemConfig>
     */
    @Override
    public ServiceResult<String, Page<BusinessSystemConfig>> queryBusinessSystemConfig(BusinessSystemConfigQueryParam businessSystemConfigQueryParam) {
        ServiceResult<String, Page<BusinessSystemConfig>> serviceResult = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(businessSystemConfigQueryParam.getPageNo(), businessSystemConfigQueryParam.getPageSize());
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("businessSystemConfigQueryParam", businessSystemConfigQueryParam);
        Integer jointProductCount = businessSystemConfigMapper.listCount(maps);
        List<BusinessSystemConfigDO> businessSystemConfigDOList = businessSystemConfigMapper.listPage(maps);
        List<BusinessSystemConfig> jointProductList = ConverterUtil.convertList(businessSystemConfigDOList, BusinessSystemConfig.class);
        Page<BusinessSystemConfig> page = new Page<>(jointProductList, jointProductCount, businessSystemConfigQueryParam.getPageNo(), businessSystemConfigQueryParam.getPageSize());
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(page);
        return serviceResult;
    }

    @Override
    public boolean verifyErpIdentity(String erpAppId, String erpAppSecret) {
        if(StringUtil.isEmpty(erpAppId) || StringUtil.isEmpty(erpAppSecret) ){
            return false;
        }
        Integer count = businessSystemConfigMapper.findCountByErpAppIdAndErpAppSecret(erpAppId, erpAppSecret);
        if(count <= 0){
            return false;
        }
        return true;
    }

    @Autowired
    private BusinessSystemConfigMapper businessSystemConfigMapper;

}
