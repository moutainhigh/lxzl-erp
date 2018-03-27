package com.lxzl.erp.common.domain.bank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/3/23
 * @Time : Created in 10:26
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankSlipClaimQueryParam  extends BasePageParam {
    private String customerNo; //客戶编码
    private String otherSideAccountNo; //客戶编码


}
