package com.lxzl.erp.core.service.export;


/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/4/14
 * @Time : Created in 11:32
 */
public interface ExcelExportView<T> {
    Object view(T t);
}
