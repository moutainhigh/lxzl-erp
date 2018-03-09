package com.lxzl.erp.core.service.k3;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.PostK3Type;
import com.lxzl.erp.common.domain.ApplicationConfig;
import com.lxzl.erp.common.domain.K3Config;
import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.*;
import com.lxzl.erp.core.k3WebServiceSdk.ErpServer.ERPServiceLocator;
import com.lxzl.erp.core.k3WebServiceSdk.ErpServer.IERPService;
import com.lxzl.erp.core.service.dingding.DingDingSupport.DingDingSupport;
import com.lxzl.erp.core.service.dingding.DingdingService;
import com.lxzl.erp.core.service.k3.converter.ConvertK3DataService;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3SendRecordMapper;
import com.lxzl.erp.dataaccess.domain.k3.K3SendRecordDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.rpc.ServiceException;
import java.util.Date;

public class K3WebServicePostRunner implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Object postData;
    private K3SendRecordMapper k3SendRecordMapper;
    private Integer postK3Type;
    private K3SendRecordDO k3SendRecordDO;
    private ConvertK3DataService convertK3DataService;
    private DingDingSupport dingDingSupport;

    public K3WebServicePostRunner(Integer postK3Type, Object postData, K3SendRecordMapper k3SendRecordMapper,K3SendRecordDO k3SendRecordDO ,ConvertK3DataService convertK3DataService,
                                  DingDingSupport dingDingSupport) {
        this.postData = postData;
        this.k3SendRecordMapper = k3SendRecordMapper;
        this.postK3Type = postK3Type;
        this.k3SendRecordDO = k3SendRecordDO;
        this.convertK3DataService = convertK3DataService;
        this.dingDingSupport = dingDingSupport;
    }

    private void printSuccessLog(ServiceResult response) {
        logger.info("【PUSH DATA TO K3 RESPONSE SUCCESS】 ： " + JSON.toJSONString(response));
    }

    private void printErrorLog(ServiceResult response) {
        logger.info("【PUSH DATA TO K3 RESPONSE FAIL】 ： " + JSON.toJSONString(response));
    }

    @Override
    public void run() {
        ServiceResult response = null;
        try {
            k3SendRecordDO.setRecordJson(JSON.toJSONString(postData));
            k3SendRecordMapper.update(k3SendRecordDO);
            logger.info("【推送消息】" + JSON.toJSONString(postData));
            IERPService service = new ERPServiceLocator().getBasicHttpBinding_IERPService();

            if (PostK3Type.POST_K3_TYPE_PRODUCT.equals(postK3Type)) {
                response = service.addICItem((FormICItem) postData);
            } else if (PostK3Type.POST_K3_TYPE_MATERIAL.equals(postK3Type)) {
                response = service.addICItem((FormICItem) postData);
            } else if (PostK3Type.POST_K3_TYPE_CUSTOMER.equals(postK3Type)) {
                response = service.addOrganization((FormOrganization) postData);
            } else if (PostK3Type.POST_K3_TYPE_SUPPLIER.equals(postK3Type)) {
                response = service.addSupply((FormSupply) postData);
            }else if (PostK3Type.POST_K3_TYPE_ORDER.equals(postK3Type)) {
                response = service.addSEorder((FormSEOrder) postData);
            }else if (PostK3Type.POST_K3_TYPE_USER.equals(postK3Type)) {
                response = service.addUser((FormUser) postData);
            }else if (PostK3Type.POST_K3_TYPE_K3_RETURN_ORDER.equals(postK3Type)) {
                response = service.addSEOutstock((FormSEOutStock) postData);
            }
            //修改推送记录
            if (response.getStatus()==0) {
                k3SendRecordDO.setReceiveResult(CommonConstant.COMMON_CONSTANT_YES);
            } else {
                k3SendRecordDO.setReceiveResult(CommonConstant.COMMON_CONSTANT_NO);
            }
            k3SendRecordDO.setSendResult(CommonConstant.COMMON_CONSTANT_YES);
            k3SendRecordDO.setResponseJson(JSON.toJSONString(response));
            k3SendRecordMapper.update(k3SendRecordDO);
            logger.info("【返回结果】" + response);
            convertK3DataService.successNotify(k3SendRecordDO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("=============【PUSH DATA TO K3 ERROR】=============");
            logger.error("【ERROR INFO】" , e);

        } catch (Throwable t) {
            logger.error("=============【PUSH DATA TO K3 ERROR】=============");
            logger.error("【ERROR INFO】" ,t);
        } finally {

            if (response == null || response.getStatus() != 0) {
                printErrorLog(response);
                dingDingSupport.dingDingSendMessage(getErrorMessage(response));
            } else {
                printSuccessLog(response);
            }
        }
    }

    public String getErrorMessage(ServiceResult response){
        String type = null;
        if("erp-prod".equals(ApplicationConfig.application)){
            type="【线上环境】";
        }else if("erp-dev".equals(ApplicationConfig.application)){
            type="【开发环境】";
        }else if("erp-adv".equals(ApplicationConfig.application)){
            type="【预发环境】";
        }else if("erp-test".equals(ApplicationConfig.application)){
            type="【测试环境】";
        }
        StringBuffer sb = new StringBuffer(type);
        if (PostK3Type.POST_K3_TYPE_PRODUCT.equals(postK3Type)) {
            sb.append("向K3推送【商品-").append(k3SendRecordDO.getRecordReferId()).append("】数据失败：");
        } else if (PostK3Type.POST_K3_TYPE_MATERIAL.equals(postK3Type)) {
            sb.append("向K3推送【配件-").append(k3SendRecordDO.getRecordReferId()).append("】数据失败：");
        } else if (PostK3Type.POST_K3_TYPE_CUSTOMER.equals(postK3Type)) {
            sb.append("向K3推送【客户-").append(k3SendRecordDO.getRecordReferId()).append("】数据失败：");
        } else if (PostK3Type.POST_K3_TYPE_SUPPLIER.equals(postK3Type)) {
            sb.append("向K3推送【供应商-").append(k3SendRecordDO.getRecordReferId()).append("】数据失败：");
        }else if (PostK3Type.POST_K3_TYPE_ORDER.equals(postK3Type)) {
            sb.append("向K3推送【订单-").append(k3SendRecordDO.getRecordReferId()).append("】数据失败：");
        }else if (PostK3Type.POST_K3_TYPE_USER.equals(postK3Type)) {
            sb.append("向K3推送【用户-").append(k3SendRecordDO.getRecordReferId()).append("】数据失败：");
        }else if (PostK3Type.POST_K3_TYPE_K3_RETURN_ORDER.equals(postK3Type)) {
            sb.append("向K3推送【退货-").append(k3SendRecordDO.getRecordReferId()).append("】数据失败：");
        }
        sb.append(JSON.toJSONString(response));
        return sb.toString();
    }
}
