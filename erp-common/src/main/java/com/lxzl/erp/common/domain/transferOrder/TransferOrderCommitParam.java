package com.lxzl.erp.common.domain.transferOrder;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.BaseCommitParam;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 16:59 2018/1/14
 * @Modified By:
 */
public class TransferOrderCommitParam extends BaseCommitParam {

    @NotBlank(message = ErrorCode.TRANSFER_ORDER_NO_NOT_NULL ,groups = {IdGroup.class})
    private String transferOrderNo;   //转移单编号

    public String getTransferOrderNo() {
        return transferOrderNo;
    }

    public void setTransferOrderNo(String transferOrderNo) {
        this.transferOrderNo = transferOrderNo;
    }
}
