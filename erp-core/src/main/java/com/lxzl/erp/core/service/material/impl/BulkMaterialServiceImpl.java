package com.lxzl.erp.core.service.material.impl;

import com.lxzl.erp.common.constant.BulkMaterialStatus;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.BulkMaterialQueryParam;
import com.lxzl.erp.common.domain.material.pojo.BulkMaterial;
import com.lxzl.erp.core.service.material.BulkMaterialService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.material.BulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductEquipmentBulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductEquipmentMaterialMapper;
import com.lxzl.erp.dataaccess.domain.material.BulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentBulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentDO;
import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentMaterialDO;
import com.lxzl.se.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class BulkMaterialServiceImpl implements BulkMaterialService {

    @Autowired
    private BulkMaterialMapper bulkMaterialMapper;

    @Autowired
    private ProductEquipmentMaterialMapper productEquipmentMaterialMapper;

    @Autowired
    private ProductEquipmentBulkMaterialMapper productEquipmentBulkMaterialMapper;

    @Autowired
    private ProductEquipmentMapper productEquipmentMapper;

    @Autowired
    private UserSupport userSupport;


    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> dismantleBulkMaterial(BulkMaterial bulkMaterial) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();

        BulkMaterialDO bulkMaterialDO = bulkMaterialMapper.findById(bulkMaterial.getBulkMaterialId());

        //散料数据判断
        String resultBulkMaterial = judgeBulkMaterial(bulkMaterialDO);
        if(!ErrorCode.SUCCESS.equals(resultBulkMaterial)){
            serviceResult.setErrorCode(resultBulkMaterial);
            return serviceResult;
        }

        //该散料是否存在设备中
        if (bulkMaterialDO.getCurrentEquipmentId() == null){
            serviceResult.setErrorCode(ErrorCode.BULK_MATERIAL_IS_NOT_IN_PRODUCT_EQUIPMENT);
            return serviceResult;
        }

        //散料处于空闲并且有当前设备时，
        //获取商品设备表
        ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(bulkMaterialDO.getCurrentEquipmentNo());

        //设备数据判断
        String resultProductEquipment = judgeProductEquipment(productEquipmentDO,bulkMaterialDO);
        if(!ErrorCode.SUCCESS.equals(resultProductEquipment)){
            serviceResult.setErrorCode(resultProductEquipment);
            return serviceResult;
        }

        // 通过当前设备ID找到当前设备物料，通过物料ID查看当前设备的该物料是否还有数量，
        ProductEquipmentMaterialDO productEquipmentMaterialDO = productEquipmentMaterialMapper.findByEquipmentIdAndMaterialId(bulkMaterialDO.getCurrentEquipmentId(),bulkMaterialDO.getMaterialId());

        //设备物料数据判断
        String resultProductEquipmentMaterial = judgeProductEquipmentMaterial(productEquipmentMaterialDO);
        if(!ErrorCode.SUCCESS.equals(resultProductEquipmentMaterial)){
            serviceResult.setErrorCode(resultProductEquipmentMaterial);
            return serviceResult;
        }

        //同时设备散料将删除
        ProductEquipmentBulkMaterialDO productEquipmentBulkMaterialDO = productEquipmentBulkMaterialMapper.findByBulkMaterialId(bulkMaterialDO.getId());
        if (productEquipmentBulkMaterialDO == null){
            serviceResult.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_BULK_MATERIAL_NOT_EXISTS);
            return serviceResult;
        }

        //数量减少并且更新设备物料表
        productEquipmentMaterialDO.setMaterialCount(productEquipmentMaterialDO.getMaterialCount()-1);
        productEquipmentMaterialMapper.update(productEquipmentMaterialDO);

        //设备散料表数据将删除
        productEquipmentBulkMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        productEquipmentBulkMaterialMapper.update(productEquipmentBulkMaterialDO);

        //拆卸后更新散料的数据状态
        bulkMaterialDO.setCurrentEquipmentId(0);
        bulkMaterialDO.setCurrentEquipmentNo("");
        bulkMaterialMapper.update(bulkMaterialDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(bulkMaterialDO.getId());
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> installBulkMaterial(BulkMaterialQueryParam bulkMaterialQueryParam) {

        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        Date now = new Date();

        BulkMaterialDO bulkMaterialDO = bulkMaterialMapper.findById(bulkMaterialQueryParam.getBulkMaterialId());

        //散料数据判断
        String resultBulkMaterial = judgeBulkMaterial(bulkMaterialDO);
        if(!ErrorCode.SUCCESS.equals(resultBulkMaterial)){
            serviceResult.setErrorCode(resultBulkMaterial);
            return serviceResult;
        }

        //判断该散料是否已经插在设备上
        if (bulkMaterialDO.getCurrentEquipmentId() != null){
            serviceResult.setErrorCode(ErrorCode.BULK_MATERIAL_IS_IN_PRODUCT_EQUIPMENT);
            return serviceResult;
        }
        //散料处于空闲状态，同时没有在设备上
        //获取商品设备表
        ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(bulkMaterialQueryParam.getCurrentEquipmentNo());

        //设备数据判断
        String resultProductEquipment = judgeProductEquipment(productEquipmentDO,bulkMaterialDO);
        if(!ErrorCode.SUCCESS.equals(resultProductEquipment)){
            serviceResult.setErrorCode(resultProductEquipment);
            return serviceResult;
        }

        //获取设备物料表
        ProductEquipmentMaterialDO productEquipmentMaterialDO = productEquipmentMaterialMapper.findByEquipmentIdAndMaterialId(productEquipmentDO.getId(),bulkMaterialDO.getMaterialId());
        if (productEquipmentMaterialDO == null){
            serviceResult.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_MATERIAL_IS_NULL);
            return serviceResult;
        }

        productEquipmentMaterialDO.setMaterialCount(productEquipmentMaterialDO.getMaterialCount()+1);
        productEquipmentMaterialMapper.update(productEquipmentMaterialDO);

        //设置散料表的当前设备的ID和NO
        bulkMaterialDO.setCurrentEquipmentId(productEquipmentDO.getId());
        bulkMaterialDO.setCurrentEquipmentNo(bulkMaterialQueryParam.getCurrentEquipmentNo());
        bulkMaterialMapper.update(bulkMaterialDO);

        //获取设备散料表
        ProductEquipmentBulkMaterialDO productEquipmentBulkMaterialDO = new ProductEquipmentBulkMaterialDO();
        productEquipmentBulkMaterialDO.setEquipmentId(bulkMaterialDO.getCurrentEquipmentId());
        productEquipmentBulkMaterialDO.setEquipmentNo(bulkMaterialDO.getCurrentEquipmentNo());
        productEquipmentBulkMaterialDO.setBulkMaterialId(bulkMaterialDO.getId());
        productEquipmentBulkMaterialDO.setBulkMaterialNo(bulkMaterialDO.getBulkMaterialNo());
        productEquipmentBulkMaterialDO.setDataStatus(bulkMaterialDO.getDataStatus());
        productEquipmentBulkMaterialDO.setCreateTime(now);
        productEquipmentBulkMaterialDO.setUpdateTime(now);
        productEquipmentBulkMaterialDO.setCreateUser(userSupport.getCurrentUserId().toString());
        productEquipmentBulkMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        productEquipmentBulkMaterialMapper.save(productEquipmentBulkMaterialDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(bulkMaterialDO.getId());
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> changeProductDismantleAndInstall(Integer dismantleBulkMaterialId,Integer installBulkMaterialId) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        Date now = new Date();

        //先从换货设备上拆卸散料然后再安装需要的散料
        //拆卸,获取散料表
        BulkMaterialDO dismantlebulkMaterialDO = bulkMaterialMapper.findById(dismantleBulkMaterialId);
        if (dismantlebulkMaterialDO == null){
            serviceResult.setErrorCode(ErrorCode.BULK_MATERIAL_IS_NULL);
            return serviceResult;
        }

        //判断散料的状态是否租赁中
        if (!BulkMaterialStatus.BULK_MATERIAL_STATUS_BUSY.equals(dismantlebulkMaterialDO.getBulkMaterialStatus())){
            serviceResult.setErrorCode(ErrorCode.BULK_MATERIAL_IS_NOT_BUSY,dismantlebulkMaterialDO.getBulkMaterialNo());
            return serviceResult;
        }

        //该散料是否存在设备中
        if (dismantlebulkMaterialDO.getCurrentEquipmentId() == null){
            serviceResult.setErrorCode(ErrorCode.BULK_MATERIAL_IS_NOT_IN_PRODUCT_EQUIPMENT);
            return serviceResult;
        }

        //散料处于租赁中并且有当前设备时，
        // 通过当前设备ID找到当前设备物料，通过物料ID查看当前设备的该物料是否还有数量，
        ProductEquipmentMaterialDO dismantleproductEquipmentMaterialDO = productEquipmentMaterialMapper.findByEquipmentIdAndMaterialId(dismantlebulkMaterialDO.getCurrentEquipmentId(),dismantlebulkMaterialDO.getMaterialId());

        //设备物料数据判断
        String resultProductEquipmentMaterial = judgeProductEquipmentMaterial(dismantleproductEquipmentMaterialDO);
        if(!ErrorCode.SUCCESS.equals(resultProductEquipmentMaterial)){
            serviceResult.setErrorCode(resultProductEquipmentMaterial);
            return serviceResult;
        }

        //同时也将设备散料也将删除
        ProductEquipmentBulkMaterialDO dismantleProductEquipmentBulkMaterialDO = productEquipmentBulkMaterialMapper.findByBulkMaterialId(dismantlebulkMaterialDO.getId());
        if (dismantleProductEquipmentBulkMaterialDO == null){
            serviceResult.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_BULK_MATERIAL_NOT_EXISTS);
            return serviceResult;
        }

        //安装,获取散料表
        BulkMaterialDO installbulkMaterialDO = bulkMaterialMapper.findById(installBulkMaterialId);

        if (installbulkMaterialDO == null) {
            serviceResult.setErrorCode(ErrorCode.BULK_MATERIAL_IS_NULL);
            return serviceResult;
        }

        //首先判断状态
        if (!BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE.equals(installbulkMaterialDO.getBulkMaterialStatus()) ||
                !(BulkMaterialStatus.BULK_MATERIAL_STATUS_BUSY.equals(installbulkMaterialDO.getBulkMaterialStatus()) && StringUtil.isEmpty(installbulkMaterialDO.getOrderNo()))) {
            serviceResult.setErrorCode(ErrorCode.BULK_MATERIAL_IS_NOT_IDLE, installbulkMaterialDO.getBulkMaterialNo());
            return serviceResult;
        }

        //散料处于空闲状态，判断该散料是否已经插在其他设备上
        if (installbulkMaterialDO.getCurrentEquipmentId() != null){
            serviceResult.setErrorCode(ErrorCode.BULK_MATERIAL_IS_IN_PRODUCT_EQUIPMENT);
            return serviceResult;
        }

        //判断安装的散料仓库和拆卸的散料仓库是否相等
        if (!installbulkMaterialDO.getCurrentWarehouseId().equals(dismantlebulkMaterialDO.getCurrentWarehouseId())){
            serviceResult.setErrorCode(ErrorCode.INSTALL_AND_DISMANTLE_WAREHOUSE_NOT_EQUAL);
            return serviceResult;
        }

        //获取设备物料表
        ProductEquipmentMaterialDO installproductEquipmentMaterialDO = productEquipmentMaterialMapper.findByEquipmentIdAndMaterialId(dismantlebulkMaterialDO.getCurrentEquipmentId(),installbulkMaterialDO.getMaterialId());
        if (installproductEquipmentMaterialDO == null){
            serviceResult.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_MATERIAL_IS_NULL);
            return serviceResult;
        }

        //拆卸的设备物料表，数量减少并且更新设备物料表
        dismantleproductEquipmentMaterialDO.setMaterialCount(dismantleproductEquipmentMaterialDO.getMaterialCount()-1);
        productEquipmentMaterialMapper.update(dismantleproductEquipmentMaterialDO);

        //删除设备散料表中该条数据
        dismantleProductEquipmentBulkMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        productEquipmentBulkMaterialMapper.update(dismantleProductEquipmentBulkMaterialDO);

        //获取安装散料的当前设备的ID，NO和订单号,改变散料状态
        installbulkMaterialDO.setCurrentEquipmentId(dismantlebulkMaterialDO.getCurrentEquipmentId());
        installbulkMaterialDO.setCurrentEquipmentNo(dismantlebulkMaterialDO.getCurrentEquipmentNo());
        installbulkMaterialDO.setOrderNo(dismantlebulkMaterialDO.getOrderNo());
        installbulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_BUSY);
        bulkMaterialMapper.update(installbulkMaterialDO);

        //清除当前拆卸散料的设备ID,NO和订单号，改变散料状态
        dismantlebulkMaterialDO.setOrderNo("");
        dismantlebulkMaterialDO.setCurrentEquipmentId(0);
        dismantlebulkMaterialDO.setCurrentEquipmentNo("");
        dismantlebulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE);
        bulkMaterialMapper.update(dismantlebulkMaterialDO);

        //获取设备散料表
        ProductEquipmentBulkMaterialDO installproductEquipmentBulkMaterialDO = new ProductEquipmentBulkMaterialDO();
        installproductEquipmentBulkMaterialDO.setEquipmentId(installbulkMaterialDO.getCurrentEquipmentId());
        installproductEquipmentBulkMaterialDO.setEquipmentNo(installbulkMaterialDO.getCurrentEquipmentNo());
        installproductEquipmentBulkMaterialDO.setBulkMaterialId(installbulkMaterialDO.getId());
        installproductEquipmentBulkMaterialDO.setBulkMaterialNo(installbulkMaterialDO.getBulkMaterialNo());
        installproductEquipmentBulkMaterialDO.setDataStatus(installbulkMaterialDO.getDataStatus());
        installproductEquipmentBulkMaterialDO.setCreateTime(now);
        installproductEquipmentBulkMaterialDO.setUpdateTime(now);
        installproductEquipmentBulkMaterialDO.setCreateUser(userSupport.getCurrentUserId().toString());
        installproductEquipmentBulkMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        productEquipmentBulkMaterialMapper.save(installproductEquipmentBulkMaterialDO);

        //安装的散料，设备物料表数据增加
        installproductEquipmentMaterialDO.setMaterialCount(installproductEquipmentMaterialDO.getMaterialCount()+1);
        productEquipmentMaterialMapper.update(installproductEquipmentMaterialDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }



    private String judgeBulkMaterial(BulkMaterialDO bulkMaterialDO){
        if (bulkMaterialDO == null){
            return String.format(ErrorCode.BULK_MATERIAL_IS_NULL);
        }

        //首先判断状态
        if (!BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE.equals(bulkMaterialDO.getBulkMaterialStatus())){
            return String.format(ErrorCode.BULK_MATERIAL_IS_NOT_IDLE,bulkMaterialDO.getBulkMaterialNo());
        }

        return ErrorCode.SUCCESS;
    }


  private String judgeProductEquipment(ProductEquipmentDO productEquipmentDO, BulkMaterialDO bulkMaterialDO){
      if (productEquipmentDO == null){
          return String.format(ErrorCode.PRODUCT_EQUIPMENT_IS_NULL);
        }

        //判断设备的状态
        if (!BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE.equals(productEquipmentDO.getEquipmentStatus())){
            return String.format(ErrorCode.PRODUCT_EQUIPMENT_IS_NOT_IDLE,productEquipmentDO.getEquipmentNo());
        }

        //判断该散料是否与设备在同一个仓库
        if (!productEquipmentDO.getCurrentWarehouseId().equals(bulkMaterialDO.getCurrentWarehouseId())){
            return ErrorCode.PRODUCT_EQUIPMENT_AND_BULK_MATERIAL_NOT_WAREHOUSE;
        }
        return ErrorCode.SUCCESS;
    }

    private String judgeProductEquipmentMaterial(ProductEquipmentMaterialDO productEquipmentMaterialDO){
        if (productEquipmentMaterialDO == null){
            return String.format(ErrorCode.PRODUCT_EQUIPMENT_MATERIAL_IS_NULL);
        }

        //通过物料ID查看当前设备的该物料是否还有数量，如果有的话就将设备的物料减少，
        if (productEquipmentMaterialDO.getMaterialCount() <= 0){
            return ErrorCode.PRODUCT_EQUIPMENT_MATERIAL_COUNT_NOT_ENOUGH;
        }

        return ErrorCode.SUCCESS;
    }
}
