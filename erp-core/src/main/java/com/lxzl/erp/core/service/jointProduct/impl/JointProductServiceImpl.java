package com.lxzl.erp.core.service.jointProduct.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.jointProduct.JointProductQueryParam;
import com.lxzl.erp.common.domain.jointProduct.pojo.JointMaterial;
import com.lxzl.erp.common.domain.jointProduct.pojo.JointProduct;
import com.lxzl.erp.common.domain.jointProduct.pojo.JointProductSku;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.common.util.ListUtil;
import com.lxzl.erp.core.service.jointProduct.JointProductService;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.jointProduct.JointMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.jointProduct.JointProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.jointProduct.JointProductSkuMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.domain.jointProduct.JointMaterialDO;
import com.lxzl.erp.dataaccess.domain.jointProduct.JointProductDO;
import com.lxzl.erp.dataaccess.domain.jointProduct.JointProductSkuDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import com.lxzl.se.dataaccess.mongo.config.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

@Service
public class JointProductServiceImpl implements JointProductService {

    /**
     * 添加组合商品
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, Integer> addJointProduct(JointProduct jointProduct) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        //判断JointProductSkuList 和 JointMaterialList 是否有值
        if (CollectionUtil.isEmpty(jointProduct.getJointProductSkuList()) && CollectionUtil.isEmpty(jointProduct.getJointMaterialList())) {
            serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return serviceResult;
        }

        //添加数据到 erp_joint_product表
        Date now = new Date();
        JointProductDO jointProductDO = new JointProductDO();
        jointProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        jointProductDO.setJointProductName(jointProduct.getJointProductName());
        jointProductDO.setRemark(jointProduct.getRemark());
        jointProductDO.setCreateTime(now);
        jointProductDO.setCreateUser(userSupport.getCurrentUserId().toString());
        jointProductDO.setUpdateTime(now);
        jointProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        //最后添加 erp_joint_product
        jointProductMapper.save(jointProductDO);

        //添加数据到 erp_joint_product_sku表
        List<JointProductSku> jointProductSkuList = jointProduct.getJointProductSkuList();
        if (CollectionUtil.isNotEmpty(jointProductSkuList)) {
            for (JointProductSku jointProductSku : jointProductSkuList) {
                ProductSkuDO productSkuDO = productSkuMapper.findById(jointProductSku.getSkuId());
                if (productSkuDO == null) {
                    //事物回滚
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                    return serviceResult;
                }
                JointProductSkuDO jointProductSkuDO = new JointProductSkuDO();
                jointProductSkuDO.setJointProductId(jointProductDO.getId());
                jointProductSkuDO.setSkuId(jointProductSku.getSkuId());
                jointProductSkuDO.setSkuCount(jointProductSku.getSkuCount());
                jointProductSkuDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                jointProductSkuDO.setUpdateTime(now);
                jointProductSkuDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                jointProductSkuDO.setCreateTime(now);
                jointProductSkuDO.setCreateUser(userSupport.getCurrentUserId().toString());
                jointProductSkuMapper.save(jointProductSkuDO);
            }
        }
        //添加数据到 erp_joint_material 表
        List<JointMaterial> jointMaterialList = jointProduct.getJointMaterialList();
        if (CollectionUtil.isNotEmpty(jointMaterialList)) {
            for (JointMaterial jointMaterial : jointMaterialList) {
                MaterialDO materialDO = materialMapper.findByNo(jointMaterial.getMaterialNo());
                if (materialDO == null) {
                    //事物回滚
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
                    return serviceResult;
                }
                JointMaterialDO jointMaterialDO = new JointMaterialDO();
                jointMaterialDO.setJointProductId(jointProductDO.getId());
                jointMaterialDO.setMaterialId(materialDO.getId());
                jointMaterialDO.setMaterialNo(jointMaterial.getMaterialNo());
                jointMaterialDO.setMaterialCount(jointMaterial.getMaterialCount());
                jointMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                jointMaterialDO.setUpdateTime(now);
                jointMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                jointMaterialDO.setCreateTime(now);
                jointMaterialDO.setCreateUser(userSupport.getCurrentUserId().toString());
                jointMaterialMapper.save(jointMaterialDO);
            }
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(jointProductDO.getId());
        return serviceResult;
    }

    /**
     * 更新组合商品
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, Integer> updateJointProduct(JointProduct jointProduct) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult();
        JointProductDO jointProductDO = jointProductMapper.findDetailByJointProductId(jointProduct.getJointProductId());
        if (jointProductDO == null) {
            serviceResult.setErrorCode(ErrorCode.JOINT_PRODUCT_NOT_EXISTS);
            return serviceResult;
        }

        if (CollectionUtil.isEmpty(jointProduct.getJointProductSkuList()) && CollectionUtil.isEmpty(jointProduct.getJointMaterialList())) {
            serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return serviceResult;
        }
        Date now = new Date();
        // 这里是跟新 erp_joint_product_sku 表
        //这是传过来的值
        List<JointProductSku> jointProductSkuList = jointProduct.getJointProductSkuList();
        //数据库查到的
        List<JointProductSkuDO> jointProductSkuDOList = jointProductDO.getJointProductSkuDOList();
        //待删除列表
        Map<Integer, JointProductSkuDO> skuDeleteMap = ListUtil.listToMap(jointProductSkuDOList, "id");

        if (CollectionUtil.isNotEmpty(jointProductSkuList)) {
            //判断id是否相同
            //判断是否有重复id
            Set<Integer> skuIdSet = new HashSet<>();
            for (JointProductSku jointProductSku : jointProductSkuList) {
                skuIdSet.add(jointProductSku.getSkuId());
            }
            if(skuIdSet.size() > 0 ) {
                if (jointProductSkuList.size() > skuIdSet.size()) {
                    serviceResult.setErrorCode(ErrorCode.PRODUCT_SKU_CAN_NOT_REPEAT);
                    return serviceResult;
                }
            }

            for (JointProductSku jointProductSku : jointProductSkuList) {
                if (jointProductSku.getJointProductSkuId() == null) {
                    //新增
                    JointProductSkuDO jointProductSkuDO = new JointProductSkuDO();
                    jointProductSkuDO.setJointProductId(jointProductDO.getId());
                    jointProductSkuDO.setSkuId(jointProductSku.getSkuId());
                    jointProductSkuDO.setSkuCount(jointProductSku.getSkuCount());
                    jointProductSkuDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    jointProductSkuDO.setCreateTime(now);
                    jointProductSkuDO.setCreateUser(userSupport.getCurrentUserId().toString());
                    jointProductSkuDO.setUpdateTime(now);
                    jointProductSkuDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    jointProductSkuMapper.save(jointProductSkuDO);
                } else {
                    //更新
                    if (skuDeleteMap.get(jointProductSku.getJointProductSkuId()) == null) {
                        serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        return serviceResult;
                    }
                    JointProductSkuDO jointProductSkuDO = skuDeleteMap.get(jointProductSku.getJointProductSkuId());
                    jointProductSkuDO.setId(jointProductSku.getJointProductSkuId());
                    jointProductSkuDO.setJointProductId(jointProductDO.getId());
                    jointProductSkuDO.setSkuId(jointProductSku.getSkuId());
                    jointProductSkuDO.setSkuCount(jointProductSku.getSkuCount());
                    jointProductSkuDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    jointProductSkuDO.setUpdateTime(now);
                    jointProductSkuDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    jointProductSkuMapper.update(jointProductSkuDO);
                    skuDeleteMap.remove(jointProductSku.getJointProductSkuId());
                }
            }
        }

        //这是传过来的值
        List<JointMaterial> jointMaterialList = jointProduct.getJointMaterialList();
        //数据库值
        List<JointMaterialDO> jointMaterialDoList = jointProductDO.getJointMaterialDOList();
        //待删除的数据
        Map<Integer, JointMaterialDO> materialDeleteMap = ListUtil.listToMap(jointMaterialDoList, "id");
        if (CollectionUtil.isNotEmpty(jointMaterialList)) {
            //以下是处理 erp_joint_material 的数据
            //判断是否有重复id
            HashSet<Integer> materialIdSet = new HashSet<>();
            for (JointMaterial jointMaterial : jointMaterialList) {
                    materialIdSet.add(jointMaterial.getMaterialId());
            }
            if(materialIdSet.size() > 0 ){
                if (jointMaterialList.size() > materialIdSet.size()) {
                    serviceResult.setErrorCode(ErrorCode.MATERIAL_CAN_NOT_REPEAT);
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    return serviceResult;
                }
            }


            for (JointMaterial jointMaterial : jointMaterialList) {
                if (jointMaterial.getJointMaterialId() == null) {
                    //新增
                    MaterialDO materialDO = materialMapper.findByNo(jointMaterial.getMaterialNo());
                    JointMaterialDO jointMaterialDO = new JointMaterialDO();
                    jointMaterialDO.setJointProductId(jointProductDO.getId());
                    jointMaterialDO.setMaterialId(materialDO.getId());
                    jointMaterialDO.setMaterialNo(jointMaterial.getMaterialNo());
                    jointMaterialDO.setMaterialCount(jointMaterial.getMaterialCount());
                    jointMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    jointMaterialDO.setCreateTime(now);
                    jointMaterialDO.setCreateUser(userSupport.getCurrentUserId().toString());
                    jointMaterialDO.setUpdateTime(now);
                    jointMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    jointMaterialMapper.save(jointMaterialDO);
                } else {
                    //更新
                    if (materialDeleteMap.get(jointMaterial.getJointMaterialId()) == null) {
                        serviceResult.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        return serviceResult;
                    }
                    JointMaterialDO jointMaterialDO = materialDeleteMap.get(jointMaterial.getJointMaterialId());
                    jointMaterialDO.setId(jointMaterial.getJointMaterialId());
                    jointMaterialDO.setMaterialNo(jointMaterial.getMaterialNo());
                    jointMaterialDO.setMaterialCount(jointMaterial.getMaterialCount());
                    jointMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    jointMaterialDO.setUpdateTime(now);
                    jointMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    jointMaterialMapper.update(jointMaterialDO);
                    materialDeleteMap.remove(jointMaterial.getJointMaterialId());
                }
            }
        }
        if (skuDeleteMap.size() > 0) {
            //删除数据库剩下的
            for (Integer jointProductSkuId : skuDeleteMap.keySet()) {
                JointProductSkuDO jointProductSkuDO = skuDeleteMap.get(jointProductSkuId);
                jointProductSkuDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                jointProductSkuDO.setUpdateTime(new Date());
                jointProductSkuDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                jointProductSkuMapper.update(jointProductSkuDO);
            }
        }
        if (materialDeleteMap.size() > 0) {
            for (Integer jointMaterialSkuId : materialDeleteMap.keySet()) {
                JointMaterialDO jointMaterialDO = materialDeleteMap.get(jointMaterialSkuId);
                jointMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                jointMaterialDO.setUpdateTime(new Date());
                jointMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                jointMaterialMapper.update(jointMaterialDO);
            }
        }


        jointProductDO.setJointProductName(jointProduct.getJointProductName());
        jointProductDO.setRemark(jointProduct.getRemark());
        jointProductDO.setUpdateTime(now);
        jointProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        jointProductMapper.update(jointProductDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(jointProductDO.getId());
        return serviceResult;
    }

    /**
     * 删除组合商品
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, Integer> deleteJointProduct(JointProduct jointProduct) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        //首先查看数据库中是否有此id
        JointProductDO jointProductDO = jointProductMapper.findById(jointProduct.getJointProductId());
        if (jointProductDO == null) {
            serviceResult.setErrorCode(ErrorCode.JOINT_PRODUCT_NOT_EXISTS);
            return serviceResult;
        }

        Date now = new Date();
        jointProductDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        jointProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        jointProductDO.setUpdateTime(now);
        jointProductMapper.update(jointProductDO);

        JointProductSkuDO jointProductSkuDO = new JointProductSkuDO();
        jointProductSkuDO.setJointProductId(jointProductDO.getId());
        jointProductSkuDO.setUpdateUser(jointProductDO.getUpdateUser());
        jointProductSkuDO.setUpdateTime(jointProductDO.getUpdateTime());
        jointProductSkuDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        jointProductSkuMapper.deleteByJointProductId(jointProductSkuDO);

        JointMaterialDO jointMaterialDO = new JointMaterialDO();
        jointMaterialDO.setJointProductId(jointProductDO.getId());
        jointMaterialDO.setUpdateUser(jointProductDO.getUpdateUser());
        jointMaterialDO.setUpdateTime(jointProductDO.getUpdateTime());
        jointMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        jointMaterialMapper.deleteByJointProductId(jointMaterialDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(jointProductDO.getId());
        return serviceResult;
    }

    /**
     * 查询组合商品详情
     */
    @Override
    public ServiceResult<String, JointProduct> queryJointProductByJointProductId(Integer jointProductId) {
        ServiceResult<String, JointProduct> serviceResult = new ServiceResult<>();
        JointProductDO jointProductDO = jointProductMapper.findDetailByJointProductId(jointProductId);
        if (jointProductDO == null) {
            serviceResult.setErrorCode(ErrorCode.JOINT_PRODUCT_NOT_EXISTS);
            return serviceResult;
        }
        List<JointMaterialDO> jointMaterialDOList = jointProductDO.getJointMaterialDOList();
        if (CollectionUtil.isNotEmpty(jointMaterialDOList)) {
            for (JointMaterialDO jointMaterialDO : jointMaterialDOList) {
                MaterialDO materialDO = materialMapper.findById(jointMaterialDO.getMaterialId());
                jointMaterialDO.setMaterialDO(materialDO);
            }
        }
        JointProduct jointProduct = ConverterUtil.convert(jointProductDO, JointProduct.class);
        List<JointProductSku> jointProductSkuList = jointProduct.getJointProductSkuList();
        if (CollectionUtil.isNotEmpty(jointProductSkuList)) {
            for (JointProductSku jointProductSku : jointProductSkuList) {
                ServiceResult<String, Product> productResult = productService.queryProductBySkuId(jointProductSku.getSkuId());
                if (!ErrorCode.SUCCESS.equals(productResult.getErrorCode())) {
                    serviceResult.setErrorCode(productResult.getErrorCode());
                    return serviceResult;
                }
                jointProductSku.setProduct(productResult.getResult());
            }
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(jointProduct);
        return serviceResult;
    }

    /**
     * 分页查询组合商品
     */
    @Override
    public ServiceResult<String, Page<JointProduct>> pageJointProduct(JointProductQueryParam jointProductQueryParam) {

        ServiceResult<String, Page<JointProduct>> serviceResult = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(jointProductQueryParam.getPageNo(), jointProductQueryParam.getPageSize());
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("jointProductQueryParam", jointProductQueryParam);
        Integer jointProductCount = jointProductMapper.findJointProductCountByParam(maps);
        List<JointProductDO> jointProductDOList = jointProductMapper.findJointProductByParams(maps);
        List<JointProduct> jointProductList = ConverterUtil.convertList(jointProductDOList, JointProduct.class);
        for (JointProduct jointProduct : jointProductList) {
            List<JointMaterial> jointMaterialList = jointProduct.getJointMaterialList();
            for (JointMaterial jointMaterial : jointMaterialList) {
                MaterialDO materialDO = materialMapper.findById(jointMaterial.getMaterialId());
                jointMaterial.setMaterial(ConverterUtil.convert(materialDO, Material.class));
            }
            List<JointProductSku> jointProductSkuList = jointProduct.getJointProductSkuList();
            for (JointProductSku jointProductSku : jointProductSkuList) {
                ServiceResult<String, Product> productResult = productService.queryProductBySkuId(jointProductSku.getSkuId());
                if (!ErrorCode.SUCCESS.equals(productResult.getErrorCode())) {
                    serviceResult.setErrorCode(productResult.getErrorCode());
                    return serviceResult;
                }
                jointProductSku.setProduct(productResult.getResult());
            }
        }

        Page<JointProduct> page = new Page<>(jointProductList, jointProductCount, jointProductQueryParam.getPageNo(), jointProductQueryParam.getPageSize());
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(page);
        return serviceResult;
    }

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private JointProductMapper jointProductMapper;

    @Autowired
    private JointProductSkuMapper jointProductSkuMapper;

    @Autowired
    private JointMaterialMapper jointMaterialMapper;

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private ProductService productService;

}
