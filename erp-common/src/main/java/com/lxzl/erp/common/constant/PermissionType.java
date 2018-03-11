package com.lxzl.erp.common.constant;

public class PermissionType {
    public static final Integer PERMISSION_TYPE_USER = 1;//用户级别权限
    public static final Integer PERMISSION_TYPE_SUB_COMPANY = 2;//分公司级别权限
    public static final Integer PERMISSION_TYPE_WAREHOUSE = 3;//仓库级别权限
    public static final Integer PERMISSION_TYPE_SUB_COMPANY_FOR_SERVICE = 4;//分公司级别权限,售后人员和仓库人员不受此限制
    public static final Integer PERMISSION_TYPE_WAREHOUSE_SUB_COMPANY = 5;//分公司级别权限,两个公司查看
    public static final Integer PERMISSION_TYPE_SUB_COMPANY_ALL = 6;//全部公司
    public static final Integer PERMISSION_TYPE_SUB_COMPANY_FOR_BUSINESS = 7;//分公司级别权限,商务部不受此限制
}
