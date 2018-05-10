package com.lxzl.erp.common.domain.printLog;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.PrintLogReferType;
import com.lxzl.erp.common.domain.base.BasePageParam;
import com.lxzl.erp.common.domain.validGroup.QueryGroup;
import com.lxzl.erp.common.util.validate.constraints.In;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/5/10
 * @Time : Created in 12:19
 */
public class PrintLogPageParam  extends BasePageParam {
    @NotBlank(message = ErrorCode.PRINT_LOG_REFER_NO_NOT_NULL,groups = {QueryGroup.class})
    private String referNo;   //关联NO
    @NotNull(message = ErrorCode.PRINT_LOG_REFER_TYPE_NOT_NULL,groups = {QueryGroup.class})
    @In(value = {PrintLogReferType.ORDER_TYPE_CHANGE,PrintLogReferType.ORDER_TYPE_RETURN},message = ErrorCode.PRINT_LOG_REFER_TYPE_ERROR,groups = {QueryGroup.class})
    private Integer referType;   //关联项类型，1-交货单,2-退货单

    public String getReferNo() {
        return referNo;
    }

    public void setReferNo(String referNo) {
        this.referNo = referNo;
    }

    public Integer getReferType() {
        return referType;
    }

    public void setReferType(Integer referType) {
        this.referType = referType;
    }
}
