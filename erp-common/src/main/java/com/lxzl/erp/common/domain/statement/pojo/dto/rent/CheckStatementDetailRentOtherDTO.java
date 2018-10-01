package com.lxzl.erp.common.domain.statement.pojo.dto.rent;

import com.lxzl.erp.common.domain.statement.pojo.dto.BaseCheckStatementDetailDTO;
import com.lxzl.erp.common.util.CheckStatementUtil;

/**
 * 退货结算类---其他类型
 *
 * @author daiqi
 * @create 2018-07-09 9:32
 */
public class CheckStatementDetailRentOtherDTO extends BaseCheckStatementDetailRentDTO {
    @Override
    protected BaseCheckStatementDetailDTO buildOther() {
        putImportData("orderNo", CheckStatementUtil.PLACEHOLDER_DEFAULT);
        putImportData("itemName", CheckStatementUtil.PLACEHOLDER_DEFAULT);
        putImportData("itemSkuName", CheckStatementUtil.PLACEHOLDER_DEFAULT);
        putImportData("isNew", CheckStatementUtil.PLACEHOLDER_DEFAULT);
        putImportData("allRentTimeLength", CheckStatementUtil.PLACEHOLDER_DEFAULT);
        putImportData("rentTimeLength", CheckStatementUtil.PLACEHOLDER_DEFAULT);
        putImportData("payMode", CheckStatementUtil.PLACEHOLDER_DEFAULT);
        putImportData("rentProgramme", CheckStatementUtil.PLACEHOLDER_DEFAULT);
        putImportData("itemCount", CheckStatementUtil.PLACEHOLDER_DEFAULT);
        putImportData("unitAmountInfo", CheckStatementUtil.PLACEHOLDER_DEFAULT);
        return this;
    }
}
