package com.lxzl.erp.core.service.material.impl;

import com.lxzl.erp.common.constant.BulkMaterialStatus;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.MaterialType;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.BulkMaterialQueryParam;
import com.lxzl.erp.common.domain.material.MaterialModelQueryParam;
import com.lxzl.erp.common.domain.material.MaterialQueryParam;
import com.lxzl.erp.common.domain.material.pojo.BulkMaterial;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.material.pojo.MaterialImg;
import com.lxzl.erp.common.domain.material.pojo.MaterialModel;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.*;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.FileService;
import com.lxzl.erp.core.service.material.MaterialService;
import com.lxzl.erp.core.service.material.impl.support.MaterialImageConverter;
import com.lxzl.erp.dataaccess.dao.mysql.material.BulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialImgMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialModelMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.*;
import com.lxzl.erp.dataaccess.dao.mysql.purchase.PurchaseOrderMaterialMapper;
import com.lxzl.erp.dataaccess.domain.material.BulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialImgDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialModelDO;
import com.lxzl.erp.dataaccess.domain.product.*;
import com.lxzl.erp.dataaccess.domain.purchase.PurchaseOrderMaterialDO;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
    private MaterialImgMapper materialImgMapper;

    @Autowired
    private ProductMaterialMapper productMaterialMapper;

    @Autowired
    private ProductEquipmentMaterialMapper productEquipmentMaterialMapper;

    @Autowired
    private BulkMaterialMapper bulkMaterialMapper;

    @Autowired
    private PurchaseOrderMaterialMapper purchaseOrderMaterialMapper;

    @Autowired
    private MaterialModelMapper materialModelMapper;

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
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> addMaterial(Material material) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();

        String verifyCode = verifyAddMaterial(material);
        if (!ErrorCode.SUCCESS.equals(verifyCode)) {
            result.setErrorCode(verifyCode);
            return result;
        }

        MaterialDO dbMaterialDO = null;
        if (MaterialType.isCapacityMaterial(material.getMaterialType())) {
            dbMaterialDO = materialMapper.findByMaterialTypeAndCapacity(material.getMaterialType(), material.getMaterialCapacityValue());
            material.setMaterialModelId(null);
        } else {
            dbMaterialDO = materialMapper.findByMaterialTypeAndModelId(material.getMaterialType(), material.getMaterialModelId());
            material.setMaterialCapacityValue(null);
        }
        if (dbMaterialDO != null) {
            result.setErrorCode(ErrorCode.RECORD_ALREADY_EXISTS);
            return result;
        }
        MaterialDO materialDO = ConverterUtil.convert(material, MaterialDO.class);

        if (MaterialType.isMainMaterial(materialDO.getMaterialType())) {
            materialDO.setIsMainMaterial(CommonConstant.COMMON_CONSTANT_YES);
        } else {
            materialDO.setIsMainMaterial(CommonConstant.COMMON_CONSTANT_NO);
        }
        materialDO.setMaterialNo(GenerateNoUtil.generateMaterialNo(currentTime));
        materialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        materialDO.setUpdateUser(loginUser.getUserId().toString());
        materialDO.setCreateUser(loginUser.getUserId().toString());
        materialDO.setUpdateTime(currentTime);
        materialDO.setCreateTime(currentTime);
        materialMapper.save(materialDO);
        saveMaterialImage(material.getMaterialImgList(), 1, materialDO.getId(), loginUser, currentTime);

        result.setResult(materialDO.getMaterialNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> updateMaterial(Material material) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();

        MaterialDO dbRecord = materialMapper.findByNo(material.getMaterialNo());
        if (dbRecord == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        if (MaterialType.isCapacityMaterial(dbRecord.getMaterialType())) {
            material.setMaterialCapacityValue(null);
        } else {
            material.setMaterialModelId(null);
        }

        MaterialDO materialDO = ConverterUtil.convert(material, MaterialDO.class);
        // 以下两个值不能改
        materialDO.setId(dbRecord.getId());
        materialDO.setMaterialType(null);
        materialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        materialDO.setUpdateUser(loginUser.getUserId().toString());
        materialDO.setUpdateTime(currentTime);
        materialMapper.update(materialDO);

        saveMaterialImage(material.getMaterialImgList(), 1, materialDO.getId(), loginUser, currentTime);

        result.setResult(materialDO.getMaterialNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, String> deleteMaterial(String materialNo) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();
        MaterialDO materialDO = materialMapper.findByNo(materialNo);
        if (materialDO == null) {
            result.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
            return result;
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", 0);
        paramMap.put("pageSize", Integer.MAX_VALUE);
        paramMap.put("materialId", materialDO.getId());

        List<ProductMaterialDO> productMaterialDOList = productMaterialMapper.listPage(paramMap);
        List<ProductEquipmentMaterialDO> productEquipmentMaterialDOList = productEquipmentMaterialMapper.listPage(paramMap);
        List<BulkMaterialDO> bulkMaterialDOList = bulkMaterialMapper.listPage(paramMap);
        List<PurchaseOrderMaterialDO> purchaseOrderMaterialDOList = purchaseOrderMaterialMapper.listPage(paramMap);
        if (CollectionUtil.isNotEmpty(productMaterialDOList)
                || CollectionUtil.isNotEmpty(productEquipmentMaterialDOList)
                || CollectionUtil.isNotEmpty(bulkMaterialDOList)
                || CollectionUtil.isNotEmpty(purchaseOrderMaterialDOList)) {
            result.setErrorCode(ErrorCode.MATERIAL_IN_USED);
            return result;
        }
        materialDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        materialDO.setUpdateTime(currentTime);
        materialDO.setUpdateUser(loginUser.getUserId().toString());
        materialMapper.update(materialDO);

        result.setResult(materialNo);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }


    void saveMaterialImage(List<MaterialImg> materialImgList, Integer type, Integer materialId, User loginUser, Date currentTime) {

        List<MaterialImg> updateMaterialImgList = new ArrayList<>();
        List<MaterialImgDO> dbMaterialImgRecord = materialImgMapper.findByMaterialIdAndType(materialId, type);
        Map<Integer, MaterialImgDO> dbMaterialImgRecordMap = ListUtil.listToMap(dbMaterialImgRecord, "id");

        if (materialImgList != null) {
            for (MaterialImg materialImg : materialImgList) {
                if (materialImg != null && materialImg.getMaterialImgId() != null) {
                    updateMaterialImgList.add(materialImg);
                    if (dbMaterialImgRecordMap.get(materialImg.getMaterialImgId()) != null) {
                        dbMaterialImgRecordMap.remove(materialImg.getMaterialImgId());
                    }
                }
            }
        }


        if (!updateMaterialImgList.isEmpty()) {
            for (MaterialImg materialImg : updateMaterialImgList) {
                MaterialImgDO materialImgDO = MaterialImageConverter.convertMaterialImg(materialImg);
                materialImgDO.setMaterialId(materialId);
                materialImgDO.setImgType(type);
                materialImgDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                materialImgDO.setUpdateUser(loginUser.getUserId().toString());
                materialImgDO.setUpdateTime(currentTime);
                materialImgMapper.update(materialImgDO);
            }
        }
        if (!dbMaterialImgRecordMap.isEmpty()) {
            for (Map.Entry<Integer, MaterialImgDO> entry : dbMaterialImgRecordMap.entrySet()) {
                MaterialImgDO materialImgDO = entry.getValue();
                materialImgDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                materialImgDO.setUpdateUser(loginUser.getUserId().toString());
                materialImgDO.setUpdateTime(currentTime);
                materialImgMapper.update(materialImgDO);
            }
        }
    }

    private String verifyAddMaterial(Material material) {
        if (material == null) {
            return ErrorCode.PARAM_IS_NOT_NULL;
        }
        if (StringUtil.isBlank(material.getMaterialName())
                || material.getMaterialType() == null
                || material.getMaterialPrice() == null
                || material.getDayRentPrice() == null
                || material.getMonthRentPrice() == null) {
            return ErrorCode.PARAM_IS_NOT_ENOUGH;
        }

        if (MaterialType.isCapacityMaterial(material.getMaterialType())
                && material.getMaterialCapacityValue() == null) {
            return ErrorCode.MATERIAL_CAPACITY_VALUE_NOT_NULL;
        } else if (MaterialType.isModelMaterial(material.getMaterialType())
                && material.getMaterialModelId() == null) {
            return ErrorCode.MATERIAL_MODEL_NOT_NULL;
        }

        if (material.getMaterialModelId() != null) {
            MaterialModelDO materialModelDO = materialModelMapper.findById(material.getMaterialModelId());
            if (materialModelDO == null
                    || !materialModelDO.getMaterialType().equals(material.getMaterialType())) {
                return ErrorCode.PARAM_IS_ERROR;
            }
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
        List<Material> materialList = ConverterUtil.convertList(materialDOList, Material.class);
        Page<Material> page = new Page<>(materialList, totalCount, materialQueryParam.getPageNo(), materialQueryParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, Material> queryMaterialByNo(String materialNo) {
        ServiceResult<String, Material> result = new ServiceResult<>();
        if (StringUtil.isBlank(materialNo)) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        MaterialDO materialDO = materialMapper.findByNo(materialNo);
        if (materialDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        Material material = ConverterUtil.convert(materialDO, Material.class);

        BulkMaterialQueryParam bulkMaterialQueryParam = new BulkMaterialQueryParam();
        bulkMaterialQueryParam.setMaterialId(materialDO.getId());
        bulkMaterialQueryParam.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE);

        Map<String, Object> queryEquipmentCountParam = new HashMap<>();
        queryEquipmentCountParam.put("start", 0);
        queryEquipmentCountParam.put("pageSize", Integer.MAX_VALUE);
        queryEquipmentCountParam.put("bulkMaterialQueryParam", bulkMaterialQueryParam);
        Integer newProductSkuCount = bulkMaterialMapper.listCount(queryEquipmentCountParam);
        bulkMaterialQueryParam.setIsNew(CommonConstant.COMMON_CONSTANT_NO);
        queryEquipmentCountParam.put("bulkMaterialQueryParam", bulkMaterialQueryParam);
        Integer oldProductSkuCount = bulkMaterialMapper.listCount(queryEquipmentCountParam);
        material.setNewMaterialCount(newProductSkuCount);
        material.setOldMaterialCount(oldProductSkuCount);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(material);
        return result;
    }

    @Override
    public ServiceResult<String, Material> queryMaterialById(Integer materialId) {
        ServiceResult<String, Material> result = new ServiceResult<>();
        if (materialId == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        MaterialDO materialDO = materialMapper.findById(materialId);
        if (materialDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(ConverterUtil.convert(materialDO, Material.class));
        return result;
    }

    @Override
    public ServiceResult<String, Page<BulkMaterial>> queryAllBulkMaterial(BulkMaterialQueryParam bulkMaterialQueryParam) {
        ServiceResult<String, Page<BulkMaterial>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(bulkMaterialQueryParam.getPageNo(), bulkMaterialQueryParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        bulkMaterialQueryParam.setIsOnEquipment(CommonConstant.COMMON_CONSTANT_NO);
        maps.put("bulkMaterialQueryParam", bulkMaterialQueryParam);

        Integer totalCount = bulkMaterialMapper.listCount(maps);
        List<BulkMaterialDO> bulkMaterialDOList = bulkMaterialMapper.listPage(maps);
        List<BulkMaterial> productList = ConverterUtil.convertList(bulkMaterialDOList, BulkMaterial.class);
        Page<BulkMaterial> page = new Page<>(productList, totalCount, bulkMaterialQueryParam.getPageNo(), bulkMaterialQueryParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, Page<BulkMaterial>> queryBulkMaterialByMaterialId(BulkMaterialQueryParam bulkMaterialQueryParam) {
        ServiceResult<String, Page<BulkMaterial>> result = new ServiceResult<>();

        PageQuery pageQuery = new PageQuery(bulkMaterialQueryParam.getPageNo(), bulkMaterialQueryParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("bulkMaterialQueryParam", bulkMaterialQueryParam);

        Integer totalCount = bulkMaterialMapper.listCount(maps);
        List<BulkMaterialDO> bulkMaterialDOList = bulkMaterialMapper.listPage(maps);
        List<BulkMaterial> productList = ConverterUtil.convertList(bulkMaterialDOList, BulkMaterial.class);
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
            if (material.getMaterialType() == null
                    || (!MaterialType.MATERIAL_TYPE_MEMORY.equals(material.getMaterialType())
                    && !MaterialType.MATERIAL_TYPE_MAIN_BOARD.equals(material.getMaterialType())
                    && !MaterialType.MATERIAL_TYPE_CPU.equals(material.getMaterialType())
                    && !MaterialType.MATERIAL_TYPE_HDD.equals(material.getMaterialType())
                    && !MaterialType.MATERIAL_TYPE_SSD.equals(material.getMaterialType())
                    && !MaterialType.MATERIAL_TYPE_GRAPHICS_CARD.equals(material.getMaterialType()))) {
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
            if (material.getMaterialType() == null
                    || MaterialType.MATERIAL_TYPE_MEMORY.equals(material.getMaterialType())
                    || MaterialType.MATERIAL_TYPE_MAIN_BOARD.equals(material.getMaterialType())
                    || MaterialType.MATERIAL_TYPE_CPU.equals(material.getMaterialType())
                    || MaterialType.MATERIAL_TYPE_HDD.equals(material.getMaterialType())
                    || MaterialType.MATERIAL_TYPE_SSD.equals(material.getMaterialType())
                    || MaterialType.MATERIAL_TYPE_GRAPHICS_CARD.equals(material.getMaterialType())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ServiceResult<String, Integer> addMaterialModel(MaterialModel materialModel) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();
        if (materialModel == null
                || materialModel.getMaterialType() == null
                || materialModel.getModelName() == null
                || !MaterialType.inThisScope(materialModel.getMaterialType())) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        if (MaterialType.isCapacityMaterial(materialModel.getMaterialType())) {
            result.setErrorCode(ErrorCode.MATERIAL_TYPE_HAVE_NO_MODEL);
            return result;
        }
        MaterialModelDO dbMaterialModelDO = materialModelMapper.findByTypeAndName(materialModel.getMaterialType(), materialModel.getModelName());
        if (dbMaterialModelDO != null) {
            result.setErrorCode(ErrorCode.RECORD_ALREADY_EXISTS);
            return result;
        }

        MaterialModelDO materialModelDO = ConverterUtil.convert(materialModel, MaterialModelDO.class);
        materialModelDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        materialModelDO.setUpdateUser(loginUser.getUserId().toString());
        materialModelDO.setCreateUser(loginUser.getUserId().toString());
        materialModelDO.setUpdateTime(currentTime);
        materialModelDO.setCreateTime(currentTime);
        materialModelMapper.save(materialModelDO);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(materialModelDO.getId());
        return result;
    }

    @Override
    public ServiceResult<String, Integer> updateMaterialModel(MaterialModel materialModel) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();

        MaterialModelDO dbMaterialModelDO = materialModelMapper.findById(materialModel.getMaterialModelId());
        if (dbMaterialModelDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        if (materialModel.getMaterialType() != null && dbMaterialModelDO.getMaterialType().equals(materialModel.getMaterialType())) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }

        MaterialModelDO materialModelDO = ConverterUtil.convert(materialModel, MaterialModelDO.class);
        materialModelDO.setMaterialType(null);
        materialModelDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        materialModelDO.setUpdateUser(loginUser.getUserId().toString());
        materialModelDO.setUpdateTime(currentTime);
        materialModelMapper.update(materialModelDO);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(materialModelDO.getId());
        return result;
    }


    @Override
    public ServiceResult<String, Integer> deleteMaterialModel(Integer materialModelId) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();

        MaterialModelDO dbMaterialModelDO = materialModelMapper.findById(materialModelId);
        if (dbMaterialModelDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }

        MaterialDO materialDO = materialMapper.findByMaterialTypeAndModelId(dbMaterialModelDO.getMaterialType(), dbMaterialModelDO.getId());
        if (materialDO != null) {
            result.setErrorCode(ErrorCode.RECORD_USED_CAN_NOT_DELETE);
            return result;
        }

        List<BulkMaterialDO> bulkMaterialDOList = bulkMaterialMapper.findByMaterialTypeAndModelId(dbMaterialModelDO.getMaterialType(), dbMaterialModelDO.getId());
        if (CollectionUtil.isEmpty(bulkMaterialDOList)) {
            result.setErrorCode(ErrorCode.RECORD_USED_CAN_NOT_DELETE);
            return result;
        }

        dbMaterialModelDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        dbMaterialModelDO.setUpdateUser(loginUser.getUserId().toString());
        dbMaterialModelDO.setUpdateTime(currentTime);
        materialModelMapper.update(dbMaterialModelDO);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(dbMaterialModelDO.getId());
        return result;
    }

    @Override
    public ServiceResult<String, Page<MaterialModel>> queryMaterialModel(MaterialModelQueryParam materialModelQueryParam) {
        ServiceResult<String, Page<MaterialModel>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(materialModelQueryParam.getPageNo(), materialModelQueryParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("materialModelQueryParam", materialModelQueryParam);
        Integer totalCount = materialModelMapper.listCount(maps);
        List<MaterialModelDO> materialModelDOList = materialModelMapper.listPage(maps);
        List<MaterialModel> materialModelList = ConverterUtil.convertList(materialModelDOList,MaterialModel.class);
        Page<MaterialModel> page = new Page<>(materialModelList, totalCount, materialModelQueryParam.getPageNo(), materialModelQueryParam.getPageSize());
        result.setResult(page);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, MaterialModel> queryMaterialModelById(Integer materialModelId) {
        ServiceResult<String, MaterialModel> result = new ServiceResult<>();
        MaterialModelDO materialModelDO = materialModelMapper.findById(materialModelId);
        if (materialModelDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }

        result.setResult(ConverterUtil.convert(materialModelDO,MaterialModel.class));
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }
}
