package com.lxzl.erp.core.service.StatementOrderCorrect;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.statementOrderCorrect.StatementOrderCorrectParam;
import com.lxzl.erp.common.domain.statementOrderCorrect.StatementOrderCorrectQueryParam;
import com.lxzl.erp.common.domain.statementOrderCorrect.pojo.StatementOrderCorrect;
import com.lxzl.erp.core.service.VerifyReceiver;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/30
 * @Time : Created in 11:07
 */
public interface StatementOrderCorrectService extends VerifyReceiver {
    /**
    * 创建冲正单
    * @Author : XiaoLuYu
    * @Date : Created in 2018/1/30 11:14
    * @param : statementOrderCorrect
    * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
    */
    ServiceResult<String,String> createStatementOrderCorrect(StatementOrderCorrect statementOrderCorrect);
    /**
     * 提交冲正单
     * @Author : XiaoLuYu
     * @Date : Created in 2018/1/30 11:14
     * @param : statementOrderCorrect
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
     */
    ServiceResult<String,String> commitStatementOrderCorrect(StatementOrderCorrectParam statementOrderCorrectParam);
    /**
    * 修改冲正单
    * @Author : XiaoLuYu
    * @Date : Created in 2018/1/30 15:47
    * @param : statementOrderCorrect
    * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
    */
    ServiceResult<String,String> updateStatementOrderCorrect(StatementOrderCorrect statementOrderCorrect);
    /**
    * 取消冲正单
    * @Author : XiaoLuYu
    * @Date : Created in 2018/1/30 17:42
    * @param : statementOrderCorrect
    * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
    */
    ServiceResult<String,String> cancelStatementOrderCorrect(String statementOrderCorrectNo);
    /**
     * 查询冲正单详情
     * @Author : XiaoLuYu
     * @Date : Created in 2018/1/30 19:02
     * @param : statementOrderCorrectNo
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,com.lxzl.erp.common.domain.statementOrderCorrect.pojo.StatementOrderCorrect>
     */
    ServiceResult<String,StatementOrderCorrect> queryStatementOrderCorrectDetailByNo(String statementOrderCorrectNo);
    /**
    * 分页查询
    * @Author : XiaoLuYu
    * @Date : Created in 2018/1/30 19:26
    * @param : statementOrderCorrectQueryParam
    * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,com.lxzl.erp.common.domain.Page<com.lxzl.erp.common.domain.statementOrderCorrect.pojo.StatementOrderCorrect>>
    */
    ServiceResult<String, Page<StatementOrderCorrect>> pageStatementOrderCorrect(StatementOrderCorrectQueryParam statementOrderCorrectQueryParam);
}
