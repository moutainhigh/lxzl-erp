package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.dynamicSql.DynamicSql;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.dynamicSql.DynamicSqlService;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    public Result selectBySql(@RequestBody DynamicSql dynamicSql) {
        return resultGenerator.generate(dynamicSqlService.selectBySql(dynamicSql));
    }

    @Autowired
    private ResultGenerator resultGenerator;

    @Autowired
    private DynamicSqlService dynamicSqlService;
}
