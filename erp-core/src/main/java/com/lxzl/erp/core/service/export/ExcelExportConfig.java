package com.lxzl.erp.core.service.export;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/4/14
 * @Time : Created in 16:02
 */

public class ExcelExportConfig {
    private List<ColConfig> configList = new ArrayList<>();

    public ExcelExportConfig addConfig(ColConfig colConfig) {
        configList.add(colConfig);
        return this;
    }
    public List<ColConfig> getConfigList(){
        return configList;
    }
}
