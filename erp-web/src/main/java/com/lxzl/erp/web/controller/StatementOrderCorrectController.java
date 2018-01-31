package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.statementOrderCorrect.StatementOrderCorrectQueryParam;
import com.lxzl.erp.common.domain.statementOrderCorrect.pojo.StatementOrderCorrect;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.CancelGroup;
import com.lxzl.erp.common.domain.validGroup.QueryGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.common.domain.validGroup.statementOrderCorrect.CommitGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.StatementOrderCorrect.StatementOrderCorrectService;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/30
 * @Time : Created in 11:03
 */
@Controller
@ControllerLog
@RequestMapping("correct")
public class StatementOrderCorrectController {
    /**
    * 创建冲正单
    * @Author : XiaoLuYu
    * @Date : Created in 2018/1/30 11:11
    * @param : statementOrderCorrect
    * @param : bindingResult
    * @Return : com.lxzl.se.common.domain.Result
    */
    @RequestMapping(value = "create",method = RequestMethod.POST)
    public Result createStatementOrderCorrect(@RequestBody @Validated(AddGroup.class) StatementOrderCorrect statementOrderCorrect, BindingResult bindingResult){
        ServiceResult<String,String> serviceResult = statementOrderCorrectService.createStatementOrderCorrect(statementOrderCorrect);
        return resultGenerator.generate(serviceResult.getErrorCode(),serviceResult.getResult());
    }

    /**
     * 提交冲正单
     * @Author : XiaoLuYu
     * @Date : Created in 2018/1/30 11:14
     * @param : statementOrderCorrect
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
     */
    @RequestMapping(value = "commit",method = RequestMethod.POST)
    public Result commitStatementOrderCorrect(@RequestBody @Validated(CommitGroup.class) StatementOrderCorrect statementOrderCorrect, BindingResult bindingResult){
        ServiceResult<String,String> serviceResult = statementOrderCorrectService.commitStatementOrderCorrect(statementOrderCorrect.getStatementCorrectNo(),statementOrderCorrect.getRemark());
        return resultGenerator.generate(serviceResult.getErrorCode(),serviceResult.getResult());
    }

    /**
     * 修改冲正单
     * @Author : XiaoLuYu
     * @Date : Created in 2018/1/30 11:11
     * @param : statementOrderCorrect
    * @param : bindingResult
     * @Return : com.lxzl.se.common.domain.Result
     */
    @RequestMapping(value = "update",method = RequestMethod.POST)
    public Result updateStatementOrderCorrect(@RequestBody @Validated(UpdateGroup.class) StatementOrderCorrect statementOrderCorrect, BindingResult bindingResult){
        ServiceResult<String,String> serviceResult = statementOrderCorrectService.updateStatementOrderCorrect(statementOrderCorrect);
        return resultGenerator.generate(serviceResult.getErrorCode(),serviceResult.getResult());
    }

    /**
     * 取消冲正单
     * @Author : XiaoLuYu
     * @Date : Created in 2018/1/30 17:42
     * @param : statementOrderCorrect
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
     */
    @RequestMapping(value = "cancel",method = RequestMethod.POST)
    public Result cancelStatementOrderCorrect(@RequestBody @Validated(CancelGroup.class) StatementOrderCorrect statementOrderCorrect, BindingResult bindingResult){
        ServiceResult<String,String> serviceResult = statementOrderCorrectService.cancelStatementOrderCorrect(statementOrderCorrect.getStatementCorrectNo());
        return resultGenerator.generate(serviceResult.getErrorCode(),serviceResult.getResult());
    }
    /**
    * 查询冲正单详情
    * @Author : XiaoLuYu
    * @Date : Created in 2018/1/30 20:45
    * @param : statementOrderCorrect
    * @param : bindingResult
    * @Return : com.lxzl.se.common.domain.Result
    */
    @RequestMapping(value = "query",method = RequestMethod.POST)
    public Result queryStatementOrderCorrectDetailByNo(@RequestBody @Validated(QueryGroup.class) StatementOrderCorrect statementOrderCorrect, BindingResult bindingResult){
        ServiceResult<String, StatementOrderCorrect> serviceResult = statementOrderCorrectService.queryStatementOrderCorrectDetailByNo(statementOrderCorrect.getStatementCorrectNo());
        return resultGenerator.generate(serviceResult.getErrorCode(),serviceResult.getResult());
    }
    /**
    * 查询分页
    * @Author : XiaoLuYu
    * @Date : Created in 2018/1/30 20:45
    * @param : statementOrderCorrectQueryParam
    * @param : bindingResult
    * @Return : com.lxzl.se.common.domain.Result
    */
    @RequestMapping(value = "page",method = RequestMethod.POST)
    public Result pageStatementOrderCorrect(@RequestBody StatementOrderCorrectQueryParam statementOrderCorrectQueryParam){
        ServiceResult<String, Page<StatementOrderCorrect>> serviceResult = statementOrderCorrectService.pageStatementOrderCorrect(statementOrderCorrectQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(),serviceResult.getResult());
    }

    @Autowired
    private StatementOrderCorrectService statementOrderCorrectService;

    @Autowired
    private ResultGenerator resultGenerator;
}
