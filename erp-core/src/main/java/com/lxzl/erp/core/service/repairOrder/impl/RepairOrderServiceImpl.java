package com.lxzl.erp.core.service.repairOrder.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.RepairOrderStatus;
import com.lxzl.erp.common.constant.WorkflowType;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.pojo.BulkMaterial;
import com.lxzl.erp.common.domain.product.pojo.ProductEquipment;
import com.lxzl.erp.common.domain.repairOrder.RepairOrderBulkMaterialQueryParam;
import com.lxzl.erp.common.domain.repairOrder.RepairOrderEquipmentQueryParam;
import com.lxzl.erp.common.domain.repairOrder.RepairOrderQueryParam;
import com.lxzl.erp.common.domain.repairOrder.pojo.RepairOrder;
import com.lxzl.erp.common.domain.repairOrder.pojo.RepairOrderBulkMaterial;
import com.lxzl.erp.common.domain.repairOrder.pojo.RepairOrderEquipment;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.common.util.GenerateNoUtil;
import com.lxzl.erp.common.util.ListUtil;
import com.lxzl.erp.core.service.repairOrder.RepairOrderService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.warehouse.impl.support.WarehouseSupport;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.material.BulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMaterialBulkMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.repairOrder.RepairOrderBulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.repairOrder.RepairOrderEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.repairOrder.RepairOrderMapper;
import com.lxzl.erp.dataaccess.domain.material.BulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.erp.dataaccess.domain.order.OrderMaterialBulkDO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductEquipmentDO;
import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentDO;
import com.lxzl.erp.dataaccess.domain.repairOrder.RepairOrderBulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.repairOrder.RepairOrderDO;
import com.lxzl.erp.dataaccess.domain.repairOrder.RepairOrderEquipmentDO;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 15:45 2017/12/14
 * @Modified By:
 */

@Service
public class RepairOrderServiceImpl implements RepairOrderService {

    private static Logger logger = LoggerFactory.getLogger(RepairOrderServiceImpl.class);

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> addRepairOrder(RepairOrder repairOrder) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();

        //先判断是否有设备或者散料需要维修
        if (CollectionUtil.isEmpty(repairOrder.getRepairOrderEquipmentList())
                && CollectionUtil.isEmpty(repairOrder.getRepairOrderBulkMaterialList())) {
            serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return serviceResult;
        }

        String repairOrderNo = GenerateNoUtil.generateRepairOrderNo(now); //设备维修单编号
        Integer equipmentCount = 0;
        Integer bulkMaterialCount = 0;
        String warehouseNo = "";

        //判断所有维修设备是否是同一仓库
        Map<Integer,String> warehouseMap = new HashMap<>();
        String wareHouseResult = VerifyWarehouse(repairOrder.getRepairOrderEquipmentList(),repairOrder.getRepairOrderBulkMaterialList(),warehouseMap);
        if (!ErrorCode.SUCCESS.equals(wareHouseResult)){
            serviceResult.setErrorCode(wareHouseResult);
            return serviceResult;
        }
        if (warehouseMap.size() != 1){
            serviceResult.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_AND_BULK_MATERIAL_NOT_WAREHOUSE);
            return serviceResult;
        }

        for (Map.Entry<Integer, String> entry:warehouseMap.entrySet()){
            warehouseNo = entry.getValue();
        }

        //如果存在设备需要维修
        if (CollectionUtil.isNotEmpty(repairOrder.getRepairOrderEquipmentList())) {
            List<RepairOrderEquipmentDO> repairOrderEquipmentDOList = ConverterUtil.convertList(repairOrder.getRepairOrderEquipmentList(),RepairOrderEquipmentDO.class);
            String saveresult = saveRepairOrderEquipmentInfo(repairOrderEquipmentDOList, repairOrderNo, userSupport.getCurrentUser(), now);
            if (ErrorCode.BULK_MATERIAL_IS_NULL.equals(saveresult)){
                serviceResult.setErrorCode(saveresult);
                return serviceResult;
            }
            equipmentCount = repairOrderEquipmentDOList.size();//送修设备数量
        }

        //如果存在散料需要维修
        if (CollectionUtil.isNotEmpty(repairOrder.getRepairOrderBulkMaterialList())) {
           List<RepairOrderBulkMaterialDO> repairOrderBulkMaterialDO = ConverterUtil.convertList(repairOrder.getRepairOrderBulkMaterialList(),RepairOrderBulkMaterialDO.class);
            serviceResult = saveRepairOrderBulkMaterialInfo(repairOrderBulkMaterialDO,repairOrderNo,userSupport.getCurrentUser(),now,bulkMaterialCount);
            if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())){
                serviceResult.setErrorCode(serviceResult.getErrorCode(),serviceResult.getFormatArgs());
                return serviceResult;
            }
            bulkMaterialCount = repairOrder.getRepairOrderBulkMaterialList().size();//送修物料数量
        }

        //然后生成维修单
        RepairOrderDO repairOrderDO = new RepairOrderDO();
        repairOrderDO.setRepairOrderNo(repairOrderNo);
        repairOrderDO.setRepairReason(repairOrder.getRepairReason());
        repairOrderDO.setRepairEquipmentCount(equipmentCount);
        repairOrderDO.setRepairBulkMaterialCount(bulkMaterialCount);
        repairOrderDO.setWarehouseNo(warehouseNo);
        repairOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        repairOrderDO.setRemark(repairOrder.getRemark());
        repairOrderDO.setCreateTime(now);
        repairOrderDO.setUpdateTime(now);
        repairOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        repairOrderDO.setCreateUser(userSupport.getCurrentUserId().toString());
        repairOrderMapper.save(repairOrderDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(repairOrderDO.getRepairOrderNo());
        return serviceResult;
    }

    private String VerifyWarehouse(List<RepairOrderEquipment> repairOrderEquipmentList,List<RepairOrderBulkMaterial> repairOrderBulkMaterialList,Map<Integer,String> warehouseMap) {

        if (CollectionUtil.isNotEmpty(repairOrderEquipmentList)){
            for (RepairOrderEquipment repairOrderEquipment:repairOrderEquipmentList){
                ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(repairOrderEquipment.getEquipmentNo());
                if (productEquipmentDO == null) {
                    return String.format(ErrorCode.PRODUCT_EQUIPMENT_NOT_EXISTS,repairOrderEquipment.getEquipmentNo());
                }
                String warehouseNo = warehouseSupport.getAvailableWarehouse(productEquipmentDO.getCurrentWarehouseId()).getWarehouseNo();
                warehouseMap.put(productEquipmentDO.getCurrentWarehouseId(),warehouseNo);
            }
        }

        if (CollectionUtil.isNotEmpty(repairOrderBulkMaterialList)){
            for (RepairOrderBulkMaterial repairOrderBulkMaterial:repairOrderBulkMaterialList){
                BulkMaterialDO bulkMaterialDO = bulkMaterialMapper.findByNo(repairOrderBulkMaterial.getBulkMaterialNo());
                if (bulkMaterialDO == null) {
                    return String.format(ErrorCode.BULK_MATERIAL_NOT_EXISTS,repairOrderBulkMaterial.getBulkMaterialNo());
                }
                String warehouseNo = warehouseSupport.getAvailableWarehouse(bulkMaterialDO.getCurrentWarehouseId()).getWarehouseNo();
                warehouseMap.put(bulkMaterialDO.getCurrentWarehouseId(),warehouseNo);
            }
        }
        return ErrorCode.SUCCESS;
    }

    @Override
    public ServiceResult<String, String> commitRepairOrder(String repairOrderNo, Integer verifyUser, String commitRemark) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();

        RepairOrderDO repairOrderDO = repairOrderMapper.findByRepairOrderNo(repairOrderNo);
        if (repairOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.REPAIR_ORDER_IS_NOT_EXISTS);
            return serviceResult;
        }

        //只有初始化维修单才能进行审核
        if (!RepairOrderStatus.REPAIR_ORDER_STATUS_INIT.equals(repairOrderDO.getRepairOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.REPAIR_ORDER_STATUS_ERROR);
            return serviceResult;
        }

        if(!repairOrderDO.getCreateUser().equals(userSupport.getCurrentUserId().toString())){
            //只有创建采购单本人可以提交
            serviceResult.setErrorCode(ErrorCode.COMMIT_ONLY_SELF);
            return serviceResult;
        }

        //提交审核判断
        ServiceResult<String, Boolean>  needVerifyResult = workflowService.isNeedVerify(WorkflowType.WORKFLOW_TYPE_REPAIR);
        if (!ErrorCode.SUCCESS.equals(needVerifyResult.getErrorCode())){
            serviceResult.setErrorCode(needVerifyResult.getErrorCode());
            return serviceResult;
        }else if(needVerifyResult.getResult()){
            if (verifyUser == null){
                serviceResult.setErrorCode(ErrorCode.VERIFY_USER_NOT_NULL);
                return serviceResult;
            }

            //调用提交审核服务
            ServiceResult<String, String>  verifyResult = workflowService.commitWorkFlow(WorkflowType.WORKFLOW_TYPE_REPAIR, repairOrderDO.getRepairOrderNo(),verifyUser,commitRemark);
            //修改提交审核状态
            if (ErrorCode.SUCCESS.equals(verifyResult.getErrorCode())){
                repairOrderDO.setRepairOrderStatus(RepairOrderStatus.REPAIR_ORDER_STATUS_VERIFYING);
                repairOrderDO.setUpdateTime(new Date());
                repairOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                repairOrderMapper.update(repairOrderDO);
                return verifyResult;
            }else{
                serviceResult.setErrorCode(verifyResult.getErrorCode());
                return serviceResult;
            }
        }else{
            repairOrderDO.setRepairOrderStatus(RepairOrderStatus.REPAIR_ORDER_STATUS_VERIFYING);
            repairOrderDO.setUpdateTime(new Date());
            repairOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            repairOrderMapper.update(repairOrderDO);

            serviceResult.setErrorCode(ErrorCode.SUCCESS);
            serviceResult.setResult(repairOrderDO.getRepairOrderNo());
            return serviceResult;
        }
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean receiveVerifyResult(boolean verifyResult, String repairOrderNo) {
        try {
            RepairOrderDO repairOrderDO = repairOrderMapper.findByRepairOrderNo(repairOrderNo);
            if (repairOrderDO == null || !RepairOrderStatus.REPAIR_ORDER_STATUS_VERIFYING.equals(repairOrderDO.getRepairOrderStatus())) {
                return false;
            }
            if (verifyResult) {
                //审核通过
                repairOrderDO.setRepairOrderStatus(RepairOrderStatus.REPAIR_ORDER_STATUS_WAIT_REPAIR);
            } else {
                repairOrderDO.setRepairOrderStatus(RepairOrderStatus.REPAIR_ORDER_STATUS_INIT);
            }
            repairOrderDO.setUpdateTime(new Date());
            repairOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            repairOrderMapper.update(repairOrderDO);
            return true;
        } catch (Exception e) {
            logger.error("审批设备维修单通知失败： {}", repairOrderNo);
            return false;
        }
    }

    @Override
    public ServiceResult<String, String> receiveRepairOrder(String repairOrderNo) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();

        RepairOrderDO repairOrderDO = repairOrderMapper.findByRepairOrderNo(repairOrderNo);
        if (repairOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.REPAIR_ORDER_IS_NOT_EXISTS);
            return serviceResult;
        }

        //只有状态是待维修的维修单才能进行维修
        if (!RepairOrderStatus.REPAIR_ORDER_STATUS_WAIT_REPAIR.equals(repairOrderDO.getRepairOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.REPAIR_ORDER_STATUS_ERROR);
            return serviceResult;
        }

        repairOrderDO.setRepairOrderStatus(RepairOrderStatus.REPAIR_ORDER_STATUS_REPAIRING);
        repairOrderDO.setUpdateTime(new Date());
        repairOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        repairOrderMapper.update(repairOrderDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(repairOrderDO.getRepairOrderNo());
        return serviceResult;
    }

    @Override

    public ServiceResult<String, String> cancelRepairOrder(String repairOrderNo) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        RepairOrderDO repairOrderDO = repairOrderMapper.findByRepairOrderNo(repairOrderNo);
        if (repairOrderDO.getRepairOrderStatus() == null || !RepairOrderStatus.REPAIR_ORDER_STATUS_INIT.equals(repairOrderDO.getRepairOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.REPAIR_ORDER_STATUS_ERROR);
            return serviceResult;
        }
        repairOrderDO.setRepairOrderStatus(RepairOrderStatus.REPAIR_ORDER_STATUS_CANCEL);
        repairOrderDO.setUpdateTime(new Date());
        repairOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        repairOrderMapper.update(repairOrderDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(repairOrderDO.getRepairOrderNo());
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> updateRepairOrder(RepairOrder repairOrder) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();

        //先判断是否有设备或者散料需要维修
        if (CollectionUtil.isEmpty(repairOrder.getRepairOrderEquipmentList())
                && CollectionUtil.isEmpty(repairOrder.getRepairOrderBulkMaterialList())) {
            serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return serviceResult;
        }

        RepairOrderDO dbrepairOrderDO = repairOrderMapper.findByRepairOrderNo(repairOrder.getRepairOrderNo());
        if (dbrepairOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.REPAIR_ORDER_IS_NOT_EXISTS);
            return serviceResult;
        }

        //判断修改的订单的状态是否是初始化
        if (!RepairOrderStatus.REPAIR_ORDER_STATUS_INIT.equals(dbrepairOrderDO.getRepairOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.REPAIR_ORDER_STATUS_ERROR);
            return serviceResult;
        }

        Integer equipmentCount = 0;
        Integer bulkMaterialCount = 0;
        String warehouseNo = "";

        //判断所有维修设备是否是同一仓库
        Map<Integer,String> warehouseMap = new HashMap<>();
        String wareHouseResult = VerifyWarehouse(repairOrder.getRepairOrderEquipmentList(),repairOrder.getRepairOrderBulkMaterialList(),warehouseMap);
        if (!ErrorCode.SUCCESS.equals(wareHouseResult)){
            serviceResult.setErrorCode(wareHouseResult);
            return serviceResult;
        }
        if (warehouseMap.size() != 1){
            serviceResult.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_AND_BULK_MATERIAL_NOT_WAREHOUSE);
            return serviceResult;
        }

        for (Map.Entry<Integer, String> entry:warehouseMap.entrySet()){
            warehouseNo = entry.getValue();
        }


        //判断设备维修单明细
        //如果传入的设备维修单明细没有值
        if (CollectionUtil.isEmpty(repairOrder.getRepairOrderEquipmentList())) {
            List<RepairOrderEquipmentDO> repairOrderEquipmentDOList = repairOrderEquipmentMapper.findByRepairOrderNo(repairOrder.getRepairOrderNo());
            //并且原设备维修单明细有值
            if (CollectionUtil.isNotEmpty(repairOrderEquipmentDOList)) {
                //清空原设备维修单明细
                repairOrderEquipmentMapper.clearDateStatus(repairOrder.getRepairOrderNo());
                equipmentCount = 0;
            }
        }else{
            //如果传入的设备维修单明细有值
            List<RepairOrderEquipmentDO> repairOrderEquipmentDOList = ConverterUtil.convertList(repairOrder.getRepairOrderEquipmentList(),RepairOrderEquipmentDO.class);
            String saveresult = saveRepairOrderEquipmentInfo(repairOrderEquipmentDOList, dbrepairOrderDO.getRepairOrderNo(), userSupport.getCurrentUser(), now);
            if (ErrorCode.BULK_MATERIAL_IS_NULL.equals(saveresult)){
                serviceResult.setErrorCode(saveresult);
                return serviceResult;
            }
            equipmentCount = Integer.parseInt(saveresult);
        }

        //判断散料维修单明细
        //如果传入的散料维修单明细没有值
        if (CollectionUtil.isEmpty(repairOrder.getRepairOrderBulkMaterialList())) {
            List<RepairOrderBulkMaterialDO> repairOrderBulkMaterialDOList = repairOrderBulkMaterialMapper.findByRepairOrderNo(repairOrder.getRepairOrderNo());
            //并且原设备维修单明细有值
            if (CollectionUtil.isNotEmpty(repairOrderBulkMaterialDOList)) {
                //清空原设备维修单明细
                repairOrderBulkMaterialMapper.clearDateStatus(repairOrder.getRepairOrderNo());
                bulkMaterialCount = 0 ;
            }
        }else{
            //如果传入的设备维修单明细有值
            List<RepairOrderBulkMaterialDO> repairOrderBulkMaterialDOList = ConverterUtil.convertList(repairOrder.getRepairOrderBulkMaterialList(),RepairOrderBulkMaterialDO.class);
            serviceResult = saveRepairOrderBulkMaterialInfo(repairOrderBulkMaterialDOList, dbrepairOrderDO.getRepairOrderNo(), userSupport.getCurrentUser(), now,bulkMaterialCount);
            if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())){
                serviceResult.setErrorCode(serviceResult.getErrorCode(),serviceResult.getFormatArgs());
                return serviceResult;
            }
            bulkMaterialCount = Integer.valueOf(serviceResult.getResult());
        }

        dbrepairOrderDO.setRepairEquipmentCount(equipmentCount);
        dbrepairOrderDO.setRepairBulkMaterialCount(bulkMaterialCount);
        dbrepairOrderDO.setRepairReason(repairOrder.getRepairReason());
        dbrepairOrderDO.setWarehouseNo(warehouseNo);
        dbrepairOrderDO.setUpdateTime(now);
        dbrepairOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        repairOrderMapper.update(dbrepairOrderDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(dbrepairOrderDO.getRepairOrderNo());
        return serviceResult;
    }

    @Override
    public ServiceResult<String, RepairOrder> queryRepairOrderByNo(String repairOrderNo) {
        ServiceResult<String, RepairOrder> serviceResult = new ServiceResult<>();
        RepairOrderDO repairOrderDO = repairOrderMapper.findByRepairOrderNo(repairOrderNo);
        if (repairOrderDO == null){
            serviceResult.setErrorCode(ErrorCode.REPAIR_ORDER_IS_NOT_EXISTS);
            return serviceResult;
        }

        RepairOrder repairOrder = ConverterUtil.convert(repairOrderDO,RepairOrder.class);

        List<RepairOrderEquipmentDO> repairOrderEquipmentDOList = repairOrderEquipmentMapper.findByRepairOrderNo(repairOrderNo);
        for (RepairOrderEquipmentDO repairOrderEquipmentDO:repairOrderEquipmentDOList){
            ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(repairOrderEquipmentDO.getEquipmentNo());
            repairOrderEquipmentDO.setProductEquipmentDO(productEquipmentDO);
        }

        List<RepairOrderBulkMaterialDO> repairOrderBulkMaterialDOList = repairOrderBulkMaterialMapper.findByRepairOrderNo(repairOrderNo);
        for (RepairOrderBulkMaterialDO repairOrderBulkMaterialDO : repairOrderBulkMaterialDOList){
            BulkMaterialDO bulkMaterialDO = bulkMaterialMapper.findByNo(repairOrderBulkMaterialDO.getBulkMaterialNo());
            repairOrderBulkMaterialDO.setBulkMaterialDO(bulkMaterialDO);
        }

        List<RepairOrderEquipment> repairOrderEquipmentList = ConverterUtil.convertList(repairOrderEquipmentDOList,RepairOrderEquipment.class);
        List<RepairOrderBulkMaterial> repairOrderBulkMaterialList = ConverterUtil.convertList(repairOrderBulkMaterialDOList,RepairOrderBulkMaterial.class);


        repairOrder.setRepairOrderEquipmentList(repairOrderEquipmentList);
        repairOrder.setRepairOrderBulkMaterialList(repairOrderBulkMaterialList);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(repairOrder);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Page<RepairOrder>> pageRepairOrder(RepairOrderQueryParam repairOrderQueryParam) {
        ServiceResult<String, Page<RepairOrder>> serviceResult = new ServiceResult<>();

        PageQuery pageQuery = new PageQuery(repairOrderQueryParam.getPageNo(), repairOrderQueryParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("queryParam", repairOrderQueryParam);

        Integer totalCount = repairOrderMapper.findRepairOrderCountByParams(maps);
        List<RepairOrderDO> RepairOrderDOList = repairOrderMapper.findRepairOrderByParams(maps);
        List<RepairOrder> repairOrderList = ConverterUtil.convertList(RepairOrderDOList,RepairOrder.class);
        Page<RepairOrder> page = new Page<>(repairOrderList, totalCount, repairOrderQueryParam.getPageNo(), repairOrderQueryParam.getPageSize());

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(page);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Page<RepairOrderEquipment>> pageRepairEquipment(RepairOrderEquipmentQueryParam repairOrderEquipmentQueryParam) {
        ServiceResult<String, Page<RepairOrderEquipment>> serviceResult = new ServiceResult<>();

        PageQuery pageQuery = new PageQuery(repairOrderEquipmentQueryParam.getPageNo(), repairOrderEquipmentQueryParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("queryParam", repairOrderEquipmentQueryParam);

        Integer totalCount = repairOrderEquipmentMapper.findRepairOrderEquipmentCountByParams(maps);
        List<RepairOrderEquipmentDO> repairOrderEquipmentDOList = repairOrderEquipmentMapper.findRepairOrderEquipmentByParams(maps);
        List<RepairOrderEquipment> repairOrderEquipmentList = ConverterUtil.convertList(repairOrderEquipmentDOList,RepairOrderEquipment.class);
        Page<RepairOrderEquipment> page = new Page<>(repairOrderEquipmentList, totalCount, repairOrderEquipmentQueryParam.getPageNo(), repairOrderEquipmentQueryParam.getPageSize());

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(page);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Page<RepairOrderBulkMaterial>> pageRepairBulkMaterial(RepairOrderBulkMaterialQueryParam repairOrderBulkMaterialQueryParam) {
        ServiceResult<String, Page<RepairOrderBulkMaterial>> serviceResult = new ServiceResult<>();

        PageQuery pageQuery = new PageQuery(repairOrderBulkMaterialQueryParam.getPageNo(), repairOrderBulkMaterialQueryParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("queryParam", repairOrderBulkMaterialQueryParam);

        Integer totalCount =  repairOrderBulkMaterialMapper.findRepairOrderBulkMaterialCountByParams(maps);
        List<RepairOrderBulkMaterialDO> repairOrderBulkMaterialDOList =  repairOrderBulkMaterialMapper.findRepairOrderBulkMaterialByParams(maps);
        List<RepairOrderBulkMaterial> repairOrderBulkMaterialList = ConverterUtil.convertList(repairOrderBulkMaterialDOList,RepairOrderBulkMaterial.class);
        Page<RepairOrderBulkMaterial> page = new Page<>(repairOrderBulkMaterialList, totalCount, repairOrderBulkMaterialQueryParam.getPageNo(), repairOrderBulkMaterialQueryParam.getPageSize());

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(page);
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> fix(List<Integer> repairEquipmentIdList, List<Integer> repairBulkMaterialIdList) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        Date now = new Date();

        if (CollectionUtil.isEmpty(repairEquipmentIdList) && CollectionUtil.isEmpty(repairBulkMaterialIdList)){
            serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return serviceResult;
        }

        //如果设备维修单明细不为空
        if (CollectionUtil.isNotEmpty(repairEquipmentIdList)){
            for (Integer repairOrderEquipmentId:repairEquipmentIdList){
                RepairOrderEquipmentDO repairOrderEquipmentDO = repairOrderEquipmentMapper.findById(repairOrderEquipmentId);
                if (repairOrderEquipmentDO == null){
                    serviceResult.setErrorCode(ErrorCode.REPAIR_ORDER_EQUIPMENT_NOT_EXISTS,repairOrderEquipmentId);
                    return serviceResult;
                }
                //如果传入的设备维修单明细的维修完成时间已经有值,就结束本次循环
                if(repairOrderEquipmentDO.getRepairEndTime() != null){
                    continue;
                }
                // 判断该设备维修单明细的维修单是否还是维修中的状态
                RepairOrderDO repairOrderDO = repairOrderMapper.findByRepairOrderNo(repairOrderEquipmentDO.getRepairOrderNo());
                if (!RepairOrderStatus.REPAIR_ORDER_STATUS_REPAIRING.equals(repairOrderDO.getRepairOrderStatus())){
                    serviceResult.setErrorCode(ErrorCode.REPAIR_ORDER_DATA_STATUS_ERROR);
                    return serviceResult;
                }
                //保存更改后的数据
                repairOrderEquipmentDO.setRepairEndTime(now);
                repairOrderEquipmentDO.setUpdateTime(now);
                repairOrderEquipmentDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                repairOrderEquipmentMapper.update(repairOrderEquipmentDO);

                //在设备维修单中增加 修复的设备数量
                repairOrderDO.setFixEquipmentCount(repairOrderDO.getFixEquipmentCount() + 1);
                repairOrderDO.setUpdateTime(now);
                repairOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                repairOrderMapper.update(repairOrderDO);
            }
       }

        //如果散料维修单明细不为空
        if (CollectionUtil.isNotEmpty(repairBulkMaterialIdList)){
            for (Integer repairOrderBulkMaterialId:repairBulkMaterialIdList){
                RepairOrderBulkMaterialDO repairOrderBulkMaterialDO = repairOrderBulkMaterialMapper.findById(repairOrderBulkMaterialId);
                if (repairOrderBulkMaterialDO == null){
                    serviceResult.setErrorCode(ErrorCode.REPAIR_ORDER_BULK_MATERRIAL_NOT_EXISTS,repairOrderBulkMaterialId);
                    return serviceResult;
                }

                //如果传入的设备维修单明细的维修完成时间已经有值,就跳出循环
                if(repairOrderBulkMaterialDO.getRepairEndTime() != null){
                    continue;
                }

                //判断该设散料维修单的维修单是否还是维修中的状态
                RepairOrderDO repairOrderDO = repairOrderMapper.findByRepairOrderNo(repairOrderBulkMaterialDO.getRepairOrderNo());
                if (!RepairOrderStatus.REPAIR_ORDER_STATUS_REPAIRING.equals(repairOrderDO.getRepairOrderStatus())){
                    serviceResult.setErrorCode(ErrorCode.REPAIR_ORDER_DATA_STATUS_ERROR);
                    return serviceResult;
                }
                //保存更改后的数据
                repairOrderBulkMaterialDO.setRepairEndTime(now);
                repairOrderBulkMaterialDO.setUpdateTime(now);
                repairOrderBulkMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                repairOrderBulkMaterialMapper.update(repairOrderBulkMaterialDO);

                //在设备维修单中增加 修复的物料数量
                repairOrderDO.setFixBulkMaterialCount(repairOrderDO.getFixBulkMaterialCount() + 1 );
                repairOrderDO.setUpdateTime(now);
                repairOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                repairOrderMapper.update(repairOrderDO);
            }
        }

       serviceResult.setErrorCode(ErrorCode.SUCCESS);
       return serviceResult;
    }

    @Override
    public ServiceResult<String, String> end(String repairOrderNo) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();

        RepairOrderDO repairOrderDO = repairOrderMapper.findByRepairOrderNo(repairOrderNo);
        if (repairOrderDO == null){
            serviceResult.setErrorCode(ErrorCode.REPAIR_ORDER_IS_NOT_EXISTS);
            return serviceResult;
        }

        if (!RepairOrderStatus.REPAIR_ORDER_STATUS_REPAIRING.equals(repairOrderDO.getRepairOrderStatus())){
            serviceResult.setErrorCode(ErrorCode.REPAIR_ORDER_DATA_STATUS_ERROR);
            return serviceResult;
        }

        repairOrderDO.setRepairOrderStatus(RepairOrderStatus.REPAIR_ORDER_STATUS_REPAIRED);
        repairOrderDO.setUpdateTime(now);
        repairOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        repairOrderMapper.update(repairOrderDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(repairOrderDO.getRepairOrderNo());
        return serviceResult;
    }



    private String saveRepairOrderEquipmentInfo(List<RepairOrderEquipmentDO> repairOrderEquipmentDOList, String repairOrderNo, User loginUser, Date currentTime){
        Map<String, RepairOrderEquipmentDO> saveRepairOrderEquipmentDOMap = new HashMap<>();
        Map<String, RepairOrderEquipmentDO> updateRepairOrderEquipmentDOMap = new HashMap<>();
        List<RepairOrderEquipmentDO> dbRepairOrderEquipmentDOList = repairOrderEquipmentMapper.findByRepairOrderNo(repairOrderNo);
        Map<Integer, RepairOrderEquipmentDO> dbRepairOrderEquipmentDOMap = ListUtil.listToMap(dbRepairOrderEquipmentDOList,  "id");
        for (RepairOrderEquipmentDO repairOrderEquipmentDO : repairOrderEquipmentDOList) {
            //如果原单中有现单中的数据
            if (dbRepairOrderEquipmentDOMap.get(repairOrderEquipmentDO.getId()) != null) {
                //将原单和现单中都存在的数据，存入此Map
                updateRepairOrderEquipmentDOMap.put(repairOrderEquipmentDO.getEquipmentNo(), repairOrderEquipmentDO);
                //将原设备维修单明细中已经存入到updateRepairOrderEquipmentDOMap的数据删除
                dbRepairOrderEquipmentDOMap.remove(repairOrderEquipmentDO.getId());
            } else {
                //如果原单中没有现单中的数据
                saveRepairOrderEquipmentDOMap.put(repairOrderEquipmentDO.getEquipmentNo(), repairOrderEquipmentDO);
            }
        }

        //新增的数据
        if (saveRepairOrderEquipmentDOMap.size() > 0){
            for (Map.Entry<String, RepairOrderEquipmentDO> entry : saveRepairOrderEquipmentDOMap.entrySet()){
                RepairOrderEquipmentDO repairOrderEquipmentDO = entry.getValue();
                //获取订单商品设备表,判断设备是否处于租赁状态
                //获取设备
                ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(repairOrderEquipmentDO.getEquipmentNo());
                if (productEquipmentDO == null) {
                    return (ErrorCode.EQUIPMENT_NOT_EXISTS);
                }

                if (StringUtil.isNotEmpty(productEquipmentDO.getOrderNo())) {//设备处于租赁状态
                    //获取订单ID
                    OrderDO orderDO = orderMapper.findByOrderNo(productEquipmentDO.getOrderNo());
                    if(orderDO.getId() == null){
                        return ErrorCode.ORDER_NOT_EXISTS;
                    }

                    OrderProductEquipmentDO orderProductEquipmentDO = orderProductEquipmentMapper.findByOrderIdAndEquipmentNo(orderDO.getId(),repairOrderEquipmentDO.getEquipmentNo());
                    repairOrderEquipmentDO.setOrderId(orderProductEquipmentDO.getOrderId());
                    repairOrderEquipmentDO.setOrderProductId(orderProductEquipmentDO.getOrderProductId());
                }

                repairOrderEquipmentDO.setRepairOrderNo(repairOrderNo);
                repairOrderEquipmentDO.setEquipmentId(productEquipmentDO.getId());
                repairOrderEquipmentDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                repairOrderEquipmentDO.setCreateUser(loginUser.getUserId().toString());
                repairOrderEquipmentDO.setUpdateUser(loginUser.getUserId().toString());
                repairOrderEquipmentDO.setCreateTime(currentTime);
                repairOrderEquipmentDO.setUpdateTime(currentTime);
                repairOrderEquipmentMapper.save(repairOrderEquipmentDO);
            }
        }

        //传入的设备维修单明细单刚好更改的就是原来设备维修单明细单的数据
        if (updateRepairOrderEquipmentDOMap.size() > 0 ){
            for (Map.Entry<String, RepairOrderEquipmentDO> entry : updateRepairOrderEquipmentDOMap.entrySet()){
                RepairOrderEquipmentDO repairOrderEquipmentDO = entry.getValue();

                RepairOrderEquipmentDO dbrepairOrderEquipmentDO = repairOrderEquipmentMapper.findById(repairOrderEquipmentDO.getId());
                //获取订单商品设备表,判断设备是否处于租赁状态
                //获取设备
                ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(repairOrderEquipmentDO.getEquipmentNo());
                if (productEquipmentDO == null) {
                    return (ErrorCode.EQUIPMENT_NOT_EXISTS);
                }

                dbrepairOrderEquipmentDO.setOrderId(null);
                dbrepairOrderEquipmentDO.setOrderProductId(null);

                if (StringUtil.isNotEmpty(productEquipmentDO.getOrderNo())) {//设备处于租赁状态
                    //获取订单ID
                    OrderDO orderDO = orderMapper.findByOrderNo(productEquipmentDO.getOrderNo());
                    if(orderDO.getId() == null){
                        return ErrorCode.ORDER_NOT_EXISTS;
                    }

                    OrderProductEquipmentDO orderProductEquipmentDO = orderProductEquipmentMapper.findByOrderIdAndEquipmentNo(orderDO.getId(),repairOrderEquipmentDO.getEquipmentNo());
                    dbrepairOrderEquipmentDO.setOrderId(orderProductEquipmentDO.getOrderId());
                    dbrepairOrderEquipmentDO.setOrderProductId(orderProductEquipmentDO.getOrderProductId());
                }

                dbrepairOrderEquipmentDO.setEquipmentId(productEquipmentDO.getId());
                dbrepairOrderEquipmentDO.setEquipmentNo(repairOrderEquipmentDO.getEquipmentNo());
                dbrepairOrderEquipmentDO.setRemark(repairOrderEquipmentDO.getRemark());
                dbrepairOrderEquipmentDO.setUpdateUser(loginUser.getUserId().toString());
                dbrepairOrderEquipmentDO.setUpdateTime(currentTime);
                repairOrderEquipmentMapper.update(dbrepairOrderEquipmentDO);
            }
        }
        int count = saveRepairOrderEquipmentDOMap.size()+ updateRepairOrderEquipmentDOMap.size()+dbRepairOrderEquipmentDOMap.size();
        return count+"";
    }


    private ServiceResult<String,String> saveRepairOrderBulkMaterialInfo(List<RepairOrderBulkMaterialDO> repairOrderBulkMaterialDOList, String repairOrderNo, User loginUser, Date currentTime,Integer bulkMaterialCount) {
        ServiceResult<String,String> serviceResult = new ServiceResult<>();
        Map<String, RepairOrderBulkMaterialDO> saveRepairOrderBulkMaterialDOMap = new HashMap<>();
        Map<String, RepairOrderBulkMaterialDO> updateRepairOrderBulkMaterialDOMap = new HashMap<>();
        List<RepairOrderBulkMaterialDO> dbRepairOrderBulkMaterialDOList = repairOrderBulkMaterialMapper.findByRepairOrderNo(repairOrderNo);
        Map<Integer, RepairOrderBulkMaterialDO> dbRepairOrderBulkMaterialDOMap = ListUtil.listToMap(dbRepairOrderBulkMaterialDOList, "id");
        for (RepairOrderBulkMaterialDO repairOrderBulkMaterialDO : repairOrderBulkMaterialDOList) {
            //如果原单中有现单中的数据
            if (dbRepairOrderBulkMaterialDOMap.get(repairOrderBulkMaterialDO.getId()) != null) {
                //将原单和现单中都存在的数据，存入此Map
                updateRepairOrderBulkMaterialDOMap.put(repairOrderBulkMaterialDO.getBulkMaterialNo(), repairOrderBulkMaterialDO);
                //将原设备维修单明细中已经存入到updateRepairOrderEquipmentDOMap的数据移除
                dbRepairOrderBulkMaterialDOMap.remove(repairOrderBulkMaterialDO.getId());
            } else {
                //如果原单中没有现单中的数据
                saveRepairOrderBulkMaterialDOMap.put(repairOrderBulkMaterialDO.getBulkMaterialNo(), repairOrderBulkMaterialDO);
            }
        }

        if (saveRepairOrderBulkMaterialDOMap.size() > 0) {
            for (Map.Entry<String, RepairOrderBulkMaterialDO> entry : saveRepairOrderBulkMaterialDOMap.entrySet()) {
                RepairOrderBulkMaterialDO repairOrderBulkMaterialDO = entry.getValue();
                //获取订单商品设备表,判断设备是否处于租赁状态
                //获取设备
                BulkMaterialDO bulkMaterialDO = bulkMaterialMapper.findByNo(repairOrderBulkMaterialDO.getBulkMaterialNo());
                if (bulkMaterialDO == null) {
                    serviceResult.setErrorCode(ErrorCode.BULK_MATERIAL_IS_NULL);
                    return serviceResult;
                }
                //判断散料是否存在设备
                if(StringUtil.isNotEmpty(bulkMaterialDO.getCurrentEquipmentNo())){
                    serviceResult.setErrorCode(ErrorCode.BULK_MATERIAL_IS_IN_PRODUCT_EQUIPMENT_NOT_REPAIR,repairOrderBulkMaterialDO.getBulkMaterialNo());
                    return serviceResult;
                }

                if (StringUtil.isNotEmpty(bulkMaterialDO.getOrderNo())) {//设备处于租赁状态
                    //获取订单
                    OrderDO orderDO = orderMapper.findByOrderNo(bulkMaterialDO.getOrderNo());
                    if(orderDO.getId() == null){
                        serviceResult.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
                        return serviceResult;
                    }
                    OrderMaterialBulkDO orderMaterialBulkDO = orderMaterialBulkMapper.findByOrderIdAndBulkMaterialNo(orderDO.getId(),repairOrderBulkMaterialDO.getBulkMaterialNo());
                    repairOrderBulkMaterialDO.setOrderId(orderMaterialBulkDO.getOrderId());
                    repairOrderBulkMaterialDO.setOrderMaterialId(orderMaterialBulkDO.getOrderMaterialId());
                }

                repairOrderBulkMaterialDO.setRepairOrderNo(repairOrderNo);
                repairOrderBulkMaterialDO.setBulkMaterialId(bulkMaterialDO.getId());
                repairOrderBulkMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                repairOrderBulkMaterialDO.setCreateUser(loginUser.getUserId().toString());
                repairOrderBulkMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                repairOrderBulkMaterialDO.setCreateTime(currentTime);
                repairOrderBulkMaterialDO.setUpdateTime(currentTime);
                repairOrderBulkMaterialMapper.save(repairOrderBulkMaterialDO);
            }
        }

        //传入的设备维修单明细单刚好更改的就是原来设备维修单明细单的数据
        if (updateRepairOrderBulkMaterialDOMap.size() > 0) {
            for (Map.Entry<String, RepairOrderBulkMaterialDO> entry : updateRepairOrderBulkMaterialDOMap.entrySet()) {
                RepairOrderBulkMaterialDO repairOrderBulkMaterialDO = entry.getValue();

                RepairOrderBulkMaterialDO dbrepairOrderBulkMaterialDO = repairOrderBulkMaterialMapper.findById(repairOrderBulkMaterialDO.getId());
                //获取订单商品设备表,判断设备是否处于租赁状态
                //获取设备
                BulkMaterialDO bulkMaterialDO = bulkMaterialMapper.findByNo(repairOrderBulkMaterialDO.getBulkMaterialNo());
                if (bulkMaterialDO == null) {
                    serviceResult.setErrorCode(ErrorCode.BULK_MATERIAL_IS_NULL);
                    return serviceResult;

                }
                if(StringUtil.isNotEmpty(bulkMaterialDO.getCurrentEquipmentNo())){
                    serviceResult.setErrorCode(ErrorCode.BULK_MATERIAL_IS_IN_PRODUCT_EQUIPMENT_NOT_REPAIR,repairOrderBulkMaterialDO.getBulkMaterialNo());
                    return serviceResult;

                }
                dbrepairOrderBulkMaterialDO.setOrderId(null);
                dbrepairOrderBulkMaterialDO.setOrderMaterialId(null);
                if (StringUtil.isNotEmpty(bulkMaterialDO.getOrderNo())) {//设备处于租赁状态
                    //获取订单ID
                    OrderDO orderDO = orderMapper.findByOrderNo(bulkMaterialDO.getOrderNo());
                    if(orderDO.getId() == null){
                        serviceResult.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
                        return serviceResult;
                    }
                    OrderMaterialBulkDO orderMaterialBulkDO = orderMaterialBulkMapper.findByOrderIdAndBulkMaterialNo(orderDO.getId(),repairOrderBulkMaterialDO.getBulkMaterialNo());
                    dbrepairOrderBulkMaterialDO.setOrderId(orderMaterialBulkDO.getOrderId());
                    dbrepairOrderBulkMaterialDO.setOrderMaterialId(orderMaterialBulkDO.getOrderMaterialId());
                }

                dbrepairOrderBulkMaterialDO.setBulkMaterialId(bulkMaterialDO.getId());
                dbrepairOrderBulkMaterialDO.setBulkMaterialNo(repairOrderBulkMaterialDO.getBulkMaterialNo());
                dbrepairOrderBulkMaterialDO.setRemark(repairOrderBulkMaterialDO.getRemark());
                dbrepairOrderBulkMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                dbrepairOrderBulkMaterialDO.setUpdateTime(currentTime);
                repairOrderBulkMaterialMapper.update(dbrepairOrderBulkMaterialDO);
            }
        }
        bulkMaterialCount = (saveRepairOrderBulkMaterialDOMap.size() + updateRepairOrderBulkMaterialDOMap.size()+dbRepairOrderBulkMaterialDOMap.size());

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(bulkMaterialCount+"");
        return serviceResult;
    }



    @Autowired
    private RepairOrderMapper repairOrderMapper;

    @Autowired
    private OrderProductEquipmentMapper orderProductEquipmentMapper;

    @Autowired
    private RepairOrderEquipmentMapper repairOrderEquipmentMapper;

    @Autowired
    private OrderMaterialBulkMapper orderMaterialBulkMapper;

    @Autowired
    private RepairOrderBulkMaterialMapper repairOrderBulkMaterialMapper;

    @Autowired
    private ProductEquipmentMapper productEquipmentMapper;

    @Autowired
    private BulkMaterialMapper bulkMaterialMapper;

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private WarehouseSupport warehouseSupport;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private OrderMapper orderMapper;
}