package com.lxzl.erp.common.domain.statement.pojo.dto.unrent;

import com.lxzl.erp.common.constant.OrderPayMode;
import com.lxzl.erp.common.domain.statement.pojo.dto.BaseCheckStatementDetailDTO;
import com.lxzl.erp.common.domain.statement.pojo.dto.CheckStatementStatisticsDTO;
import com.lxzl.erp.common.util.CheckStatementUtil;
import org.apache.commons.lang.time.DateFormatUtils;

/**
 * 退货结算类---其他类型
 *
 * @author daiqi
 * @create 2018-07-09 9:32
 */
public class CheckStatementDetailUnRentOtherDTO extends BaseCheckStatementDetailUnRentDTO {
    @Override
    protected BaseCheckStatementDetailDTO loadData() {
        super.loadData();
        super.setOrderOriginalId(super.getStatementOrderDetailId());
        return this;
    }

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

    @Override
    public boolean isAddTheMonth(CheckStatementStatisticsDTO statementStatisticsDTO) {
        boolean startStatementExpectPayTimFlag = super.getStatementExpectPayTime().getTime() >= statementStatisticsDTO.getMonthStartTime();
        boolean endStatementExpectPayTimFlag = super.getStatementExpectPayTime().getTime() <= statementStatisticsDTO.getMonthEndTime();
        if (startStatementExpectPayTimFlag && endStatementExpectPayTimFlag) {
            return true;
        }
        return false;
    }

    @Override
    public String doGetNoThisMonthCacheKey(CheckStatementStatisticsDTO statementStatisticsDTO) {
        String cacheKey = this.getStatementOrderDetailId() + "_" + this.getOrderItemActualId() + "_" + super.getOrderItemType();
        if (OrderPayMode.PAY_MODE_PAY_AFTER.equals(super.getPayMode())) {
            cacheKey = cacheKey + "_" + DateFormatUtils.format(super.getStatementExpectPayTime(), "yyyyMMdd");
        }
        return cacheKey;
    }

    @Override
    public boolean isMergeReturnOrder(CheckStatementStatisticsDTO statementStatisticsDTO) {
        return false;
    }
}
