package com.lxzl.erp.core.service.workbench;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.workbench.WorkbenchQueryParam;
import com.lxzl.erp.common.domain.workbench.pojo.Workbench;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 17:07 2018/7/16
 * @Modified By:
 */
public interface WorkbenchService {

    /**
     * 查询工作台数量
     * @param workbenchQueryParam
     * @return
     */
    ServiceResult<String,Workbench> queryWorkbenchCount(WorkbenchQueryParam workbenchQueryParam);

    /**
     * 查询仓库工作台数量
     * @param workbenchQueryParam
     * @return
     */
    ServiceResult<String,Workbench> queryWorkhouseWorkbenchCount(WorkbenchQueryParam workbenchQueryParam);
}
