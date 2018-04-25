package com.lxzl.erp.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.dingding.approve.DingdingApproveCallBackDTO;
import com.lxzl.erp.common.domain.dingding.approve.DingdingApproveDTO;
import com.lxzl.erp.common.domain.jointProduct.pojo.JointProduct;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.dingding.DingdingService;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.common.domain.ResultCode;
import com.lxzl.se.dataaccess.mysql.ddl.DdlConfig;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author daiqi
 * @create 2018-04-20 15:34
 */
@Controller
@ControllerLog
@RequestMapping("/dingding")
public class DingdingController extends BaseController {
    @Autowired
    private DingdingService dingdingService;
    @Autowired
    private ResultGenerator resultGenerator;

    /**
     * 绑定钉钉用户列表信息
     */
    @RequestMapping(value = "bindDingDingUsers")
    public Result bindDingDingUsers() {
        ServiceResult<String, Object> serviceResult = dingdingService.bindDingDingUsers();
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 测试接口-----根据手机号获取钉钉用户编号
     */
    @RequestMapping(value = "getDingdingIdByPhone")
    public Result getDingdingIdByPhone(@RequestParam String phone) {
        String userId = dingdingService.getDingdingIdByPhone(phone);
        ServiceResult<String, Object> serviceResult = new ServiceResult();
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(userId);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**  
     * <p>
     * 测试接口-----发起审批实例到钉钉网关
     * </p>
     * <pre>
     *     所需参数示例及其说明
     *     参数名称 : 示例值 : 说明 : 是否必须
     * </pre>
     * @author daiqi  
     * @date 2018/4/23 9:32
     * @param  dingdingApproveDTO
      
     * @return com.lxzl.se.common.domain.Result  
     */  
    @RequestMapping(value = "applyApprovingWorkflow")
    public Result applyApprovingWorkflow(@RequestBody DingdingApproveDTO dingdingApproveDTO) {
        ServiceResult<String, Object> serviceResult = dingdingService.applyApprovingWorkflow(dingdingApproveDTO);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**  
     * <p>
     * 钉钉审批结果回调接口
     * </p>
     * <pre>
     *     所需参数示例及其说明
     *     参数名称 : 示例值 : 说明 : 是否必须
     * </pre>
     * @author daiqi  
     * @date 2018/4/23 15:11
     * @param  dingdingApproveCallBackDTO
      
     * @return com.lxzl.se.common.domain.Result  
     */  
    @RequestMapping(value = "applyApprovingWorkflowCallBack")
    public Result applyApprovingWorkflowCallBack(@RequestBody DingdingApproveCallBackDTO dingdingApproveCallBackDTO) {
//        String str = JSONObject.toJSONString(dataMap);
//        System.out.println("回调的数据为：" + str);
//        DingdingApproveCallBackDTO dingdingApproveCallBackDTO = JSONObject.parseObject(str, DingdingApproveCallBackDTO.class);
        ServiceResult<String, Object> serviceResult = dingdingService.applyApprovingWorkflowCallBack(dingdingApproveCallBackDTO);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
}
