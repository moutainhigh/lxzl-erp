package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.dynamicSql.DynamicSqlQueryParam;
import com.lxzl.erp.common.domain.dynamicSql.DynamicSqlSelectParam;
import com.lxzl.erp.common.domain.dynamicSql.pojo.DynamicSql;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.dynamicSql.DynamicSqlService;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    public Result selectBySql(@RequestBody DynamicSqlSelectParam dynamicSqlSelectParam) {
        return resultGenerator.generate(dynamicSqlService.selectBySql(dynamicSqlSelectParam));
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public Result saveDynamicSql(@RequestBody @Validated({AddGroup.class})DynamicSql dynamicSql,BindingResult validResult) {
        ServiceResult<String, String> serviceResult = dynamicSqlService.saveDynamicSql(dynamicSql);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Result deleteDynamicSql(@RequestBody @Validated({IdGroup.class})DynamicSql dynamicSql, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = dynamicSqlService.deleteDynamicSql(dynamicSql);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "detail", method = RequestMethod.POST)
    public Result detailDynamicSql(@RequestBody @Validated({IdGroup.class})DynamicSql dynamicSql, BindingResult validResult) {
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
}