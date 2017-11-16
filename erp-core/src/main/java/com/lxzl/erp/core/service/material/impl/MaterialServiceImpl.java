package com.lxzl.erp.core.service.material.impl;

import com.lxzl.erp.common.constant.CategoryType;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.MaterialType;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.BulkMaterialQueryParam;
import com.lxzl.erp.common.domain.material.MaterialQueryParam;
import com.lxzl.erp.common.domain.material.pojo.BulkMaterial;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.material.pojo.MaterialImg;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.FileUtil;
import com.lxzl.erp.common.util.GenerateNoUtil;
import com.lxzl.erp.core.service.FileService;
import com.lxzl.erp.core.service.material.MaterialService;
import com.lxzl.erp.core.service.material.impl.support.MaterialConverter;
import com.lxzl.erp.core.service.material.impl.support.MaterialImageConverter;
import com.lxzl.erp.dataaccess.dao.mysql.material.BulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialImgMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductCategoryMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductCategoryPropertyValueMapper;
import com.lxzl.erp.dataaccess.domain.material.BulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialImgDO;
import com.lxzl.erp.dataaccess.domain.product.ProductCategoryDO;
import com.lxzl.erp.dataaccess.domain.product.ProductCategoryPropertyValueDO;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.util.*;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-13 13:43
 */
@Service("materialService")
public class MaterialServiceImpl implements MaterialService {

    private static Logger logger = LoggerFactory.getLogger(MaterialServiceImpl.class);

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private BulkMaterialMapper bulkMaterialMapper;

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Autowired
    private ProductCategoryPropertyValueMapper productCategoryPropertyValueMapper;

    @Autowired
    private MaterialImgMapper materialImgMapper;

    @Autowired
    private FileService fileService;

    @Autowired(required = false)
    private HttpSession session;

    @Override
    public ServiceResult<String, List<MaterialImg>> uploadImage(MultipartFile[] files) {
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        ServiceResult<String, List<MaterialImg>> result = new ServiceResult<>();
        List<MaterialImg> imgList = new ArrayList<>();

        try {
            for (MultipartFile file : files) {
                String filePath = FileUtil.uploadFile(file);
                Map<String, String> extraInfo = new HashMap<>();
                InputStream inputStream = file.getInputStream();
                byte[] fileBytes = file.getBytes();
                long size = file.getSize();
                String fileId = fileService.uploadFile(filePath, fileBytes, size, CommonConstant.UPLOAD_USER, extraInfo, inputStream);
                MaterialImgDO materialImgDO = new MaterialImgDO();
                materialImgDO.setOriginalName(file.getOriginalFilename());
                materialImgDO.setImgUrl(fileId);
                materialImgDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                materialImgDO.setCreateUser(loginUser.getUserId().toString());
                materialImgDO.setUpdateUser(loginUser.getUserId().toString());
                materialImgDO.setCreateTime(new Date());
                materialImgDO.setUpdateTime(new Date());
                materialImgMapper.save(materialImgDO);

                imgList.add(MaterialImageConverter.convertMaterialImgDO(materialImgDO));
                FileUtil.deleteFile(filePath);
            }
        } catch (Exception e) {
            logger.error("upload material image file error", e);
            result.setErrorCode(ErrorCode.PRODUCT_IMAGE_UPLOAD_ERROR);
            return result;
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(imgList);
        return result;
    }

    @Override
    public ServiceResult<String, Integer> deleteImage(Integer imgId) {
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        ServiceResult<String, Integer> result = new ServiceResult<>();
        MaterialImgDO dbRecord = materialImgMapper.findById(imgId);
        if (dbRecord == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        MaterialImgDO materialImgDO = new MaterialImgDO();
        materialImgDO.setId(imgId);
        materialImgDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        materialImgDO.setUpdateUser(loginUser.getUserId().toString());
        materialImgDO.setUpdateTime(new Date());
        Integer returnCode = materialImgMapper.update(materialImgDO);
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(returnCode);
        return result;
    }

    @Override
    public ServiceResult<String, String> addMaterial(Material material) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();

        String verifyCode = verifyAddMaterial(material);
        if (!ErrorCode.SUCCESS.equals(verifyCode)) {
            result.setErrorCode(verifyCode);
            return result;
        }

        MaterialDO dbMaterialDO = materialMapper.findByPropertyAndValueId(material.getPropertyId(), material.getPropertyValueId());
        if (dbMaterialDO != null) {
            result.setErrorCode(ErrorCode.RECORD_ALREADY_EXISTS);
            return result;
        }

        MaterialDO materialDO = MaterialConverter.convertMaterial(material);
        materialDO.setMaterialNo(GenerateNoUtil.generateMaterialNo(currentTime));
        materialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        materialDO.setUpdateUser(loginUser.getUserId().toString());
        materialDO.setCreateUser(loginUser.getUserId().toString());
        materialDO.setUpdateTime(currentTime);
        materialDO.setCreateTime(currentTime);
        materialMapper.save(materialDO);

        result.setResult(materialDO.getMaterialNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, String> updateMaterial(Material material) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();

        MaterialDO dbRecord = materialMapper.findById(material.getMaterialId());
        if (dbRecord == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }

        String verifyCode = verifyAddMaterial(material);
        if (!ErrorCode.SUCCESS.equals(verifyCode)) {
            result.setErrorCode(verifyCode);
            return result;
        }

        MaterialDO materialDO = MaterialConverter.convertMaterial(material);
        // 以下两个值不能改
        materialDO.setPropertyId(null);
        materialDO.setPropertyValueId(null);
        materialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        materialDO.setUpdateUser(loginUser.getUserId().toString());
        materialDO.setUpdateTime(currentTime);
        materialMapper.update(materialDO);

        result.setResult(materialDO.getMaterialNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    private String verifyAddMaterial(Material material) {
        if (material == null) {
            return ErrorCode.PARAM_IS_NOT_NULL;
        }
        if (StringUtil.isBlank(material.getMaterialName())
                || material.getMaterialType() == null
                || material.getMaterialPrice() == null
                || material.getPropertyId() == null
                || material.getPropertyValueId() == null) {
            return ErrorCode.PARAM_IS_NOT_ENOUGH;
        }

        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = productCategoryPropertyValueMapper.findById(material.getPropertyValueId());
        if (productCategoryPropertyValueDO == null || !material.getPropertyId().equals(productCategoryPropertyValueDO.getPropertyId())) {
            return ErrorCode.PARAM_IS_ERROR;
        } else {
            material.setCategoryId(productCategoryPropertyValueDO.getCategoryId());
        }

        ProductCategoryDO productCategoryDO = productCategoryMapper.findById(productCategoryPropertyValueDO.getCategoryId());
        if (!CategoryType.CATEGORY_TYPE_MATERIAL.equals(productCategoryDO.getCategoryType())) {
            return ErrorCode.PARAM_IS_ERROR;
        }
        return ErrorCode.SUCCESS;
    }

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
    public ServiceResult<String, Material> queryMaterialByNo(String materialNo) {
        ServiceResult<String, Material> result = new ServiceResult<>();
        if(StringUtil.isBlank(materialNo)){
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        MaterialDO materialDO = materialMapper.findByNo(materialNo);
        if(materialDO == null){
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(MaterialConverter.convertMaterialDO(materialDO));
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
        if (bulkMaterialQueryParam.getMaterialId() == null) {
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

    @Override
    public boolean isAllMainMaterial(List<Material> materialList) {
        if (CollectionUtil.isEmpty(materialList)) {
            return false;
        }
        for (Material material : materialList) {
            if(!MaterialType.MATERIAL_TYPE_MEMORY.equals(material.getMaterialType())
                    || !MaterialType.MATERIAL_TYPE_MAIN_BOARD.equals(material.getMaterialType())
                    || !MaterialType.MATERIAL_TYPE_CPU.equals(material.getMaterialType())
                    || !MaterialType.MATERIAL_TYPE_GRAPHICS_CARD.equals(material.getMaterialType())){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isAllGadget(List<Material> materialList) {
        if (CollectionUtil.isEmpty(materialList)) {
            return false;
        }
        for (Material material : materialList) {
            if(MaterialType.MATERIAL_TYPE_MEMORY.equals(material.getMaterialType())
                    || MaterialType.MATERIAL_TYPE_MAIN_BOARD.equals(material.getMaterialType())
                    || MaterialType.MATERIAL_TYPE_CPU.equals(material.getMaterialType())
                    || MaterialType.MATERIAL_TYPE_GRAPHICS_CARD.equals(material.getMaterialType())){
                return false;
            }
        }
        return true;
    }
}
