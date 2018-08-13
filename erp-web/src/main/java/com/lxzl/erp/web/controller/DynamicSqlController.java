package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.constant.DynamicSqlTpye;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.dingding.message.DingdingMessageDTO;
import com.lxzl.erp.common.domain.dynamicSql.AdoptExecuteParam;
import com.lxzl.erp.common.domain.dynamicSql.DynamicSqlParam;
import com.lxzl.erp.common.domain.dynamicSql.DynamicSqlQueryParam;
import com.lxzl.erp.common.domain.dynamicSql.pojo.DynamicSql;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.util.validate.constraints.In;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.dingding.DingdingService;
import com.lxzl.erp.core.service.dynamicSql.DynamicSqlService;
import com.lxzl.erp.dataaccess.domain.dynamicSql.DynamicSqlHolderDO;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>Description: </p>
 *
 * @Auther: huahongbin
 * @Date: 2018/5/26
 */
@Controller
@ControllerLog
@RequestMapping("/dynamicSql")
public class DynamicSqlController extends BaseController {

    @RequestMapping(value = "select", method = RequestMethod.POST)
    public Result select(@RequestBody DynamicSqlParam dynamicSqlParam) {
        return resultGenerator.generate(dynamicSqlService.executeBySql(dynamicSqlParam,
                new HashSet<DynamicSqlTpye>() {
                    {
                        add(DynamicSqlTpye.SELECT);
                    }
                }));
    }

    @RequestMapping(value = "executeDML", method = RequestMethod.POST)
    public Result executeDML(@RequestBody DynamicSqlParam dynamicSqlParam) {
        return resultGenerator.generate(dynamicSqlService.executeBySql(dynamicSqlParam,
                new HashSet<DynamicSqlTpye>() {
                    {
                        add(DynamicSqlTpye.INSERT);
                        add(DynamicSqlTpye.UPDATE);
                    }
                }));
    }

    @RequestMapping(value = "adoptList", method = RequestMethod.POST)
    public Result listExecuteBySql(@RequestBody PageQuery pageQuery) {
        return resultGenerator.generate(dynamicSqlService.pageDynamicSqlHolder(pageQuery));
    }

    @RequestMapping(value = "adopt", method = RequestMethod.POST)
    public Result adoptExecuteBySql(@RequestBody @Validated({AddGroup.class}) AdoptExecuteParam adoptExecuteParam) {
        final ServiceResult<String, DynamicSqlHolderDO> serviceResult =
                dynamicSqlService.adoptDynamicSqlHolder(adoptExecuteParam.getDynamicSqlHolderId());
        final String time = new SimpleDateFormat("yyyy-MM-dd HHmmss").format(serviceResult.getResult().getCreateTime());
        final Integer userId = Integer.parseInt(serviceResult.getResult().getCreateUser());
        if (serviceResult.getErrorCode().equals(ErrorCode.SUCCESS))
            dingdingService.sendMessageToDingding(new DingdingMessageDTO() {{
                setReceiverUserId(userId);
                setMessageTitle("SQL执行审批结果");
                setMessageContent("你在【" + time + "】提交的SQL已经通过，请登录ERP查看结果。");
            }});
        return resultGenerator.generate(serviceResult);
    }

    @RequestMapping(value = "reject", method = RequestMethod.POST)
    public Result rejectExecuteBySql(@RequestBody @Validated({AddGroup.class}) AdoptExecuteParam adoptExecuteParam) {
        final ServiceResult<String, DynamicSqlHolderDO> serviceResult =
                dynamicSqlService.rejectDynamicSqlHolder(adoptExecuteParam.getDynamicSqlHolderId(), adoptExecuteParam.getResult());
        final String time = new SimpleDateFormat("yyyy-MM-dd HHmmss").format(serviceResult.getResult().getCreateTime());
        final Integer userId = Integer.parseInt(serviceResult.getResult().getCreateUser());
        if (serviceResult.getErrorCode().equals(ErrorCode.SUCCESS))
            dingdingService.sendMessageToDingding(new DingdingMessageDTO() {{
                setReceiverUserId(userId);
                setMessageTitle("SQL执行审批结果");
                setMessageContent("你在【" + time + "】提交的SQL已被拒绝，请登录ERP查看结果。");
            }});
        return resultGenerator.generate(serviceResult);
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public Result saveDynamicSql(@RequestBody @Validated({AddGroup.class}) DynamicSql dynamicSql, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = dynamicSqlService.saveDynamicSql(dynamicSql);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result updateDynamicSql(@RequestBody @Validated({AddGroup.class}) DynamicSql dynamicSql, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = dynamicSqlService.updateDynamicSql(dynamicSql);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Result deleteDynamicSql(@RequestBody @Validated({IdGroup.class}) DynamicSql dynamicSql, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = dynamicSqlService.deleteDynamicSql(dynamicSql);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "detail", method = RequestMethod.POST)
    public Result detailDynamicSql(@RequestBody @Validated({IdGroup.class}) DynamicSql dynamicSql, BindingResult validResult) {
        ServiceResult<String, DynamicSql> serviceResult = dynamicSqlService.detailDynamicSql(dynamicSql);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "page", method = RequestMethod.POST)
    public Result pageDynamicSql(@RequestBody DynamicSqlQueryParam dynamicSqlQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<DynamicSql>> serviceResult = dynamicSqlService.pageDynamicSql(dynamicSqlQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @Autowired
    private ResultGenerator resultGenerator;

    @Autowired
    private DynamicSqlService dynamicSqlService;

    @Autowired
    private DingdingService dingdingService;
}