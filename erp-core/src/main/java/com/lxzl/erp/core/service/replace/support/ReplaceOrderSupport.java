package com.lxzl.erp.core.service.replace.support;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.PostK3Type;
import com.lxzl.erp.common.constant.ReplaceOrderStatus;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.replace.pojo.ReplaceOrder;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.FormSEOutStock;
import com.lxzl.erp.core.k3WebServiceSdk.ErpServer.ERPServiceLocator;
import com.lxzl.erp.core.k3WebServiceSdk.ErpServer.IERPService;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.customer.impl.support.CustomerSupport;
import com.lxzl.erp.core.service.customer.order.CustomerOrderSupport;
import com.lxzl.erp.core.service.dingding.DingDingSupport.DingDingSupport;
import com.lxzl.erp.core.service.k3.K3Service;
import com.lxzl.erp.core.service.k3.PostK3ServiceManager;
import com.lxzl.erp.core.service.k3.converter.ConvertK3DataService;
import com.lxzl.erp.core.service.material.BulkMaterialService;
import com.lxzl.erp.core.service.material.MaterialService;
import com.lxzl.erp.core.service.material.impl.support.BulkMaterialSupport;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.core.service.permission.PermissionSupport;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.product.impl.support.ProductSupport;
import com.lxzl.erp.core.service.replace.impl.ReplaceOrderServiceImpl;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.warehouse.impl.support.WarehouseSupport;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.changeOrder.ChangeOrderMaterialBulkMapper;
import com.lxzl.erp.dataaccess.dao.mysql.changeOrder.ChangeOrderProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3SendRecordMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.BulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialTypeMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.*;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.dao.mysql.reletorder.ReletOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.replace.ReplaceOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.replace.ReplaceOrderMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.replace.ReplaceOrderProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.system.ImgMysqlMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.domain.k3.K3SendRecordDO;
import com.lxzl.erp.dataaccess.domain.replace.ReplaceOrderDO;
import com.lxzl.se.common.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\9\19 0019 14:00
 */
@Component
public class ReplaceOrderSupport {
    private static final Logger logger = LoggerFactory.getLogger(ReplaceOrderServiceImpl.class);


    public void sendReplaceOrderToK3Asynchronous(final ReplaceOrder replaceOrder, final K3SendRecordDO k3SendRecordDO) {
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                logger.info("【异步向K3推送换货消息，换货单号：" + replaceOrder.getReplaceOrderNo() + "】,发送数据：" + JSON.toJSONString(replaceOrder));
                sendReplaceOrderToK3Method(replaceOrder, k3SendRecordDO);
            }
        });
    }
    private String getErrorMessage(com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.ServiceResult response, K3SendRecordDO k3SendRecordDO) {
        StringBuffer sb = new StringBuffer(dingDingSupport.getEnvironmentString());
        sb.append("向K3推送【换货-").append(k3SendRecordDO.getRecordReferId()).append("】数据失败：");
        sb.append(JSON.toJSONString(response));
        return sb.toString();
    }
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void sendReplaceOrderToK3Method(ReplaceOrder replaceOrder, K3SendRecordDO k3SendRecordDO) {
        com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.ServiceResult response = null;
        try {
            ConvertK3DataService convertK3DataService = postK3ServiceManager.getService(PostK3Type.POST_K3_TYPE_REPLACE_ORDER);
            Object postData = convertK3DataService.getK3PostWebServiceData(null, replaceOrder);
            IERPService service = new ERPServiceLocator().getBasicHttpBinding_IERPService();
            response = service.addSEOutstock((FormSEOutStock) postData);
            //修改推送记录
            if (response == null) {
                k3SendRecordDO.setReceiveResult(CommonConstant.COMMON_CONSTANT_NO);
                logger.info("【PUSH DATA TO K3 RESPONSE FAIL】 ： 换货单号--" + replaceOrder.getReplaceOrderNo() + ",响应结果" + JSON.toJSONString(response));
                dingDingSupport.dingDingSendMessage(getErrorMessage(response, k3SendRecordDO));
            } else if (response.getStatus() != 0) {
                k3SendRecordDO.setReceiveResult(CommonConstant.COMMON_CONSTANT_NO);
                logger.info("【PUSH DATA TO K3 RESPONSE FAIL】 ： 换货单号--" + replaceOrder.getReplaceOrderNo() + ",响应结果" + JSON.toJSONString(response));
                dingDingSupport.dingDingSendMessage(getErrorMessage(response, k3SendRecordDO));
                throw new BusinessException(response.getResult());
            } else {
                k3SendRecordDO.setReceiveResult(CommonConstant.COMMON_CONSTANT_YES);
                logger.info("【PUSH DATA TO K3 RESPONSE SUCCESS】 ： 换货单号--" + replaceOrder.getReplaceOrderNo() + ",响应结果" + JSON.toJSONString(response));
            }
            k3SendRecordDO.setSendResult(CommonConstant.COMMON_CONSTANT_YES);
            k3SendRecordDO.setResponseJson(JSON.toJSONString(response));
            k3SendRecordMapper.update(k3SendRecordDO);
        } catch (Exception e) {
            dingDingSupport.dingDingSendMessage(getErrorMessage(response, k3SendRecordDO));
            StringWriter exceptionFormat = new StringWriter();
            e.printStackTrace(new PrintWriter(exceptionFormat, true));
            logger.error("【换货K3服务异常：换货单号--" + replaceOrder.getReplaceOrderNo() + "】错误原因：" + e);
            //将K3返回的具体错误信息返回，不返回自己定义的K3退货失败
            throw new BusinessException(response.getResult());
        }
    }
    @Autowired
    private ReplaceOrderMapper replaceOrderMapper;
    @Autowired
    private K3ReturnOrderMapper k3ReturnOrderMapper;
    @Autowired
    private K3ReturnOrderDetailMapper k3ReturnOrderDetailMapper;
    @Autowired
    private ProductSupport productSupport;
    @Autowired
    private CustomerOrderSupport customerOrderSupport;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ProductSkuMapper productSkuMapper;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserSupport userSupport;
    @Autowired
    private ProductEquipmentMapper productEquipmentMapper;
    @Autowired
    private ChangeOrderProductEquipmentMapper changeOrderProductEquipmentMapper;
    @Autowired
    private ChangeOrderMaterialBulkMapper changeOrderMaterialBulkMapper;
    @Autowired
    private BulkMaterialMapper bulkMaterialMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private BulkMaterialSupport bulkMaterialSupport;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private BulkMaterialService bulkMaterialService;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OrderProductMapper orderProductMapper;
    @Autowired
    private StatementService statementService;
    @Autowired
    private GenerateNoSupport generateNoSupport;
    @Autowired
    private PermissionSupport permissionSupport;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WarehouseSupport warehouseSupport;
    @Autowired
    private OrderProductEquipmentMapper orderProductEquipmentMapper;
    @Autowired
    private OrderMaterialBulkMapper orderMaterialBulkMapper;
    @Autowired
    private OrderMaterialMapper orderMaterialMapper;
    @Autowired
    private CustomerSupport customerSupport;
    @Autowired
    private MaterialTypeMapper materialTypeMapper;
    @Autowired
    private ReletOrderMapper reletOrderMapper;
    @Autowired
    private ReplaceOrderProductMapper replaceOrderProductMapper;
    @Autowired
    private ReplaceOrderMaterialMapper replaceOrderMaterialMapper;
    @Autowired
    private ImgMysqlMapper imgMysqlMapper;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private K3SendRecordMapper k3SendRecordMapper;
    @Autowired
    private PostK3ServiceManager postK3ServiceManager;
    @Autowired
    private DingDingSupport dingDingSupport;
    @Autowired
    private K3Service k3Service;
}
