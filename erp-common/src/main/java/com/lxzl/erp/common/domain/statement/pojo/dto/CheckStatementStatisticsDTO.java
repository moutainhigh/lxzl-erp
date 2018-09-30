package com.lxzl.erp.common.domain.statement.pojo.dto;

import com.lxzl.erp.common.util.DateUtil;
import org.apache.commons.lang.time.DateFormatUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 对账单详统计数据传输对象
 *
 * @author daiqi
 * @create 2018-07-04 10:46
 */
public class CheckStatementStatisticsDTO {
    /**
     * 对账单详情数据传输对象列表
     */
    private List<BaseCheckStatementDetailDTO> checkStatementDetailDTOS;
    /**
     * 对账单汇总数据传输对象
     */
    private CheckStatementSummaryDTO checkStatementSummaryDTO;

    private String month;

    private Long monthStartTime;

    private Long monthEndTime;
    private String customerName;

    public CheckStatementStatisticsDTO(Date month) {
        this.checkStatementDetailDTOS = new ArrayList<>();
        this.checkStatementSummaryDTO = new CheckStatementSummaryDTO();
        this.month = DateFormatUtils.format(month, "yyyy-MM");
        this.monthStartTime = DateUtil.getMonthBegin(month);
        this.monthEndTime = DateUtil.getMonthEnd(month);
    }

    public List<BaseCheckStatementDetailDTO> getCheckStatementDetailDTOS() {
        return checkStatementDetailDTOS;
    }

    public void setCheckStatementDetailDTOS(List<BaseCheckStatementDetailDTO> checkStatementDetailDTOS) {
        this.checkStatementDetailDTOS = checkStatementDetailDTOS;
    }

    public CheckStatementSummaryDTO getCheckStatementSummaryDTO() {
        return checkStatementSummaryDTO;
    }

    public void setCheckStatementSummaryDTO(CheckStatementSummaryDTO checkStatementSummaryDTO) {
        this.checkStatementSummaryDTO = checkStatementSummaryDTO;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Long getMonthStartTime() {
        return monthStartTime;
    }

    public void setMonthStartTime(Long monthStartTime) {
        this.monthStartTime = monthStartTime;
    }

    public Long getMonthEndTime() {
        return monthEndTime;
    }

    public void setMonthEndTime(Long monthEndTime) {
        this.monthEndTime = monthEndTime;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void addCheckStatementDetailDTO(BaseCheckStatementDetailDTO checkStatementDetailDTO) {
        this.checkStatementDetailDTOS.add(checkStatementDetailDTO);
    }
}
