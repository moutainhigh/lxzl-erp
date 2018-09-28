package com.lxzl.erp.common.domain.replace;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.BaseCommitParam;
import com.lxzl.erp.common.domain.validGroup.statementOrderCorrect.CommitGroup;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\9\15 0015 16:22
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReplaceOrderCommitParam extends BaseCommitParam {
    @NotBlank(message = ErrorCode.ORDER_NO_NOT_NULL,groups = {CommitGroup.class})
    private String replaceOrderNo;   //换货编号

    public String getReplaceOrderNo() {
        return replaceOrderNo;
    }

    public void setReplaceOrderNo(String replaceOrderNo) {
        this.replaceOrderNo = replaceOrderNo;
    }
}
