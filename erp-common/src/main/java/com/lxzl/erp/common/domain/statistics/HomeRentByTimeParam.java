package com.lxzl.erp.common.domain.statistics;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.TimeDimensionType;
import com.lxzl.erp.common.util.validate.constraints.In;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author lk
 * @Description: 长短租首页统计（时间维度）
 * @date 2018/1/17 14:42
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HomeRentByTimeParam implements Serializable {
    @NotNull(message = ErrorCode.PARAM_IS_ERROR)
    @In(value = {TimeDimensionType.TIME_DIMENSION_TYPE_MONTH,TimeDimensionType.TIME_DIMENSION_TYPE_YEAR},message = ErrorCode.PARAM_IS_ERROR)
    private Integer timeDimensionType ; //时间维度类型，1-当月，2-当年

    public Integer getTimeDimensionType() {
        return timeDimensionType;
    }

    public void setTimeDimensionType(Integer timeDimensionType) {
        this.timeDimensionType = timeDimensionType;
    }
}
