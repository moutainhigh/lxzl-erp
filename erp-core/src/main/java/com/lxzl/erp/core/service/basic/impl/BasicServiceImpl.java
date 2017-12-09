package com.lxzl.erp.core.service.basic.impl;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.basic.BrandQueryParam;
import com.lxzl.erp.common.domain.basic.pojo.Brand;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.basic.BasicService;
import com.lxzl.erp.dataaccess.dao.mysql.basic.BrandMapper;
import com.lxzl.erp.dataaccess.domain.basic.BrandDO;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-09 13:02
 */
@Service("basicService")
public class BasicServiceImpl implements BasicService {

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public ServiceResult<String, Page<Brand>> queryAllBrand(BrandQueryParam param) {
        ServiceResult<String, Page<Brand>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(param.getPageNo(), param.getPageSize());
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", pageQuery.getStart());
        paramMap.put("pageSize", pageQuery.getPageSize());
        paramMap.put("brandQueryParam", param);
        List<BrandDO> brandList = brandMapper.listPage(paramMap);
        Integer totalCount = brandMapper.listCount(paramMap);
        Page<Brand> page = new Page<>(ConverterUtil.convertList(brandList, Brand.class), totalCount, param.getPageNo(), param.getPageSize());
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }
}
