package com.lxzl.erp.core.service.repairOrder.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.repairOrder.RepairOrderBulkMaterialQueryParam;
import com.lxzl.erp.common.domain.repairOrder.RepairOrderCommitParam;
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
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.repairOrder.RepairOrderService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.material.BulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMaterialBulkMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.repairOrder.RepairOrderBulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.repairOrder.RepairOrderEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.repairOrder.RepairOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.warehouse.WarehouseMapper;
import com.lxzl.erp.dataaccess.domain.material.BulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.erp.dataaccess.domain.order.OrderMaterialBulkDO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductEquipmentDO;
import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentDO;
import com.lxzl.erp.dataaccess.domain.repairOrder.RepairOrderBulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.repairOrder.RepairOrderDO;
import com.lxzl.erp.dataaccess.domain.repairOrder.RepairOrderEquipmentDO;
import com.lxzl.erp.dataaccess.domain.warehouse.WarehouseDO;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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

        String repairOrderNo = generateNoSupport.generateRepairOrderNo(now,repairOrder.getWarehouseNo()); //设备维修单编号
        Integer equipmentCount = 0;
        Integer bulkMaterialCount = 0;
        String warehouseNo = "";

        //判断所有维修设备是否是同一仓库
        ServiceResult<String, WarehouseDO> wareHouseResult = verifyWarehouse(repairOrder.getRepairOrderEquipmentList(), repairOrder.getRepairOrderBulkMaterialList());
        if (!ErrorCode.SUCCESS.equals(wareHouseResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }

        warehouseNo = wareHouseResult.getResult().getWarehouseNo();

        //如果存在设备需要维修
        if (CollectionUtil.isNotEmpty(repairOrder.getRepairOrderEquipmentList())) {
            serviceResult = saveRepairOrderEquipmentInfo(repairOrder.getRepairOrderEquipmentList(), repairOrderNo, userSupport.getCurrentUser(), now, equipmentCount);
            if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
                serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
                return serviceResult;
            }
            equipmentCount = repairOrder.getRepairOrderEquipmentList().size();//送修设备数量
        }

        //如果存在散料需要维修
        if (CollectionUtil.isNotEmpty(repairOrder.getRepairOrderBulkMaterialList())) {
            serviceResult = saveRepairOrderBulkMaterialInfo(repairOrder.getRepairOrderBulkMaterialList(), repairOrderNo, userSupport.getCurrentUser(), now, bulkMaterialCount);
            if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
                serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
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

    private ServiceResult<String, WarehouseDO> verifyWarehouse(List<RepairOrderEquipment> repairOrderEquipmentList, List<RepairOrderBulkMaterial> repairOrderBulkMaterialList) {
        ServiceResult<String, WarehouseDO> serviceResult = new ServiceResult<>();
        Integer warehouseId = null;
        if (CollectionUtil.isNotEmpty(repairOrderEquipmentList)) {
            for (RepairOrderEquipment repairOrderEquipment : repairOrderEquipmentList) {
                ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(repairOrderEquipment.getEquipmentNo());
                if (productEquipmentDO == null) {
                    serviceResult.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_NOT_EXISTS, repairOrderEquipment.getEquipmentNo());
                    return serviceResult;
                }
                if (warehouseId == null) {
                    warehouseId = productEquipmentDO.getCurrentWarehouseId();
                } else if (!warehouseId.equals(productEquipmentDO.getCurrentWarehouseId())) {
                    serviceResult.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_NOT_SAME_WAREHOUSE, productEquipmentDO.getEquipmentNo());
                    return serviceResult;
                }
            }
        }

        if (CollectionUtil.isNotEmpty(repairOrderBulkMaterialList)) {
            for (RepairOrderBulkMaterial repairOrderBulkMaterial : repairOrderBulkMaterialList) {
                BulkMaterialDO bulkMaterialDO = bulkMaterialMapper.findByNo(repairOrderBulkMaterial.getBulkMaterialNo());
                if (bulkMaterialDO == null) {
                    serviceResult.setErrorCode(ErrorCode.BULK_MATERIAL_NOT_EXISTS, repairOrderBulkMaterial.getBulkMaterialNo());
                    return serviceResult;
                }
                if (warehouseId == null) {
                    warehouseId = bulkMaterialDO.getCurrentWarehouseId();
                } else if (!warehouseId.equals(bulkMaterialDO.getCurrentWarehouseId())) {
                    serviceResult.setErrorCode(ErrorCode.BULK_MATERIAL_NOT_SAME_WAREHOUSE, bulkMaterialDO.getBulkMaterialNo());
                    return serviceResult;
                }
            }
        }
        WarehouseDO warehouseDO = warehouseMapper.findById(warehouseId);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(warehouseDO);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, String> commitRepairOrder(RepairOrderCommitParam repairOrderCommitParam) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();

        RepairOrderDO repairOrderDO = repairOrderMapper.findByRepairOrderNo(repairOrderCommitParam.getRepairOrderNo());
        if (repairOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.REPAIR_ORDER_IS_NOT_EXISTS);
            return serviceResult;
        }

        //只有初始化维修单才能进行审核
        if (!RepairOrderStatus.REPAIR_ORDER_STATUS_INIT.equals(repairOrderDO.getRepairOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.REPAIR_ORDER_STATUS_ERROR);
            return serviceResult;
        }

        if (!repairOrderDO.getCreateUser().equals(userSupport.getCurrentUserId().toString())) {
            //只有创建采购单本人可以提交
            serviceResult.setErrorCode(ErrorCode.COMMIT_ONLY_SELF);
            return serviceResult;
        }

        //提交审核判断
        ServiceResult<String, Boolean> needVerifyResult = workflowService.isNeedVerify(WorkflowType.WORKFLOW_TYPE_REPAIR);
        if (!ErrorCode.SUCCESS.equals(needVerifyResult.getErrorCode())) {
            serviceResult.setErrorCode(needVerifyResult.getErrorCode());
            return serviceResult;
        } else if (needVerifyResult.getResult()) {
            if (repairOrderCommitParam.getVerifyUser() == null) {
                serviceResult.setErrorCode(ErrorCode.VERIFY_USER_NOT_NULL);
                return serviceResult;
            }
            String verifyMatters = "1.维修原因，2维修的设备的数量和每个设备标号，3维修的物料数量和散料编号";

            //调用提交审核服务
            ServiceResult<String, String> verifyResult = workflowService.commitWorkFlow(WorkflowType.WORKFLOW_TYPE_REPAIR, repairOrderDO.getRepairOrderNo(), repairOrderCommitParam.getVerifyUser(), verifyMatters, repairOrderCommitParam.getCommitRemark(), repairOrderCommitParam.getImgIdList(),null);
            //修改提交审核状态
            if (ErrorCode.SUCCESS.equals(verifyResult.getErrorCode())) {
                repairOrderDO.setRepairOrderStatus(RepairOrderStatus.REPAIR_ORDER_STATUS_VERIFYING);
                repairOrderDO.setUpdateTime(new Date());
                repairOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                repairOrderMapper.update(repairOrderDO);
                return verifyResult;
            } else {
                serviceResult.setErrorCode(verifyResult.getErrorCode());
                return serviceResult;
            }
        } else {
            repairOrderDO.setRepairOrderStatus(RepairOrderStatus.REPAIR_ORDER_STATUS_WAIT_REPAIR);
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
    public String receiveVerifyResult(boolean verifyResult, String repairOrderNo) {
        try {
            RepairOrderDO repairOrderDO = repairOrderMapper.findByRepairOrderNo(repairOrderNo);
            if (repairOrderDO == null || !RepairOrderStatus.REPAIR_ORDER_STATUS_VERIFYING.equals(repairOrderDO.getRepairOrderStatus())) {
                return ErrorCode.BUSINESS_EXCEPTION;
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
            return ErrorCode.SUCCESS;
        } catch (Exception e) {
            logger.error("审批设备维修单通知失败： {}", repairOrderNo);
            return ErrorCode.SUCCESS;
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
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> cancelRepairOrder(RepairOrder repairOrder) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        RepairOrderDO repairOrderDO = repairOrderMapper.findByRepairOrderNo(repairOrder.getRepairOrderNo());
        if (repairOrderDO.getRepairOrderStatus() == null || !RepairOrderStatus.REPAIR_ORDER_STATUS_INIT.equals(repairOrderDO.getRepairOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.REPAIR_ORDER_STATUS_ERROR);
            return serviceResult;
        }

        if (repairOrderDO.getRepairEquipmentCount() > 0) {
            List<RepairOrderEquipmentDO> repairOrderEquipmentDOList = repairOrderEquipmentMapper.findByRepairOrderNo(repairOrderDO.getRepairOrderNo());
            for (RepairOrderEquipmentDO repairOrderEquipmentDO : repairOrderEquipmentDOList) {

                ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(repairOrderEquipmentDO.getEquipmentNo());
                if (productEquipmentDO == null) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_NOT_EXISTS,repairOrderEquipmentDO.getEquipmentNo());
                    return serviceResult;
                }
                //如果是原本是租赁状态的送来维修的设备，就设置状态为租赁中，否则状态为设备空闲
                if(StringUtil.isNotEmpty(productEquipmentDO.getOrderNo())){
                    productEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_BUSY);
                }else{
                    productEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_IDLE);
                }
                productEquipmentMapper.update(productEquipmentDO);
            }
        }

        if (repairOrderDO.getRepairEquipmentCount() > 0) {
            List<RepairOrderBulkMaterialDO> repairOrderBulkMaterialDOList = repairOrderBulkMaterialMapper.findByRepairOrderNo(repairOrderDO.getRepairOrderNo());
            for (RepairOrderBulkMaterialDO repairOrderBulkMaterialDO : repairOrderBulkMaterialDOList) {

                BulkMaterialDO bulkMaterialDO = bulkMaterialMapper.findByNo(repairOrderBulkMaterialDO.getBulkMaterialNo());
                if (bulkMaterialDO == null) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.BULK_MATERIAL_NOT_EXISTS,repairOrderBulkMaterialDO.getBulkMaterialNo());
                    return serviceResult;
                }

                //如果是原本是租赁状态的送来维修的散料，就设置状态为租赁中，否则状态为散料空闲
                if (StringUtil.isNotEmpty(bulkMaterialDO.getOrderNo())){
                    bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_BUSY);
                }
                bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE);

                bulkMaterialMapper.update(bulkMaterialDO);
            }
        }

        repairOrderDO.setRepairOrderStatus(RepairOrderStatus.REPAIR_ORDER_STATUS_CANCEL);
        repairOrderDO.setRemark(repairOrder.getRemark());
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
        ServiceResult<String, WarehouseDO> wareHouseResult = verifyWarehouse(repairOrder.getRepairOrderEquipmentList(), repairOrder.getRepairOrderBulkMaterialList());
        if (!ErrorCode.SUCCESS.equals(wareHouseResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }

        warehouseNo = wareHouseResult.getResult().getWarehouseNo();

        //判断设备维修单明细
        serviceResult = saveRepairOrderEquipmentInfo(repairOrder.getRepairOrderEquipmentList(), dbrepairOrderDO.getRepairOrderNo(), userSupport.getCurrentUser(), now, equipmentCount);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }
        equipmentCount = Integer.parseInt(serviceResult.getResult());

        //判断散料维修单明细
        serviceResult = saveRepairOrderBulkMaterialInfo(repairOrder.getRepairOrderBulkMaterialList(), dbrepairOrderDO.getRepairOrderNo(), userSupport.getCurrentUser(), now, bulkMaterialCount);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            serviceResult.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return serviceResult;
        }
        bulkMaterialCount = Integer.valueOf(serviceResult.getResult());

        //todo  维修单可能修改备注  已改
        dbrepairOrderDO.setRepairEquipmentCount(equipmentCount);
        dbrepairOrderDO.setRepairBulkMaterialCount(bulkMaterialCount);
        dbrepairOrderDO.setRepairReason(repairOrder.getRepairReason());
        dbrepairOrderDO.setWarehouseNo(warehouseNo);
        dbrepairOrderDO.setRemark(repairOrder.getRemark());
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
        if (repairOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.REPAIR_ORDER_IS_NOT_EXISTS);
            return serviceResult;
        }

        List<RepairOrderEquipmentDO> repairOrderEquipmentDOList = repairOrderDO.getRepairOrderEquipmentDOList();
        for (RepairOrderEquipmentDO repairOrderEquipmentDO : repairOrderEquipmentDOList) {
            ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(repairOrderEquipmentDO.getEquipmentNo());
            repairOrderEquipmentDO.setProductEquipmentDO(productEquipmentDO);
        }

        List<RepairOrderBulkMaterialDO> repairOrderBulkMaterialDOList = repairOrderDO.getRepairOrderBulkMaterialDOList();
        for (RepairOrderBulkMaterialDO repairOrderBulkMaterialDO : repairOrderBulkMaterialDOList) {
            BulkMaterialDO bulkMaterialDO = bulkMaterialMapper.findByNo(repairOrderBulkMaterialDO.getBulkMaterialNo());
            repairOrderBulkMaterialDO.setBulkMaterialDO(bulkMaterialDO);
        }
        RepairOrder repairOrder = ConverterUtil.convert(repairOrderDO, RepairOrder.class);

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
        maps.put("repairOrderQueryParam", repairOrderQueryParam);

        Integer totalCount = repairOrderMapper.findRepairOrderCountByParams(maps);
        List<RepairOrderDO> RepairOrderDOList = repairOrderMapper.findRepairOrderByParams(maps);
        List<RepairOrder> repairOrderList = ConverterUtil.convertList(RepairOrderDOList, RepairOrder.class);
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
        maps.put("repairOrderEquipmentQueryParam", repairOrderEquipmentQueryParam);

        Integer totalCount = repairOrderEquipmentMapper.findRepairOrderEquipmentCountByParams(maps);
        List<RepairOrderEquipmentDO> repairOrderEquipmentDOList = repairOrderEquipmentMapper.findRepairOrderEquipmentByParams(maps);
        List<RepairOrderEquipment> repairOrderEquipmentList = ConverterUtil.convertList(repairOrderEquipmentDOList, RepairOrderEquipment.class);
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
        maps.put("repairOrderBulkMaterialQueryParam", repairOrderBulkMaterialQueryParam);

        Integer totalCount = repairOrderBulkMaterialMapper.findRepairOrderBulkMaterialCountByParams(maps);
        List<RepairOrderBulkMaterialDO> repairOrderBulkMaterialDOList = repairOrderBulkMaterialMapper.findRepairOrderBulkMaterialByParams(maps);
        List<RepairOrderBulkMaterial> repairOrderBulkMaterialList = ConverterUtil.convertList(repairOrderBulkMaterialDOList, RepairOrderBulkMaterial.class);
        Page<RepairOrderBulkMaterial> page = new Page<>(repairOrderBulkMaterialList, totalCount, repairOrderBulkMaterialQueryParam.getPageNo(), repairOrderBulkMaterialQueryParam.getPageSize());

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(page);
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> fix(List<RepairOrderEquipment> repairOrderEquipmentList, List<RepairOrderBulkMaterial> repairOrderBulkMaterialList) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        Date now = new Date();

        if (CollectionUtil.isEmpty(repairOrderEquipmentList) && CollectionUtil.isEmpty(repairOrderBulkMaterialList)) {
            serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return serviceResult;
        }

        Map<String, RepairOrderDO> repairOrderDOMap = new HashMap<>();
        //todo 考虑修复完成后是否可以填写维修备注 已改
        //如果设备维修单明细不为空
        if (CollectionUtil.isNotEmpty(repairOrderEquipmentList)) {
            for (RepairOrderEquipment repairOrderEquipment : repairOrderEquipmentList) {
                RepairOrderEquipmentDO repairOrderEquipmentDO = repairOrderEquipmentMapper.findById(repairOrderEquipment.getRepairOrderEquipmentId());
                if (repairOrderEquipmentDO == null) {
                    //todo 回滚  已改
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.REPAIR_ORDER_EQUIPMENT_NOT_EXISTS, repairOrderEquipment.getRepairOrderEquipmentId());
                    return serviceResult;
                }
                //如果传入的设备维修单明细的维修完成时间已经有值,就结束本次循环
                if (repairOrderEquipmentDO.getRepairEndTime() != null) {
                    continue;
                }

                RepairOrderDO repairOrderDO = repairOrderDOMap.get(repairOrderEquipmentDO.getRepairOrderNo());
                if (repairOrderDO == null) {
                    repairOrderDO = repairOrderMapper.findByRepairOrderNo(repairOrderEquipmentDO.getRepairOrderNo());
                    // 判断该设备维修单明细的维修单是否还是维修中的状态
                    if (!RepairOrderStatus.REPAIR_ORDER_STATUS_REPAIRING.equals(repairOrderDO.getRepairOrderStatus())) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        serviceResult.setErrorCode(ErrorCode.REPAIR_ORDER_DATA_STATUS_ERROR);
                        return serviceResult;
                    }
                    repairOrderDOMap.put(repairOrderDO.getRepairOrderNo(), repairOrderDO);
                }
                //在设备维修单中增加 修复的设备数量
                repairOrderDO.setFixEquipmentCount(repairOrderDO.getFixEquipmentCount() + 1);

                //保存更改后的数据
                repairOrderEquipmentDO.setRepairEndTime(now);
                repairOrderEquipmentDO.setRepairEndRemark(repairOrderEquipment.getRepairEndRemark());
                repairOrderEquipmentDO.setUpdateTime(now);
                repairOrderEquipmentDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                repairOrderEquipmentMapper.update(repairOrderEquipmentDO);

            }
        }

        //如果散料维修单明细不为空
        if (CollectionUtil.isNotEmpty(repairOrderBulkMaterialList)) {

            for (RepairOrderBulkMaterial repairOrderBulkMaterial : repairOrderBulkMaterialList) {
                RepairOrderBulkMaterialDO repairOrderBulkMaterialDO = repairOrderBulkMaterialMapper.findById(repairOrderBulkMaterial.getRepairOrderBulkMaterialId());
                if (repairOrderBulkMaterialDO == null) {
                    serviceResult.setErrorCode(ErrorCode.REPAIR_ORDER_BULK_MATERIAL_NOT_EXISTS, repairOrderBulkMaterial.getRepairOrderBulkMaterialId());
                    return serviceResult;
                }

                //如果传入的设备维修单明细的维修完成时间已经有值,就跳出循环
                if (repairOrderBulkMaterialDO.getRepairEndTime() != null) {
                    continue;
                }

                //判断该设散料维修单的维修单是否还是维修中的状态
                //todo 这里可以做一下优化，将已经查到的维修单，放入一个map中，如果map有了就不再查，直接取 已改
                RepairOrderDO repairOrderDO = repairOrderDOMap.get(repairOrderBulkMaterialDO.getRepairOrderNo());
                if (repairOrderDO == null) {
                    repairOrderDO = repairOrderMapper.findByRepairOrderNo(repairOrderBulkMaterialDO.getRepairOrderNo());
                    if (!RepairOrderStatus.REPAIR_ORDER_STATUS_REPAIRING.equals(repairOrderDO.getRepairOrderStatus())) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        serviceResult.setErrorCode(ErrorCode.REPAIR_ORDER_DATA_STATUS_ERROR);
                        return serviceResult;
                    }
                    repairOrderDOMap.put(repairOrderDO.getRepairOrderNo(), repairOrderDO);
                }
                //在设备维修单中增加 修复的物料数量
                repairOrderDO.setFixBulkMaterialCount(repairOrderDO.getFixBulkMaterialCount() + 1);

                //保存更改后的数据
                repairOrderBulkMaterialDO.setRepairEndTime(now);
                repairOrderBulkMaterialDO.setRepairEndRemark(repairOrderBulkMaterial.getRepairEndRemark());
                repairOrderBulkMaterialDO.setUpdateTime(now);
                repairOrderBulkMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                repairOrderBulkMaterialMapper.update(repairOrderBulkMaterialDO);

            }
        }

        for (String repairOrderNo : repairOrderDOMap.keySet()) {
            RepairOrderDO repairOrderDO = repairOrderDOMap.get(repairOrderNo);
            repairOrderDO.setUpdateTime(now);
            repairOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            repairOrderMapper.update(repairOrderDO);
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, String> end(RepairOrder repairOrder) {
        //todo 结束维修单时，要同时改变设备状态
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();

        RepairOrderDO repairOrderDO = repairOrderMapper.findByRepairOrderNo(repairOrder.getRepairOrderNo());
        if (repairOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.REPAIR_ORDER_IS_NOT_EXISTS);
            return serviceResult;
        }

        if (!RepairOrderStatus.REPAIR_ORDER_STATUS_REPAIRING.equals(repairOrderDO.getRepairOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.REPAIR_ORDER_DATA_STATUS_ERROR);
            return serviceResult;
        }

        if (repairOrderDO.getRepairEquipmentCount() > 0) {
            List<RepairOrderEquipmentDO> repairOrderEquipmentDOList = repairOrderEquipmentMapper.findByRepairOrderNo(repairOrderDO.getRepairOrderNo());
            for (RepairOrderEquipmentDO repairOrderEquipmentDO : repairOrderEquipmentDOList) {

                ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(repairOrderEquipmentDO.getEquipmentNo());
                if (productEquipmentDO == null) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_NOT_EXISTS,repairOrderEquipmentDO.getEquipmentNo());
                    return serviceResult;
                }
                //维修单中，还没有维修完成，暂时先进行报废
                if (repairOrderEquipmentDO.getRepairEndTime() == null) {
                    productEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_SCRAP);
                } else {
                    //如果是原本是租赁状态的送来维修的设备，就设置状态为租赁中，否则状态为设备空闲
                    if(StringUtil.isNotEmpty(productEquipmentDO.getOrderNo())){
                        productEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_BUSY);
                    }else{
                        productEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_IDLE);
                    }
                }
                productEquipmentMapper.update(productEquipmentDO);
            }
        }

        if (repairOrderDO.getRepairEquipmentCount() > 0) {
            List<RepairOrderBulkMaterialDO> repairOrderBulkMaterialDOList = repairOrderBulkMaterialMapper.findByRepairOrderNo(repairOrderDO.getRepairOrderNo());
            for (RepairOrderBulkMaterialDO repairOrderBulkMaterialDO : repairOrderBulkMaterialDOList) {

                BulkMaterialDO bulkMaterialDO = bulkMaterialMapper.findByNo(repairOrderBulkMaterialDO.getBulkMaterialNo());
                if (bulkMaterialDO == null) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.BULK_MATERIAL_NOT_EXISTS,repairOrderBulkMaterialDO.getBulkMaterialNo());
                    return serviceResult;
                }

                //维修单中，还没有维修完成，暂时先进行报废
                if (repairOrderBulkMaterialDO.getRepairEndTime() == null) {
                    bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_SCRAP);
                } else {
                    //如果是原本是租赁状态的送来维修的散料，就设置状态为租赁中，否则状态为散料空闲
                    if (StringUtil.isNotEmpty(bulkMaterialDO.getOrderNo())){
                        bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_BUSY);
                    }
                        bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE);
                }
                bulkMaterialMapper.update(bulkMaterialDO);
            }
        }

        repairOrderDO.setRepairOrderStatus(RepairOrderStatus.REPAIR_ORDER_STATUS_REPAIRED);
        repairOrderDO.setRepairEndRemark(repairOrder.getRepairEndRemark());
        repairOrderDO.setUpdateTime(now);
        repairOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        repairOrderMapper.update(repairOrderDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(repairOrderDO.getRepairOrderNo());
        return serviceResult;
    }

    private ServiceResult<String, String> saveRepairOrderEquipmentInfo(List<RepairOrderEquipment> repairOrderEquipmentList, String repairOrderNo, User loginUser, Date currentTime, Integer equipmentCount) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Map<String, RepairOrderEquipment> saveRepairOrderEquipmentMap = new HashMap<>();
        Map<String, RepairOrderEquipment> updateRepairOrderEquipmentMap = new HashMap<>();
        List<RepairOrderEquipmentDO> dbRepairOrderEquipmentDOList = repairOrderEquipmentMapper.findByRepairOrderNo(repairOrderNo);
        Map<String, RepairOrderEquipmentDO> dbRepairOrderEquipmentDOMap = ListUtil.listToMap(dbRepairOrderEquipmentDOList, "equipmentNo");
        for (RepairOrderEquipment repairOrderEquipment : repairOrderEquipmentList) {
            //如果原单中有现单中的数据
            if (dbRepairOrderEquipmentDOMap.get(repairOrderEquipment.getEquipmentNo()) != null) {
                //将原单和现单中都存在的数据，存入此Map
                updateRepairOrderEquipmentMap.put(repairOrderEquipment.getEquipmentNo(), repairOrderEquipment);
                //将原设备维修单明细中已经存入到updateRepairOrderEquipmentDOMap的数据删除
                dbRepairOrderEquipmentDOMap.remove(repairOrderEquipment.getEquipmentNo());
            } else {
                //如果原单中没有现单中的数据
                saveRepairOrderEquipmentMap.put(repairOrderEquipment.getEquipmentNo(), repairOrderEquipment);
            }
        }

        //新增的数据
        if (saveRepairOrderEquipmentMap.size() > 0) {
            for (String EquipmentNo : saveRepairOrderEquipmentMap.keySet()) {
                //获取订单商品设备表,判断设备是否处于租赁状态
                //获取设备
                ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(EquipmentNo);
                if (productEquipmentDO == null) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.EQUIPMENT_NOT_EXISTS, EquipmentNo);
                    return serviceResult;
                }
                //如果设备只有处于空闲或者租赁中，就不能进行新增维修操作
                if (ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_IDLE.equals(productEquipmentDO.getEquipmentStatus()) ||
                        ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_BUSY.equals(productEquipmentDO.getEquipmentStatus())) {
                    productEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_REPAIRING);
                    productEquipmentMapper.update(productEquipmentDO);
                }else{
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_STATUS_NOT_REPAIR, productEquipmentDO.getEquipmentNo());
                    return serviceResult;
                }

                RepairOrderEquipmentDO repairOrderEquipmentDO = new RepairOrderEquipmentDO();

                if (StringUtil.isNotEmpty(productEquipmentDO.getOrderNo())) {//设备处于租赁状态
                    //获取订单ID
                    OrderDO orderDO = orderMapper.findByOrderNo(productEquipmentDO.getOrderNo());
                    if (orderDO == null) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        serviceResult.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
                        return serviceResult;
                    }

                    OrderProductEquipmentDO orderProductEquipmentDO = orderProductEquipmentMapper.findByOrderIdAndEquipmentNo(orderDO.getId(), EquipmentNo);
                    repairOrderEquipmentDO.setOrderId(orderProductEquipmentDO.getOrderId());
                    repairOrderEquipmentDO.setOrderProductId(orderProductEquipmentDO.getOrderProductId());
                }

                repairOrderEquipmentDO.setRepairOrderNo(repairOrderNo);
                repairOrderEquipmentDO.setEquipmentId(productEquipmentDO.getId());
                repairOrderEquipmentDO.setEquipmentNo(EquipmentNo);
                repairOrderEquipmentDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                repairOrderEquipmentDO.setRemark(saveRepairOrderEquipmentMap.get(EquipmentNo).getRemark());
                repairOrderEquipmentDO.setCreateUser(loginUser.getUserId().toString());
                repairOrderEquipmentDO.setUpdateUser(loginUser.getUserId().toString());
                repairOrderEquipmentDO.setCreateTime(currentTime);
                repairOrderEquipmentDO.setUpdateTime(currentTime);
                repairOrderEquipmentMapper.save(repairOrderEquipmentDO);

            }
        }

        //传入的设备维修单明细单刚好更改的就是原来设备维修单明细单的数据
        if (updateRepairOrderEquipmentMap.size() > 0) {
            for (String equipmentNo : updateRepairOrderEquipmentMap.keySet()) {
                RepairOrderEquipmentDO dbrepairOrderEquipmentDO = repairOrderEquipmentMapper.findByEquipmentNoAndRepairOrderNo(equipmentNo, repairOrderNo);
                //获取订单商品设备表,判断设备是否处于租赁状态
                //获取设备
                ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(equipmentNo);
                if (productEquipmentDO == null) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.EQUIPMENT_NOT_EXISTS);
                    return serviceResult;
                }

                dbrepairOrderEquipmentDO.setOrderId(null);
                dbrepairOrderEquipmentDO.setOrderProductId(null);

                if (StringUtil.isNotEmpty(productEquipmentDO.getOrderNo())) {//设备处于租赁状态
                    //获取订单ID
                    OrderDO orderDO = orderMapper.findByOrderNo(productEquipmentDO.getOrderNo());
                    if (orderDO.getId() == null) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        serviceResult.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
                        return serviceResult;
                    }
                    OrderProductEquipmentDO orderProductEquipmentDO = orderProductEquipmentMapper.findByOrderIdAndEquipmentNo(orderDO.getId(), equipmentNo);
                    dbrepairOrderEquipmentDO.setOrderId(orderProductEquipmentDO.getOrderId());
                    dbrepairOrderEquipmentDO.setOrderProductId(orderProductEquipmentDO.getOrderProductId());
                }
                dbrepairOrderEquipmentDO.setRemark(updateRepairOrderEquipmentMap.get(equipmentNo).getRemark());
                dbrepairOrderEquipmentDO.setUpdateUser(loginUser.getUserId().toString());
                dbrepairOrderEquipmentDO.setUpdateTime(currentTime);
                repairOrderEquipmentMapper.update(dbrepairOrderEquipmentDO);
            }

            if (dbRepairOrderEquipmentDOMap.size() > 0) {
                for (String equipmentNo : dbRepairOrderEquipmentDOMap.keySet()) {
                    //todo 这里不用再查询 ,已改
                    RepairOrderEquipmentDO dbRepairOrderEquipmentDO = dbRepairOrderEquipmentDOMap.get(equipmentNo);
                    dbRepairOrderEquipmentDO.setUpdateUser(loginUser.getUserId().toString());
                    dbRepairOrderEquipmentDO.setUpdateTime(currentTime);
                    repairOrderEquipmentMapper.clearDateStatusByEquipmentNo(equipmentNo);
                }
            }
        }
        equipmentCount = saveRepairOrderEquipmentMap.size() + updateRepairOrderEquipmentMap.size();
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(equipmentCount + "");
        return serviceResult;

    }


    private ServiceResult<String, String> saveRepairOrderBulkMaterialInfo(List<RepairOrderBulkMaterial> repairOrderBulkMaterialList, String repairOrderNo, User loginUser, Date currentTime, Integer bulkMaterialCount) {
        //todo 如果传来的数据有重复怎么办，如果想创建的时候加上维修备注怎么办  已改
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Map<String, RepairOrderBulkMaterial> saveRepairOrderBulkMaterialMap = new HashMap<>();
        Map<String, RepairOrderBulkMaterial> updateRepairOrderBulkMaterialMap = new HashMap<>();
        List<RepairOrderBulkMaterialDO> dbRepairOrderBulkMaterialDOList = repairOrderBulkMaterialMapper.findByRepairOrderNo(repairOrderNo);
        Map<String, RepairOrderBulkMaterialDO> dbRepairOrderBulkMaterialDOMap = ListUtil.listToMap(dbRepairOrderBulkMaterialDOList, "bulkMaterialNo");
        for (RepairOrderBulkMaterial repairOrderBulkMaterial : repairOrderBulkMaterialList) {
            //如果原单中有现单中的数据
            if (dbRepairOrderBulkMaterialDOMap.get(repairOrderBulkMaterial.getBulkMaterialNo()) != null) {
                //将原单和现单中都存在的数据，存入此Map
                updateRepairOrderBulkMaterialMap.put(repairOrderBulkMaterial.getBulkMaterialNo(), repairOrderBulkMaterial);
                //将原设备维修单明细中已经存入到updateRepairOrderEquipmentDOMap的数据移除
                dbRepairOrderBulkMaterialDOMap.remove(repairOrderBulkMaterial.getBulkMaterialNo());
            } else {
                //如果原单中没有现单中的数据
                saveRepairOrderBulkMaterialMap.put(repairOrderBulkMaterial.getBulkMaterialNo(), repairOrderBulkMaterial);
            }
        }

        if (saveRepairOrderBulkMaterialMap.size() > 0) {
            for (String bulkMaterialNo : saveRepairOrderBulkMaterialMap.keySet()) {
                //获取订单商品设备表,判断设备是否处于租赁状态
                //获取设备
                BulkMaterialDO bulkMaterialDO = bulkMaterialMapper.findByNo(bulkMaterialNo);
                if (bulkMaterialDO == null) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.BULK_MATERIAL_NOT_EXISTS);
                    return serviceResult;
                }

                //如果散料只有处于设备空闲中或者租赁中就不能进行新增维修操作
                if (BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE.equals(bulkMaterialDO.getBulkMaterialStatus()) ||
                        BulkMaterialStatus.BULK_MATERIAL_STATUS_BUSY.equals(bulkMaterialDO.getBulkMaterialStatus())) {
                    bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_REPAIRING);
                    bulkMaterialMapper.update(bulkMaterialDO);
                }else{
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.BULK_MATERIAL_STATUS_NOT_REPAIR, bulkMaterialDO.getBulkMaterialNo());
                    return serviceResult;
                }

                RepairOrderBulkMaterialDO repairOrderBulkMaterialDO = new RepairOrderBulkMaterialDO();
                //判断散料是否存在设备
                if (StringUtil.isNotEmpty(bulkMaterialDO.getCurrentEquipmentNo())) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.BULK_MATERIAL_IS_IN_PRODUCT_EQUIPMENT_NOT_REPAIR, repairOrderBulkMaterialDO.getBulkMaterialNo());
                    return serviceResult;
                }

                if (StringUtil.isNotEmpty(bulkMaterialDO.getOrderNo())) {//设备处于租赁状态
                    //获取订单
                    OrderDO orderDO = orderMapper.findByOrderNo(bulkMaterialDO.getOrderNo());
                    if (orderDO.getId() == null) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        serviceResult.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
                        return serviceResult;
                    }
                    OrderMaterialBulkDO orderMaterialBulkDO = orderMaterialBulkMapper.findByOrderIdAndBulkMaterialNo(orderDO.getId(), bulkMaterialNo);
                    repairOrderBulkMaterialDO.setOrderId(orderMaterialBulkDO.getOrderId());
                    repairOrderBulkMaterialDO.setOrderMaterialId(orderMaterialBulkDO.getOrderMaterialId());
                }
                repairOrderBulkMaterialDO.setRepairOrderNo(repairOrderNo);
                repairOrderBulkMaterialDO.setBulkMaterialId(bulkMaterialDO.getId());
                repairOrderBulkMaterialDO.setBulkMaterialNo(bulkMaterialNo);
                repairOrderBulkMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                repairOrderBulkMaterialDO.setRemark(saveRepairOrderBulkMaterialMap.get(bulkMaterialNo).getRemark());
                repairOrderBulkMaterialDO.setCreateUser(loginUser.getUserId().toString());
                repairOrderBulkMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                repairOrderBulkMaterialDO.setCreateTime(currentTime);
                repairOrderBulkMaterialDO.setUpdateTime(currentTime);
                repairOrderBulkMaterialMapper.save(repairOrderBulkMaterialDO);
            }
        }

        //传入的设备维修单明细单刚好更改的就是原来设备维修单明细单的数据
        if (updateRepairOrderBulkMaterialMap.size() > 0) {
            for (String bulkMaterialNo : updateRepairOrderBulkMaterialMap.keySet()) {
                RepairOrderBulkMaterialDO dbrepairOrderBulkMaterialDO = repairOrderBulkMaterialMapper.findByBulkMaterialNoAndRepairOrderNo(bulkMaterialNo, repairOrderNo);
                //获取订单商品设备表,判断设备是否处于租赁状态
                //获取设备
                BulkMaterialDO bulkMaterialDO = bulkMaterialMapper.findByNo(bulkMaterialNo);
                if (bulkMaterialDO == null) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.BULK_MATERIAL_IS_NULL);
                    return serviceResult;

                }
                if (StringUtil.isNotEmpty(bulkMaterialDO.getCurrentEquipmentNo())) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.BULK_MATERIAL_IS_IN_PRODUCT_EQUIPMENT_NOT_REPAIR, bulkMaterialDO.getBulkMaterialNo());
                    return serviceResult;

                }
                dbrepairOrderBulkMaterialDO.setOrderId(null);
                dbrepairOrderBulkMaterialDO.setOrderMaterialId(null);
                if (StringUtil.isNotEmpty(bulkMaterialDO.getOrderNo())) {//设备处于租赁状态
                    //获取订单ID
                    OrderDO orderDO = orderMapper.findByOrderNo(bulkMaterialDO.getOrderNo());
                    if (orderDO.getId() == null) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        serviceResult.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
                        return serviceResult;
                    }
                    OrderMaterialBulkDO orderMaterialBulkDO = orderMaterialBulkMapper.findByOrderIdAndBulkMaterialNo(orderDO.getId(), bulkMaterialNo);
                    dbrepairOrderBulkMaterialDO.setOrderId(orderMaterialBulkDO.getOrderId());
                    dbrepairOrderBulkMaterialDO.setOrderMaterialId(orderMaterialBulkDO.getOrderMaterialId());
                }
                dbrepairOrderBulkMaterialDO.setRemark(updateRepairOrderBulkMaterialMap.get(bulkMaterialNo).getRemark());
                dbrepairOrderBulkMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                dbrepairOrderBulkMaterialDO.setUpdateTime(currentTime);
                repairOrderBulkMaterialMapper.update(dbrepairOrderBulkMaterialDO);
            }
        }

        if (dbRepairOrderBulkMaterialDOMap.size() > 0) {
            for (String bulkMaterialNo : dbRepairOrderBulkMaterialDOMap.keySet()) {
                RepairOrderBulkMaterialDO dbRepairOrderBulkMaterialDO = dbRepairOrderBulkMaterialDOMap.get(bulkMaterialNo);
                dbRepairOrderBulkMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                dbRepairOrderBulkMaterialDO.setUpdateTime(currentTime);
                repairOrderBulkMaterialMapper.clearDateStatusByBulkMaterialNo(bulkMaterialNo);
            }
        }
        bulkMaterialCount = (saveRepairOrderBulkMaterialMap.size() + updateRepairOrderBulkMaterialMap.size());

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(bulkMaterialCount + "");
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
    private WorkflowService workflowService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private WarehouseMapper warehouseMapper;

    @Autowired
    private GenerateNoSupport generateNoSupport;
}