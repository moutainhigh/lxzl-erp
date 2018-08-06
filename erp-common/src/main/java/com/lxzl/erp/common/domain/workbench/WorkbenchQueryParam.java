package com.lxzl.erp.common.domain.workbench;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 18:15 2018/8/3
 * @Modified By:
 */
public class WorkbenchQueryParam {

    private Integer workbenchName;
    private Integer isRecycleBin;
    private Integer isDisabled;

    public Integer getIsRecycleBin() {
        return isRecycleBin;
    }

    public void setIsRecycleBin(Integer isRecycleBin) {
        this.isRecycleBin = isRecycleBin;
    }

    public Integer getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(Integer isDisabled) {
        this.isDisabled = isDisabled;
    }

    public Integer getWorkbenchName() {
        return workbenchName;
    }

    public void setWorkbenchName(Integer workbenchName) {
        this.workbenchName = workbenchName;
    }
}
