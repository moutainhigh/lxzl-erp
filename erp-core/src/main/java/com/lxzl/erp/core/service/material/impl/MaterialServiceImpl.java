package com.lxzl.erp.core.service.material.impl;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.BulkMaterialQueryParam;
import com.lxzl.erp.common.domain.material.MaterialQueryParam;
import com.lxzl.erp.common.domain.material.pojo.BulkMaterial;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.core.service.material.MaterialService;
import com.lxzl.erp.core.service.product.impl.support.MaterialConverter;
import com.lxzl.erp.dataaccess.dao.mysql.material.BulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.domain.material.BulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
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
 * @date 2017-11-13 13:43
 */
@Service("materialService")
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private BulkMaterialMapper bulkMaterialMapper;

    @Override
    public ServiceResult<String, Page<Material>> queryAllMaterial(MaterialQueryParam materialQueryParam) {
        ServiceResult<String, Page<Material>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(materialQueryParam.getPageNo(), materialQueryParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("materialQueryParam", materialQueryParam);

        Integer totalCount = materialMapper.listCount(maps);
        List<MaterialDO> materialDOList = materialMapper.listPage(maps);
        List<Material> productList = MaterialConverter.convertMaterialDOList(materialDOList);
        Page<Material> page = new Page<>(productList, totalCount, materialQueryParam.getPageNo(), materialQueryParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, Page<BulkMaterial>> queryAllBulkMaterial(BulkMaterialQueryParam bulkMaterialQueryParam) {
        ServiceResult<String, Page<BulkMaterial>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(bulkMaterialQueryParam.getPageNo(), bulkMaterialQueryParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("bulkMaterialQueryParam", bulkMaterialQueryParam);

        Integer totalCount = bulkMaterialMapper.listCount(maps);
        List<BulkMaterialDO> bulkMaterialDOList = bulkMaterialMapper.listPage(maps);
        List<BulkMaterial> productList = MaterialConverter.convertProductBulkMaterialDOList(bulkMaterialDOList);
        Page<BulkMaterial> page = new Page<>(productList, totalCount, bulkMaterialQueryParam.getPageNo(), bulkMaterialQueryParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, Page<BulkMaterial>> queryBulkMaterialByMaterialId(BulkMaterialQueryParam bulkMaterialQueryParam) {
        ServiceResult<String, Page<BulkMaterial>> result = new ServiceResult<>();
        if(bulkMaterialQueryParam.getMaterialId() == null){
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }

        PageQuery pageQuery = new PageQuery(bulkMaterialQueryParam.getPageNo(), bulkMaterialQueryParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("bulkMaterialQueryParam", bulkMaterialQueryParam);

        Integer totalCount = bulkMaterialMapper.listCount(maps);
        List<BulkMaterialDO> bulkMaterialDOList = bulkMaterialMapper.listPage(maps);
        List<BulkMaterial> productList = MaterialConverter.convertProductBulkMaterialDOList(bulkMaterialDOList);
        Page<BulkMaterial> page = new Page<>(productList, totalCount, bulkMaterialQueryParam.getPageNo(), bulkMaterialQueryParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }
}
