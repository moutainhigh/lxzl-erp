package com.lxzl.erp.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.dingding.approve.DingdingApproveCallBackDTO;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.dingding.DingdingService;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
     * 获取所有用户信息提供给钉钉的接口
     */
    @RequestMapping(value = "getAllUsersToDingding")
    public Result getAllUsersToDingding() {
        ServiceResult<String, Object> serviceResult = dingdingService.getAllUsersToDingding();
        return resultGenerator.generate(serviceResult);
    }

    /**
     * <p>
     * 钉钉审批结果回调接口
     * </p>
     * <pre>
     *     所需参数示例及其说明
     *     参数名称 : 示例值 : 说明 : 是否必须
     * </pre>
     *
     * @param dingdingApproveCallBackDTO
     * @return com.lxzl.se.common.domain.Result
     * @author daiqi
     * @date 2018/4/23 15:11
     */
    @RequestMapping(value = "applyApprovingWorkflowCallBack")
    public Result applyApprovingWorkflowCallBack(@RequestBody DingdingApproveCallBackDTO dingdingApproveCallBackDTO) {
        ServiceResult<String, Object> serviceResult = dingdingService.applyApprovingWorkflowCallBack(dingdingApproveCallBackDTO);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }


    /**
     * 测试接口-----向钉钉网关注册用户
     */
    @RequestMapping(value = "registerUserToDingding")
    public Result registerUserToDingding(@RequestParam Integer userId) {
        ServiceResult<String, Object> serviceResult = dingdingService.registerUserToDingding(userId);
        return resultGenerator.generate(serviceResult);
    }

    /**
     * 测试接口---提交审核工作流到钉钉
     */
    @RequestMapping(value = "applyApprovingWorkflowToDingding")
    public Result applyApprovingWorkflowToDingding(@RequestParam String workflowNo) {
        User user = new User();
        user.setUserId(500335);
        super.getHttpServletRequest().getSession().setAttribute(CommonConstant.ERP_USER_SESSION_KEY, user);
        ServiceResult<String, Object> serviceResult = dingdingService.applyApprovingWorkflowToDingding(workflowNo);
        return resultGenerator.generate(serviceResult);
    }

    /**
     * 测试接口---注销钉钉网关的审批工作流实例
     */
    @RequestMapping(value = "delApprovingWorkflow")
    public Result delApprovingWorkflow(@RequestParam String workflowNo) {
        ServiceResult<String, Object> serviceResult = dingdingService.delApprovingWorkflow(workflowNo);
        return resultGenerator.generate(serviceResult);
    }
}
