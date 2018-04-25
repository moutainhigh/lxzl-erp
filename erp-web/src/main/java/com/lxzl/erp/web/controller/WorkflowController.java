package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.domain.workflow.VerifyWorkflowParam;
import com.lxzl.erp.common.domain.workflow.WorkflowLinkQueryParam;
import com.lxzl.erp.common.domain.workflow.WorkflowTemplateQueryParam;
import com.lxzl.erp.common.domain.workflow.pojo.WorkflowLink;
import com.lxzl.erp.common.domain.workflow.pojo.WorkflowTemplate;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-07 19:24
 */

@RequestMapping("/workflow")
@Controller
@ControllerLog
public class WorkflowController extends BaseController {

    @RequestMapping(value = "verifyWorkFlow", method = RequestMethod.POST)
    public Result verifyWorkFlow(@RequestBody VerifyWorkflowParam param, HttpServletRequest request) {
        ServiceResult<String, Integer> serviceResult = workflowService.verifyWorkFlow(param.getWorkflowLinkNo(), param.getVerifyStatus(), param.getReturnType(), param.getVerifyOpinion(), param.getNextVerifyUser(), param.getImgIdList());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryNextVerifyUsers", method = RequestMethod.POST)
    public Result queryNextVerifyUsers(@RequestBody WorkflowLinkQueryParam workflowQueryParam, HttpServletRequest request) {
        ServiceResult<String, List<User>> serviceResult = workflowService.getNextVerifyUsers(workflowQueryParam.getWorkflowType(), workflowQueryParam.getWorkflowReferNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryWorkflowLinkPage", method = RequestMethod.POST)
    public Result queryWorkflowLinkPage(@RequestBody WorkflowLinkQueryParam workflowLinkQueryParam, HttpServletRequest request) {
        ServiceResult<String, Page<WorkflowLink>> serviceResult = workflowService.getWorkflowLinkPage(workflowLinkQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryCurrentUserWorkflowLinkPage", method = RequestMethod.POST)
    public Result queryCurrentUserWorkflowLinkPage(@RequestBody WorkflowLinkQueryParam workflowLinkQueryParam, HttpServletRequest request) {
        ServiceResult<String, Page<WorkflowLink>> serviceResult = workflowService.getCurrentUserWorkflowLinkPage(workflowLinkQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryWorkflowLinkDetail", method = RequestMethod.POST)
    public Result queryWorkflowLinkDetail(@RequestBody WorkflowLinkQueryParam workflowLinkQueryParam, HttpServletRequest request) {
        ServiceResult<String, WorkflowLink> serviceResult = workflowService.getWorkflowLink(workflowLinkQueryParam.getWorkflowLinkNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "commitWorkFlow", method = RequestMethod.POST)
    public Result commitWorkFlow(@RequestBody WorkflowLinkQueryParam workflowLinkQueryParam, HttpServletRequest request) {
        User user = new User();
        user.setUserId(500013);
        request.getSession().setAttribute(CommonConstant.ERP_USER_SESSION_KEY, user);
        ServiceResult<String, String> serviceResult = workflowService.commitWorkFlow(workflowLinkQueryParam.getWorkflowType(), workflowLinkQueryParam.getWorkflowReferNo(), workflowLinkQueryParam.getCurrentVerifyUser(), workflowLinkQueryParam.getVerifyMatters(), null, null, null);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "isNeedVerify", method = RequestMethod.POST)
    public Result isNeedVerify(@RequestBody WorkflowLinkQueryParam workflowLinkQueryParam, HttpServletRequest request) {
        ServiceResult<String, Boolean> serviceResult = workflowService.isNeedVerify(workflowLinkQueryParam.getWorkflowType());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "updateWorkflowNodeList", method = RequestMethod.POST)
    public Result updateWorkflowNodeList(@RequestBody @Validated WorkflowTemplate workflowTemplate, BindingResult bindingResult) {
        ServiceResult<String, Integer> serviceResult = workflowService.updateWorkflowNodeList(workflowTemplate);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "findWorkflowTemplate", method = RequestMethod.POST)
    public Result findWorkflowTemplate(@RequestBody WorkflowTemplate workflowTemplate, BindingResult bindingResult) {
        ServiceResult<String, WorkflowTemplate> serviceResult = workflowService.findWorkflowTemplate(workflowTemplate.getWorkflowTemplateId());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "pageWorkflowTemplate", method = RequestMethod.POST)
    public Result pageWorkflowTemplate(@RequestBody WorkflowTemplateQueryParam workflowTemplateQueryParam, BindingResult bindingResult) {
        ServiceResult<String, Page<WorkflowTemplate>> serviceResult = workflowService.pageWorkflowTemplate(workflowTemplateQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "workflowImportData", method = RequestMethod.POST)
    public Result workflowImportData() {
        ServiceResult<String, String> serviceResult = workflowService.workflowImportData();
        return resultGenerator.generate(serviceResult);
    }

    @RequestMapping(value = "queryWorkflowLinkDetailByType", method = RequestMethod.POST)
    public Result queryWorkflowLinkDetailByType(@RequestBody WorkflowLinkQueryParam workflowLinkQueryParam) {
        ServiceResult<String, WorkflowLink> serviceResult = workflowService.getWorkflowLink(workflowLinkQueryParam.getWorkflowType(), workflowLinkQueryParam.getWorkflowReferNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private ResultGenerator resultGenerator;
}
