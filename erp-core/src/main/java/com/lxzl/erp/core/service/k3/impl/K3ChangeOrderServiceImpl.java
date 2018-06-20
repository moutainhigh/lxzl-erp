package com.lxzl.erp.core.service.k3.impl;


import com.lxzl.erp.common.constant.ChangeOrderStatus;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.WorkflowType;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.k3.K3ChangeOrderCommitParam;
import com.lxzl.erp.common.domain.k3.pojo.K3ChangeOrder;
import com.lxzl.erp.common.domain.k3.pojo.K3ChangeOrderDetail;
import com.lxzl.erp.common.domain.k3.pojo.changeOrder.K3ChangeOrderQueryParam;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.FormICItem;
import com.lxzl.erp.core.service.k3.K3ChangeOrderService;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.k3.*;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderProductMapper;
import com.lxzl.erp.dataaccess.domain.k3.*;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductDO;
import com.lxzl.se.common.util.date.DateUtil;
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
 * @Date: Created in 20:01 2018/4/10
 * @Modified By:
 */
@Service("k3ChangeOrderService")
public class K3ChangeOrderServiceImpl implements K3ChangeOrderService {

    private static final Logger logger = LoggerFactory.getLogger(K3ChangeOrderServiceImpl.class);

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> createChangeOrder(K3ChangeOrder k3ChangeOrder) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date now = new Date();
        if (k3ChangeOrder == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        K3ChangeOrderDO k3ChangeOrderDO = ConverterUtil.convert(k3ChangeOrder, K3ChangeOrderDO.class);
        k3ChangeOrderDO.setChangeOrderNo("LXK3RO" + DateUtil.formatDate(now, "yyyyMMddHHmmssSSS"));
        k3ChangeOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        k3ChangeOrderDO.setChangeOrderStatus(ChangeOrderStatus.CHANGE_ORDER_STATUS_WAIT_COMMIT);
        k3ChangeOrderDO.setCreateTime(now);
        k3ChangeOrderDO.setUpdateTime(now);
        k3ChangeOrderDO.setCreateUser(loginUser.getUserId().toString());
        k3ChangeOrderDO.setUpdateUser(loginUser.getUserId().toString());
        k3ChangeOrderMapper.save(k3ChangeOrderDO);

        List<K3ChangeOrderDetail> k3ChangeOrderDetailList = k3ChangeOrder.getK3ChangeOrderDetailList();
        if (CollectionUtil.isNotEmpty(k3ChangeOrderDetailList)) {
            for (K3ChangeOrderDetail k3ChangeOrderDetail : k3ChangeOrderDetailList) {
                K3ChangeOrderDetailDO k3ChangeOrderDetailDO = ConverterUtil.convert(k3ChangeOrderDetail, K3ChangeOrderDetailDO.class);
                if (k3ChangeOrderDetailDO.getChangeSkuId() != null) {
                    ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(k3ChangeOrderDetailDO.getChangeSkuId());
                    Product product = productServiceResult.getResult();
                    K3MappingCategoryDO k3MappingCategoryDO = k3MappingCategoryMapper.findByErpCode(product.getCategoryId().toString());
                    K3MappingBrandDO k3MappingBrandDO = k3MappingBrandMapper.findByErpCode(product.getBrandId().toString());
                    String number = "10." + k3MappingCategoryDO.getK3CategoryCode() + "." + k3MappingBrandDO.getK3BrandCode() + "." + product.getProductModel();
                    k3ChangeOrderDetailDO.setChangeProductNo(number);

                    OrderProductDO orderProductDO = orderProductMapper.findById(Integer.parseInt(k3ChangeOrderDetailDO.getOrderItemId()));
                    if (orderProductDO != null) {
                        k3ChangeOrderDetailDO.setRentType(orderProductDO.getRentType());
                    }
                } else if (k3ChangeOrderDetailDO.getChangeMaterialId() != null) {
                    MaterialDO materialDO = materialMapper.findById(k3ChangeOrderDetailDO.getChangeMaterialId());
                    K3MappingMaterialTypeDO k3MappingMaterialTypeDO = k3MappingMaterialTypeMapper.findByErpCode(materialDO.getMaterialType().toString());
                    K3MappingBrandDO k3MappingBrandDO = k3MappingBrandMapper.findByErpCode(materialDO.getBrandId().toString());
                    FormICItem formICItem = new FormICItem();
                    formICItem.setModel(materialDO.getMaterialModel());//型号名称
                    formICItem.setName(materialDO.getMaterialName());//商品名称
                    String number = "20." + k3MappingMaterialTypeDO.getK3MaterialTypeCode() + "." + k3MappingBrandDO.getK3BrandCode() + "." + materialDO.getMaterialModel();
                    k3ChangeOrderDetailDO.setChangeProductNo(number);

                    OrderMaterialDO orderMaterialDO = orderMaterialMapper.findById(Integer.parseInt(k3ChangeOrderDetailDO.getOrderItemId()));
                    if (orderMaterialDO != null) {
                        k3ChangeOrderDetailDO.setRentType(orderMaterialDO.getRentType());
                    }
                }

                k3ChangeOrderDetailDO.setChangeOrderId(k3ChangeOrderDO.getId());
                k3ChangeOrderDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                k3ChangeOrderDetailDO.setCreateTime(now);
                k3ChangeOrderDetailDO.setCreateUser(loginUser.getUserId().toString());
                k3ChangeOrderDetailDO.setUpdateTime(now);
                k3ChangeOrderDetailDO.setUpdateUser(loginUser.getUserId().toString());
                k3ChangeOrderDetailMapper.save(k3ChangeOrderDetailDO);
            }
        }

        result.setResult(k3ChangeOrderDO.getChangeOrderNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> updateChangeOrder(K3ChangeOrder k3ChangeOrder) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();

        if (k3ChangeOrder == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        K3ChangeOrderDO dbK3ChangeOrderDO = k3ChangeOrderMapper.findByNo(k3ChangeOrder.getChangeOrderNo());
        if (dbK3ChangeOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        if (!ChangeOrderStatus.CHANGE_ORDER_STATUS_WAIT_COMMIT.equals(dbK3ChangeOrderDO.getChangeOrderStatus())) {
            result.setErrorCode(ErrorCode.K3_CHANGE_ORDER_STATUS_CAN_NOT_UPDATE);
            return result;
        }


        K3ChangeOrderDO k3ChangeOrderDO = ConverterUtil.convert(k3ChangeOrder, K3ChangeOrderDO.class);
        k3ChangeOrderDO.setId(dbK3ChangeOrderDO.getId());
        k3ChangeOrderDO.setUpdateTime(currentTime);
        k3ChangeOrderDO.setUpdateUser(loginUser.getUserId().toString());
        k3ChangeOrderMapper.update(k3ChangeOrderDO);
        result.setResult(k3ChangeOrderDO.getChangeOrderNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, String> sendChangeOrderToK3(String changeOrderNo) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();

        K3ChangeOrderDO k3ChangeOrderDO = k3ChangeOrderMapper.findByNo(changeOrderNo);
        if (k3ChangeOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        if (CollectionUtil.isEmpty(k3ChangeOrderDO.getK3ChangeOrderDetailDOList())) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        k3ChangeOrderDO.setChangeOrderStatus(ChangeOrderStatus.CHANGE_ORDER_STATUS_END);
        k3ChangeOrderDO.setUpdateTime(currentTime);
        k3ChangeOrderDO.setUpdateUser(loginUser.getUserId().toString());
        k3ChangeOrderMapper.update(k3ChangeOrderDO);
        statementService.createK3ChangeOrderStatement(changeOrderNo);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> addChangeOrder(K3ChangeOrder k3ChangeOrder) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date now = new Date();
        K3ChangeOrderDO k3ChangeOrderDO = k3ChangeOrderMapper.findByNo(k3ChangeOrder.getChangeOrderNo());
        if (k3ChangeOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        if (CollectionUtil.isEmpty(k3ChangeOrder.getK3ChangeOrderDetailList())) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_ENOUGH);
            return result;
        }
        if (!ChangeOrderStatus.CHANGE_ORDER_STATUS_WAIT_COMMIT.equals(k3ChangeOrderDO.getChangeOrderStatus())) {
            result.setErrorCode(ErrorCode.K3_CHANGE_ORDER_STATUS_CAN_NOT_OPERATE);
            return result;
        }

        for (K3ChangeOrderDetail k3ChangeOrderDetail : k3ChangeOrder.getK3ChangeOrderDetailList()) {
            K3ChangeOrderDetailDO k3ChangeOrderDetailDO = ConverterUtil.convert(k3ChangeOrderDetail, K3ChangeOrderDetailDO.class);
            if (k3ChangeOrderDetailDO.getChangeSkuId() != null) {
                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(k3ChangeOrderDetailDO.getChangeSkuId());
                Product product = productServiceResult.getResult();
                K3MappingCategoryDO k3MappingCategoryDO = k3MappingCategoryMapper.findByErpCode(product.getCategoryId().toString());
                K3MappingBrandDO k3MappingBrandDO = k3MappingBrandMapper.findByErpCode(product.getBrandId().toString());
                String number = "10." + k3MappingCategoryDO.getK3CategoryCode() + "." + k3MappingBrandDO.getK3BrandCode() + "." + product.getProductModel();
                k3ChangeOrderDetailDO.setChangeProductNo(number);

                OrderProductDO orderProductDO = orderProductMapper.findById(Integer.parseInt(k3ChangeOrderDetailDO.getOrderItemId()));
                if (orderProductDO != null) {
                    k3ChangeOrderDetailDO.setRentType(orderProductDO.getRentType());
                }
            } else if (k3ChangeOrderDetailDO.getChangeMaterialId() != null) {
                MaterialDO materialDO = materialMapper.findById(k3ChangeOrderDetailDO.getChangeMaterialId());
                K3MappingMaterialTypeDO k3MappingMaterialTypeDO = k3MappingMaterialTypeMapper.findByErpCode(materialDO.getMaterialType().toString());
                K3MappingBrandDO k3MappingBrandDO = k3MappingBrandMapper.findByErpCode(materialDO.getBrandId().toString());
                FormICItem formICItem = new FormICItem();
                formICItem.setModel(materialDO.getMaterialModel());//型号名称
                formICItem.setName(materialDO.getMaterialName());//商品名称
                String number = "20." + k3MappingMaterialTypeDO.getK3MaterialTypeCode() + "." + k3MappingBrandDO.getK3BrandCode() + "." + materialDO.getMaterialModel();
                k3ChangeOrderDetailDO.setChangeProductNo(number);

                OrderMaterialDO orderMaterialDO = orderMaterialMapper.findById(Integer.parseInt(k3ChangeOrderDetailDO.getOrderItemId()));
                if (orderMaterialDO != null) {
                    k3ChangeOrderDetailDO.setRentType(orderMaterialDO.getRentType());
                }
            }

            k3ChangeOrderDetailDO.setChangeOrderId(k3ChangeOrderDO.getId());
            k3ChangeOrderDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            k3ChangeOrderDetailDO.setCreateTime(now);
            k3ChangeOrderDetailDO.setCreateUser(loginUser.getUserId().toString());
            k3ChangeOrderDetailDO.setUpdateTime(now);
            k3ChangeOrderDetailDO.setUpdateUser(loginUser.getUserId().toString());
            k3ChangeOrderDetailMapper.save(k3ChangeOrderDetailDO);
        }
        result.setResult(k3ChangeOrderDO.getChangeOrderNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, String> deleteChangeOrder(Integer k3ChangeOrderDetailId) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        K3ChangeOrderDetailDO k3ChangeOrderDetailDO = k3ChangeOrderDetailMapper.findById(k3ChangeOrderDetailId);
        if (k3ChangeOrderDetailDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        K3ChangeOrderDO k3ChangeOrderDO = k3ChangeOrderMapper.findById(k3ChangeOrderDetailDO.getChangeOrderId());
        if (ChangeOrderStatus.CHANGE_ORDER_STATUS_WAIT_COMMIT.equals(k3ChangeOrderDO.getChangeOrderStatus())) {
            result.setErrorCode(ErrorCode.K3_CHANGE_ORDER_STATUS_CAN_NOT_OPERATE);
            return result;
        }

        k3ChangeOrderDetailDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        k3ChangeOrderDetailDO.setUpdateTime(currentTime);
        k3ChangeOrderDetailDO.setUpdateUser(loginUser.getUserId().toString());
        k3ChangeOrderDetailMapper.update(k3ChangeOrderDetailDO);

        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, Page<K3ChangeOrder>> queryChangeOrder(K3ChangeOrderQueryParam param) {
        ServiceResult<String, Page<K3ChangeOrder>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(param.getPageNo(), param.getPageSize());

        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("k3ChangeOrderQueryParam", param);

        Integer totalCount = k3ChangeOrderMapper.listCount(maps);
        List<K3ChangeOrderDO> orderDOList = k3ChangeOrderMapper.listPage(maps);
        List<K3ChangeOrder> orderList = ConverterUtil.convertList(orderDOList, K3ChangeOrder.class);
        Page<K3ChangeOrder> page = new Page<>(orderList, totalCount, param.getPageNo(), param.getPageSize());
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }


    @Override
    public ServiceResult<String, K3ChangeOrder> queryChangeOrderByNo(String changeOrderNo) {
        ServiceResult<String, K3ChangeOrder> result = new ServiceResult<>();

        K3ChangeOrderDO k3ChangeOrderDO = k3ChangeOrderMapper.findByNo(changeOrderNo);
        if (k3ChangeOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }

        result.setResult(ConverterUtil.convert(k3ChangeOrderDO, K3ChangeOrder.class));
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> cancelK3ChangeOrder(K3ChangeOrder k3ChangeOrder) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date now = new Date();

        K3ChangeOrderDO k3ChangeOrderDO = k3ChangeOrderMapper.findByNo(k3ChangeOrder.getChangeOrderNo());
        if (k3ChangeOrderDO == null) {
            result.setErrorCode(ErrorCode.K3_CHANGE_ORDER_IS_NOT_NULL);
            return result;
        }
        //判断何时可以取消
        if (!ChangeOrderStatus.CHANGE_ORDER_STATUS_WAIT_COMMIT.equals(k3ChangeOrderDO.getChangeOrderStatus()) &&
                !ChangeOrderStatus.CHANGE_ORDER_STATUS_VERIFYING.equals(k3ChangeOrderDO.getChangeOrderStatus())) {
            result.setErrorCode(ErrorCode.K3_CHANGE_ORDER_STATUS_CAN_NOT_CANCEL);
            return result;
        }

        //判断状态审核中执行工作流取消审核
        if (ChangeOrderStatus.CHANGE_ORDER_STATUS_VERIFYING.equals(k3ChangeOrderDO.getChangeOrderStatus())) {
            ServiceResult<String, String> cancelWorkFlowResult = workflowService.cancelWorkFlow(WorkflowType.WORKFLOW_TYPE_K3_CHANGE, k3ChangeOrderDO.getChangeOrderNo());
            if (!ErrorCode.SUCCESS.equals(cancelWorkFlowResult.getErrorCode())) {
                result.setErrorCode(cancelWorkFlowResult.getErrorCode());
                return result;
            }
        }
        k3ChangeOrderDO.setUpdateTime(now);
        k3ChangeOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        k3ChangeOrderDO.setChangeOrderStatus(ChangeOrderStatus.CHANGE_ORDER_STATUS_CANCEL);
        k3ChangeOrderMapper.update(k3ChangeOrderDO);
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(k3ChangeOrderDO.getChangeOrderNo());
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> commitK3ChangeOrder(K3ChangeOrderCommitParam k3ChangeOrderCommitParam) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date now = new Date();
        K3ChangeOrderDO k3ChangeOrderDO = k3ChangeOrderMapper.findByNo(k3ChangeOrderCommitParam.getChangeOrderNo());
        if (k3ChangeOrderDO == null) {
            result.setErrorCode(ErrorCode.K3_CHANGE_ORDER_IS_NOT_NULL);
            return result;
        } else if (!ChangeOrderStatus.CHANGE_ORDER_STATUS_WAIT_COMMIT.equals(k3ChangeOrderDO.getChangeOrderStatus())) {
            //只有待提交状态的换货单可以提交
            result.setErrorCode(ErrorCode.K3_CHANGE_ORDER_COMMITTED_CAN_NOT_COMMIT_AGAIN);
            return result;
        }
        if (!k3ChangeOrderDO.getCreateUser().equals(loginUser.getUserId().toString())) {
            //只有创建换货单本人可以提交
            result.setErrorCode(ErrorCode.COMMIT_ONLY_SELF);
            return result;
        }
        if (CollectionUtil.isEmpty(k3ChangeOrderDO.getK3ChangeOrderDetailDOList())) {
            result.setErrorCode(ErrorCode.K3_CHANGE_ORDER_DETAIL_COMMITTED_NOT_NULL);
            return result;
        }

        ServiceResult<String, Boolean> needVerifyResult = workflowService.isNeedVerify(WorkflowType.WORKFLOW_TYPE_CHANGE);
        if (!ErrorCode.SUCCESS.equals(needVerifyResult.getErrorCode())) {
            result.setErrorCode(needVerifyResult.getErrorCode());
            return result;
        } else if (needVerifyResult.getResult()) {
            if (k3ChangeOrderCommitParam.getVerifyUserId() == null) {
                result.setErrorCode(ErrorCode.VERIFY_USER_NOT_NULL);
                return result;
            }
            //调用提交审核服务
            k3ChangeOrderCommitParam.setVerifyMatters("K3换货单审核事项：1.服务费和运费 2.换货方式 3.商品与配件的商品差价和换货数量");
            ServiceResult<String, String> verifyResult = workflowService.commitWorkFlow(WorkflowType.WORKFLOW_TYPE_K3_CHANGE, k3ChangeOrderCommitParam.getChangeOrderNo(), k3ChangeOrderCommitParam.getVerifyUserId(), k3ChangeOrderCommitParam.getVerifyMatters(), k3ChangeOrderCommitParam.getRemark(), k3ChangeOrderCommitParam.getImgIdList(), null);
            //修改提交审核状态
            if (ErrorCode.SUCCESS.equals(verifyResult.getErrorCode())) {
                k3ChangeOrderDO.setChangeOrderStatus(ChangeOrderStatus.CHANGE_ORDER_STATUS_VERIFYING);
                k3ChangeOrderDO.setUpdateUser(loginUser.getUserId().toString());
                k3ChangeOrderDO.setUpdateTime(now);
                k3ChangeOrderMapper.update(k3ChangeOrderDO);
                return verifyResult;
            } else {
                result.setErrorCode(verifyResult.getErrorCode());
                return result;
            }
        } else {
            k3ChangeOrderDO.setChangeOrderStatus(ChangeOrderStatus.CHANGE_ORDER_STATUS_END);
            k3ChangeOrderDO.setUpdateUser(loginUser.getUserId().toString());
            k3ChangeOrderDO.setUpdateTime(now);
            k3ChangeOrderMapper.update(k3ChangeOrderDO);
            result.setErrorCode(ErrorCode.SUCCESS);
            return result;
        }
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String receiveVerifyResult(boolean verifyResult, String businessNo) {
        K3ChangeOrderDO k3ChangeOrderDO = k3ChangeOrderMapper.findByNo(businessNo);
        try {
            if (k3ChangeOrderDO != null) {//k3换货单
                //不是审核中状态的收货单，拒绝处理
                if (!ChangeOrderStatus.CHANGE_ORDER_STATUS_VERIFYING.equals(k3ChangeOrderDO.getChangeOrderStatus())) {
                    return ErrorCode.BUSINESS_EXCEPTION;
                }
                if (verifyResult) {
                    k3ChangeOrderDO.setChangeOrderStatus(ChangeOrderStatus.CHANGE_ORDER_STATUS_END);
                } else {
                    k3ChangeOrderDO.setChangeOrderStatus(ChangeOrderStatus.CHANGE_ORDER_STATUS_WAIT_COMMIT);
                }
                k3ChangeOrderMapper.update(k3ChangeOrderDO);
                return ErrorCode.SUCCESS;
            }else {
                return ErrorCode.BUSINESS_EXCEPTION;
            }
        } catch (Exception e) {
            if (k3ChangeOrderDO != null) {
                logger.error("【K3换货单审核后，业务处理异常】", e);
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                logger.error("【数据已回滚】");
            }
            return ErrorCode.BUSINESS_EXCEPTION;
        }
    }

    @Autowired
    private K3MappingBrandMapper k3MappingBrandMapper;

    @Autowired
    private K3MappingCategoryMapper k3MappingCategoryMapper;

    @Autowired
    private K3ChangeOrderMapper k3ChangeOrderMapper;

    @Autowired
    private K3ChangeOrderDetailMapper k3ChangeOrderDetailMapper;

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private K3MappingMaterialTypeMapper k3MappingMaterialTypeMapper;

    @Autowired
    private OrderProductMapper orderProductMapper;

    @Autowired
    private OrderMaterialMapper orderMaterialMapper;

    @Autowired
    private StatementService statementService;


}
